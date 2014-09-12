DROP TABLE `banned_inets`;

CREATE TABLE `banned_inets` (
  `inet_min_incl` int(4) unsigned NOT NULL,
  `inet_max_incl` int(4) unsigned NOT NULL,
  `active` bit(1) NOT NULL DEFAULT b'1',
  `number_registration_attempts` int(10) unsigned DEFAULT '0',
  `notes` varchar(255) DEFAULT NULL,
  `created_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`inet_min_incl`,`inet_max_incl`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
