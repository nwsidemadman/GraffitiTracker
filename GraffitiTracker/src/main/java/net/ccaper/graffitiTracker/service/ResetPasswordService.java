package net.ccaper.graffitiTracker.service;

/**
 * 
 * @author ccaper
 * 
 *         Service for resetting the
 *         {@link net.ccaper.graffitiTracker.object.AppUser}'s password
 * 
 */
public interface ResetPasswordService {

  /**
   * Delete reset password when timestamp expired.
   */
  void deleteResetPasswordWhenTimestampExpired();
}
