package net.ccaper.graffitiTracker.mvc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ccaper.graffitiTracker.objects.AppUser;
import net.ccaper.graffitiTracker.objects.ChicagoCityServiceGraffiti;
import net.ccaper.graffitiTracker.service.AppUserService;
import net.ccaper.graffitiTracker.service.UserSecurityService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MapsControllerTest {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testMapFilter_notAnonymousUser() throws Exception {
    String username = "testUser";
    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    UserSecurityService userSecurityServiceMock = mock(UserSecurityService.class);
    when(userSecurityServiceMock.isUserAnonymous()).thenReturn(false);
    when(userSecurityServiceMock.getUsernameFromSecurity())
        .thenReturn(username);
    AppUserService appUserServiceMock = mock(AppUserService.class);
    when(appUserServiceMock.getUserByUsername(username)).thenReturn(appUser);
    MapsController classUnderTest = new MapsController();
    classUnderTest.setUserSecurityService(userSecurityServiceMock);
    classUnderTest.setAppUserService(appUserServiceMock);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("maps/mapFilter",
        classUnderTest.mapFilter(model));
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    assertTrue(model.containsKey("mapForm"));
    assertTrue(model.containsKey("graffiti"));
    assertTrue(((List<ChicagoCityServiceGraffiti>) model.get("graffiti")).size() == 0);
    verify(userSecurityServiceMock).isUserAnonymous();
    verify(userSecurityServiceMock).getUsernameFromSecurity();
    verify(appUserServiceMock).getUserByUsername(username);
  }
  
  @SuppressWarnings("unchecked")
  @Test
  public void testMapFilter_anonymousUser() throws Exception {
    UserSecurityService userSecurityServiceMock = mock(UserSecurityService.class);
    when(userSecurityServiceMock.isUserAnonymous()).thenReturn(true);
    MapsController classUnderTest = new MapsController();
    classUnderTest.setUserSecurityService(userSecurityServiceMock);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("maps/mapFilter",
        classUnderTest.mapFilter(model));
    assertFalse(model.containsKey("appUser"));
    assertTrue(model.containsKey("mapForm"));
    assertTrue(model.containsKey("graffiti"));
    assertTrue(((List<ChicagoCityServiceGraffiti>) model.get("graffiti")).size() == 0);
    verify(userSecurityServiceMock).isUserAnonymous();
  }
}
