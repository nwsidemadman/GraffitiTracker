package net.ccaper.GraffitiTracker.service;

import java.util.List;

import net.ccaper.GraffitiTracker.objects.LoginInet;

public interface LoginAddressService {
  void updateLoginAddressByUsername(String username, String ipAddress);
  
  List<LoginInet> getLoginAddressesByUserId(int userId);
}
