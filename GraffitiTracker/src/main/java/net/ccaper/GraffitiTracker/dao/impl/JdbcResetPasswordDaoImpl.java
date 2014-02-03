package net.ccaper.GraffitiTracker.dao.impl;

import javax.sql.DataSource;

import net.ccaper.GraffitiTracker.dao.ResetPasswordDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Repository;

@Repository("resetPasswordDao")
public class JdbcResetPasswordDaoImpl extends NamedParameterJdbcDaoSupport implements
ResetPasswordDao {

  @Autowired
  public void setDs(DataSource dataSource) {
    setDataSource(dataSource);
  }
}
