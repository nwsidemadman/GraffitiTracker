package net.ccaper.GraffitiTracker.mvc.listeners;

import net.ccaper.GraffitiTracker.service.AppUserService;
import net.ccaper.GraffitiTracker.service.LoginAddressService;
import net.ccaper.GraffitiTracker.service.UserSecurityService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class AuthenticationListener implements
    ApplicationListener<InteractiveAuthenticationSuccessEvent> {
  private static final Logger logger = LoggerFactory
      .getLogger(AuthenticationListener.class);
  @Autowired
  AppUserService appUserService;
  @Autowired
  LoginAddressService loginAddressService;
  @Autowired
  UserSecurityService userSecurityService;

  @Override
  public void onApplicationEvent(InteractiveAuthenticationSuccessEvent event) {
    String username = userSecurityService.getUsernameFromSecurity();
    appUserService.updateLoginTimestamps(username);
    loginAddressService.updateLoginAddressByUsername(username,
        ((WebAuthenticationDetails) event.getAuthentication().getDetails())
            .getRemoteAddress());
  }
}
