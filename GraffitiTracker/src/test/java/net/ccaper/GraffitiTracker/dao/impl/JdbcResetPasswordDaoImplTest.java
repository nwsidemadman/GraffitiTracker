package net.ccaper.GraffitiTracker.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Map;

import net.ccaper.GraffitiTracker.dao.ResetPasswordDao;
import net.ccaper.GraffitiTracker.objects.UserSecurityQuestion;

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
    final UserSecurityQuestion userSecurityQuestion = new UserSecurityQuestion();
    userSecurityQuestion.setUserid(1);
    userSecurityQuestion.setSecurityQuestion("test question");

    class JdbcResetPasswordDaoImplMock extends JdbcResetPasswordDaoImpl {
      @Override
      UserSecurityQuestion getUserSecurityQuestionByUniqueUrlParam(Map<String, String> uniqueUrlParamParamMap) {
        return userSecurityQuestion;
      }
    }

    ResetPasswordDao resetPasswordDaoMock = new JdbcResetPasswordDaoImplMock();
    assertEquals(userSecurityQuestion, resetPasswordDaoMock.getUserSecurityQuestionByUniqueUrlParam("test"));
  }

  @Test
  public void testGetUserIdByUniqueUrlParam() throws Exception {
    class JdbcResetPasswordDaoImplMock extends JdbcResetPasswordDaoImpl {
      @Override
      UserSecurityQuestion getUserSecurityQuestionByUniqueUrlParam(Map<String, String> uniqueUrlParamParamMap) {
        throw new EmptyResultDataAccessException("test", 0);
      }
    }

    ResetPasswordDao resetPasswordDaoMock = new JdbcResetPasswordDaoImplMock();
    assertNull(resetPasswordDaoMock.getUserSecurityQuestionByUniqueUrlParam("test"));
  }
}
