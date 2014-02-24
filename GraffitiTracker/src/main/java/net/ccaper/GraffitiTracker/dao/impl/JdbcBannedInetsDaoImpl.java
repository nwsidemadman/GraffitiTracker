package net.ccaper.GraffitiTracker.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import net.ccaper.GraffitiTracker.dao.BannedInetsDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Repository;

@Repository("bannedInetsDao")
public class JdbcBannedInetsDaoImpl extends NamedParameterJdbcDaoSupport
implements BannedInetsDao {
  private static final String BANNED_INETS_TABLE = "banned_inets";
  private static final String MIN_INET_COL = "inet_min_incl";
  private static final String MAX_INET_COL = "inet_max_incl";
  private static final String ACTIVE_COL = "active";
  private static final String NUMBER_REGISTRATION_ATTEMPTS_COL = "number_registration_attempts";
  private static final String NOTES_COL = "notes";
  private static final String INET = "inet";
  private static final String SQL_SELECT_COUNT_INET_IN_RANGE = String
      .format(
          "SELECT COUNT(*) FROM %s WHERE inet_aton(:%s) >= %s AND inet_aton(:%s) <= %s AND %s = 1;",
          BANNED_INETS_TABLE, INET, MIN_INET_COL, INET, MAX_INET_COL,
          ACTIVE_COL).toLowerCase();
  private static final String SQL_UPDATE_NUMBER_REGISTRATION_ATTEMPTS_INET_IN_RANGE = String
      .format(
          "UPDATE %s SET %s = %s + 1 WHERE inet_aton(:%s) >= %s AND inet_aton(:%s) <= %s AND %s = 1;",
          BANNED_INETS_TABLE, NUMBER_REGISTRATION_ATTEMPTS_COL,
          NUMBER_REGISTRATION_ATTEMPTS_COL, INET, MIN_INET_COL, INET,
          MAX_INET_COL, ACTIVE_COL).toLowerCase();
  RowMapper<Integer> countRowMapper = new RowMapper<Integer>() {
    @Override
    public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
      return new Integer(rs.getInt(1));
    }
  };

  @Autowired
  public void setDs(DataSource dataSource) {
    setDataSource(dataSource);
  }

  @Override
  public int selectCountInetInRange(String inet) {
    Map<String, String> inetParamMap = new HashMap<String, String>();
    inetParamMap.put(INET, inet);
    return getNamedParameterJdbcTemplate().queryForObject(
        SQL_SELECT_COUNT_INET_IN_RANGE, inetParamMap, countRowMapper);
  }

  @Override
  public void updateNumberRegistrationAttemptsInetInRange(String inet) {
    Map<String, String> inetParamMap = new HashMap<String, String>();
    inetParamMap.put(INET, inet);
    getNamedParameterJdbcTemplate().update(SQL_UPDATE_NUMBER_REGISTRATION_ATTEMPTS_INET_IN_RANGE, inetParamMap);
  }
}