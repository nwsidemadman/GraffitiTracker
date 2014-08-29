package net.ccaper.graffitiTracker.service;

import java.util.List;
import java.util.SortedMap;

import net.ccaper.graffitiTracker.objects.LoginInet;

public interface LoginAddressService {
  void updateLoginAddressByUsername(String username, String ipAddress);
  
  List<LoginInet> getLoginAddressesByUserId(int userId);
  
  SortedMap<String, Integer> getUsersSharingInet(String inet);
}
