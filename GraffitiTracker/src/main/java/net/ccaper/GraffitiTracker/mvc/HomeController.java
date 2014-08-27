package net.ccaper.GraffitiTracker.mvc;

import java.util.Map;

import net.ccaper.GraffitiTracker.objects.AppUser;
import net.ccaper.GraffitiTracker.service.AppUserService;
import net.ccaper.GraffitiTracker.service.UserSecurityService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
  private static final Logger logger = LoggerFactory
      .getLogger(HomeController.class);
  @Autowired
  private AppUserService appUserService;
  @Autowired
  private UserSecurityService userSecurityService;

  // visible for testing
  void setAppUserService(AppUserService appUserService) {
    this.appUserService = appUserService;
  }

  // visible for testing
  void setUserSecurityService(UserSecurityService userSecurityService) {
    this.userSecurityService = userSecurityService;
  }

  /**
   * Simply selects the home view to render by returning its name.
   */
  @RequestMapping(value = { "/", "/home" }, method = RequestMethod.GET)
  public String showHomePage(Map<String, Object> model) {
    if (!userSecurityService.isUserAnonymous()) {
      String username = userSecurityService.getUsernameFromSecurity();
      AppUser appUser = appUserService.getUserByUsername(username);
      model.put("appUser", appUser);
    }
    return "home";
  }

  @RequestMapping(value = "/contact", method = RequestMethod.GET)
  public String contact(Map<String, Object> model) {
    if (!userSecurityService.isUserAnonymous()) {
      String username = userSecurityService.getUsernameFromSecurity();
      AppUser appUser = appUserService.getUserByUsername(username);
      model.put("appUser", appUser);
    }
    return "contact";
  }

  @RequestMapping(value = "/about", method = RequestMethod.GET)
  public String about(Map<String, Object> model) {
    if (!userSecurityService.isUserAnonymous()) {
      String username = userSecurityService.getUsernameFromSecurity();
      AppUser appUser = appUserService.getUserByUsername(username);
      model.put("appUser", appUser);
    }
    return "about";
  }

  @RequestMapping(value = "/legal", method = RequestMethod.GET)
  public String legal(Map<String, Object> model) {
    if (!userSecurityService.isUserAnonymous()) {
      String username = userSecurityService.getUsernameFromSecurity();
      AppUser appUser = appUserService.getUserByUsername(username);
      model.put("appUser", appUser);
    }
    return "legal";
  }

  @RequestMapping(value = "/privacy", method = RequestMethod.GET)
  public String privacy(Map<String, Object> model) {
    if (!userSecurityService.isUserAnonymous()) {
      String username = userSecurityService.getUsernameFromSecurity();
      AppUser appUser = appUserService.getUserByUsername(username);
      model.put("appUser", appUser);
    }
    return "privacy";
  }
}
