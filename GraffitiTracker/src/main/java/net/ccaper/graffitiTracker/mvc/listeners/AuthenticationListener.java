package net.ccaper.graffitiTracker.mvc.listeners;

import net.ccaper.graffitiTracker.service.AppUserService;
import net.ccaper.graffitiTracker.service.LoginAddressService;
import net.ccaper.graffitiTracker.service.UserSecurityService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

/**
 * @author ccaper
 * The listener interface for receiving authentication events.
 * The class that is interested in processing a authentication
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addAuthenticationListener<code> method. When
 * the authentication event occurs, that object's appropriate
 * method is invoked.
 *
 * @see AuthenticationEvent
 */
public class AuthenticationListener implements
    ApplicationListener<InteractiveAuthenticationSuccessEvent> {
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
