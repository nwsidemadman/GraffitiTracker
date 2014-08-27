package net.ccaper.GraffitiTracker.mvc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import net.ccaper.GraffitiTracker.mvc.HomeController;
import net.ccaper.GraffitiTracker.objects.AppUser;
import net.ccaper.GraffitiTracker.service.AppUserService;
import net.ccaper.GraffitiTracker.service.UserSecurityService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HomeControllerTest {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void homeReturnsCorrectViewAndModel_AnonymousUser() {
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(true);
    HomeController classUnderTest = new HomeController();
    classUnderTest.setUserSecurityService(userSecurityService);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("home", classUnderTest.showHomePage(model));
    assertFalse(model.containsKey("appUser"));
    verify(userSecurityService).isUserAnonymous();
  }

  @Test
  public void homeReturnsCorrectViewAndModel_NotAnonymousUser() {
    String username = "testUser";
    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    AppUserService userServiceMock = mock(AppUserService.class);
    when(userServiceMock.getUserByUsername(username)).thenReturn(appUser);
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(false);
    when(userSecurityService.getUsernameFromSecurity()).thenReturn(username);
    HomeController classUnderTest = new HomeController();
    classUnderTest.setUserSecurityService(userSecurityService);
    classUnderTest.setAppUserService(userServiceMock);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("home", classUnderTest.showHomePage(model));
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    verify(userServiceMock).getUserByUsername(username);
    verify(userSecurityService).isUserAnonymous();
    verify(userSecurityService).getUsernameFromSecurity();
  }

  @Test
  public void testContactReturnsCorrectViewAndModel_AnonymousUser()
      throws Exception {
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(true);
    HomeController classUnderTest = new HomeController();
    classUnderTest.setUserSecurityService(userSecurityService);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("contact", classUnderTest.contact(model));
    assertFalse(model.containsKey("appUser"));
    verify(userSecurityService).isUserAnonymous();
  }

  @Test
  public void testContactReturnsCorrectViewAndModel_NotAnonymousUser()
      throws Exception {
    String username = "testUser";
    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    AppUserService userServiceMock = mock(AppUserService.class);
    when(userServiceMock.getUserByUsername(username)).thenReturn(appUser);
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(false);
    when(userSecurityService.getUsernameFromSecurity()).thenReturn(username);
    HomeController classUnderTest = new HomeController();
    classUnderTest.setUserSecurityService(userSecurityService);
    classUnderTest.setAppUserService(userServiceMock);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("contact", classUnderTest.contact(model));
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    verify(userServiceMock).getUserByUsername(username);
    verify(userSecurityService).isUserAnonymous();
    verify(userSecurityService).getUsernameFromSecurity();
  }

  @Test
  public void testAboutReturnsCorrectViewAndModel_AnonymousUser()
      throws Exception {
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(true);
    HomeController classUnderTest = new HomeController();
    classUnderTest.setUserSecurityService(userSecurityService);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("about", classUnderTest.about(model));
    assertFalse(model.containsKey("appUser"));
    verify(userSecurityService).isUserAnonymous();
  }

  @Test
  public void testAboutReturnsCorrectViewAndModel_NotAnonymousUser()
      throws Exception {
    String username = "testUser";
    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    AppUserService userServiceMock = mock(AppUserService.class);
    when(userServiceMock.getUserByUsername(username)).thenReturn(appUser);
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(false);
    when(userSecurityService.getUsernameFromSecurity()).thenReturn(username);
    HomeController classUnderTest = new HomeController();
    classUnderTest.setUserSecurityService(userSecurityService);
    classUnderTest.setAppUserService(userServiceMock);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("about", classUnderTest.about(model));
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    verify(userServiceMock).getUserByUsername(username);
    verify(userSecurityService).isUserAnonymous();
    verify(userSecurityService).getUsernameFromSecurity();
  }

  @Test
  public void testLegalReturnsCorrectViewAndModel_AnonymousUser()
      throws Exception {
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(true);
    HomeController classUnderTest = new HomeController();
    classUnderTest.setUserSecurityService(userSecurityService);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("legal", classUnderTest.legal(model));
    assertFalse(model.containsKey("appUser"));
    verify(userSecurityService).isUserAnonymous();
  }

  @Test
  public void testLegalReturnsCorrectViewAndModel_NotAnonymousUser()
      throws Exception {
    String username = "testUser";
    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    AppUserService userServiceMock = mock(AppUserService.class);
    when(userServiceMock.getUserByUsername(username)).thenReturn(appUser);
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(false);
    when(userSecurityService.getUsernameFromSecurity()).thenReturn(username);
    HomeController classUnderTest = new HomeController();
    classUnderTest.setUserSecurityService(userSecurityService);
    classUnderTest.setAppUserService(userServiceMock);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("legal", classUnderTest.legal(model));
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    verify(userServiceMock).getUserByUsername(username);
    verify(userSecurityService).isUserAnonymous();
    verify(userSecurityService).getUsernameFromSecurity();
  }

  @Test
  public void testPrivacyReturnsCorrectViewAndModel_AnonymousUser()
      throws Exception {
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(true);
    HomeController classUnderTest = new HomeController();
    classUnderTest.setUserSecurityService(userSecurityService);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("privacy", classUnderTest.privacy(model));
    assertFalse(model.containsKey("appUser"));
    verify(userSecurityService).isUserAnonymous();
  }

  @Test
  public void testPrivacyReturnsCorrectViewAndModel_NotAnonymousUser()
      throws Exception {
    String username = "testUser";
    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    AppUserService userServiceMock = mock(AppUserService.class);
    when(userServiceMock.getUserByUsername(username)).thenReturn(appUser);
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(false);
    when(userSecurityService.getUsernameFromSecurity()).thenReturn(username);
    HomeController classUnderTest = new HomeController();
    classUnderTest.setUserSecurityService(userSecurityService);
    classUnderTest.setAppUserService(userServiceMock);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("privacy", classUnderTest.privacy(model));
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    verify(userServiceMock).getUserByUsername(username);
    verify(userSecurityService).isUserAnonymous();
    verify(userSecurityService).getUsernameFromSecurity();
  }
}
