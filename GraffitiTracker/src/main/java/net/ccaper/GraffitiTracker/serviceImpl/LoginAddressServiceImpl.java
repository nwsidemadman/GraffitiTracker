package net.ccaper.GraffitiTracker.serviceImpl;

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
    // TODO check formatting of ip address
    loginAddressDao.updateLoginAddressByUsername(username, ipAddress);
  }
}
