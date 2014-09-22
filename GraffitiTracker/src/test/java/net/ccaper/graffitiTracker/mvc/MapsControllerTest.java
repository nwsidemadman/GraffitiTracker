package net.ccaper.graffitiTracker.mvc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ccaper.graffitiTracker.objects.AppUser;
import net.ccaper.graffitiTracker.objects.ChicagoCityServiceGraffiti;
import net.ccaper.graffitiTracker.objects.MapForm;
import net.ccaper.graffitiTracker.service.AppUserService;
import net.ccaper.graffitiTracker.service.ChicagoCityServicesGraffitiService;
import net.ccaper.graffitiTracker.service.UserSecurityService;

import org.apache.commons.lang3.StringUtils;
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
    assertEquals("maps/mapFilter", classUnderTest.mapFilter(model));
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    assertTrue(model.containsKey("mapForm"));
    assertTrue(model.containsKey("graffiti"));
    assertTrue(((List<ChicagoCityServiceGraffiti>) model.get("graffiti"))
        .size() == 0);
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
    assertEquals("maps/mapFilter", classUnderTest.mapFilter(model));
    assertFalse(model.containsKey("appUser"));
    assertTrue(model.containsKey("mapForm"));
    assertTrue(model.containsKey("graffiti"));
    assertTrue(((List<ChicagoCityServiceGraffiti>) model.get("graffiti"))
        .size() == 0);
    verify(userSecurityServiceMock).isUserAnonymous();
  }

  @Test
  public void testStripNoneChoice_null() throws Exception {
    String choiceString = null;
    MapsController classUnderTest = new MapsController();
    assertTrue(classUnderTest.stripNoneChoice(choiceString).size() == 0);
  }

  @Test
  public void testStripNoneChoice_empty() throws Exception {
    String choiceString = StringUtils.EMPTY;
    MapsController classUnderTest = new MapsController();
    assertTrue(classUnderTest.stripNoneChoice(choiceString).size() == 0);
  }

  @Test
  public void testStripNoneChoice_HappyPath() throws Exception {
    List<String> expected = new ArrayList<String>();
    expected.add("test1");
    expected.add("NONE");
    expected.add("test2");
    String choiceString = StringUtils.join(expected, ",");
    expected.remove("NONE");
    MapsController classUnderTest = new MapsController();
    assertEquals(expected, classUnderTest.stripNoneChoice(choiceString));
  }

  @Test
  public void testGraffitiMap_notAnonymousUser() throws Exception {
    final List<String> status = new ArrayList<String>();
    status.add("'open'");
    class MapsControllerMock extends MapsController {
      @Override
      List<String> stripNoneChoice(String choicesString) {
        return status;
      }
    }
    
    String username = "testUser";
    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    UserSecurityService userSecurityServiceMock = mock(UserSecurityService.class);
    when(userSecurityServiceMock.isUserAnonymous()).thenReturn(false);
    when(userSecurityServiceMock.getUsernameFromSecurity())
        .thenReturn(username);
    AppUserService appUserServiceMock = mock(AppUserService.class);
    when(appUserServiceMock.getUserByUsername(username)).thenReturn(appUser);
    MapsController classUnderTest = new MapsControllerMock();
    classUnderTest.setUserSecurityService(userSecurityServiceMock);
    classUnderTest.setAppUserService(appUserServiceMock);
    ChicagoCityServicesGraffitiService chicagoCityServicesGraffitiServiceMock = mock(ChicagoCityServicesGraffitiService.class);
    MapForm userChoices = new MapForm();
    userChoices.setStatus("'closed'");
    userChoices.setStartDate(400L);
    userChoices.setEndDate(500L);
    List<ChicagoCityServiceGraffiti> results = new ArrayList<ChicagoCityServiceGraffiti>();
    results.add(new ChicagoCityServiceGraffiti());
    when(
        chicagoCityServicesGraffitiServiceMock.getAllGraffiti(status,
            userChoices.getStartDateAsTimestamp(),
            userChoices.getEndDateAsTimestamp())).thenReturn(results);
    classUnderTest
        .setChicagoCityServicesGraffitiService(chicagoCityServicesGraffitiServiceMock);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("maps/map", classUnderTest.graffitiMap(userChoices, model));
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    assertTrue(model.containsKey("graffiti"));
    assertEquals(results, model.get("graffiti"));
    verify(userSecurityServiceMock).isUserAnonymous();
    verify(userSecurityServiceMock).getUsernameFromSecurity();
    verify(appUserServiceMock).getUserByUsername(username);
    verify(chicagoCityServicesGraffitiServiceMock).getAllGraffiti(status,
        userChoices.getStartDateAsTimestamp(),
        userChoices.getEndDateAsTimestamp());
  }

  @Test
  public void testGraffitiMap_anonymousUser() throws Exception {
    final List<String> status = new ArrayList<String>();
    status.add("'open'");
    class MapsControllerMock extends MapsController {
      @Override
      List<String> stripNoneChoice(String choicesString) {
        return status;
      }
    }
    
    UserSecurityService userSecurityServiceMock = mock(UserSecurityService.class);
    when(userSecurityServiceMock.isUserAnonymous()).thenReturn(true);
    MapsController classUnderTest = new MapsControllerMock();
    classUnderTest.setUserSecurityService(userSecurityServiceMock);
    ChicagoCityServicesGraffitiService chicagoCityServicesGraffitiServiceMock = mock(ChicagoCityServicesGraffitiService.class);
    MapForm userChoices = new MapForm();
    userChoices.setStatus("'closed'");
    userChoices.setStartDate(400L);
    userChoices.setEndDate(500L);
    List<ChicagoCityServiceGraffiti> results = new ArrayList<ChicagoCityServiceGraffiti>();
    results.add(new ChicagoCityServiceGraffiti());
    when(
        chicagoCityServicesGraffitiServiceMock.getAllGraffiti(status,
            userChoices.getStartDateAsTimestamp(),
            userChoices.getEndDateAsTimestamp())).thenReturn(results);
    classUnderTest
        .setChicagoCityServicesGraffitiService(chicagoCityServicesGraffitiServiceMock);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("maps/map", classUnderTest.graffitiMap(userChoices, model));
    assertFalse(model.containsKey("appUser"));
    assertTrue(model.containsKey("graffiti"));
    assertEquals(results, model.get("graffiti"));
    verify(userSecurityServiceMock).isUserAnonymous();
    verify(chicagoCityServicesGraffitiServiceMock).getAllGraffiti(status,
        userChoices.getStartDateAsTimestamp(),
        userChoices.getEndDateAsTimestamp());
  }
}
