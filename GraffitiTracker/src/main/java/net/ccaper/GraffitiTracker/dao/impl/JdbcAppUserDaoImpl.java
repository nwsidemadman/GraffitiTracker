package net.ccaper.GraffitiTracker.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import net.ccaper.GraffitiTracker.dao.AppUserDao;
import net.ccaper.GraffitiTracker.enums.RoleEnum;
import net.ccaper.GraffitiTracker.objects.AppUser;
import net.ccaper.GraffitiTracker.objects.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Repository;

@Repository("appUserDao")
public class JdbcAppUserDaoImpl extends NamedParameterJdbcDaoSupport implements
AppUserDao {
  static final String USERS_TABLE = "app_users";
  static final String USER_ID_COL = "user_id";
  static final String USERNAME_COL = "username";
  private static final String EMAIL_COL = "email";
  private static final String IS_ACTIVE_COL = "is_active";
  private static final String REGISTER_TIMESTAMP_COL = "register_timestamp";
  private static final String PASSWORD_COL = "password";
  private static final String CURRENT_LOGIN_TIMESTAMP_COL = "current_login_timestamp";
  private static final String PREVIOUS_LOGIN_TIMESTAMP_COL = "previous_login_timestamp";
  private static final String LOGIN_COUNT_COL = "login_count";
  private static final String SQL_SELECT_USER_BY_USERNAME = String.format(
      "SELECT %s, %s, %s, %s, %s, %s, %s, %s FROM %s WHERE %s = :%s",
      USER_ID_COL, USERNAME_COL, EMAIL_COL, IS_ACTIVE_COL,
      REGISTER_TIMESTAMP_COL, PASSWORD_COL, PREVIOUS_LOGIN_TIMESTAMP_COL,
      LOGIN_COUNT_COL, USERS_TABLE, USERNAME_COL, USERNAME_COL).toLowerCase();
  private static final String SQL_INSERT_USER = String.format(
      "INSERT INTO %s (%s, %s, %s) VALUES (:%s, :%s, :%s)", USERS_TABLE,
      USERNAME_COL, EMAIL_COL, PASSWORD_COL, USERNAME_COL, EMAIL_COL,
      PASSWORD_COL).toLowerCase();
  private static final String SQL_SELECT_COUNT_USERNAME = String.format(
      "SELECT COUNT(%s) FROM %S WHERE %S = :%s", USERNAME_COL, USERS_TABLE,
      USERNAME_COL, USERNAME_COL).toLowerCase();
  private static final String SQL_SELECT_COUNT_EMAIL = String.format(
      "SELECT COUNT(%s) FROM %S WHERE %S = :%s", EMAIL_COL, USERS_TABLE,
      EMAIL_COL, EMAIL_COL).toLowerCase();
  private static final String SQL_UPDATE_LOGIN_TIMESTAMPS = String
      .format(
          "UPDATE %s SET %s = (SELECT %s FROM (SELECT * FROM %s) AS c1 WHERE c1.%s = :%s), %s = current_timestamp, %s = %s + 1 WHERE %s = :%s",
          USERS_TABLE, PREVIOUS_LOGIN_TIMESTAMP_COL,
          CURRENT_LOGIN_TIMESTAMP_COL, USERS_TABLE, USERNAME_COL, USERNAME_COL,
          CURRENT_LOGIN_TIMESTAMP_COL, LOGIN_COUNT_COL, LOGIN_COUNT_COL,
          USERNAME_COL, USERNAME_COL).toLowerCase();
  private static final String SQL_UPDATE_APPUSER_AS_ACTIVE = String.format(
      "UPDATE %s SET %S = 1 WHERE %s = :%s",
      USERS_TABLE, IS_ACTIVE_COL, USER_ID_COL, USER_ID_COL).toLowerCase();
  private static final String ROLES_TABLE = "roles";
  private static final String ROLE_COL = "role";
  private static final String ROLE_GRANTED_TIMESTAMP_COL = "role_granted_timestamp";
  private static final String SQL_SELECT_ROLES_BY_USER_ID = String.format(
      "SELECT %s, %s FROM %s where %s = :%s", ROLE_COL,
      ROLE_GRANTED_TIMESTAMP_COL, ROLES_TABLE, USER_ID_COL, USER_ID_COL).toLowerCase();
  private static final String SQL_INSERT_ROLE = String.format(
      "INSERT INTO %s (%s) VALUES ((SELECT %s FROM %s WHERE %S = :%s))",
      ROLES_TABLE, USER_ID_COL, USER_ID_COL, USERS_TABLE, USERNAME_COL,
      USERNAME_COL).toLowerCase();

  RowMapper<AppUser> appUserRowMapper = new RowMapper<AppUser>() {
    @Override
    public AppUser mapRow(ResultSet rs, int rowNum) throws SQLException {
      AppUser appUser = new AppUser();
      appUser.setUserId(rs.getInt(USER_ID_COL));
      appUser.setUsername(rs.getString(USERNAME_COL));
      appUser.setEmail(rs.getString(EMAIL_COL));
      appUser.setIsActive(rs.getBoolean(IS_ACTIVE_COL));
      appUser.setRegisterTimestamp(rs.getTimestamp(REGISTER_TIMESTAMP_COL));
      appUser.setPassword(rs.getString(PASSWORD_COL));
      appUser.setPreviousLoginTimestamp(rs
          .getTimestamp(PREVIOUS_LOGIN_TIMESTAMP_COL));
      appUser.setLoginCount(rs.getInt(LOGIN_COUNT_COL));
      return appUser;
    }
  };

  RowMapper<Integer> countRowMapper = new RowMapper<Integer>() {
    @Override
    public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
      return new Integer(rs.getInt(1));
    }
  };

  RowMapper<Role> rolesRowMapper = new RowMapper<Role>() {
    @Override
    public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
      Role role = new Role();
      role.setRole(RoleEnum.getRoleEnumFromDbString(rs.getString(ROLE_COL)));
      role.setGrantedTimestamp(rs.getTimestamp(ROLE_GRANTED_TIMESTAMP_COL));
      return role;
    }
  };

  @Autowired
  public void setDs(DataSource dataSource) {
    setDataSource(dataSource);
  }

  @Override
  public AppUser getAppUserByUsername(String username) {
    Map<String, String> userParamMap = new HashMap<String, String>();
    userParamMap.put(USERNAME_COL, username);
    AppUser appUser = getNamedParameterJdbcTemplate().queryForObject(
        SQL_SELECT_USER_BY_USERNAME, userParamMap, appUserRowMapper);
    Map<String, Integer> rolesParamMap = new HashMap<String, Integer>();
    rolesParamMap.put(USER_ID_COL, appUser.getUserId());
    List<Role> roles = getNamedParameterJdbcTemplate().query(
        SQL_SELECT_ROLES_BY_USER_ID, rolesParamMap, rolesRowMapper);
    appUser.setRoles(new HashSet<Role>(roles));
    return appUser;
  }

  @Override
  public void addAppUser(AppUser appUser) {
    Map<String, String> userParamMap = new HashMap<String, String>();
    userParamMap.put(USERNAME_COL, appUser.getUsername());
    userParamMap.put(EMAIL_COL, appUser.getEmail());
    userParamMap.put(PASSWORD_COL, appUser.getPassword());
    getNamedParameterJdbcTemplate().update(SQL_INSERT_USER, userParamMap);
    Map<String, String> roleParamMap = new HashMap<String, String>();
    roleParamMap.put(USERNAME_COL, appUser.getUsername());
    getNamedParameterJdbcTemplate().update(SQL_INSERT_ROLE, roleParamMap);
  }

  @Override
  public int getCountUsernames(String username) {
    Map<String, String> userParamMap = new HashMap<String, String>();
    userParamMap.put(USERNAME_COL, username);
    return getNamedParameterJdbcTemplate().queryForObject(
        SQL_SELECT_COUNT_USERNAME, userParamMap, countRowMapper);
  }

  @Override
  public int getCountEmails(String email) {
    Map<String, String> userParamMap = new HashMap<String, String>();
    userParamMap.put(EMAIL_COL, email);
    return getNamedParameterJdbcTemplate().queryForObject(
        SQL_SELECT_COUNT_EMAIL, userParamMap, countRowMapper);
  }

  @Override
  public void updateLoginTimestamps(String username) {
    Map<String, String> userParamMap = new HashMap<String, String>();
    userParamMap.put(USERNAME_COL, username);
    getNamedParameterJdbcTemplate().update(SQL_UPDATE_LOGIN_TIMESTAMPS,
        userParamMap);
  }

  @Override
  public void updateAppUserAsActive(int userid) {
    Map<String, Integer> useridParamMap = new HashMap<String, Integer>();
    useridParamMap.put(USERNAME_COL, userid);
    getNamedParameterJdbcTemplate().update(SQL_UPDATE_APPUSER_AS_ACTIVE, useridParamMap);
  }
}
