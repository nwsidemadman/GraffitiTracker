package net.ccaper.graffitiTracker.dao;

import java.util.List;

import net.ccaper.graffitiTracker.objects.BannedInet;
import net.ccaper.graffitiTracker.objects.OriginalEditedBannedInet;

/**
 * 
 * @author ccaper
 * Data access object for {@link net.ccaper.graffitiTracker.objects.BannedInet}
 */
public interface BannedInetsDao {
  
  /**
   * Checks if is inet in range.
   *
   * @param inet the inet
   * @return true, if is inet in range (aka, banned)
   */
  boolean isInetInRange(String inet);

  /**
   * Update number registration attempts inet in range.
   *
   * @param inet the inet
   */
  void updateNumberRegistrationAttemptsInetInRange(String inet);
  
  /**
   * Insert or update banned inets.
   *
   * @param bannedInet the banned inet
   */
  void insertOrNonPKUpdateBannedInets(BannedInet bannedInet);
  
  // TODO(ccaper): javadoc
  void pkUpdateBannedInets(OriginalEditedBannedInet originalEditedBannedInet);
  
  /**
   * Gets the all banned inets.
   *
   * @return the all banned inets
   */
  List<BannedInet> getAllBannedInets();
}
