package net.ccaper.GraffitiTracker.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Map;

import net.ccaper.GraffitiTracker.dao.ResetPasswordDao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.EmptyResultDataAccessException;

public class JdbcResetPasswordDaoImplTest {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testGetUserIdByUniqueUrlParam_HappyPath() throws Exception {
    final Integer userId = 1;

    class JdbcResetPasswordDaoImplMock extends JdbcResetPasswordDaoImpl {
      @Override
      Integer getUserIdByUniqueUrlParam(Map<String, String> getUserIdByUniqueUrlParam) {
        return userId;
      }
    }

    ResetPasswordDao resetPasswordDaoMock = new JdbcResetPasswordDaoImplMock();
    assertEquals(userId, resetPasswordDaoMock.getUserIdByUniqueUrlParam("test"));
  }

  @Test
  public void testGetUserIdByUniqueUrlParam() throws Exception {
    class JdbcResetPasswordDaoImplMock extends JdbcResetPasswordDaoImpl {
      @Override
      Integer getUserIdByUniqueUrlParam(Map<String, String> getUserIdByUniqueUrlParam) {
        throw new EmptyResultDataAccessException("test", 0);
      }
    }

    ResetPasswordDao resetPasswordDaoMock = new JdbcResetPasswordDaoImplMock();
    assertNull(resetPasswordDaoMock.getUserIdByUniqueUrlParam("test"));
  }
}
