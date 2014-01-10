package net.ccaper.GraffitiTracker.dao.impl;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Repository;

import net.ccaper.GraffitiTracker.dao.LoginAddressDao;

@Repository("loginAddressesDao")
public class JdbcLoginAddressDaoImpl extends NamedParameterJdbcDaoSupport implements LoginAddressDao {
  private static final String LOGIN_ADDRESSES_TABLE = "login_addresses";
  private static final String INET_ADDRESS_COL = "inet_address";
  private static final String LAST_VISIT_TIMESTAMP_COL = "last_visit_timestamp";
  private static final String NUMBER_VISITS_COL = "number_visits";
  private static final String USERS_TABLE = JdbcAppUserDaoImpl.USERS_TABLE;
  private static final String USER_ID_COL = JdbcAppUserDaoImpl.USER_ID_COL;
  private static final String USERNAME_COL = JdbcAppUserDaoImpl.USERNAME_COL;
  
  private static final String SQL_UPDATE_LOGIN_ADDRESSES = String.format(
      "INSERT INTO %s (%s, %s, %s) VALUES ((SELECT %s FROM %s WHERE %s = :%s), INET_ATON(:%s), current_timestamp) "
      + "ON DUPLICATE KEY UPDATE %s = %s + 1, %s = current_timestamp",
      LOGIN_ADDRESSES_TABLE, USER_ID_COL, INET_ADDRESS_COL, LAST_VISIT_TIMESTAMP_COL,
      USER_ID_COL, USERS_TABLE, USERNAME_COL, USERNAME_COL, INET_ADDRESS_COL, NUMBER_VISITS_COL, NUMBER_VISITS_COL,
      LAST_VISIT_TIMESTAMP_COL).toLowerCase();
  
  @Autowired
  public void setDs(DataSource dataSource) {
    setDataSource(dataSource);
  }

  @Override
  public void updateLoginAddressByUsername(String username, String ipAddress) {
    Map<String, String> loginAddressesParamMap = new HashMap<String, String>();
    loginAddressesParamMap.put(USERNAME_COL, username);
    loginAddressesParamMap.put(INET_ADDRESS_COL, ipAddress);
    getNamedParameterJdbcTemplate().update(SQL_UPDATE_LOGIN_ADDRESSES, loginAddressesParamMap);
  }
}
