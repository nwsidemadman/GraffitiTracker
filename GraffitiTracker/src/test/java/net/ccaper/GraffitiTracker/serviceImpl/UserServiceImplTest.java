package net.ccaper.GraffitiTracker.serviceImpl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UserServiceImplTest {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testUsernameExists() throws Exception {
    class UserServiceImplMock extends UserServiceImpl {
      @Override
      int getCountUsernames(String username) {
        return 1;
      }
    }

    UserServiceImpl userServiceImplMock = new UserServiceImplMock();
    assertTrue(userServiceImplMock.doesUsernameExist("testUsername"));
  }

  @Test
  public void testUsernameDoesNotExist() throws Exception {
    class UserServiceImplMock extends UserServiceImpl {
      @Override
      int getCountUsernames(String username) {
        return 0;
      }
    }

    UserServiceImpl userServiceImplMock = new UserServiceImplMock();
    assertFalse(userServiceImplMock.doesUsernameExist("testUsername"));
  }

  @Test
  public void testEmailExists() throws Exception {
    class UserServiceImplMock extends UserServiceImpl {
      @Override
      int getCountEmails(String email) {
        return 1;
      }
    }

    UserServiceImpl userServiceImplMock = new UserServiceImplMock();
    assertTrue(userServiceImplMock.doesEmailExist("test@test.com"));
  }

  @Test
  public void testEmailDoesNotExist() throws Exception {
    class UserServiceImplMock extends UserServiceImpl {
      @Override
      int getCountEmails(String email) {
        return 0;
      }
    }

    UserServiceImpl userServiceImplMock = new UserServiceImplMock();
    assertFalse(userServiceImplMock.doesEmailExist("test@test.com"));
  }
}
