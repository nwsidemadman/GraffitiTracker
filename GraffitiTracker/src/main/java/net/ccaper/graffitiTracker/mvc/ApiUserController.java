package net.ccaper.graffitiTracker.mvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ccaper.graffitiTracker.objects.AdminEditAppUser;
import net.ccaper.graffitiTracker.objects.AppUser;
import net.ccaper.graffitiTracker.objects.LoginInet;
import net.ccaper.graffitiTracker.service.AppUserService;
import net.ccaper.graffitiTracker.service.LoginAddressService;

import org.codehaus.jackson.map.annotate.JsonView;
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

/**
 * Handles the actions for Api calls to the
 * {@link net.ccaper.graffitiTracker.objects.AppUser}
 * 
 * @author ccaper
 * 
 */
@RestController
@RequestMapping("/api/users")
@Scope("session")
public class ApiUserController {
  private static final Logger logger = LoggerFactory
      .getLogger(ApiUserController.class);
  @Autowired
  private AppUserService appUserService;
  @Autowired
  private LoginAddressService loginAddressService;

  // visible for testing
  /**
   * Sets the {@link net.ccaper.graffitiTracker.service.AppUserService}.
   * 
   * @param appUserService
   *          the new {@link net.ccaper.graffitiTracker.service.AppUserService}
   */
  void setAppUserService(AppUserService appUserService) {
    this.appUserService = appUserService;
  }

  // visible for testing
  /**
   * Sets the {@link net.ccaper.graffitiTracker.service.LoginAddressService}.
   * 
   * @param loginAddressService
   *          the new
   *          {@link net.ccaper.graffitiTracker.service.LoginAddressService}
   */
  void setLoginAddressService(LoginAddressService loginAddressService) {
    this.loginAddressService = loginAddressService;
  }

  /**
   * Gets the all {@link net.ccaper.graffitiTracker.objects.AppUser}.
   * 
   * @return the all {@link net.ccaper.graffitiTracker.objects.AppUser}s
   */
  @RequestMapping(method = RequestMethod.GET)
  @JsonView
  public Map<String, List<AppUser>> getAllUsers() {
    Map<String, List<AppUser>> data = new HashMap<String, List<AppUser>>(1);
    data.put("data", appUserService.getAllUsers());
    return data;
  }

  /**
   * Gets the user login addresses.
   * 
   * @param userId
   *          the user id
   * @return the user {@link net.ccaper.graffitiTracker.objects.LoginInet}s
   */
  @RequestMapping(value = "/{userId}/logins", method = RequestMethod.GET)
  @JsonView
  public Map<String, List<LoginInet>> getUserLoginAddresses(
      @PathVariable int userId) {
    Map<String, List<LoginInet>> data = new HashMap<String, List<LoginInet>>(1);
    data.put("data", loginAddressService.getLoginAddressesByUserId(userId));
    return data;
  }

  /**
   * Edits the {@link net.ccaper.graffitiTracker.objects.AdminEditAppUser}.
   * 
   * @param userId
   *          the user id
   * @param editedUser
   *          the {@link net.ccaper.graffitiTracker.objects.AdminEditAppUser}
   */
  @RequestMapping(value = "{userId}", method = RequestMethod.PUT)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void editUser(@PathVariable int userId,
      @RequestBody AdminEditAppUser editedUser) {
    AppUser uneditedUser = appUserService.getUserById(userId);
    appUserService.updateAppUser(uneditedUser, editedUser);
  }
}
