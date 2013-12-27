package net.ccaper.GraffitiTracker.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.ccaper.GraffitiTracker.dao.UserDao;
import net.ccaper.GraffitiTracker.objects.User;
import net.ccaper.GraffitiTracker.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService {
  @Autowired
  UserDao userDao;

  @Override
  public User getUser(String username) {
    return userDao.getUserByUsername(username);
  }
}
