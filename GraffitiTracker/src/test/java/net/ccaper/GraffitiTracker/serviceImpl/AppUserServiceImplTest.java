package net.ccaper.GraffitiTracker.serviceImpl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AppUserServiceImplTest {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testUsernameExists() throws Exception {
    class AppUserServiceImplMock extends AppUserServiceImpl {
      @Override
      int getCountUsernames(String username) {
        return 1;
      }
    }

    AppUserServiceImpl appUserServiceImplMock = new AppUserServiceImplMock();
    assertTrue(appUserServiceImplMock.doesUsernameExist("testUsername"));
  }

  @Test
  public void testUsernameDoesNotExist() throws Exception {
    class AppUserServiceImplMock extends AppUserServiceImpl {
      @Override
      int getCountUsernames(String username) {
        return 0;
      }
    }

    AppUserServiceImpl appUserServiceImplMock = new AppUserServiceImplMock();
    assertFalse(appUserServiceImplMock.doesUsernameExist("testUsername"));
  }

  @Test
  public void testEmailExists() throws Exception {
    class AppUserServiceImplMock extends AppUserServiceImpl {
      @Override
      int getCountEmails(String email) {
        return 1;
      }
    }

    AppUserServiceImpl appUserServiceImplMock = new AppUserServiceImplMock();
    assertTrue(appUserServiceImplMock.doesEmailExist("test@test.com"));
  }

  @Test
  public void testEmailDoesNotExist() throws Exception {
    class AppUserServiceImplMock extends AppUserServiceImpl {
      @Override
      int getCountEmails(String email) {
        return 0;
      }
    }

    AppUserServiceImpl appUserServiceImplMock = new AppUserServiceImplMock();
    assertFalse(appUserServiceImplMock.doesEmailExist("test@test.com"));
  }

  @Test
  public void testUniqueUrlParamExists() throws Exception {
    class AppUserServiceImplMock extends AppUserServiceImpl {
      @Override
      int getCountUniqueUrlParams(String uniqueUrlParam) {
        return 1;
      }
    }

    AppUserServiceImpl appUserServiceImplMock = new AppUserServiceImplMock();
    assertTrue(appUserServiceImplMock.doesUniqueUrlParamExist("test"));
  }

  @Test
  public void testUniqueUrlParamDoesNotExist() throws Exception {
    class AppUserServiceImplMock extends AppUserServiceImpl {
      @Override
      int getCountUniqueUrlParams(String uniqueUrlParam) {
        return 0;
      }
    }

    AppUserServiceImpl appUserServiceImplMock = new AppUserServiceImplMock();
    assertFalse(appUserServiceImplMock.doesUniqueUrlParamExist("test"));
  }
}
