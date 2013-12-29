package net.ccaper.net.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Repository;

import net.ccaper.GraffitiTracker.dao.UserDao;
import net.ccaper.GraffitiTracker.enums.RoleEnum;
import net.ccaper.GraffitiTracker.objects.Role;
import net.ccaper.GraffitiTracker.objects.User;

@Repository("userDao")
public class JdbcUserDaoImpl extends NamedParameterJdbcDaoSupport implements
    UserDao {
  private static final String USERS_TABLE = "users";
  private static final String USER_ID_COL = "user_id";
  private static final String USERNAME_COL = "username";
  private static final String EMAIL_COL = "email";
  private static final String IS_ACTIVE_COL = "is_active";
  private static final String REGISTER_DATE_COL = "register_date";
  private static final String PASSWORD_COL = "password";
  private static final String LAST_LOGIN_COL = "last_login";
  private static final String SQL_SELECT_USER_BY_USERNAME = String.format(
      "SELECT %s, %s, %s, %s, %s, %s, %s FROM %s WHERE %s = :%s", USER_ID_COL,
      USERNAME_COL, EMAIL_COL, IS_ACTIVE_COL, REGISTER_DATE_COL, PASSWORD_COL,
      LAST_LOGIN_COL, USERS_TABLE, USERNAME_COL, USERNAME_COL);
  private static final String SQL_INSERT_USER = String.format(
      "INSERT INTO %s (%s, %s, %s) VALUES (:%s, :%s, :%s)", USERS_TABLE,
      USERNAME_COL, EMAIL_COL, PASSWORD_COL, USERNAME_COL, EMAIL_COL,
      PASSWORD_COL);
  private static final String ROLES_TABLE = "roles";
  private static final String ROLE_COL = "role";
  private static final String ROLE_GRANTED_TIMESTAMP_COL = "role_granted_timestamp";
  private static final String SQL_SELECT_ROLES_BY_USER_ID = String.format(
      "SELECT %s, %s FROM %s where %s = :%s", ROLE_COL,
      ROLE_GRANTED_TIMESTAMP_COL, ROLES_TABLE, USER_ID_COL, USER_ID_COL);
  private static final String SQL_INSERT_ROLE = String.format(
      "INSERT INTO %s (%s) VALUES ((SELECT %s FROM %s WHERE %S = :%s))",
      ROLES_TABLE, USER_ID_COL, USER_ID_COL, USERS_TABLE, USERNAME_COL,
      USERNAME_COL);

  RowMapper<User> userRowMapper = new RowMapper<User>() {
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
      User user = new User();
      user.setUserId(rs.getInt(USER_ID_COL));
      user.setUsername(rs.getString(USERNAME_COL));
      user.setEmail(rs.getString(EMAIL_COL));
      user.setIsActive(rs.getBoolean(IS_ACTIVE_COL));
      user.setRegisterDate(rs.getTimestamp(REGISTER_DATE_COL));
      user.setPasswordEncoded(rs.getString(PASSWORD_COL));
      user.setLastLogin(rs.getTimestamp(LAST_LOGIN_COL));
      return user;
    }
  };

  RowMapper<Role> rolesRowMapper = new RowMapper<Role>() {
    public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
      Role role = new Role();
      role.setRole(RoleEnum.getRoleEnumFromDbString(rs.getString(ROLE_COL)));
      role.setGrantedTimestamp(rs.getTimestamp(ROLE_GRANTED_TIMESTAMP_COL));
      return role;
    }
  };

  @Override
  public User getUserByUsername(String username) {
    Map<String, String> userParamMap = new HashMap<String, String>();
    userParamMap.put(USERNAME_COL, username);
    User user = getNamedParameterJdbcTemplate().queryForObject(
        SQL_SELECT_USER_BY_USERNAME, userParamMap, userRowMapper);
    Map<String, Integer> rolesParamMap = new HashMap<String, Integer>();
    rolesParamMap.put(USER_ID_COL, user.getUserId());
    List<Role> roles = getNamedParameterJdbcTemplate().query(
        SQL_SELECT_ROLES_BY_USER_ID, rolesParamMap, rolesRowMapper);
    user.setRoles(new HashSet<Role>(roles));
    return user;
  }

  @Override
  public void addUser(User user) {
    Map<String, String> userParamMap = new HashMap<String, String>();
    userParamMap.put(USERNAME_COL, user.getUsername());
    userParamMap.put(EMAIL_COL, user.getEmail());
    userParamMap.put(PASSWORD_COL, user.getPasswordEncoded());
    getNamedParameterJdbcTemplate().update(SQL_INSERT_USER, userParamMap);
    Map<String, String> roleParamMap = new HashMap<String, String>();
    roleParamMap.put(USERNAME_COL, user.getUsername());
    getNamedParameterJdbcTemplate().update(SQL_INSERT_ROLE, roleParamMap);
  }
}
