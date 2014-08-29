package net.ccaper.graffitiTracker.service;

import net.ccaper.graffitiTracker.objects.BannedInet;

/**
 * 
 * @author ccaper
 * 
 *         Servie for handling
 *         {@link net.ccaper.graffitiTracker.objects.BannedInet}s
 * 
 */
public interface BannedInetsService {

  /**
   * Checks if is inet banned.
   * 
   * @param inet
   *          the inet
   * @return true, if inet is banned
   */
  boolean isInetBanned(String inet);

  /**
   * Update number registration attempts on inet in range.
   * 
   * @param inet
   *          the inet
   */
  void updateNumberRegistrationAttemptsInetInRange(String inet);

  /**
   * Insert or update {@link net.ccaper.graffitiTracker.objects.BannedInet}.
   * 
   * @param bannedInet
   *          the {@link net.ccaper.graffitiTracker.objects.BannedInet}
   */
  void insertOrUpdateBannedInets(BannedInet bannedInet);
}
