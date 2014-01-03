package net.ccaper.GraffitiTracker.mvc;

import java.util.Map;

import net.ccaper.GraffitiTracker.objects.User;
import net.ccaper.GraffitiTracker.service.UserService;

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
  // TODO: unit test
  private static final Logger logger = LoggerFactory
      .getLogger(HomeController.class);
  @Autowired
  UserService userService;

  /**
   * Simply selects the home view to render by returning its name.
   */
  @RequestMapping(value = { "/", "/home" }, method = RequestMethod.GET)
  public String showHomePage(Map<String, Object> model) {
    logger.info("Welcome home!");

    AuthenticationTrustResolver authenticationTrustResolver = new AuthenticationTrustResolverImpl();
    if (!authenticationTrustResolver.isAnonymous(SecurityContextHolder
        .getContext().getAuthentication())) {
      String username = SecurityContextHolder.getContext().getAuthentication()
          .getName();
      User user = userService.getUser(username);
      model.put("user", user);
    }

    return "home";
  }
}
