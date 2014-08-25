package net.ccaper.GraffitiTracker.mvc;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ccaper.GraffitiTracker.objects.AdminEditAppUser;
import net.ccaper.GraffitiTracker.objects.AppUser;
import net.ccaper.GraffitiTracker.objects.LoginInet;
import net.ccaper.GraffitiTracker.service.AppUserService;
import net.ccaper.GraffitiTracker.service.LoginAddressService;

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
  
  @Test
  public void testGetUserLoginAddresses() throws Exception {
    int userId = 5;
    List<LoginInet> loginInets = new ArrayList<LoginInet>(1);
    LoginInet loginInet = new LoginInet();
    loginInet.setInet("127.0.0.1");
    loginInet.setIsInetBanned(false);
    loginInet.setLastVisit(new Timestamp(5L));
    loginInet.setNumberVisits(5);
    loginInets.add(loginInet);
    ApiUserController apiUserController = new ApiUserController();
    LoginAddressService loginAddressServiceMock = mock(LoginAddressService.class);
    when(loginAddressServiceMock.getLoginAddressesByUserId(userId)).thenReturn(loginInets);
    apiUserController.setLoginAddressService(loginAddressServiceMock);
    Map<String, List<LoginInet>> data = new HashMap<String, List<LoginInet>>();
    data.put("data", loginInets);
    assertEquals(data, apiUserController.getUserLoginAddresses(userId));
    verify(loginAddressServiceMock).getLoginAddressesByUserId(userId);
  }
  
  @Test
  public void testEditUser() throws Exception {
    AppUser uneditedUser = new AppUser();
    uneditedUser.setUserId(5);
    AdminEditAppUser editedUser = new AdminEditAppUser();
    ApiUserController apiUserController = new ApiUserController();
    AppUserService userServiceMock = mock(AppUserService.class);
    when(userServiceMock.getUserById(uneditedUser.getUserId())).thenReturn(uneditedUser);
    apiUserController.setAppUserService(userServiceMock);
    apiUserController.editUser(uneditedUser.getUserId(), editedUser);
    verify(userServiceMock).getUserById(uneditedUser.getUserId());
  }
}
