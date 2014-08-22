package net.ccaper.GraffitiTracker.mvc;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ccaper.GraffitiTracker.objects.AppUser;
import net.ccaper.GraffitiTracker.service.AppUserService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ApiUserControllerTest {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testGetAllUsers() throws Exception {
    List<AppUser> appUsers = new ArrayList<AppUser>();
    AppUser appUser = new AppUser();
    appUser.setUsername("test");
    appUsers.add(appUser);
    ApiUserController apiUserController = new ApiUserController();
    AppUserService userServiceMock = mock(AppUserService.class);
    when(userServiceMock.getAllUsers()).thenReturn(appUsers);
    apiUserController.setAppUserService(userServiceMock);
    Map<String, List<AppUser>> data = new HashMap<String, List<AppUser>>(1);
    data.put("data", appUsers);
    assertEquals(data, apiUserController.getAllUsers());
    verify(userServiceMock).getAllUsers();
  }
}
