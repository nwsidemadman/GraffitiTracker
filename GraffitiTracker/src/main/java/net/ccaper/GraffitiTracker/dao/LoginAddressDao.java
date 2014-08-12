package net.ccaper.GraffitiTracker.dao;

import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;

import net.ccaper.GraffitiTracker.objects.LoginInet;

public interface LoginAddressDao {
  void updateLoginAddressByUsername(String username, String ipAddress);
  
  List<LoginInet> getLoginAddressByUserId(int userId);
  
  List<ImmutablePair<Integer, String>> getUsersSharingInet(String inet);
}
