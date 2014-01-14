DROP TABLE `login_addresses`;

CREATE TABLE `login_addresses` (
  `user_id` int(10) unsigned NOT NULL,
  `inet_address` int(4) unsigned NOT NULL,
  `number_visits` int(10) unsigned NOT NULL DEFAULT '1',
  `last_visit_timestamp` timestamp NOT NULL,
  PRIMARY KEY (`user_id`,`inet_address`),
  CONSTRAINT `user_id_login_addesses` FOREIGN KEY (`user_id`) REFERENCES `app_users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;