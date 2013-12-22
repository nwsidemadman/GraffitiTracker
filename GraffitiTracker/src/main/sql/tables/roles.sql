DROP TABLE `graffiti_tracker`.`roles`;

CREATE TABLE `roles` (
  `user_id` int(10) unsigned NOT NULL,
  `role` enum('ROLE_SUPERADMIN','ROLE_ADMIN','ROLE_BASIC','ROLE_SUBSCRIPTION','ROLE_LICENSED','ROLE_TRIAL') NOT NULL DEFAULT 'ROLE_BASIC',
  `role_creation_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`,`role`),
  CONSTRAINT `user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

    
INSERT INTO `graffiti_tracker`.`roles` (`user_id`, `role`) VALUES ((SELECT `user_id` FROM `graffiti_tracker`.`users` WHERE `username` = 'ccaper'), 'ROLE_SUPERADMIN');