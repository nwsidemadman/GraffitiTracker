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
  public void returnsCorrectViewAndModel_AnonymousUser() {
    final String username = "testUser";
    
    class HomeControllerMock extends HomeController {
      @Override
      boolean isUserAnonymous() {
        return true;
      }
      
      @Override
      String getUsernameFromSecurity() {
        return username;
      }
    }
    
    HomeController controllerMock = new HomeControllerMock();
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("home", controllerMock.showHomePage(model));
    assertFalse(model.containsKey("appUser"));
  }
  
  @Test
  public void returnsCorrectViewAndModel_NotAnonymousUser() {
    final String username = "testUser";
    
    class HomeControllerMock extends HomeController {
      @Override
      boolean isUserAnonymous() {
        return false;
      }
      
      @Override
      String getUsernameFromSecurity() {
        return username;
      }
    }
    
    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    AppUserService userServiceMock = mock(AppUserService.class);
    when(userServiceMock.getUser(username))
    .thenReturn(appUser);
    HomeController controllerMock = new HomeControllerMock();
    controllerMock.setAppUserService(userServiceMock);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("home", controllerMock.showHomePage(model));
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    verify(userServiceMock).getUser(username);
  }
  
  @Test
  public void testContact() throws Exception {
    HomeController controller = new HomeController();
    assertEquals("contact", controller.contact());
  }
  
  @Test
  public void testAbout() throws Exception {
    HomeController controller = new HomeController();
    assertEquals("about", controller.about());
  }
  
  @Test
  public void testLegal() throws Exception {
    HomeController controller = new HomeController();
    assertEquals("legal", controller.legal());
  }
  
  @Test
  public void testPrivacy() throws Exception {
    HomeController controller = new HomeController();
    assertEquals("privacy", controller.privacy());
  }
}
