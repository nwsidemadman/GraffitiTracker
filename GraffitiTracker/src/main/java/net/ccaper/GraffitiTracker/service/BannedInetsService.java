package net.ccaper.GraffitiTracker.service;

public interface BannedInetsService {
  boolean isInetBanned(String inet);

  void updateNumberRegistrationAttemptsInetInRange(String inet);
}
