package net.ccaper.graffitiTracker.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import net.ccaper.graffitiTracker.dao.ResetPasswordDao;
import net.ccaper.graffitiTracker.objects.UserSecurityQuestion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author ccaper
 * 
 * Implementation for JDBC version of the {@link net.ccaper.graffitiTracker.dao.ResetPassword}
 *
 */
@Repository("resetPasswordDao")
public class JdbcResetPasswordDaoImpl extends NamedParameterJdbcDaoSupport
    implements ResetPasswordDao {
  private final static String RESET_PASSWORD_TABLE = "reset_password";
  private static final String USER_ID_COL = JdbcAppUserDaoImpl.USER_ID_COL;
  private static final String ID_FK_COL = JdbcAppUserDaoImpl.ID_FK_COL;
  private static final String UNIQUE_URL_PARAM_COL = "unique_url_param";
  private static final String RESET_PASSWORD_TIMESTAMP_COL = "reset_password_timestamp";
  private static final String USERS_TABLE = JdbcAppUserDaoImpl.USERS_TABLE;
  private static final String USERNAME_COL = JdbcAppUserDaoImpl.USERNAME_COL;
  private static final String SECURITY_QUESTION_COL = JdbcAppUserDaoImpl.SECURITY_QUESTION_COL;
  private static final String SQL_INSERT_RESET_PASSWORD_BY_USERNAME = String
      .format(
          "INSERT INTO %s (%s, %s) VALUES ((SELECT %s FROM %s WHERE %s = :%s), UUID())",
          RESET_PASSWORD_TABLE, ID_FK_COL, UNIQUE_URL_PARAM_COL, USER_ID_COL,
          USERS_TABLE, USERNAME_COL, USERNAME_COL).toLowerCase();
  private static final String SQL_SELECT_UNIQUE_PARAM_BY_USERNAME = String
      .format(
          "SELECT %s FROM %s INNER JOIN %s ON %s.%s = %s.%s WHERE %s.%s = :%s "
              + "AND %s = (SELECT MAX(%s) FROM %s WHERE %s.%s = :%s)",
          UNIQUE_URL_PARAM_COL, RESET_PASSWORD_TABLE, USERS_TABLE,
          RESET_PASSWORD_TABLE, ID_FK_COL, USERS_TABLE, USER_ID_COL,
          USERS_TABLE, USERNAME_COL, USERNAME_COL,
          RESET_PASSWORD_TIMESTAMP_COL, RESET_PASSWORD_TIMESTAMP_COL,
          RESET_PASSWORD_TABLE, USERS_TABLE, USERNAME_COL, USERNAME_COL)
      .toLowerCase();
  private static final String SQL_SELECT_USER_ID_AND_SECURITY_QUESTION_BY_UNIQUE_URL_PARAM = String
      .format(
          "SELECT %s.%s, %s FROM %s INNER JOIN %s on %s.%s = %s.%s WHERE %s = :%s",
          USERS_TABLE, USER_ID_COL, SECURITY_QUESTION_COL,
          RESET_PASSWORD_TABLE, USERS_TABLE, RESET_PASSWORD_TABLE, ID_FK_COL,
          USERS_TABLE, USER_ID_COL, UNIQUE_URL_PARAM_COL, UNIQUE_URL_PARAM_COL)
      .toLowerCase();
  private static final String SQL_DELETE_BY_UNIQUE_URL_PARAM = String.format(
      "DELETE FROM %s WHERE %s = :%s", RESET_PASSWORD_TABLE,
      UNIQUE_URL_PARAM_COL, UNIQUE_URL_PARAM_COL).toLowerCase();
  private static final String SQL_DELETE_EXPIRED_RESET_PASSWORDS = String
      .format("DELETE %s FROM %s WHERE %s < (NOW() - INTERVAL 1 day)",
          RESET_PASSWORD_TABLE, RESET_PASSWORD_TABLE,
          RESET_PASSWORD_TIMESTAMP_COL).toLowerCase();

  private RowMapper<String> uniqueUrlParamRowMapper = new RowMapper<String>() {
    @Override
    public String mapRow(ResultSet rs, int rowNum) throws SQLException {
      return new String(rs.getString(UNIQUE_URL_PARAM_COL));
    }
  };

  private RowMapper<UserSecurityQuestion> securityQuestionRowMapper = new RowMapper<UserSecurityQuestion>() {
    @Override
    public UserSecurityQuestion mapRow(ResultSet rs, int rowNum)
        throws SQLException {
      UserSecurityQuestion userSecurityQuestion = new UserSecurityQuestion();
      userSecurityQuestion.setUserid(rs.getInt(USER_ID_COL));
      userSecurityQuestion.setSecurityQuestion(rs
          .getString(SECURITY_QUESTION_COL));
      return userSecurityQuestion;
    }
  };

  /**
   * Sets the ds.
   *
   * @param dataSource the new ds
   */
  @Autowired
  public void setDs(DataSource dataSource) {
    setDataSource(dataSource);
  }

  /* (non-Javadoc)
   * @see net.ccaper.graffitiTracker.dao.ResetPasswordDao#addResetPassword(java.lang.String)
   */
  @Override
  public void addResetPassword(String username) {
    Map<String, String> usernameParamMap = new HashMap<String, String>(1);
    usernameParamMap.put(USERNAME_COL, username);
    getNamedParameterJdbcTemplate().update(
        SQL_INSERT_RESET_PASSWORD_BY_USERNAME, usernameParamMap);
  }

  /* (non-Javadoc)
   * @see net.ccaper.graffitiTracker.dao.ResetPasswordDao#getUniqueUrlParamByUsername(java.lang.String)
   */
  @Override
  public String getUniqueUrlParamByUsername(String username) {
    Map<String, String> usernameParamMap = new HashMap<String, String>(1);
    usernameParamMap.put(USERNAME_COL, username);
    return getNamedParameterJdbcTemplate().queryForObject(
        SQL_SELECT_UNIQUE_PARAM_BY_USERNAME, usernameParamMap,
        uniqueUrlParamRowMapper);
  }

  /* (non-Javadoc)
   * @see net.ccaper.graffitiTracker.dao.ResetPasswordDao#getUserSecurityQuestionByUniqueUrlParam(java.lang.String)
   */
  @Override
  public UserSecurityQuestion getUserSecurityQuestionByUniqueUrlParam(
      String uniqueUrlParam) throws EmptyResultDataAccessException {
    Map<String, String> uniqueUrlParamParamMap = new HashMap<String, String>(1);
    uniqueUrlParamParamMap.put(UNIQUE_URL_PARAM_COL, uniqueUrlParam);
    return getUserSecurityQuestionByUniqueUrlParam(uniqueUrlParamParamMap);
  }

  // visible for mocking
  /**
   * Gets the user security question by unique url param.
   *
   * @param uniqueUrlParamParamMap the unique url param param map
   * @return the user security question by unique url param
   */
  UserSecurityQuestion getUserSecurityQuestionByUniqueUrlParam(
      Map<String, String> uniqueUrlParamParamMap) {
    return getNamedParameterJdbcTemplate().queryForObject(
        SQL_SELECT_USER_ID_AND_SECURITY_QUESTION_BY_UNIQUE_URL_PARAM,
        uniqueUrlParamParamMap, securityQuestionRowMapper);
  }

  /* (non-Javadoc)
   * @see net.ccaper.graffitiTracker.dao.ResetPasswordDao#deleteResetPasswordByUniqueUrlParam(java.lang.String)
   */
  @Override
  public void deleteResetPasswordByUniqueUrlParam(String uniqueUrlParam) {
    Map<String, String> uniqueUrlParamParamMap = new HashMap<String, String>(1);
    uniqueUrlParamParamMap.put(UNIQUE_URL_PARAM_COL, uniqueUrlParam);
    getNamedParameterJdbcTemplate().update(SQL_DELETE_BY_UNIQUE_URL_PARAM,
        uniqueUrlParamParamMap);
  }

  /* (non-Javadoc)
   * @see net.ccaper.graffitiTracker.dao.ResetPasswordDao#deleteResetPasswordWhereTimestampExpired()
   */
  @Override
  public void deleteResetPasswordWhereTimestampExpired() {
    getNamedParameterJdbcTemplate().update(SQL_DELETE_EXPIRED_RESET_PASSWORDS,
        new HashMap<String, String>(0));
  }
}
