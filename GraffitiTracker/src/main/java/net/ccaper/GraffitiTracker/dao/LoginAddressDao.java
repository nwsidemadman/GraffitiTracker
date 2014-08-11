package net.ccaper.GraffitiTracker.dao;

import java.util.List;

import net.ccaper.GraffitiTracker.objects.LoginInet;

public interface LoginAddressDao {
  void updateLoginAddressByUsername(String username, String ipAddress);
  
  List<LoginInet> getLoginAddressByUserId(int userId);
}
