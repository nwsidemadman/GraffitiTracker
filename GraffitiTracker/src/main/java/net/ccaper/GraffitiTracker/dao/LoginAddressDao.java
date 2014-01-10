package net.ccaper.GraffitiTracker.dao;

public interface LoginAddressDao {
  void updateLoginAddressByUsername(String username, String ipAddress);
}
