package net.ccaper.graffitiTracker.mvc;

import java.util.Map;

import net.ccaper.graffitiTracker.objects.AppUser;
import net.ccaper.graffitiTracker.service.AppUserService;
import net.ccaper.graffitiTracker.service.UserSecurityService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 
 * @author ccaper Handles requests for the application home page.
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
   * Sets the {@link net.ccaper.graffitiTracker.service.UserSecurityService}.
   * 
   * @param userSecurityService
   *          the new
   *          {@link net.ccaper.graffitiTracker.service.UserSecurityService}
   */
  void setUserSecurityService(UserSecurityService userSecurityService) {
    this.userSecurityService = userSecurityService;
  }

  /**
   * Simply selects the home view to render by returning its name.
   * 
   * @param model
   *          the model
   * @return the view name
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

  /**
   * Contact.
   * 
   * @param model
   *          the model
   * @return the view name
   */
  @RequestMapping(value = "/contact", method = RequestMethod.GET)
  public String contact(Map<String, Object> model) {
    if (!userSecurityService.isUserAnonymous()) {
      String username = userSecurityService.getUsernameFromSecurity();
      AppUser appUser = appUserService.getUserByUsername(username);
      model.put("appUser", appUser);
    }
    return "contact";
  }

  /**
   * About.
   * 
   * @param model
   *          the model
   * @return the view name
   */
  @RequestMapping(value = "/about", method = RequestMethod.GET)
  public String about(Map<String, Object> model) {
    if (!userSecurityService.isUserAnonymous()) {
      String username = userSecurityService.getUsernameFromSecurity();
      AppUser appUser = appUserService.getUserByUsername(username);
      model.put("appUser", appUser);
    }
    return "about";
  }

  /**
   * Legal.
   * 
   * @param model
   *          the model
   * @return the view name
   */
  @RequestMapping(value = "/legal", method = RequestMethod.GET)
  public String legal(Map<String, Object> model) {
    if (!userSecurityService.isUserAnonymous()) {
      String username = userSecurityService.getUsernameFromSecurity();
      AppUser appUser = appUserService.getUserByUsername(username);
      model.put("appUser", appUser);
    }
    return "legal";
  }

  /**
   * Privacy.
   * 
   * @param model
   *          the model
   * @return the view name
   */
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
