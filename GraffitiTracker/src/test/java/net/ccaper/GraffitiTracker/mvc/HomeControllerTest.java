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
import net.ccaper.GraffitiTracker.objects.User;
import net.ccaper.GraffitiTracker.service.UserService;

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
    assertFalse(model.containsKey("user"));
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
    
    User user = new User();
    user.setUsername(username);
    UserService userServiceMock = mock(UserService.class);
    when(userServiceMock.getUser(username))
    .thenReturn(user);
    HomeController controllerMock = new HomeControllerMock();
    controllerMock.setUserService(userServiceMock);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("home", controllerMock.showHomePage(model));
    assertTrue(model.containsKey("user"));
    assertEquals(username, ((User) model.get("user")).getUsername());
    verify(userServiceMock).getUser(username);
  }
}
