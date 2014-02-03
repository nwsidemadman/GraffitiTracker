package net.ccaper.GraffitiTracker.dao.impl;

import javax.sql.DataSource;

import net.ccaper.GraffitiTracker.dao.ResetPasswordDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Repository;

@Repository("resetPasswordDao")
public class JdbcResetPasswordDaoImpl extends NamedParameterJdbcDaoSupport
implements ResetPasswordDao {
  private final static String RESET_PASSWORD_TABLE = "reset_password";
  private static final String USER_ID_COL = JdbcAppUserDaoImpl.USER_ID_COL;
  private static final String UNIQUE_URL_PARAM_COL = "unique_url_param";
  private static final String RESET_PASSWORD_TIMESTAMP_COL = "reset_password_timestamp_col";
  private static final String USERS_TABLE = JdbcAppUserDaoImpl.USERS_TABLE;
  private static final String USERNAME_COL = JdbcAppUserDaoImpl.USERNAME_COL;
  private static final String SQL_INSERT_RESET_PASSWORD_BY_USERNAME = String
      .format(
          "INSERT INTO %s (%s, %s) VALUES ((SELECT %s FROM %s WHERE %s = :%s), UUID())",
          RESET_PASSWORD_TABLE, USER_ID_COL, UNIQUE_URL_PARAM_COL,
          USER_ID_COL, USERS_TABLE, USERNAME_COL, USERNAME_COL).toLowerCase();

  @Autowired
  public void setDs(DataSource dataSource) {
    setDataSource(dataSource);
  }
}
