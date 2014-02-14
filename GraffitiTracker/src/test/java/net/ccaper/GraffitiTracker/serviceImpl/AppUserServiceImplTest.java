package net.ccaper.GraffitiTracker.serviceImpl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import net.ccaper.GraffitiTracker.dao.AppUserDao;

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
    AppUserDao appUserDaoMock = mock(AppUserDao.class);
    String username = "testUsername";
    when(appUserDaoMock.getCountUsernames(username)).thenReturn(1);
    AppUserServiceImpl appUserServiceImpl = new AppUserServiceImpl();
    appUserServiceImpl.setAppUserDao(appUserDaoMock);
    assertTrue(appUserServiceImpl.doesUsernameExist(username));
    verify(appUserDaoMock).getCountUsernames(username);
  }

  @Test
  public void testUsernameDoesNotExist() throws Exception {
    AppUserDao appUserDaoMock = mock(AppUserDao.class);
    String username = "testUsername";
    when(appUserDaoMock.getCountUsernames(username)).thenReturn(0);
    AppUserServiceImpl appUserServiceImpl = new AppUserServiceImpl();
    appUserServiceImpl.setAppUserDao(appUserDaoMock);
    assertFalse(appUserServiceImpl.doesUsernameExist(username));
    verify(appUserDaoMock).getCountUsernames(username);
  }

  @Test
  public void testEmailExists() throws Exception {
    AppUserDao appUserDaoMock = mock(AppUserDao.class);
    String email = "test@test.com";
    when(appUserDaoMock.getCountEmails(email)).thenReturn(1);
    AppUserServiceImpl appUserServiceImpl = new AppUserServiceImpl();
    appUserServiceImpl.setAppUserDao(appUserDaoMock);
    assertTrue(appUserServiceImpl.doesEmailExist(email));
    verify(appUserDaoMock).getCountEmails(email);
  }

  @Test
  public void testEmailDoesNotExist() throws Exception {
    AppUserDao appUserDaoMock = mock(AppUserDao.class);
    String email = "test@test.com";
    when(appUserDaoMock.getCountEmails(email)).thenReturn(0);
    AppUserServiceImpl appUserServiceImpl = new AppUserServiceImpl();
    appUserServiceImpl.setAppUserDao(appUserDaoMock);
    assertFalse(appUserServiceImpl.doesEmailExist(email));
    verify(appUserDaoMock).getCountEmails(email);
  }
}
