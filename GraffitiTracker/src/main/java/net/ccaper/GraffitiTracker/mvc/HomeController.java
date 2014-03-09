package net.ccaper.GraffitiTracker.mvc;

import java.util.Map;

import net.ccaper.GraffitiTracker.objects.AppUser;
import net.ccaper.GraffitiTracker.service.AppUserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.context.SecurityContextHolder;
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
  AppUserService appUserService;

  public void setAppUserService(AppUserService appUserService) {
    this.appUserService = appUserService;
  }

  /**
   * Simply selects the home view to render by returning its name.
   */
  @RequestMapping(value = { "/", "/home" }, method = RequestMethod.GET)
  public String showHomePage(Map<String, Object> model) {
    if (!isUserAnonymous()) {
      String username = getUsernameFromSecurity();
      AppUser appUser = appUserService.getUser(username);
      model.put("appUser", appUser);
    }

    return "home";
  }

  // visible for mocking
  String getUsernameFromSecurity() {
    return SecurityContextHolder.getContext().getAuthentication().getName();
  }

  // visible for mocking
  boolean isUserAnonymous() {
    AuthenticationTrustResolver authenticationTrustResolver = new AuthenticationTrustResolverImpl();
    return authenticationTrustResolver.isAnonymous(SecurityContextHolder
        .getContext().getAuthentication());
  }
  
  @RequestMapping(value = "/contact", method = RequestMethod.GET)
  public String contact(Map<String, Object> model) {
    if (!isUserAnonymous()) {
      String username = getUsernameFromSecurity();
      AppUser appUser = appUserService.getUser(username);
      model.put("appUser", appUser);
    }
    // TODO: can this be static?
    return "contact";
  }
  
  @RequestMapping(value = "/about", method = RequestMethod.GET)
  public String about(Map<String, Object> model) {
    if (!isUserAnonymous()) {
      String username = getUsernameFromSecurity();
      AppUser appUser = appUserService.getUser(username);
      model.put("appUser", appUser);
    }
    // TODO: can this be static?
    return "about";
  }
  
  @RequestMapping(value = "/legal", method = RequestMethod.GET)
  public String legal(Map<String, Object> model) {
    if (!isUserAnonymous()) {
      String username = getUsernameFromSecurity();
      AppUser appUser = appUserService.getUser(username);
      model.put("appUser", appUser);
    }
    // TODO: can this be static?
    return "legal";
  }
  
  @RequestMapping(value = "/privacy", method = RequestMethod.GET)
  public String privacy(Map<String, Object> model) {
    if (!isUserAnonymous()) {
      String username = getUsernameFromSecurity();
      AppUser appUser = appUserService.getUser(username);
      model.put("appUser", appUser);
    }
    // TODO: can this be static?
    return "privacy";
  }
}
