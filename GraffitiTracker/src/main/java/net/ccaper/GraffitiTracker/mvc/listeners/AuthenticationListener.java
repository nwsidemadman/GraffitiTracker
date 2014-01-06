package net.ccaper.GraffitiTracker.mvc.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationListener implements
    ApplicationListener<InteractiveAuthenticationSuccessEvent> {
  private static final Logger logger = LoggerFactory
      .getLogger(AuthenticationListener.class);

  @Override
  public void onApplicationEvent(InteractiveAuthenticationSuccessEvent event) {
    String username = SecurityContextHolder.getContext().getAuthentication()
        .getName();
    logger.info(String
        .format("The user '%s' successfully logged in.", username));
    // TODO: update last login
  }
}
