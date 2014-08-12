package net.ccaper.GraffitiTracker.service;

import java.util.List;
import java.util.Map;

import net.ccaper.GraffitiTracker.objects.LoginInet;

public interface LoginAddressService {
  void updateLoginAddressByUsername(String username, String ipAddress);
  
  List<LoginInet> getLoginAddressesByUserId(int userId);
  
  Map<Integer, String> getUsersSharingInet(String inet);
}
