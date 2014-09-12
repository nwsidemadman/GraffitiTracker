package net.ccaper.graffitiTracker.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import net.ccaper.graffitiTracker.dao.BannedInetsDao;
import net.ccaper.graffitiTracker.objects.BannedInet;
import net.ccaper.graffitiTracker.objects.OriginalEditedBannedInet;

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
  private static final String ORIG_MIN_INET_COL = "orig_inet_min_incl";
  private static final String ORIG_MAX_INET_COL = "orig_inet_max_incl";
  static final String ACTIVE_COL = "active";
  private static final String CREATED_TIMESTAMP_COL = "created_timestamp";
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
  private static final String SQL_INSERT_OR_NON_PK_UPDATE_BANNED_INET = String
      .format(
          "INSERT INTO %s (%s, %s, %s, %s) VALUES (inet_aton(:%s), inet_aton(:%s), :%s, :%s) "
              + "ON DUPLICATE KEY UPDATE %s = :%s, %s = :%s",
          BANNED_INETS_TABLE, MIN_INET_COL, MAX_INET_COL, ACTIVE_COL, NOTES_COL,
          MIN_INET_COL, MAX_INET_COL, ACTIVE_COL, NOTES_COL, ACTIVE_COL, ACTIVE_COL,
          NOTES_COL, NOTES_COL).toLowerCase();
  private static final String SQL_PK_UPDATE_BANNED_INET = String.format(
      "UPDATE %s SET %s = inet_aton(:%s), %s = inet_aton(:%s), %s = :%s, %s = "
          + ":%s WHERE %s = inet_aton(:%s) AND %s = inet_aton(:%s)",
      BANNED_INETS_TABLE, MIN_INET_COL, MIN_INET_COL, MAX_INET_COL,
      MAX_INET_COL, ACTIVE_COL, ACTIVE_COL, NOTES_COL, NOTES_COL, MIN_INET_COL,
      ORIG_MIN_INET_COL, MAX_INET_COL, ORIG_MAX_INET_COL).toLowerCase();
  private static final String SQL_GET_BANNED_INETS = String.format(
      "SELECT inet_ntoa(%s) as %s, inet_ntoa(%s) as %s, %s, %s, %s, %s FROM %s",
      MIN_INET_COL, MIN_INET_COL, MAX_INET_COL, MAX_INET_COL, ACTIVE_COL, CREATED_TIMESTAMP_COL,
      NUMBER_REGISTRATION_ATTEMPTS_COL, NOTES_COL, BANNED_INETS_TABLE)
      .toLowerCase();
  private static final String SQL_DELETE_BANNED_INET = String.format(
      "DELETE FROM %s where %s = inet_aton(:%s) and %s = inet_aton(:%s)",
      BANNED_INETS_TABLE, MIN_INET_COL, MIN_INET_COL, MAX_INET_COL,
      MAX_INET_COL).toLowerCase();

  RowMapper<Boolean> booleanRowMapper = new RowMapper<Boolean>() {
    @Override
    public Boolean mapRow(ResultSet rs, int rowNum) throws SQLException {
      return rs.getBoolean(1);
    }
  };

  RowMapper<BannedInet> bannedInetRowMapper = new RowMapper<BannedInet>() {
    @Override
    public BannedInet mapRow(ResultSet rs, int rowNum) throws SQLException {
      BannedInet bannedInet = new BannedInet();
      bannedInet.setInetMinIncl(rs.getString(MIN_INET_COL));
      bannedInet.setInetMaxIncl(rs.getString(MAX_INET_COL));
      bannedInet.setIsActive(rs.getBoolean(ACTIVE_COL));
      bannedInet.setCreatedTimestamp(rs.getTimestamp(CREATED_TIMESTAMP_COL));
      bannedInet.setNumberRegistrationAttempts(rs
          .getInt(NUMBER_REGISTRATION_ATTEMPTS_COL));
      bannedInet.setNotes(rs.getString(NOTES_COL));
      return bannedInet;
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
  public void insertOrNonPKUpdateBannedInets(BannedInet bannedInet) {
    Map<String, Object> bannedInetParamMap = new HashMap<String, Object>(4);
    bannedInetParamMap.put(MIN_INET_COL, bannedInet.getInetMinIncl());
    bannedInetParamMap.put(MAX_INET_COL, bannedInet.getInetMaxIncl());
    bannedInetParamMap.put(ACTIVE_COL, bannedInet.getIsActive());
    bannedInetParamMap.put(NOTES_COL, bannedInet.getNotes());
    getNamedParameterJdbcTemplate().update(
        SQL_INSERT_OR_NON_PK_UPDATE_BANNED_INET, bannedInetParamMap);
  }

  /* (non-Javadoc)
   * @see net.ccaper.graffitiTracker.dao.BannedInetsDao#pkUpdateBannedInets(net.ccaper.graffitiTracker.objects.OriginalEditedBannedInet)
   */
  @Override
  public void pkUpdateBannedInets(
      OriginalEditedBannedInet originalEditedBannedInet) {
    Map<String, Object> bannedInetParamMap = new HashMap<String, Object>(6);
    bannedInetParamMap.put(MIN_INET_COL, originalEditedBannedInet
        .getEditedBannedInet().getInetMinIncl());
    bannedInetParamMap.put(MAX_INET_COL, originalEditedBannedInet
        .getEditedBannedInet().getInetMaxIncl());
    bannedInetParamMap.put(ORIG_MIN_INET_COL, originalEditedBannedInet
        .getOriginalBannedInet().getInetMinIncl());
    bannedInetParamMap.put(ORIG_MAX_INET_COL, originalEditedBannedInet
        .getOriginalBannedInet().getInetMaxIncl());
    bannedInetParamMap.put(ACTIVE_COL, originalEditedBannedInet
        .getEditedBannedInet().getIsActive());
    bannedInetParamMap.put(NOTES_COL, originalEditedBannedInet
        .getEditedBannedInet().getNotes());
    getNamedParameterJdbcTemplate().update(SQL_PK_UPDATE_BANNED_INET,
        bannedInetParamMap);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.ccaper.graffitiTracker.dao.BannedInetsDao#getAllBannedInets()
   */
  @Override
  public List<BannedInet> getAllBannedInets() {
    return getNamedParameterJdbcTemplate().query(SQL_GET_BANNED_INETS,
        bannedInetRowMapper);
  }

  /* (non-Javadoc)
   * @see net.ccaper.graffitiTracker.dao.BannedInetsDao#deleteBannedInet(java.lang.String, java.lang.String)
   */
  @Override
  public void deleteBannedInet(String minInetIncl, String maxInetIncl) {
    Map<String, String> paramMap = new HashMap<String, String>(2);
    paramMap.put(MIN_INET_COL, minInetIncl);
    paramMap.put(MAX_INET_COL, maxInetIncl);
    getNamedParameterJdbcTemplate().update(SQL_DELETE_BANNED_INET, paramMap);
  }
}
