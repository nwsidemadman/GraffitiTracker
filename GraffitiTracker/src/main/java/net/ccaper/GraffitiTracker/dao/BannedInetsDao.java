package net.ccaper.GraffitiTracker.dao;

import net.ccaper.GraffitiTracker.objects.BannedInet;

/**
 * 
 * @author ccaper
 * Data access object for {@link net.ccaper.GraffitiTracker.objects.BannedInet}
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
  void insertOrUpdateBannedInets(BannedInet bannedInet);
}
