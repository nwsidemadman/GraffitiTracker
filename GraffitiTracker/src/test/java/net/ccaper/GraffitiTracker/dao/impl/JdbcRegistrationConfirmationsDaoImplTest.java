package net.ccaper.GraffitiTracker.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Map;

import net.ccaper.GraffitiTracker.dao.RegistrationConfirmationsDao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.EmptyResultDataAccessException;

public class JdbcRegistrationConfirmationsDaoImplTest {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testGetUserIdByUniqueUrlParam_HappyPath() throws Exception {
    final Integer userId = 1;

    class JdbcRegistrationConfirmationsDaoImplMock extends JdbcRegistrationConfirmationsDaoImpl {
      @Override
      Integer getUserIdByUniqueUrlParam(Map<String, String> getUseridByUniqueUrlParam) {
        return userId;
      }
    }

    RegistrationConfirmationsDao registrationConfirmationsDaoMock = new JdbcRegistrationConfirmationsDaoImplMock();
    assertEquals(userId, registrationConfirmationsDaoMock.getUserIdByUniqueUrlParam("test"));
  }

  @Test
  public void testGetUserIdByUniqueUrlParam_EmptyResults() throws Exception {
    class JdbcRegistrationConfirmationsDaoImplMock extends JdbcRegistrationConfirmationsDaoImpl {
      @Override
      Integer getUserIdByUniqueUrlParam(Map<String, String> getUseridByUniqueUrlParam) {
        throw new EmptyResultDataAccessException("test", 0);
      }
    }

    RegistrationConfirmationsDao registrationConfirmationsDaoMock = new JdbcRegistrationConfirmationsDaoImplMock();
    assertNull(registrationConfirmationsDaoMock.getUserIdByUniqueUrlParam("test"));
  }
}
