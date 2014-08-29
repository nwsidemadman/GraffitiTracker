package net.ccaper.graffitiTracker.service;

import java.util.List;
import java.util.SortedMap;

import net.ccaper.graffitiTracker.objects.LoginInet;

/**
 * 
 * @author ccaper
 * 
 *         Service for handling
 *         {@link net.ccaper.graffitiTracker.objects.AppUser}'s login inets
 * 
 */
public interface LoginAddressService {

  /**
   * Update login address by username.
   * 
   * @param username
   *          the username
   * @param ipAddress
   *          the ip address
   */
  void updateLoginAddressByUsername(String username, String ipAddress);

  /**
   * Gets the login addresses by user id.
   * 
   * @param userId
   *          the user id
   * @return the login addresses by user id
   */
  List<LoginInet> getLoginAddressesByUserId(int userId);

  /**
   * Gets the users sharing inet.
   * 
   * @param inet
   *          the inet
   * @return the users sharing inet
   */
  SortedMap<String, Integer> getUsersSharingInet(String inet);
}
