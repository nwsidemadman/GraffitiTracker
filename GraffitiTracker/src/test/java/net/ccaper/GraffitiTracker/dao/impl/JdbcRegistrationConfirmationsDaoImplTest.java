package net.ccaper.GraffitiTracker.dao.impl;

import static org.junit.Assert.assertEquals;

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
  public void testGetUsernameByEmail_HappyPath() {
    final Integer userid = 1;

    class JdbcRegistrationConfirmationsDaoImplMock extends JdbcRegistrationConfirmationsDaoImpl {
      @Override
      Integer getUseridByUniqueUrlParam(Map<String, String> getUseridByUniqueUrlParam) {
        return userid;
      }
    }

    RegistrationConfirmationsDao registrationConfirmationsDaoMock = new JdbcRegistrationConfirmationsDaoImplMock();
    assertEquals(userid, registrationConfirmationsDaoMock.getUseridByUniqueUrlParam("test"));
  }

  @Test
  public void testGetUsernameByEmail_EmptyResults() {
    class JdbcRegistrationConfirmationsDaoImplMock extends JdbcRegistrationConfirmationsDaoImpl {
      @Override
      Integer getUseridByUniqueUrlParam(Map<String, String> getUseridByUniqueUrlParam) {
        throw new EmptyResultDataAccessException("test", 0);
      }
    }

    RegistrationConfirmationsDao registrationConfirmationsDaoMock = new JdbcRegistrationConfirmationsDaoImplMock();
    assertEquals(null, registrationConfirmationsDaoMock.getUseridByUniqueUrlParam("test"));
  }
}
