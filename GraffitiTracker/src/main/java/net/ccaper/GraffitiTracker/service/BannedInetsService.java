package net.ccaper.GraffitiTracker.service;

public interface BannedInetsService {
  int getCountInetInRange(String inet);

  void updateNumberRegistrationAttemptsInetInRange(String inet);
}
