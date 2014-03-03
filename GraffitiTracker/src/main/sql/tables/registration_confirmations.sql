DROP TABLE `registration_confirmations`;

CREATE TABLE `registration_confirmations` (
  `user_id` int(10) unsigned NOT NULL,
  `unique_url_param` char(36) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `unique_url_path_UNIQUE` (`unique_url_param`),
  CONSTRAINT `user_id_registration_confirmations` FOREIGN KEY (`user_id`) REFERENCES `app_users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;