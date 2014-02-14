package net.ccaper.GraffitiTracker.dao;

public interface BannedInetsDao {
  int selectCountInetInRange(String inet);

  void updateNumberRegistrationAttemptsInetInRange(String inet);
}
