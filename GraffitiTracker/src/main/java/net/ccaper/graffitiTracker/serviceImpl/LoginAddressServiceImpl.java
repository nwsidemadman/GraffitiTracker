package net.ccaper.graffitiTracker.serviceImpl;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.ccaper.graffitiTracker.dao.LoginAddressDao;
import net.ccaper.graffitiTracker.objects.LoginInet;
import net.ccaper.graffitiTracker.service.LoginAddressService;
import net.ccaper.graffitiTracker.utils.InetAddressUtils;

/**
 * 
 * @author ccaper
 * 
 *         Implementation of the login address service
 * 
 */
@Service("loginAddressService")
public class LoginAddressServiceImpl implements LoginAddressService {
  private static final Logger logger = LoggerFactory
      .getLogger(LoginAddressServiceImpl.class);
  @Autowired
  private LoginAddressDao loginAddressDao;

  // visible for testing
  /**
   * Sets the login address dao.
   * 
   * @param loginAddressDao
   *          the new login address dao
   */
  void setLoginAddressDao(LoginAddressDao loginAddressDao) {
    this.loginAddressDao = loginAddressDao;
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.ccaper.graffitiTracker.service.LoginAddressService#
   * updateLoginAddressByUsername(java.lang.String, java.lang.String)
   */
  @Override
  public void updateLoginAddressByUsername(String username, String ipAddress) {
    if (InetAddressUtils.isInetValid(ipAddress)) {
      loginAddressDao.updateLoginAddressByUsername(username, ipAddress);
    } else {
      logger.error(String.format("The ip address '%s' is not valid.", ipAddress));
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.ccaper.graffitiTracker.service.LoginAddressService#
   * getLoginAddressesByUserId(int)
   */
  @Override
  public List<LoginInet> getLoginAddressesByUserId(int userId) {
    return loginAddressDao.getLoginAddressByUserId(userId);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * net.ccaper.graffitiTracker.service.LoginAddressService#getUsersSharingInet
   * (java.lang.String)
   */
  @Override
  public SortedMap<String, Integer> getUsersSharingInet(String inet) {
    List<ImmutablePair<String, Integer>> tuples = loginAddressDao
        .getUsersSharingInet(inet);
    SortedMap<String, Integer> results = new TreeMap<String, Integer>();
    for (ImmutablePair<String, Integer> tuple : tuples) {
      results.put(tuple.getLeft(), tuple.getRight());
    }
    return results;
  }
}
