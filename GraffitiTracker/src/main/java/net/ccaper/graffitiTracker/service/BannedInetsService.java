package net.ccaper.graffitiTracker.service;

import java.util.List;

import net.ccaper.graffitiTracker.objects.BannedInet;
import net.ccaper.graffitiTracker.objects.OriginalEditedBannedInet;

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
   * Insert or update {@link net.ccaper.graffitiTracker.objects.BannedInet}
   * where values changing is not inet portion.
   * 
   * @param bannedInet
   *          the {@link net.ccaper.graffitiTracker.objects.BannedInet}
   */
  void insertOrNonInetUpdateBannedInets(BannedInet bannedInet);
  
  // TODO(ccaper): javadoc
  void inetUpdateBannedInets(OriginalEditedBannedInet originalEditedBannedInet);

  /**
   * Gets the all banned inets.
   * 
   * @return the all banned inets
   */
  List<BannedInet> getAllBannedInets();
}
