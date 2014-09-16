Drop table 'app_users';

CREATE TABLE 'app_users' (
  'id' int(10) unsigned NOT NULL AUTO_INCREMENT,
  'username' varchar(20) NOT NULL,
  'email' varchar(100) NOT NULL,
  'is_active' bit(1) NOT NULL DEFAULT b'0',
  'registration_timestamp' timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  'password' varchar(64) NOT NULL,
  'current_login_timestamp' timestamp NULL DEFAULT NULL,
  'previous_login_timestamp' timestamp NULL DEFAULT NULL,
  'login_count' int(10) unsigned NOT NULL DEFAULT '0',
  'security_question' varchar(75) NOT NULL,
  'security_answer' varchar(40) NOT NULL,
  PRIMARY KEY ('id'),
  UNIQUE KEY 'id_UNIQUE' ('id'),
  UNIQUE KEY 'username_UNIQUE' ('username'),
  UNIQUE KEY 'email_UNIQUE' ('email')
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
