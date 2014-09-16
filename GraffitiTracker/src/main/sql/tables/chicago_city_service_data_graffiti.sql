Drop table `chicago_city_service_data_graffiti`;

CREATE TABLE `chicago_city_service_data_graffiti` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `service_request_id` varchar(11) NOT NULL,
  `status` enum('open', 'closed') NOT NULL DEFAULT 'open',
  `status_notes` varchar(30),
  `requested_datetime` timestamp NOT NULL,
  `updated_datetime` timestamp NOT NULL,
  `address` varchar(100) NOT NULL,
  `lat` float(17, 14) NOT NULL,
  `long` float(17, 14) NOT NULL,
  `media_url` varchar(256),
  `system_created_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `system_updated_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY(`id`),
  UNIQUE KEY `service_request_id_UNIQUE` (`service_request_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
