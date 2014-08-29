package net.ccaper.graffitiTracker.service;

public interface UserSecurityService {
  String getUsernameFromSecurity();
  
  boolean isUserAnonymous();
}
