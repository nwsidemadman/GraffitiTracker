package net.ccaper.GraffitiTracker.mvc;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

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
    assertEquals(appUsers, apiUserController.getAllUsers());
    verify(userServiceMock).getAllUsers();
  }

  @Test
  public void testUser_HappyPath() throws Exception {
    AppUser appUser = new AppUser();
    appUser.setUsername("test");
    appUser.setUserId(1);
    ;
    ApiUserController apiUserController = new ApiUserController();
    AppUserService userServiceMock = mock(AppUserService.class);
    when(userServiceMock.getUserById(appUser.getUserId())).thenReturn(appUser);
    apiUserController.setAppUserService(userServiceMock);
    assertEquals(appUser, apiUserController.getUser(appUser.getUserId()));
    verify(userServiceMock).getUserById(appUser.getUserId());
  }

  @Test
  public void testUser_NoUser() throws Exception {
    int userId = 5;
    ApiUserController apiUserController = new ApiUserController();
    AppUserService userServiceMock = mock(AppUserService.class);
    when(userServiceMock.getUserById(userId)).thenReturn(null);
    apiUserController.setAppUserService(userServiceMock);
    assertNull(apiUserController.getUser(userId));
    verify(userServiceMock).getUserById(userId);
  }
}
