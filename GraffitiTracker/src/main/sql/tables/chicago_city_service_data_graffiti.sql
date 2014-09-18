Drop table `chicago_city_service_data_graffiti`;

CREATE TABLE `chicago_city_service_data_graffiti` (
  `service_request_id` varchar(11) NOT NULL,
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `status` enum('open', 'closed') NOT NULL DEFAULT 'open',
  `status_notes` varchar(30),
  `requested_datetime` timestamp NOT NULL,
  `updated_datetime` timestamp NOT NULL,
  `address` varchar(100) NOT NULL,
  `latitude` float(17, 14) NOT NULL,
  `longitude` float(17, 14) NOT NULL,
  `media_url` varchar(256),
  `system_created_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `system_updated_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY(`service_request_id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
