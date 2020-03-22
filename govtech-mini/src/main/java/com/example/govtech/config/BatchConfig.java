package com.example.govtech.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.example.govtech.listener.JobCompletionListener;
import com.example.govtech.model.User;
import com.example.govtech.processor.UserItemProcessor;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
	
	@Autowired
	public JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	public DataSource dataSource;
	
	@Bean
	public Job importUserJob(JobCompletionListener listener, Step step1) {
	  return jobBuilderFactory.get("importUserJob")
	    .incrementer(new RunIdIncrementer())
	    .listener(listener)
	    .start(step1)
	    //.end()
	    .build();
	}

	@Bean
	public Step step1(JdbcBatchItemWriter<User> writer) {
	  return stepBuilderFactory.get("step1")
	    .<User, User> chunk(10)
	    .reader(reader())
	    .processor(processor())
	    .writer(writer)
	    .build();
	}
	
	//convert each line to pojo
	@Bean
	public FlatFileItemReader<User> reader() {
	  return new FlatFileItemReaderBuilder<User>()
	    .name("userItemReader")
	    .linesToSkip(1)
	    .resource(new ClassPathResource("users.csv"))
	    .delimited()
	    .names(new String[]{"firstName", "lastName", "salary"})
	    .fieldSetMapper(new BeanWrapperFieldSetMapper<User>() {{
	      setTargetType(User.class);
	    }})
	    .build();
	}

	//process and transform the pojo
	@Bean
	public UserItemProcessor processor() {
	  return new UserItemProcessor();
	}

	//write the pojos to db
	@Bean
	public JdbcBatchItemWriter<User> writer(DataSource dataSource) {
	  return new JdbcBatchItemWriterBuilder<User>()
	    .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
	    .sql("INSERT INTO user (firstname, lastname, salary) VALUES (:firstname, :lastname, :salary)")
	    .dataSource(dataSource)
	    .build();
	}
	
	
}
