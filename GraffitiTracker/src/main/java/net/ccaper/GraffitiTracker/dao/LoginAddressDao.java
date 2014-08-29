package net.ccaper.GraffitiTracker.dao;

import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;

import net.ccaper.GraffitiTracker.objects.LoginInet;

/**
 * 
 * @author ccaper Data Access Object for
 *         {@link net.ccaper.GraffitiTracker.objects.LoginInet}
 * 
 */
public interface LoginAddressDao {

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
   * Gets the {@link net.ccaper.GraffitiTracker.objects.LoginInet} by user id.
   * 
   * @param userId
   *          the user id
   * @return the {@link net.ccaper.GraffitiTracker.objects.LoginInet} by user id
   */
  List<LoginInet> getLoginAddressByUserId(int userId);

  /**
   * Gets the users sharing inet.
   * 
   * @param inet
   *          the inet
   * @return the users sharing inet that is an ImmutablePair where key is
   *         username and value is userid
   */
  List<ImmutablePair<String, Integer>> getUsersSharingInet(String inet);
}
