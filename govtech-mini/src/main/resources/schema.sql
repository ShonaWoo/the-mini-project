CREATE DATABASE `govtech`;

DROP TABLE IF EXISTS `govtech`.`user`;
CREATE TABLE  `govtech`.`user` (
  `id` int(11) NOT NULL auto_increment,
  `firstname` varchar(45) NOT NULL default '',
  `lastname` varchar(45) NOT NULL default '',
  `salary` int(11) NOT NULL default 0,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;