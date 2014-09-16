DROP TABLE `roles`;

CREATE TABLE `roles` (
  `app_user_id` int(10) unsigned NOT NULL,
  `role` enum(`ROLE_SUPERADMIN`,`ROLE_ADMIN`,`ROLE_BASIC`,`ROLE_SUBSCRIPTION`,`ROLE_LICENSED`,`ROLE_TRIAL`) NOT NULL DEFAULT `ROLE_BASIC`,
  `role_granted_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`app_user_id`,`role`),
  CONSTRAINT `app_user_id_roles` FOREIGN KEY (`app_user_id`) REFERENCES `app_users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
