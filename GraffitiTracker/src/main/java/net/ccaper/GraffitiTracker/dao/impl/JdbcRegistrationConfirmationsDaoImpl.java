package net.ccaper.GraffitiTracker.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import net.ccaper.GraffitiTracker.dao.RegistrationConfirmationsDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Repository;

@Repository("registrationConfirmationsDao")
public class JdbcRegistrationConfirmationsDaoImpl extends
NamedParameterJdbcDaoSupport implements RegistrationConfirmationsDao {
  private static final String REGISTRATION_CONFIRMATIONS_TABLE = "registration_confirmations";
  private static final String USER_ID_COL = JdbcAppUserDaoImpl.USER_ID_COL;
  private static final String UNIQUE_URL_PATH_COL = "unique_url_param";
  private static final String REGISTRATION_TIMESTAMP_COL = "registration_timestamp";
  private static final String USERS_TABLE = JdbcAppUserDaoImpl.USERS_TABLE;
  private static final String USERNAME_COL = JdbcAppUserDaoImpl.USERNAME_COL;
  private static final String SQL_INSERT_REGISTRATION_CONFIRMATION_BY_USERNAME = String
      .format(
          "INSERT INTO %s (%s, %s) VALUES ((SELECT %s FROM %s WHERE %s = :%s), UUID())",
          REGISTRATION_CONFIRMATIONS_TABLE, USER_ID_COL, UNIQUE_URL_PATH_COL,
          USER_ID_COL, USERS_TABLE, USERNAME_COL, USERNAME_COL).toLowerCase();
  private static final String SQL_SELECT_UNIQUE_URL_PARAM_BY_USERNAME = String
      .format(
          "SELECT %s FROM %s INNER JOIN %s WHERE %s.%s = %s.%s AND %s = :%s",
          UNIQUE_URL_PATH_COL, USERS_TABLE, REGISTRATION_CONFIRMATIONS_TABLE,
          USERS_TABLE, USER_ID_COL, REGISTRATION_CONFIRMATIONS_TABLE,
          USER_ID_COL, USERNAME_COL, USERNAME_COL).toLowerCase();

  RowMapper<String> uniqueUrlParamRowMapper = new RowMapper<String>() {
    @Override
    public String mapRow(ResultSet rs, int rowNum) throws SQLException {
      return new String(rs.getString(UNIQUE_URL_PATH_COL));
    }
  };

  @Autowired
  public void setDs(DataSource dataSource) {
    setDataSource(dataSource);
  }

  @Override
  public void addRegistrationConfirmationByUsername(String username) {
    Map<String, String> registrationConfirmationParamMap = new HashMap<String, String>();
    registrationConfirmationParamMap.put(USERNAME_COL, username);
    getNamedParameterJdbcTemplate().update(
        SQL_INSERT_REGISTRATION_CONFIRMATION_BY_USERNAME,
        registrationConfirmationParamMap);
  }

  @Override
  public String getUniqueUrlParamByUsername(String username) {
    Map<String, String> userParamMap = new HashMap<String, String>();
    userParamMap.put(USERNAME_COL, username);
    return getNamedParameterJdbcTemplate().queryForObject(
        SQL_SELECT_UNIQUE_URL_PARAM_BY_USERNAME, userParamMap,
        uniqueUrlParamRowMapper);
  }
}
