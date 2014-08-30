package net.ccaper.graffitiTracker.serviceImpl;

import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import net.ccaper.graffitiTracker.service.UserSecurityService;

/**
 * 
 * @author ccaper
 * 
 *         Implementation of Spring user security for the users security service
 * 
 */
@Service("userSecurityService")
public class UserSecurityServiceImpl implements UserSecurityService {

  /*
   * (non-Javadoc)
   * 
   * @see
   * net.ccaper.graffitiTracker.service.UserSecurityService#getUsernameFromSecurity
   * ()
   */
  @Override
  public String getUsernameFromSecurity() {
    return SecurityContextHolder.getContext().getAuthentication().getName();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * net.ccaper.graffitiTracker.service.UserSecurityService#isUserAnonymous()
   */
  @Override
  public boolean isUserAnonymous() {
    AuthenticationTrustResolver authenticationTrustResolver = new AuthenticationTrustResolverImpl();
    return authenticationTrustResolver.isAnonymous(SecurityContextHolder
        .getContext().getAuthentication());
  }
}
