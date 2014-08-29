package net.ccaper.graffitiTracker.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import net.ccaper.graffitiTracker.dao.BannedInetsDao;
import net.ccaper.graffitiTracker.objects.BannedInet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author ccaper Implementation for the JDBC version of the
 *         {@link net.ccaper.graffitiTracker.dao.BannedInetsDao}
 * 
 */
@Repository("bannedInetsDao")
public class JdbcBannedInetsDaoImpl extends NamedParameterJdbcDaoSupport
    implements BannedInetsDao {
  static final String BANNED_INETS_TABLE = "banned_inets";
  static final String MIN_INET_COL = "inet_min_incl";
  static final String MAX_INET_COL = "inet_max_incl";
  static final String ACTIVE_COL = "active";
  private static final String NUMBER_REGISTRATION_ATTEMPTS_COL = "number_registration_attempts";
  private static final String NOTES_COL = "notes";
  private static final String INET = "inet";
  private static final String SQL_IS_INET_IN_RANGE = String
      .format(
          "SELECT CASE WHEN (SELECT COUNT(*) FROM %s WHERE inet_aton(:%s) >= %s "
              + "AND inet_aton(:%s) <= %s AND %s = 1) > 0 THEN TRUE ELSE FALSE END",
          BANNED_INETS_TABLE, INET, MIN_INET_COL, INET, MAX_INET_COL,
          ACTIVE_COL).toLowerCase();
  private static final String SQL_UPDATE_NUMBER_REGISTRATION_ATTEMPTS_INET_IN_RANGE = String
      .format(
          "UPDATE %s SET %s = %s + 1 WHERE inet_aton(:%s) >= %s AND inet_aton(:%s) <= %s AND %s = 1",
          BANNED_INETS_TABLE, NUMBER_REGISTRATION_ATTEMPTS_COL,
          NUMBER_REGISTRATION_ATTEMPTS_COL, INET, MIN_INET_COL, INET,
          MAX_INET_COL, ACTIVE_COL).toLowerCase();
  private static final String SQL_INSERT_OR_UPDATE_BANNED_INET = String
      .format(
          "insert into %s (%s, %s, %s) values (inet_aton(:%s), inet_aton(:%s), :%s) "
              + "on duplicate key update %s = true, %s = concat(%s, concat('; ', :%s))",
          BANNED_INETS_TABLE, MIN_INET_COL, MAX_INET_COL, NOTES_COL,
          MIN_INET_COL, MAX_INET_COL, NOTES_COL, ACTIVE_COL, NOTES_COL,
          NOTES_COL, NOTES_COL).toLowerCase();

  RowMapper<Boolean> booleanRowMapper = new RowMapper<Boolean>() {
    @Override
    public Boolean mapRow(ResultSet rs, int rowNum) throws SQLException {
      return rs.getBoolean(1);
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
   * net.ccaper.graffitiTracker.dao.BannedInetsDao#isInetInRange(java.lang.String
   * )
   */
  @Override
  public boolean isInetInRange(String inet) {
    Map<String, String> inetParamMap = new HashMap<String, String>(1);
    inetParamMap.put(INET, inet);
    return getNamedParameterJdbcTemplate().queryForObject(SQL_IS_INET_IN_RANGE,
        inetParamMap, booleanRowMapper);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.ccaper.graffitiTracker.dao.BannedInetsDao#
   * updateNumberRegistrationAttemptsInetInRange(java.lang.String)
   */
  @Override
  public void updateNumberRegistrationAttemptsInetInRange(String inet) {
    Map<String, String> inetParamMap = new HashMap<String, String>(1);
    inetParamMap.put(INET, inet);
    getNamedParameterJdbcTemplate().update(
        SQL_UPDATE_NUMBER_REGISTRATION_ATTEMPTS_INET_IN_RANGE, inetParamMap);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * net.ccaper.graffitiTracker.dao.BannedInetsDao#insertOrUpdateBannedInets
   * (net.ccaper.graffitiTracker.objects.BannedInet)
   */
  @Override
  public void insertOrUpdateBannedInets(BannedInet bannedInet) {
    Map<String, String> bannedInetParamMap = new HashMap<String, String>(3);
    bannedInetParamMap.put(MIN_INET_COL, bannedInet.getInetMinIncl());
    bannedInetParamMap.put(MAX_INET_COL, bannedInet.getInetMaxIncl());
    bannedInetParamMap.put(NOTES_COL, bannedInet.getNotes());
    getNamedParameterJdbcTemplate().update(SQL_INSERT_OR_UPDATE_BANNED_INET,
        bannedInetParamMap);
  }
}
