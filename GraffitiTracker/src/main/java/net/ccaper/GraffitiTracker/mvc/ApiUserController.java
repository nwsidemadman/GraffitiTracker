package net.ccaper.GraffitiTracker.mvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ccaper.GraffitiTracker.objects.AppUser;
import net.ccaper.GraffitiTracker.objects.BannedInet;
import net.ccaper.GraffitiTracker.objects.LoginInet;
import net.ccaper.GraffitiTracker.service.AppUserService;
import net.ccaper.GraffitiTracker.service.LoginAddressService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
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
  
  // TODO(ccaper): this will likely be pulled out in favor of controller call, remove this AND security
  @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
  public @ResponseBody Map<String, AppUser> getUser(@PathVariable int userId) {
    Map<String, AppUser> data = new HashMap<String, AppUser>(1);
    data.put("data", appUserService.getUserById(userId));
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
  @RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
  @ResponseBody public AppUser editUser(@RequestBody AppUser appUser) {
    logger.info("AppUser: " + appUser);
    return appUser;
  }
}
