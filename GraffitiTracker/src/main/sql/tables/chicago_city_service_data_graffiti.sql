Drop table `chicago_city_service_data_graffiti`;

CREATE TABLE `chicago_city_service_data_graffiti` (
  `service_request_id` varchar(11) NOT NULL,
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `status` enum('open', 'closed', 'submitted') NOT NULL DEFAULT 'open',
  `requested_datetime` timestamp NOT NULL,
  `updated_datetime` timestamp NOT NULL,
  `address` varchar(100) NOT NULL,
  `latitude` float(17, 14) NOT NULL,
  `longitude` float(17, 14) NOT NULL,
  `media_url` varchar(256),
  `ward` tinyint(2) unsigned NOT NULL,
  `police_district` tinyint(2) unsigned NOT NULL,
  `zipcode` smallint(5) unsigned NOT NULL,
  `system_created_timestamp` timestamp NOT NULL,
  `system_updated_timestamp` timestamp NOT NULL,
  PRIMARY KEY(`service_request_id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
