DROP TABLE 'login_addresses';

CREATE TABLE 'login_addresses' (
  'app_user_id' int(10) unsigned NOT NULL,
  'inet_address' int(4) unsigned NOT NULL,
  'number_visits' int(10) unsigned NOT NULL DEFAULT '1',
  'last_visit_timestamp' timestamp NOT NULL,
  PRIMARY KEY ('app_user_id','inet_address'),
  CONSTRAINT 'app_user_id_login_addesses' FOREIGN KEY ('app_user_id') REFERENCES 'app_users' ('id') ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;