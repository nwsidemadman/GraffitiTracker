package net.ccaper.GraffitiTracker.serviceImpl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.ccaper.GraffitiTracker.dao.LoginAddressDao;
import net.ccaper.GraffitiTracker.service.LoginAddressService;

@Service("loginAddressService")
public class LoginAddressServiceImpl implements LoginAddressService {
  @Autowired
  LoginAddressDao loginAddressDao;

  @Override
  public void updateLoginAddressByUsername(String username, String ipAddress) {
    if (isIpAddressValid(ipAddress)) {
      loginAddressDao.updateLoginAddressByUsername(username, ipAddress);
    }
  }
  
  // visible for testing
  boolean isIpAddressValid(String ipAddress) {
    String[] ipTokens = StringUtils.split(ipAddress, ".");
    if (ipTokens.length != 4) {
      throw new IllegalArgumentException(String.format("%s is not a valid IPV4 address.", ipAddress));
    }
    for (String ipToken : ipTokens) {
      try {
        int number = Integer.parseInt(ipToken);
        if (number < 0 || number > 255) {
          throw new IllegalArgumentException(String.format("%s is not a valid IPV4 address.", ipAddress));
        }
      } catch (NumberFormatException e) {
        throw new IllegalArgumentException(String.format("%s is not a valid IPV4 address.", ipAddress));
      }
    }
    return true;
  }
}
