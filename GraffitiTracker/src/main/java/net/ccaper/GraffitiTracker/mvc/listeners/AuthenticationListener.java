package net.ccaper.GraffitiTracker.mvc.listeners;

import net.ccaper.GraffitiTracker.service.AppUserService;
import net.ccaper.GraffitiTracker.service.LoginAddressService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class AuthenticationListener implements
    ApplicationListener<InteractiveAuthenticationSuccessEvent> {
  private static final Logger logger = LoggerFactory
      .getLogger(AuthenticationListener.class);
  AppUserService appUserService;
  LoginAddressService loginAddressService;

  public void setAppUserService(AppUserService appUserService) {
    this.appUserService = appUserService;
  }

  public void setLoginAddressService(LoginAddressService loginAddressService) {
    this.loginAddressService = loginAddressService;
  }

  @Override
  public void onApplicationEvent(InteractiveAuthenticationSuccessEvent event) {
    String username = SecurityContextHolder.getContext().getAuthentication()
        .getName();
    appUserService.updateLoginTimestamps(username);
    loginAddressService.updateLoginAddressByUsername(username,
        ((WebAuthenticationDetails) event.getAuthentication().getDetails())
            .getRemoteAddress());
  }
}
