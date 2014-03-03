DROP TABLE `reset_password`;

CREATE TABLE `reset_password` (
  `user_id` int(10) unsigned NOT NULL,
  `unique_url_param` char(36) NOT NULL,
  `reset_password_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`, `unique_url_param`),
  UNIQUE KEY `unique_url_path_UNIQUE` (`unique_url_param`),
  CONSTRAINT `user_id_reset_password` FOREIGN KEY (`user_id`) REFERENCES `app_users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;