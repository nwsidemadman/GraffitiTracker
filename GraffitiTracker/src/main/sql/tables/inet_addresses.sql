DROP TABLE `login_addresses`;

CREATE TABLE `login_addresses` (
  `user_id` int(10) unsigned NOT NULL,
  `inet_address` int(4) unsigned NOT NULL,
  `number_visits` int(10) unsigned NOT NULL DEFAULT '0',
  `last_visit_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`,`inet_address`),
  CONSTRAINT `user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;