package net.ccaper.GraffitiTracker.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import net.ccaper.GraffitiTracker.dao.ResetPasswordDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Repository;

@Repository("resetPasswordDao")
public class JdbcResetPasswordDaoImpl extends NamedParameterJdbcDaoSupport
implements ResetPasswordDao {
  private final static String RESET_PASSWORD_TABLE = "reset_password";
  private static final String USER_ID_COL = JdbcAppUserDaoImpl.USER_ID_COL;
  private static final String UNIQUE_URL_PARAM_COL = "unique_url_param";
  private static final String RESET_PASSWORD_TIMESTAMP_COL = "reset_password_timestamp";
  private static final String USERS_TABLE = JdbcAppUserDaoImpl.USERS_TABLE;
  private static final String USERNAME_COL = JdbcAppUserDaoImpl.USERNAME_COL;
  private static final String SQL_INSERT_RESET_PASSWORD_BY_USERNAME = String
      .format(
          "INSERT INTO %s (%s, %s) VALUES ((SELECT %s FROM %s WHERE %s = :%s), UUID())",
          RESET_PASSWORD_TABLE, USER_ID_COL, UNIQUE_URL_PARAM_COL, USER_ID_COL,
          USERS_TABLE, USERNAME_COL, USERNAME_COL).toLowerCase();
  private static final String SQL_SELECT_UNIQUE_PARAM_BY_USERNAME = String
      .format(
          "SELECT %s FROM %s INNER JOIN %s ON %s.%s = %s.%s WHERE %s.%s = :%s "
              + "AND %s = (SELECT MAX(%s) FROM %s WHERE %s.%s = :%s)",
              UNIQUE_URL_PARAM_COL, RESET_PASSWORD_TABLE, USERS_TABLE,
              RESET_PASSWORD_TABLE, USER_ID_COL, USERS_TABLE, USER_ID_COL,
              USERS_TABLE, USERNAME_COL, USERNAME_COL,
              RESET_PASSWORD_TIMESTAMP_COL, RESET_PASSWORD_TIMESTAMP_COL,
              RESET_PASSWORD_TABLE, USERS_TABLE, USERNAME_COL, USERNAME_COL)
              .toLowerCase();
  private static final String SQL_SELECT_USER_ID_BY_UNIQUE_URL_PARAM = String
      .format("SELECT %s FROM %s WHERE %s = :%s",
          USER_ID_COL, RESET_PASSWORD_TABLE, UNIQUE_URL_PARAM_COL,
          UNIQUE_URL_PARAM_COL).toLowerCase();

  RowMapper<String> uniqueUrlParamRowMapper = new RowMapper<String>() {
    @Override
    public String mapRow(ResultSet rs, int rowNum) throws SQLException {
      return new String(rs.getString(UNIQUE_URL_PARAM_COL));
    }
  };

  RowMapper<Integer> useridRowMapper = new RowMapper<Integer>() {
    @Override
    public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
      return new Integer(rs.getInt(USER_ID_COL));
    }
  };

  @Autowired
  public void setDs(DataSource dataSource) {
    setDataSource(dataSource);
  }

  @Override
  public void addResetPassword(String username) {
    Map<String, String> usernameParamMap = new HashMap<String, String>();
    usernameParamMap.put(USERNAME_COL, username);
    getNamedParameterJdbcTemplate().update(
        SQL_INSERT_RESET_PASSWORD_BY_USERNAME, usernameParamMap);
  }

  @Override
  public String getUniqueUrlParamByUsername(String username) {
    Map<String, String> usernameParamMap = new HashMap<String, String>();
    usernameParamMap.put(USERNAME_COL, username);
    return getNamedParameterJdbcTemplate().queryForObject(
        SQL_SELECT_UNIQUE_PARAM_BY_USERNAME, usernameParamMap,
        uniqueUrlParamRowMapper);
  }

  @Override
  public Integer getUserIdByUniqueUrlParam(String uniqueUrlParam) {
    Map<String, String> uniqueUrlParamParamMap = new HashMap<String, String>();
    uniqueUrlParamParamMap.put(UNIQUE_URL_PARAM_COL, uniqueUrlParam);
    try {
      return getUserIdByUniqueUrlParam(uniqueUrlParamParamMap);
    } catch (EmptyResultDataAccessException e) {
      return null;
    }
  }

  // visible for mocking
  Integer getUserIdByUniqueUrlParam(Map<String, String> uniqueUrlParamParamMap) {
    return getNamedParameterJdbcTemplate().queryForObject(
        SQL_SELECT_USER_ID_BY_UNIQUE_URL_PARAM, uniqueUrlParamParamMap,
        useridRowMapper);
  }
}
