package net.ccaper.graffitiTracker.serviceImpl;

import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import net.ccaper.graffitiTracker.service.UserSecurityService;

@Service("userSecurityService")
public class UserSecurityServiceImpl implements UserSecurityService {
  @Override
  public String getUsernameFromSecurity() {
    return SecurityContextHolder.getContext().getAuthentication().getName();
  }

  @Override
  public boolean isUserAnonymous() {
    AuthenticationTrustResolver authenticationTrustResolver = new AuthenticationTrustResolverImpl();
    return authenticationTrustResolver.isAnonymous(SecurityContextHolder
        .getContext().getAuthentication());
  }
}
