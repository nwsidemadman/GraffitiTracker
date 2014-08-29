package net.ccaper.graffitiTracker.service;

import net.ccaper.graffitiTracker.objects.BannedInet;

public interface BannedInetsService {
  boolean isInetBanned(String inet);

  void updateNumberRegistrationAttemptsInetInRange(String inet);
  
  void insertOrUpdateBannedInets(BannedInet bannedInet);
}
