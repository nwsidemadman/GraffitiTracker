package net.ccaper.GraffitiTracker.service;

import net.ccaper.GraffitiTracker.objects.BannedInet;

public interface BannedInetsService {
  boolean isInetBanned(String inet);

  void updateNumberRegistrationAttemptsInetInRange(String inet);
  
  void insertOrUpdateBannedInets(BannedInet bannedInet);
}
