package net.ccaper.GraffitiTracker.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import net.ccaper.GraffitiTracker.dao.RegistrationConfirmationsDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Repository;

@Repository("registrationConfirmationsDao")
public class JdbcRegistrationConfirmationsDaoImpl extends
NamedParameterJdbcDaoSupport implements RegistrationConfirmationsDao {
  static final String REGISTRATION_CONFIRMATIONS_TABLE = "registration_confirmations";
  private static final String USER_ID_COL = JdbcAppUserDaoImpl.USER_ID_COL;
  private static final String ID_FK_COL = JdbcAppUserDaoImpl.ID_FK_COL;
  private static final String UNIQUE_URL_PARAM_COL = "unique_url_param";
  private static final String USERS_TABLE = JdbcAppUserDaoImpl.USERS_TABLE;
  private static final String USERNAME_COL = JdbcAppUserDaoImpl.USERNAME_COL;
  private static final String SQL_INSERT_REGISTRATION_CONFIRMATION_BY_USERNAME = String
      .format(
          "INSERT INTO %s (%s, %s) VALUES ((SELECT %s FROM %s WHERE %s = :%s), UUID())",
          REGISTRATION_CONFIRMATIONS_TABLE, ID_FK_COL, UNIQUE_URL_PARAM_COL,
          USER_ID_COL, USERS_TABLE, USERNAME_COL, USERNAME_COL).toLowerCase();
  private static final String SQL_SELECT_UNIQUE_URL_PARAM_BY_USERNAME = String
      .format(
          "SELECT %s FROM %s INNER JOIN %s WHERE %s.%s = %s.%s AND %s = :%s",
          UNIQUE_URL_PARAM_COL, USERS_TABLE, REGISTRATION_CONFIRMATIONS_TABLE,
          USERS_TABLE, USER_ID_COL, REGISTRATION_CONFIRMATIONS_TABLE,
          ID_FK_COL, USERNAME_COL, USERNAME_COL).toLowerCase();
  private static final String SQL_DELETE_BY_UNIQUE_URL_PARAM = String.format(
      "DELETE FROM %s WHERE %s = :%s", REGISTRATION_CONFIRMATIONS_TABLE,
      UNIQUE_URL_PARAM_COL, UNIQUE_URL_PARAM_COL).toLowerCase();
  private static final String SQL_SELECT_USER_ID_BY_UNIQUE_URL_PARAM = String
      .format("SELECT %s FROM %s WHERE %s = :%s", ID_FK_COL,
          REGISTRATION_CONFIRMATIONS_TABLE, UNIQUE_URL_PARAM_COL,
          UNIQUE_URL_PARAM_COL).toLowerCase();

  RowMapper<String> uniqueUrlParamRowMapper = new RowMapper<String>() {
    @Override
    public String mapRow(ResultSet rs, int rowNum) throws SQLException {
      return new String(rs.getString(UNIQUE_URL_PARAM_COL));
    }
  };

  RowMapper<Integer> countRowMapper = new RowMapper<Integer>() {
    @Override
    public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
      return new Integer(rs.getInt(1));
    }
  };

  RowMapper<Integer> useridRowMapper = new RowMapper<Integer>() {
    @Override
    public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
      return new Integer(rs.getInt(ID_FK_COL));
    }
  };

  @Autowired
  public void setDs(DataSource dataSource) {
    setDataSource(dataSource);
  }

  @Override
  public void addRegistrationConfirmationByUsername(String username) {
    Map<String, String> registrationConfirmationParamMap = new HashMap<String, String>(1);
    registrationConfirmationParamMap.put(USERNAME_COL, username);
    getNamedParameterJdbcTemplate().update(
        SQL_INSERT_REGISTRATION_CONFIRMATION_BY_USERNAME,
        registrationConfirmationParamMap);
  }

  @Override
  public String getUniqueUrlParamByUsername(String username) {
    Map<String, String> userParamMap = new HashMap<String, String>(1);
    userParamMap.put(USERNAME_COL, username);
    return getNamedParameterJdbcTemplate().queryForObject(
        SQL_SELECT_UNIQUE_URL_PARAM_BY_USERNAME, userParamMap,
        uniqueUrlParamRowMapper);
  }

  @Override
  public void deleteRegistrationConfirmationByUniqueUrlParam(
      String uniqueUrlParam) {
    Map<String, String> uniqueUrlParamParamMap = new HashMap<String, String>(1);
    uniqueUrlParamParamMap.put(UNIQUE_URL_PARAM_COL, uniqueUrlParam);
    getNamedParameterJdbcTemplate().update(SQL_DELETE_BY_UNIQUE_URL_PARAM,
        uniqueUrlParamParamMap);
  }

  // TODO(ccaper): move logic out of dao and into service
  @Override
  public Integer getUserIdByUniqueUrlParam(String uniqueUrlParam) {
    Map<String, String> uniqueUrlParamParamMap = new HashMap<String, String>(1);
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
