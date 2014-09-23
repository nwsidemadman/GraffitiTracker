package net.ccaper.graffitiTracker.mvc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import net.ccaper.graffitiTracker.objects.AppUser;
import net.ccaper.graffitiTracker.objects.CityServiceUpdateForm;
import net.ccaper.graffitiTracker.service.AppUserService;
import net.ccaper.graffitiTracker.service.ChicagoCityServicesGraffitiService;
import net.ccaper.graffitiTracker.service.UserSecurityService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CityServicesControllerTest {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testGetUpdateForm_notAnonymousUser() throws Exception {
    String username = "testUser";
    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    UserSecurityService userSecurityServiceMock = mock(UserSecurityService.class);
    when(userSecurityServiceMock.isUserAnonymous()).thenReturn(false);
    when(userSecurityServiceMock.getUsernameFromSecurity())
        .thenReturn(username);
    AppUserService appUserServiceMock = mock(AppUserService.class);
    when(appUserServiceMock.getUserByUsername(username)).thenReturn(appUser);
    CityServicesController classUnderTest = new CityServicesController();
    classUnderTest.setUserSecurityService(userSecurityServiceMock);
    classUnderTest.setAppUserService(appUserServiceMock);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("city_services/update", classUnderTest.getUpdateForm(model));
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    assertTrue(model.containsKey("cityServiceUpdateForm"));
    verify(userSecurityServiceMock).isUserAnonymous();
    verify(userSecurityServiceMock).getUsernameFromSecurity();
    verify(appUserServiceMock).getUserByUsername(username);
  }

  @Test
  public void testGetUpdateForm_anonymousUser() throws Exception {
    UserSecurityService userSecurityServiceMock = mock(UserSecurityService.class);
    when(userSecurityServiceMock.isUserAnonymous()).thenReturn(true);
    CityServicesController classUnderTest = new CityServicesController();
    classUnderTest.setUserSecurityService(userSecurityServiceMock);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("city_services/update", classUnderTest.getUpdateForm(model));
    assertTrue(model.containsKey("cityServiceUpdateForm"));
    verify(userSecurityServiceMock).isUserAnonymous();
  }

  @Test
  public void testUpdateCityServiceData_notAnonymousUser() throws Exception {
    String username = "testUser";
    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    UserSecurityService userSecurityServiceMock = mock(UserSecurityService.class);
    when(userSecurityServiceMock.isUserAnonymous()).thenReturn(false);
    when(userSecurityServiceMock.getUsernameFromSecurity())
        .thenReturn(username);
    AppUserService appUserServiceMock = mock(AppUserService.class);
    when(appUserServiceMock.getUserByUsername(username)).thenReturn(appUser);
    ChicagoCityServicesGraffitiService chicagoCityServicesGraffitiServiceMock = mock(ChicagoCityServicesGraffitiService.class);
    CityServicesController classUnderTest = new CityServicesController();
    classUnderTest.setUserSecurityService(userSecurityServiceMock);
    classUnderTest.setAppUserService(appUserServiceMock);
    classUnderTest.setChicagoCityServicesGraffitiService(chicagoCityServicesGraffitiServiceMock);
    Map<String, Object> model = new HashMap<String, Object>();
    CityServiceUpdateForm cityServiceUpdateForm = new CityServiceUpdateForm();
    assertEquals("city_services/updateStatus",
        classUnderTest.updateCityServiceData(cityServiceUpdateForm, model));
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    verify(userSecurityServiceMock).isUserAnonymous();
    verify(userSecurityServiceMock).getUsernameFromSecurity();
    verify(appUserServiceMock).getUserByUsername(username);
  }

  @Test
  public void testUpdateCityServiceData_anonymousUser() throws Exception {
    UserSecurityService userSecurityServiceMock = mock(UserSecurityService.class);
    when(userSecurityServiceMock.isUserAnonymous()).thenReturn(true);
    ChicagoCityServicesGraffitiService chicagoCityServicesGraffitiServiceMock = mock(ChicagoCityServicesGraffitiService.class);
    CityServicesController classUnderTest = new CityServicesController();
    classUnderTest.setUserSecurityService(userSecurityServiceMock);
    classUnderTest.setChicagoCityServicesGraffitiService(chicagoCityServicesGraffitiServiceMock);
    Map<String, Object> model = new HashMap<String, Object>();
    CityServiceUpdateForm cityServiceUpdateForm = new CityServiceUpdateForm();
    assertEquals("city_services/updateStatus",
        classUnderTest.updateCityServiceData(cityServiceUpdateForm, model));
    verify(userSecurityServiceMock).isUserAnonymous();
  }
}
