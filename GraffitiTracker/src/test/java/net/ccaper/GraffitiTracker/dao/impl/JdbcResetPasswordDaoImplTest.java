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
    final String securityQuestion = "test question";

    class JdbcResetPasswordDaoImplMock extends JdbcResetPasswordDaoImpl {
      @Override
      String getSecurityQuestionByUniqueUrlParam(Map<String, String> uniqueUrlParamParamMap) {
        return securityQuestion;
      }
    }

    ResetPasswordDao resetPasswordDaoMock = new JdbcResetPasswordDaoImplMock();
    assertEquals(securityQuestion, resetPasswordDaoMock.getSecurityQuestionByUniqueUrlParam("test"));
  }

  @Test
  public void testGetUserIdByUniqueUrlParam() throws Exception {
    class JdbcResetPasswordDaoImplMock extends JdbcResetPasswordDaoImpl {
      @Override
      String getSecurityQuestionByUniqueUrlParam(Map<String, String> uniqueUrlParamParamMap) {
        throw new EmptyResultDataAccessException("test", 0);
      }
    }

    ResetPasswordDao resetPasswordDaoMock = new JdbcResetPasswordDaoImplMock();
    assertNull(resetPasswordDaoMock.getSecurityQuestionByUniqueUrlParam("test"));
  }
}
