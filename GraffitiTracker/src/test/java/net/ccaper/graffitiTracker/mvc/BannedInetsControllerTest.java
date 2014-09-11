package net.ccaper.graffitiTracker.mvc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.ccaper.graffitiTracker.objects.AppUser;
import net.ccaper.graffitiTracker.objects.BannedInet;
import net.ccaper.graffitiTracker.service.AppUserService;
import net.ccaper.graffitiTracker.service.UserSecurityService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BannedInetsControllerTest {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testListBannedInets_notAnonymousUser() throws Exception {
    String username = "testUser";
    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    UserSecurityService userSecurityServiceMock = mock(UserSecurityService.class);
    when(userSecurityServiceMock.isUserAnonymous()).thenReturn(false);
    when(userSecurityServiceMock.getUsernameFromSecurity())
        .thenReturn(username);
    AppUserService appUserServiceMock = mock(AppUserService.class);
    when(appUserServiceMock.getUserByUsername(username)).thenReturn(appUser);
    BannedInetsController classUnderTest = new BannedInetsController();
    classUnderTest.setUserSecurityService(userSecurityServiceMock);
    classUnderTest.setAppUserService(appUserServiceMock);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("bannedInets/listBannedInets",
        classUnderTest.listBannedInets(model));
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    verify(userSecurityServiceMock).isUserAnonymous();
    verify(userSecurityServiceMock).getUsernameFromSecurity();
    verify(appUserServiceMock).getUserByUsername(username);
  }

  @Test
  public void testListBannedInets_anonymousUser() throws Exception {
    UserSecurityService userSecurityServiceMock = mock(UserSecurityService.class);
    when(userSecurityServiceMock.isUserAnonymous()).thenReturn(true);
    BannedInetsController classUnderTest = new BannedInetsController();
    classUnderTest.setUserSecurityService(userSecurityServiceMock);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("bannedInets/listBannedInets",
        classUnderTest.listBannedInets(model));
    assertFalse(model.containsKey("appUser"));
    verify(userSecurityServiceMock).isUserAnonymous();
  }

  @Test
  public void testEditCreateBannedInet_Create() throws Exception {
    Map<String, Object> model = new HashMap<String, Object>();
    BannedInet bannedInet = new BannedInet();
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    String contextPath = "test";
    when(requestMock.getContextPath()).thenReturn(contextPath);
    BannedInetsController classUnderTest = new BannedInetsController();
    assertEquals("bannedInets/editCreateBannedInet",
        classUnderTest.editCreateBannedInet(model, bannedInet, requestMock));
    assertTrue(model.containsKey("isNew"));
    assertEquals(true, model.get("isNew"));
    assertTrue(model.containsKey("editedBannedInet"));
    assertEquals(bannedInet, model.get("editedBannedInet"));
    assertTrue(model.containsKey("contextPath"));
    assertEquals(contextPath, model.get("contextPath"));
    verify(requestMock).getContextPath();
  }
  
  @Test
  public void testEditCreateBannedInet_Edit() throws Exception {
    Map<String, Object> model = new HashMap<String, Object>();
    BannedInet bannedInet = new BannedInet();
    bannedInet.setInetMinIncl("127.0.0.1");
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    String contextPath = "test";
    when(requestMock.getContextPath()).thenReturn(contextPath);
    BannedInetsController classUnderTest = new BannedInetsController();
    assertEquals("bannedInets/editCreateBannedInet",
        classUnderTest.editCreateBannedInet(model, bannedInet, requestMock));
    assertTrue(model.containsKey("isNew"));
    assertEquals(false, model.get("isNew"));
    assertTrue(model.containsKey("editedBannedInet"));
    assertEquals(bannedInet, model.get("editedBannedInet"));
    assertTrue(model.containsKey("contextPath"));
    assertEquals(contextPath, model.get("contextPath"));
    verify(requestMock).getContextPath();
  }
}
