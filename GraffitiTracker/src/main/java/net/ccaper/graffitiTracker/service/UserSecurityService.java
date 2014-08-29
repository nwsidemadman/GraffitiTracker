package net.ccaper.graffitiTracker.service;

/**
 * 
 * @author ccaper
 * 
 *         Service for getting info from framework security
 * 
 */
public interface UserSecurityService {

  /**
   * Gets the username from security.
   * 
   * @return the username from security
   */
  String getUsernameFromSecurity();

  /**
   * Checks if is user anonymous.
   * 
   * @return true, if user is anonymous
   */
  boolean isUserAnonymous();
}
