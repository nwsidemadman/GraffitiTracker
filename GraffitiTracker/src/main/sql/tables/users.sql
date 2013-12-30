Drop table `graffiti_tracker`.`users`;

CREATE TABLE `users` (
  `user_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(20) NOT NULL,
  `email` varchar(100) NOT NULL,
  `is_active` bit(1) NOT NULL DEFAULT b'1',
  `register_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `password` varchar(64) NOT NULL,
  `last_login` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `userId_UNIQUE` (`user_id`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

INSERT INTO `graffiti_tracker`.`users` (`username`, `email`, `password`) VALUES ('ccaper', 'ccaper@gmail.com', 'test');