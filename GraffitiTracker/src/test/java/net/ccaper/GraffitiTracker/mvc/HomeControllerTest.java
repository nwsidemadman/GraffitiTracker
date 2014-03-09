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
  public void homeReturnsCorrectViewAndModel_AnonymousUser() {
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
  public void homeReturnsCorrectViewAndModel_NotAnonymousUser() {
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
    when(userServiceMock.getUser(username)).thenReturn(appUser);
    HomeController controllerMock = new HomeControllerMock();
    controllerMock.setAppUserService(userServiceMock);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("home", controllerMock.showHomePage(model));
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    verify(userServiceMock).getUser(username);
  }

  @Test
  public void testContactReturnsCorrectViewAndModel_AnonymousUser()
      throws Exception {
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
    assertEquals("contact", controllerMock.contact(model));
    assertFalse(model.containsKey("appUser"));
  }
  
  @Test
  public void testContactReturnsCorrectViewAndModel_NotAnonymousUser()
      throws Exception {
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
    when(userServiceMock.getUser(username)).thenReturn(appUser);
    HomeController controllerMock = new HomeControllerMock();
    controllerMock.setAppUserService(userServiceMock);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("contact", controllerMock.contact(model));
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    verify(userServiceMock).getUser(username);
  }

  @Test
  public void testAboutReturnsCorrectViewAndModel_AnonymousUser() throws Exception {
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
    assertEquals("about", controllerMock.about(model));
    assertFalse(model.containsKey("appUser"));
  }
  
  @Test
  public void testAboutReturnsCorrectViewAndModel_NotAnonymousUser()
      throws Exception {
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
    when(userServiceMock.getUser(username)).thenReturn(appUser);
    HomeController controllerMock = new HomeControllerMock();
    controllerMock.setAppUserService(userServiceMock);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("about", controllerMock.about(model));
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    verify(userServiceMock).getUser(username);
  }

  @Test
  public void testLegalReturnsCorrectViewAndModel_AnonymousUser() throws Exception {
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
    assertEquals("legal", controllerMock.legal(model));
    assertFalse(model.containsKey("appUser"));
  }
  
  @Test
  public void testLegalReturnsCorrectViewAndModel_NotAnonymousUser()
      throws Exception {
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
    when(userServiceMock.getUser(username)).thenReturn(appUser);
    HomeController controllerMock = new HomeControllerMock();
    controllerMock.setAppUserService(userServiceMock);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("legal", controllerMock.legal(model));
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    verify(userServiceMock).getUser(username);
  }

  @Test
  public void testPrivacyReturnsCorrectViewAndModel_AnonymousUser() throws Exception {
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
    assertEquals("privacy", controllerMock.privacy(model));
    assertFalse(model.containsKey("appUser"));
  }
  
  @Test
  public void testPrivacyReturnsCorrectViewAndModel_NotAnonymousUser()
      throws Exception {
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
    when(userServiceMock.getUser(username)).thenReturn(appUser);
    HomeController controllerMock = new HomeControllerMock();
    controllerMock.setAppUserService(userServiceMock);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("privacy", controllerMock.privacy(model));
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    verify(userServiceMock).getUser(username);
  }
}
