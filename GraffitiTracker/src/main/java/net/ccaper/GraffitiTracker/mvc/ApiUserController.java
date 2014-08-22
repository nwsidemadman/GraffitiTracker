package net.ccaper.GraffitiTracker.mvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ccaper.GraffitiTracker.objects.AppUser;
import net.ccaper.GraffitiTracker.objects.LoginInet;
import net.ccaper.GraffitiTracker.objects.AdminEditAppUser;
import net.ccaper.GraffitiTracker.service.AppUserService;
import net.ccaper.GraffitiTracker.service.LoginAddressService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@Scope("session")
public class ApiUserController {
  private static final Logger logger = LoggerFactory
      .getLogger(ApiUserController.class);
  
  @Autowired
  AppUserService appUserService;
  
  @Autowired
  LoginAddressService loginAddressService;
  
  public void setAppUserService(AppUserService appUserService) {
    this.appUserService = appUserService;
  }
  
  public void setLoginAddressService(LoginAddressService loginAddressService) {
    this.loginAddressService = loginAddressService;
  }
  
  @RequestMapping(method = RequestMethod.GET)
  public @ResponseBody Map<String, List<AppUser>> getAllUsers() {
    Map<String, List<AppUser>> data = new HashMap<String, List<AppUser>>(1);
    data.put("data", appUserService.getAllUsers());
    return data;
  }
  
  // TODO(ccaper): unit test
  @RequestMapping(value = "/{userId}/logins", method = RequestMethod.GET)
  public @ResponseBody Map<String, List<LoginInet>> getUserLoginAddresses(@PathVariable int userId) {
    Map<String, List<LoginInet>> data = new HashMap<String, List<LoginInet>>(1);
    data.put("data", loginAddressService.getLoginAddressesByUserId(userId));
    return data;
  }
  
  //TODO(ccaper): unit test
  @RequestMapping(value = "{userId}", method = RequestMethod.PUT)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void editUser(@PathVariable int userId, @RequestBody AdminEditAppUser editedUser) {
    AppUser uneditedUser = appUserService.getUserById(userId);
    appUserService.updateAppUser(uneditedUser, editedUser);
  }
}
