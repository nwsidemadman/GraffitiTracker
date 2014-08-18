package net.ccaper.GraffitiTracker.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Repository;

import net.ccaper.GraffitiTracker.dao.LoginAddressDao;
import net.ccaper.GraffitiTracker.objects.LoginInet;

@Repository("loginAddressesDao")
public class JdbcLoginAddressDaoImpl extends NamedParameterJdbcDaoSupport
    implements LoginAddressDao {
  private static final String LOGIN_ADDRESSES_TABLE = "login_addresses";
  private static final String INET_ADDRESS_COL = "inet_address";
  private static final String INET_ADDRESS_AS_STRING_COL = "inet_address_string";
  private static final String LAST_VISIT_TIMESTAMP_COL = "last_visit_timestamp";
  private static final String NUMBER_VISITS_COL = "number_visits";
  private static final String IS_INETBANNDED_COL = "is_inet_banned";
  private static final String USERS_TABLE = JdbcAppUserDaoImpl.USERS_TABLE;
  private static final String USER_ID_COL = JdbcAppUserDaoImpl.USER_ID_COL;
  private static final String ID_FK_COL = JdbcAppUserDaoImpl.ID_FK_COL;
  private static final String USERNAME_COL = JdbcAppUserDaoImpl.USERNAME_COL;
  private static final String BANNED_INETS_TABLE = JdbcBannedInetsDaoImpl.BANNED_INETS_TABLE;
  private static final String MIN_BANNED_INET_COL = JdbcBannedInetsDaoImpl.MIN_INET_COL;
  private static final String MAX_BANNED_INET_COL = JdbcBannedInetsDaoImpl.MAX_INET_COL;
  private static final String ACTIVE_BANNED_INET_COL = JdbcBannedInetsDaoImpl.ACTIVE_COL;

  private static final String SQL_UPDATE_LOGIN_ADDRESSES = String
      .format(
          "INSERT INTO %s (%s, %s, %s) VALUES ((SELECT %s FROM %s WHERE %s = :%s), INET_ATON(:%s), current_timestamp) "
              + "ON DUPLICATE KEY UPDATE %s = %s + 1, %s = current_timestamp",
          LOGIN_ADDRESSES_TABLE, ID_FK_COL, INET_ADDRESS_COL,
          LAST_VISIT_TIMESTAMP_COL, USER_ID_COL, USERS_TABLE, USERNAME_COL,
          USERNAME_COL, INET_ADDRESS_COL, NUMBER_VISITS_COL, NUMBER_VISITS_COL,
          LAST_VISIT_TIMESTAMP_COL).toLowerCase();

  private static final String SQL_GET_LOGIN_ADDRESSES_BY_USERID = String
      .format(
          "SELECT INET_NTOA(%s) as %s, %s, %s, (SELECT case when (select count(*) "
              + "FROM %s WHERE inet_aton(%s) >= %s "
              + "AND inet_aton(%s) <= %s AND %s = 1) > 0 "
              + "then true else false end) as %s FROM %s WHERE %s = :%s",
          INET_ADDRESS_COL, INET_ADDRESS_AS_STRING_COL, NUMBER_VISITS_COL,
          LAST_VISIT_TIMESTAMP_COL, BANNED_INETS_TABLE,
          INET_ADDRESS_AS_STRING_COL, MIN_BANNED_INET_COL,
          INET_ADDRESS_AS_STRING_COL, MAX_BANNED_INET_COL,
          ACTIVE_BANNED_INET_COL, IS_INETBANNDED_COL, LOGIN_ADDRESSES_TABLE,
          ID_FK_COL, ID_FK_COL).toLowerCase();

  private static final String SQL_GET_USERS_SHARING_INET = String
      .format(
          "SELECT %s.%s, %s.%s FROM %s INNER JOIN %s on %s.%s = %s.%s WHERE %s.%s = inet_aton(:%s)",
          USERS_TABLE, USER_ID_COL, USERS_TABLE, USERNAME_COL, USERS_TABLE,
          LOGIN_ADDRESSES_TABLE, LOGIN_ADDRESSES_TABLE, ID_FK_COL, USERS_TABLE,
          USER_ID_COL, LOGIN_ADDRESSES_TABLE, INET_ADDRESS_COL,
          INET_ADDRESS_COL).toLowerCase();

  RowMapper<LoginInet> loginInetRowMapper = new RowMapper<LoginInet>() {
    @Override
    public LoginInet mapRow(ResultSet rs, int rowNum) throws SQLException {
      LoginInet loginInet = new LoginInet();
      loginInet.setInet(rs.getString(INET_ADDRESS_AS_STRING_COL));
      loginInet.setNumberVisits(rs.getInt(NUMBER_VISITS_COL));
      loginInet.setLastVisit(rs.getTimestamp(LAST_VISIT_TIMESTAMP_COL));
      loginInet.setIsInetBanned(rs.getBoolean(IS_INETBANNDED_COL));
      return loginInet;
    }
  };

  RowMapper<ImmutablePair<String, Integer>> userRowMapper = new RowMapper<ImmutablePair<String, Integer>>() {
    @Override
    public ImmutablePair<String, Integer> mapRow(ResultSet rs, int rowNum)
        throws SQLException {
      return new ImmutablePair<String, Integer>(rs.getString(USERNAME_COL),
          rs.getInt(USER_ID_COL));
    }
  };

  @Autowired
  public void setDs(DataSource dataSource) {
    setDataSource(dataSource);
  }

  @Override
  public void updateLoginAddressByUsername(String username, String ipAddress) {
    Map<String, String> loginAddressParamMap = new HashMap<String, String>(2);
    loginAddressParamMap.put(USERNAME_COL, username);
    loginAddressParamMap.put(INET_ADDRESS_COL, ipAddress);
    getNamedParameterJdbcTemplate().update(SQL_UPDATE_LOGIN_ADDRESSES,
        loginAddressParamMap);
  }

  @Override
  public List<LoginInet> getLoginAddressByUserId(int userId) {
    Map<String, Integer> loginAddressParamMap = new HashMap<String, Integer>(1);
    loginAddressParamMap.put(ID_FK_COL, userId);
    return getNamedParameterJdbcTemplate().query(
        SQL_GET_LOGIN_ADDRESSES_BY_USERID, loginAddressParamMap,
        loginInetRowMapper);
  }

  @Override
  public List<ImmutablePair<String, Integer>> getUsersSharingInet(String inet) {
    Map<String, String> loginAddressParamMap = new HashMap<String, String>(1);
    loginAddressParamMap.put(INET_ADDRESS_COL, inet);
    return getNamedParameterJdbcTemplate().query(SQL_GET_USERS_SHARING_INET,
        loginAddressParamMap, userRowMapper);
  }
}
