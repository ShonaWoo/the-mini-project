package com.example.govtech.processor;

import org.springframework.batch.item.ItemProcessor;

import com.example.govtech.model.User;

public class UserItemProcessor implements ItemProcessor<User, User> {

	@Override
	public User process(User item) throws Exception {
		// TODO Auto-generated method stub
		final String capsLastname = item.getLastname().toUpperCase();

	    final User transformedUser = new User(item.getFirstname(), capsLastname, item.getSalary());

	    return transformedUser;
	}

}
