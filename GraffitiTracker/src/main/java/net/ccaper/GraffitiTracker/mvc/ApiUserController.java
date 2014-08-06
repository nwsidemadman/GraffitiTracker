package net.ccaper.GraffitiTracker.mvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ccaper.GraffitiTracker.objects.AppUser;
import net.ccaper.GraffitiTracker.service.AppUserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PathVariable;
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
  
  public void setAppUserService(AppUserService appUserService) {
    this.appUserService = appUserService;
  }
  
  @RequestMapping(method = RequestMethod.GET, produces = "application/json")
  public @ResponseBody Map<String, List<AppUser>> getAllUsers() {
    Map<String, List<AppUser>> data = new HashMap<String, List<AppUser>>(1);
    data.put("data", appUserService.getAllUsers());
    return data;
  }
  
  @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
  public @ResponseBody Map<String, AppUser> getUser(@PathVariable int id) {
    Map<String, AppUser> data = new HashMap<String, AppUser>(1);
    data.put("data", appUserService.getUserById(id));
    return data;
  }
}
