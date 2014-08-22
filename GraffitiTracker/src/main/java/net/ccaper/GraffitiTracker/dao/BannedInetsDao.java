package net.ccaper.GraffitiTracker.dao;

import net.ccaper.GraffitiTracker.objects.BannedInet;

public interface BannedInetsDao {
  boolean isInetInRange(String inet);

  void updateNumberRegistrationAttemptsInetInRange(String inet);
  
  void insertOrUpdateBannedInets(BannedInet bannedInet);
}
