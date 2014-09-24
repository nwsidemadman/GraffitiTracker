package net.ccaper.graffitiTracker.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import net.ccaper.graffitiTracker.dao.AppUserDao;
import net.ccaper.graffitiTracker.enums.RoleEnum;
import net.ccaper.graffitiTracker.objects.AppUser;
import net.ccaper.graffitiTracker.objects.Role;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author ccaper Implementation for the JDBC version of the
 *         {@link net.ccaper.graffitiTracker.dao.AppUserDao}
 * 
 */
@Repository("appUserDao")
public class JdbcAppUserDaoImpl extends NamedParameterJdbcDaoSupport implements
    AppUserDao {
  static final String USERS_TABLE = "app_users";
  static final String USER_ID_COL = "id";
  static final String ID_FK_COL = "app_user_id";
  static final String USERNAME_COL = "username";
  private static final String EMAIL_COL = "email";
  private static final String IS_ACTIVE_COL = "is_active";
  private static final String REGISTRATION_TIMESTAMP_COL = "registration_timestamp";
  private static final String PASSWORD_COL = "password";
  private static final String CURRENT_LOGIN_TIMESTAMP_COL = "current_login_timestamp";
  private static final String PREVIOUS_LOGIN_TIMESTAMP_COL = "previous_login_timestamp";
  private static final String LOGIN_COUNT_COL = "login_count";
  static final String SECURITY_QUESTION_COL = "security_question";
  private static final String SECURITY_ANSWER_COL = "security_answer";
  private static final String NUMBER_OF_DAYS = "number_of_days";
  private static final String REGISTRATION_CONFIRMATIONS_TABLE = JdbcRegistrationConfirmationsDaoImpl.REGISTRATION_CONFIRMATIONS_TABLE;
  private static final String SQL_SELECT_USER_BY_USERNAME = String.format(
      "SELECT %s, %s, %s, %s, %s, %s, %s, %s, %s, %s FROM %s WHERE %s = :%s",
      USER_ID_COL, USERNAME_COL, EMAIL_COL, IS_ACTIVE_COL,
      REGISTRATION_TIMESTAMP_COL, PASSWORD_COL, PREVIOUS_LOGIN_TIMESTAMP_COL,
      LOGIN_COUNT_COL, SECURITY_QUESTION_COL, SECURITY_ANSWER_COL, USERS_TABLE,
      USERNAME_COL, USERNAME_COL).toLowerCase();
  private static final String SQL_SELECT_ALL_USERS = String.format(
      "SELECT %s, %s, %s, %s, %s, %s, %s FROM %s", USER_ID_COL, USERNAME_COL,
      EMAIL_COL, IS_ACTIVE_COL, REGISTRATION_TIMESTAMP_COL,
      CURRENT_LOGIN_TIMESTAMP_COL, LOGIN_COUNT_COL, USERS_TABLE).toLowerCase();
  private static final String SQL_SELECT_USER_BY_ID = String.format(
      "SELECT %s, %s, %s, %s, %s, %s, %s FROM %s WHERE %s = :%s", USER_ID_COL,
      USERNAME_COL, EMAIL_COL, IS_ACTIVE_COL, REGISTRATION_TIMESTAMP_COL,
      CURRENT_LOGIN_TIMESTAMP_COL, LOGIN_COUNT_COL, USERS_TABLE, USER_ID_COL,
      USER_ID_COL).toLowerCase();
  private static final String SQL_INSERT_USER = String.format(
      "INSERT INTO %s (%s, %s, %s, %s, %s) VALUES (:%s, :%s, :%s, :%s, :%s)",
      USERS_TABLE, USERNAME_COL, EMAIL_COL, PASSWORD_COL,
      SECURITY_QUESTION_COL, SECURITY_ANSWER_COL, USERNAME_COL, EMAIL_COL,
      PASSWORD_COL, SECURITY_QUESTION_COL, SECURITY_ANSWER_COL).toLowerCase();
  private static final String SQL_DOES_USERNAME_EXIST = String
      .format(
          "SELECT CASE WHEN (SELECT COUNT(%s) FROM %S WHERE %S = :%s) > 0 THEN TRUE ELSE FALSE END",
          USERNAME_COL, USERS_TABLE, USERNAME_COL, USERNAME_COL).toLowerCase();
  private static final String SQL_DOES_EMAIL_EXIST = String
      .format(
          "SELECT CASE WHEN (SELECT COUNT(%s) FROM %S WHERE %S = :%s) > 0 THEN TRUE ELSE FALSE END",
          EMAIL_COL, USERS_TABLE, EMAIL_COL, EMAIL_COL).toLowerCase();
  private static final String SQL_SELECT_USERNAME_BY_EMAIL = String.format(
      "SELECT %s FROM %s WHERE %s = :%s and %s = 1;", USERNAME_COL,
      USERS_TABLE, EMAIL_COL, EMAIL_COL, IS_ACTIVE_COL).toLowerCase();
  private static final String SQL_SELECT_EMAIL_BY_USERNAME = String.format(
      "SELECT %s FROM %s WHERE %s = :%s and %s = 1;", EMAIL_COL, USERS_TABLE,
      USERNAME_COL, USERNAME_COL, IS_ACTIVE_COL).toLowerCase();
  private static final String SQL_UPDATE_LOGIN_TIMESTAMPS = String
      .format(
          "UPDATE %s SET %s = (SELECT %s FROM (SELECT * FROM %s) AS c1 WHERE c1.%s = :%s), %s = current_timestamp, %s = %s + 1 WHERE %s = :%s",
          USERS_TABLE, PREVIOUS_LOGIN_TIMESTAMP_COL,
          CURRENT_LOGIN_TIMESTAMP_COL, USERS_TABLE, USERNAME_COL, USERNAME_COL,
          CURRENT_LOGIN_TIMESTAMP_COL, LOGIN_COUNT_COL, LOGIN_COUNT_COL,
          USERNAME_COL, USERNAME_COL).toLowerCase();
  private static final String SQL_UPDATE_ISACTIVE_BY_USERID = String.format(
      "UPDATE %s SET %s = :%s WHERE %s = :%s", USERS_TABLE, IS_ACTIVE_COL,
      IS_ACTIVE_COL, USER_ID_COL, USER_ID_COL).toLowerCase();
  private static final String SQL_DELETE_EXPIRED_REGISTRATION_USERS = String
      .format(
          "DELETE %s FROM %s INNER JOIN %s ON %s.%s = %s.%s WHERE %s.%s = 0 AND %s.%s < (NOW() - INTERVAL 2 day)",
          USERS_TABLE, USERS_TABLE, REGISTRATION_CONFIRMATIONS_TABLE,
          USERS_TABLE, USER_ID_COL, REGISTRATION_CONFIRMATIONS_TABLE,
          ID_FK_COL, USERS_TABLE, IS_ACTIVE_COL, USERS_TABLE,
          REGISTRATION_TIMESTAMP_COL).toLowerCase();
  private static final String SQL_SELECT_COUNT_NEW_USERS_IN_LAST_X_DAYS = String
      .format(
          "SELECT COUNT(*) FROM %s WHERE %s = 1 AND %s > (NOW() - INTERVAL :%s day)",
          USERS_TABLE, IS_ACTIVE_COL, REGISTRATION_TIMESTAMP_COL,
          NUMBER_OF_DAYS).toLowerCase();
  private static final String SQL_SELECT_COUNT_LOGINS_IN_LAST_X_DAYS = String
      .format(
          "SELECT COUNT(*) FROM %s WHERE %s = 1 AND %s > (NOW() - INTERVAL :%s day)",
          USERS_TABLE, IS_ACTIVE_COL, CURRENT_LOGIN_TIMESTAMP_COL,
          NUMBER_OF_DAYS).toLowerCase();
  private static final String SQL_SELECT_COUNT_UNCONFIRMED_USERS_IN_LAST_X_DAYS = String
      .format(
          "select count(*) from %s inner join %s on %s.%s = %s.%s where %s = 0 and %s > (NOW() - INTERVAL :%s DAY)",
          USERS_TABLE, REGISTRATION_CONFIRMATIONS_TABLE, USERS_TABLE,
          USER_ID_COL, REGISTRATION_CONFIRMATIONS_TABLE, ID_FK_COL,
          IS_ACTIVE_COL, REGISTRATION_TIMESTAMP_COL, NUMBER_OF_DAYS);
  private static final String SQL_SELECT_SECURITY_ANSWER_BY_USER_ID = String
      .format("SELECT %s FROM %s WHERE %s = :%s", SECURITY_ANSWER_COL,
          USERS_TABLE, USER_ID_COL, USER_ID_COL).toLowerCase();
  private static final String SQL_SELECT_USERNAME_BY_USER_ID = String.format(
      "SELECT %s FROM %s WHERE %s = :%s", USERNAME_COL, USERS_TABLE,
      USER_ID_COL, USER_ID_COL).toLowerCase();
  private static final String SQL_UPDATE_PASSWORD_BY_USER_ID = String.format(
      "UPDATE %s SET %s = :%s WHERE %s = :%s", USERS_TABLE, PASSWORD_COL,
      PASSWORD_COL, USER_ID_COL, USER_ID_COL).toLowerCase();
  private static final String SQL_UPDATE_SECURITY_QUESTION_BY_USER_ID = String
      .format("UPDATE %s SET %s = :%s WHERE %s = :%s", USERS_TABLE,
          SECURITY_QUESTION_COL, SECURITY_QUESTION_COL, USER_ID_COL,
          USER_ID_COL).toLowerCase();
  private static final String SQL_UPDATE_SECURITY_ANSWER_BY_USER_ID = String
      .format("UPDATE %s SET %s = :%s WHERE %s = :%s", USERS_TABLE,
          SECURITY_ANSWER_COL, SECURITY_ANSWER_COL, USER_ID_COL, USER_ID_COL)
      .toLowerCase();
  private static final String SQL_UPDATE_EMAIL_BY_USER_ID = String.format(
      "UPDATE %s SET %s = :%s WHERE %s = :%s", USERS_TABLE, EMAIL_COL,
      EMAIL_COL, USER_ID_COL, USER_ID_COL).toLowerCase();
  private static final String ROLES_TABLE = "roles";
  private static final String ROLE_COL = "role";
  private static final String ROLE_GRANTED_TIMESTAMP_COL = "role_granted_timestamp";
  private static final String SQL_SELECT_ROLES_BY_USER_ID = String.format(
      "SELECT %s, %s FROM %s where %s = :%s ORDER BY %S ASC", ROLE_COL,
      ROLE_GRANTED_TIMESTAMP_COL, ROLES_TABLE, ID_FK_COL, ID_FK_COL, ROLE_COL)
      .toLowerCase();
  private static final String SQL_INSERT_ROLE = String.format(
      "INSERT INTO %s (%s) VALUES ((SELECT %s FROM %s WHERE %S = :%s))",
      ROLES_TABLE, ID_FK_COL, USER_ID_COL, USERS_TABLE, USERNAME_COL,
      USERNAME_COL).toLowerCase();
  private static final String SQL_SELECT_SUPERADMIN_EMAILS = String.format(
      "SELECT %s FROM %s INNER JOIN %S ON %s.%s = %s.%s WHERE %s.%s = '%s'",
      EMAIL_COL, USERS_TABLE, ROLES_TABLE, USERS_TABLE, USER_ID_COL,
      ROLES_TABLE, ID_FK_COL, ROLES_TABLE, ROLE_COL,
      RoleEnum.SUPERADMIN.getDbString()).toLowerCase();
  private static final String SQL_INSERT_ROLES = String.format(
      "INSERT INTO %s (%s, %s) VALUES ", ROLES_TABLE, ID_FK_COL, ROLE_COL)
      .toLowerCase();
  private static final String SQL_DELETE_ROLES = String.format(
      "DELETE FROM %s WHERE %s = :%s and %s in ", ROLES_TABLE, ID_FK_COL,
      ID_FK_COL, ROLE_COL).toLowerCase();

  private RowMapper<AppUser> appUserRowMapper = new RowMapper<AppUser>() {
    @Override
    public AppUser mapRow(ResultSet rs, int rowNum) throws SQLException {
      AppUser appUser = new AppUser();
      appUser.setUserId(rs.getInt(USER_ID_COL));
      appUser.setUsername(rs.getString(USERNAME_COL));
      appUser.setEmail(rs.getString(EMAIL_COL));
      appUser.setIsActive(rs.getBoolean(IS_ACTIVE_COL));
      appUser.setRegisterTimestamp(rs.getTimestamp(REGISTRATION_TIMESTAMP_COL));
      appUser.setPassword(rs.getString(PASSWORD_COL));
      appUser.setPreviousLoginTimestamp(rs
          .getTimestamp(PREVIOUS_LOGIN_TIMESTAMP_COL));
      appUser.setLoginCount(rs.getInt(LOGIN_COUNT_COL));
      appUser.setSecurityQuestion(rs.getString(SECURITY_QUESTION_COL));
      appUser.setSecurityAnswer(rs.getString(SECURITY_ANSWER_COL));
      return appUser;
    }
  };

  private RowMapper<AppUser> appUserAdminViewRowMapper = new RowMapper<AppUser>() {
    @Override
    public AppUser mapRow(ResultSet rs, int rowNum) throws SQLException {
      AppUser appUser = new AppUser();
      appUser.setUserId(rs.getInt(USER_ID_COL));
      appUser.setUsername(rs.getString(USERNAME_COL));
      appUser.setEmail(rs.getString(EMAIL_COL));
      appUser.setIsActive(rs.getBoolean(IS_ACTIVE_COL));
      appUser.setRegisterTimestamp(rs.getTimestamp(REGISTRATION_TIMESTAMP_COL));
      appUser.setCurrentLoginTimestamp(rs
          .getTimestamp(CURRENT_LOGIN_TIMESTAMP_COL));
      appUser.setLoginCount(rs.getInt(LOGIN_COUNT_COL));
      return appUser;
    }
  };

  private RowMapper<Integer> countRowMapper = new RowMapper<Integer>() {
    @Override
    public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
      return new Integer(rs.getInt(1));
    }
  };

  private RowMapper<Boolean> booleanRowMapper = new RowMapper<Boolean>() {
    @Override
    public Boolean mapRow(ResultSet rs, int rowNum) throws SQLException {
      return rs.getBoolean(1);
    }
  };

  private RowMapper<Role> rolesRowMapper = new RowMapper<Role>() {
    @Override
    public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
      Role role = new Role();
      role.setRole(RoleEnum.getRoleEnumFromDbString(rs.getString(ROLE_COL)));
      role.setGrantedTimestamp(rs.getTimestamp(ROLE_GRANTED_TIMESTAMP_COL));
      return role;
    }
  };

  private RowMapper<String> emailRowMapper = new RowMapper<String>() {
    @Override
    public String mapRow(ResultSet rs, int rowNum) throws SQLException {
      return rs.getString(EMAIL_COL);
    }
  };

  private RowMapper<String> usernameRowMapper = new RowMapper<String>() {
    @Override
    public String mapRow(ResultSet rs, int rowNum) throws SQLException {
      return rs.getString(USERNAME_COL);
    }
  };

  private RowMapper<String> securityAnswerRowMapper = new RowMapper<String>() {
    @Override
    public String mapRow(ResultSet rs, int rowNum) throws SQLException {
      return rs.getString(SECURITY_ANSWER_COL);
    }
  };

  /**
   * Sets the ds.
   * 
   * @param dataSource
   *          the new ds
   */
  @Autowired
  public void setDs(DataSource dataSource) {
    setDataSource(dataSource);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * net.ccaper.graffitiTracker.dao.AppUserDao#getAppUserByUsername(java.lang
   * .String)
   */
  @Override
  public AppUser getAppUserByUsername(String username) {
    Map<String, String> userParamMap = new HashMap<String, String>(1);
    userParamMap.put(USERNAME_COL, username);
    AppUser appUser = getNamedParameterJdbcTemplate().queryForObject(
        SQL_SELECT_USER_BY_USERNAME, userParamMap, appUserRowMapper);
    Map<String, Integer> rolesParamMap = new HashMap<String, Integer>(1);
    rolesParamMap.put(ID_FK_COL, appUser.getUserId());
    List<Role> roles = getNamedParameterJdbcTemplate().query(
        SQL_SELECT_ROLES_BY_USER_ID, rolesParamMap, rolesRowMapper);
    appUser.setRoles(roles);
    return appUser;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * net.ccaper.graffitiTracker.dao.AppUserDao#addAppUser(net.ccaper.graffitiTracker
   * .objects.AppUser)
   */
  @Override
  public void addAppUser(AppUser appUser) {
    Map<String, String> userParamMap = new HashMap<String, String>(5);
    userParamMap.put(USERNAME_COL, appUser.getUsername());
    userParamMap.put(EMAIL_COL, appUser.getEmail());
    userParamMap.put(PASSWORD_COL, appUser.getPassword());
    userParamMap.put(SECURITY_QUESTION_COL, appUser.getSecurityQuestion());
    userParamMap.put(SECURITY_ANSWER_COL, appUser.getSecurityAnswer());
    getNamedParameterJdbcTemplate().update(SQL_INSERT_USER, userParamMap);
    Map<String, String> roleParamMap = new HashMap<String, String>(1);
    roleParamMap.put(USERNAME_COL, appUser.getUsername());
    getNamedParameterJdbcTemplate().update(SQL_INSERT_ROLE, roleParamMap);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * net.ccaper.graffitiTracker.dao.AppUserDao#doesUsernameExist(java.lang.String
   * )
   */
  @Override
  public boolean doesUsernameExist(String username) {
    Map<String, String> userParamMap = new HashMap<String, String>(1);
    userParamMap.put(USERNAME_COL, username);
    return getNamedParameterJdbcTemplate().queryForObject(
        SQL_DOES_USERNAME_EXIST, userParamMap, booleanRowMapper);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * net.ccaper.graffitiTracker.dao.AppUserDao#doesEmailExist(java.lang.String)
   */
  @Override
  public boolean doesEmailExist(String email) {
    Map<String, String> userParamMap = new HashMap<String, String>(1);
    userParamMap.put(EMAIL_COL, email);
    return getNamedParameterJdbcTemplate().queryForObject(SQL_DOES_EMAIL_EXIST,
        userParamMap, booleanRowMapper);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * net.ccaper.graffitiTracker.dao.AppUserDao#updateLoginTimestamps(java.lang
   * .String)
   */
  @Override
  public void updateLoginTimestamps(String username) {
    Map<String, String> userParamMap = new HashMap<String, String>(1);
    userParamMap.put(USERNAME_COL, username);
    getNamedParameterJdbcTemplate().update(SQL_UPDATE_LOGIN_TIMESTAMPS,
        userParamMap);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.ccaper.graffitiTracker.dao.AppUserDao#updateIsActiveByUserid(int,
   * boolean)
   */
  @Override
  public void updateIsActiveByUserid(int userid, boolean isActive) {
    Map<String, Object> useridParamMap = new HashMap<String, Object>(2);
    useridParamMap.put(USER_ID_COL, userid);
    useridParamMap.put(IS_ACTIVE_COL, isActive);
    getNamedParameterJdbcTemplate().update(SQL_UPDATE_ISACTIVE_BY_USERID,
        useridParamMap);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * net.ccaper.graffitiTracker.dao.AppUserDao#deleteAppUsersWhenRegistrationExpired
   * ()
   */
  @Override
  public void deleteAppUsersWhenRegistrationExpired() {
    getNamedParameterJdbcTemplate().update(
        SQL_DELETE_EXPIRED_REGISTRATION_USERS, new HashMap<String, String>(0));
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.ccaper.graffitiTracker.dao.AppUserDao#getCountNewUsers(int)
   */
  @Override
  public int getCountNewUsers(int numberOfDays) {
    Map<String, Integer> numberOfDaysParamMap = new HashMap<String, Integer>(1);
    numberOfDaysParamMap.put(NUMBER_OF_DAYS, numberOfDays);
    return getNamedParameterJdbcTemplate().queryForObject(
        SQL_SELECT_COUNT_NEW_USERS_IN_LAST_X_DAYS, numberOfDaysParamMap,
        countRowMapper);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.ccaper.graffitiTracker.dao.AppUserDao#getCountLogins(int)
   */
  @Override
  public int getCountLogins(int numberOfDays) {
    Map<String, Integer> numberOfDaysParamMap = new HashMap<String, Integer>(1);
    numberOfDaysParamMap.put(NUMBER_OF_DAYS, numberOfDays);
    return getNamedParameterJdbcTemplate().queryForObject(
        SQL_SELECT_COUNT_LOGINS_IN_LAST_X_DAYS, numberOfDaysParamMap,
        countRowMapper);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * net.ccaper.graffitiTracker.dao.AppUserDao#getCountUnconfirmedUsers(int)
   */
  @Override
  public int getCountUnconfirmedUsers(int numberOfDays) {
    Map<String, Integer> numberOfDaysParamMap = new HashMap<String, Integer>(1);
    numberOfDaysParamMap.put(NUMBER_OF_DAYS, numberOfDays);
    return getNamedParameterJdbcTemplate().queryForObject(
        SQL_SELECT_COUNT_UNCONFIRMED_USERS_IN_LAST_X_DAYS,
        numberOfDaysParamMap, countRowMapper);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.ccaper.graffitiTracker.dao.AppUserDao#getSuperAdminEmails()
   */
  @Override
  public List<String> getSuperAdminEmails() {
    return getNamedParameterJdbcTemplate().query(SQL_SELECT_SUPERADMIN_EMAILS,
        emailRowMapper);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * net.ccaper.graffitiTracker.dao.AppUserDao#getUsernameByEmail(java.lang.
   * String)
   */
  @Override
  public String getUsernameByEmail(String email)
      throws EmptyResultDataAccessException {
    Map<String, String> emailParamMap = new HashMap<String, String>(1);
    emailParamMap.put(EMAIL_COL, email);
    return getUsernameByEmail(emailParamMap);
  }

  // visible for mocking
  /**
   * Gets the username by email.
   * 
   * @param emailParamMap
   *          the email param map
   * @return the username by email
   */
  String getUsernameByEmail(Map<String, String> emailParamMap) {
    return getNamedParameterJdbcTemplate().queryForObject(
        SQL_SELECT_USERNAME_BY_EMAIL, emailParamMap, usernameRowMapper);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * net.ccaper.graffitiTracker.dao.AppUserDao#getEmailByUsername(java.lang.
   * String)
   */
  @Override
  public String getEmailByUsername(String username)
      throws EmptyResultDataAccessException {
    Map<String, String> usernameParamMap = new HashMap<String, String>(1);
    usernameParamMap.put(USERNAME_COL, username);
    return getEmailByUsername(usernameParamMap);
  }

  // visible for mocking
  /**
   * Gets the email by username.
   * 
   * @param usernameParamMap
   *          the username param map
   * @return the email by username
   */
  String getEmailByUsername(Map<String, String> usernameParamMap) {
    return getNamedParameterJdbcTemplate().queryForObject(
        SQL_SELECT_EMAIL_BY_USERNAME, usernameParamMap, emailRowMapper);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * net.ccaper.graffitiTracker.dao.AppUserDao#getSecurityAnswerByUserid(int)
   */
  @Override
  public String getSecurityAnswerByUserid(int userid) {
    Map<String, Integer> userIdParamMap = new HashMap<String, Integer>(1);
    userIdParamMap.put(USER_ID_COL, userid);
    return getNamedParameterJdbcTemplate().queryForObject(
        SQL_SELECT_SECURITY_ANSWER_BY_USER_ID, userIdParamMap,
        securityAnswerRowMapper);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.ccaper.graffitiTracker.dao.AppUserDao#getUsernameByUserid(int)
   */
  @Override
  public String getUsernameByUserid(int userid) {
    Map<String, Integer> userIdParamMap = new HashMap<String, Integer>(1);
    userIdParamMap.put(USER_ID_COL, userid);
    return getNamedParameterJdbcTemplate().queryForObject(
        SQL_SELECT_USERNAME_BY_USER_ID, userIdParamMap, usernameRowMapper);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.ccaper.graffitiTracker.dao.AppUserDao#updatePasswordByUserid(int,
   * java.lang.String)
   */
  @Override
  public void updatePasswordByUserid(int userid, String passwordEncoded) {
    Map<String, Object> paramMap = new HashMap<String, Object>(2);
    paramMap.put(USER_ID_COL, userid);
    paramMap.put(PASSWORD_COL, passwordEncoded);
    getNamedParameterJdbcTemplate().update(SQL_UPDATE_PASSWORD_BY_USER_ID,
        paramMap);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.ccaper.graffitiTracker.dao.AppUserDao#updateEmailByUserid(int,
   * java.lang.String)
   */
  @Override
  public void updateEmailByUserid(int userid, String email) {
    Map<String, Object> paramMap = new HashMap<String, Object>(2);
    paramMap.put(USER_ID_COL, userid);
    paramMap.put(EMAIL_COL, email);
    getNamedParameterJdbcTemplate().update(SQL_UPDATE_EMAIL_BY_USER_ID,
        paramMap);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * net.ccaper.graffitiTracker.dao.AppUserDao#updateSecurityQuestionByUserid
   * (int, java.lang.String)
   */
  @Override
  public void updateSecurityQuestionByUserid(int userid, String securityQuestion) {
    Map<String, Object> paramMap = new HashMap<String, Object>(2);
    paramMap.put(USER_ID_COL, userid);
    paramMap.put(SECURITY_QUESTION_COL, securityQuestion);
    getNamedParameterJdbcTemplate().update(
        SQL_UPDATE_SECURITY_QUESTION_BY_USER_ID, paramMap);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * net.ccaper.graffitiTracker.dao.AppUserDao#updateSecurityAnswerByUserid(int,
   * java.lang.String)
   */
  @Override
  public void updateSecurityAnswerByUserid(int userid, String securityAnswer) {
    Map<String, Object> paramMap = new HashMap<String, Object>(2);
    paramMap.put(USER_ID_COL, userid);
    paramMap.put(SECURITY_ANSWER_COL, securityAnswer);
    getNamedParameterJdbcTemplate().update(
        SQL_UPDATE_SECURITY_ANSWER_BY_USER_ID, paramMap);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.ccaper.graffitiTracker.dao.AppUserDao#getAllUsers()
   */
  @Override
  public List<AppUser> getAllUsers() {
    List<AppUser> users = getNamedParameterJdbcTemplate().query(
        SQL_SELECT_ALL_USERS, appUserAdminViewRowMapper);
    for (AppUser user : users) {
      Map<String, Integer> rolesParamMap = new HashMap<String, Integer>(1);
      rolesParamMap.put(ID_FK_COL, user.getUserId());
      List<Role> roles = getNamedParameterJdbcTemplate().query(
          SQL_SELECT_ROLES_BY_USER_ID, rolesParamMap, rolesRowMapper);
      user.setRoles(roles);
    }
    return users;
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.ccaper.graffitiTracker.dao.AppUserDao#getAppUserById(int)
   */
  @Override
  public AppUser getAppUserById(int id) throws EmptyResultDataAccessException {
    AppUser user = getAppUserByIdNoRoles(id);
    List<Role> roles = getRolesById(id);
    user.setRoles(roles);
    return user;
  }

  // visible for testing
  /**
   * Gets the {@link net.ccaper.graffitiTracker.objects.AppUser} by id with no
   * roles.
   * 
   * @param id
   *          the id
   * @return the {@link net.ccaper.graffitiTracker.objects.AppUser} by id with
   *         no roles
   * @throws EmptyResultDataAccessException
   *           when empty
   */
  AppUser getAppUserByIdNoRoles(int id) throws EmptyResultDataAccessException {
    Map<String, Integer> idParamMap = new HashMap<String, Integer>(1);
    idParamMap.put(USER_ID_COL, id);
    return getNamedParameterJdbcTemplate().queryForObject(
        SQL_SELECT_USER_BY_ID, idParamMap, appUserAdminViewRowMapper);
  }

  // visible for testing
  /**
   * Gets the {@link net.ccaper.graffitiTracker.objects.Role}s by userid.
   * 
   * @param id
   *          the userid
   * @return the {@link net.ccaper.graffitiTracker.objects.Role}s by id
   */
  List<Role> getRolesById(int id) {
    Map<String, Integer> rolesParamMap = new HashMap<String, Integer>(1);
    rolesParamMap.put(ID_FK_COL, id);
    return getNamedParameterJdbcTemplate().query(SQL_SELECT_ROLES_BY_USER_ID,
        rolesParamMap, rolesRowMapper);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.ccaper.graffitiTracker.dao.AppUserDao#addRolesByUserid(int,
   * java.util.List)
   */
  @Override
  public void addRolesByUserid(int id, List<RoleEnum> roleAdditions) {
    String sql = SQL_INSERT_ROLES
        + generateInsertRolesValues(id, roleAdditions);
    getNamedParameterJdbcTemplate().update(sql, new HashMap<String, String>(0));
  }

  // visible for testing
  /**
   * Generate insert roles values portion of sql statement.
   * 
   * @param id
   *          the userid
   * @param roleAdditions
   *          the {@link net.ccaper.graffitiTracker.objects.Role} additions
   * @return the insert {@link net.ccaper.graffitiTracker.objects.Role}s values
   *         portion of sql statement
   */
  static String generateInsertRolesValues(int id, List<RoleEnum> roleAdditions) {
    if (roleAdditions == null) {
      roleAdditions = new ArrayList<RoleEnum>(0);
    }
    List<String> values = new ArrayList<String>(roleAdditions.size());
    for (RoleEnum role : roleAdditions) {
      values.add(String.format("(%d, '%s')", id, role.getDbString()));
    }
    return StringUtils.join(values, ", ");
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.ccaper.graffitiTracker.dao.AppUserDao#deleteRolesByUserid(int,
   * java.util.List)
   */
  @Override
  public void deleteRolesByUserid(int id, List<RoleEnum> roleDeletions) {
    String sql = SQL_DELETE_ROLES + generateDeleteRolesIns(roleDeletions);
    Map<String, Integer> paramMap = new HashMap<String, Integer>(1);
    paramMap.put(ID_FK_COL, id);
    getNamedParameterJdbcTemplate().update(sql, paramMap);
  }

  // visible for testing
  /**
   * Generate delete roles ins portion of the sql statement.
   * 
   * @param roleDeletions
   *          the {@link net.ccaper.graffitiTracker.objects.Role} deletions
   * @return the roles ins portion of the sql statement
   */
  static String generateDeleteRolesIns(List<RoleEnum> roleDeletions) {
    if (roleDeletions == null) {
      roleDeletions = new ArrayList<RoleEnum>(0);
    }
    List<String> ins = new ArrayList<String>(roleDeletions.size());
    for (RoleEnum role : roleDeletions) {
      ins.add(String.format("'%s'", role.getDbString()));
    }
    return "(" + StringUtils.join(ins, ", ") + ")";
  }
}
