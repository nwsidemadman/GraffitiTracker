package net.ccaper.GraffitiTracker.serviceImpl;

import net.ccaper.GraffitiTracker.dao.UserDao;
import net.ccaper.GraffitiTracker.objects.User;
import net.ccaper.GraffitiTracker.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements UserService {
  @Autowired
  UserDao userDao;

  @Override
  public User getUser(String username) {
    return userDao.getUserByUsername(username);
  }

  @Override
  public void addUser(User user) {
    userDao.addUser(user);
  }

  @Override
  public boolean doesUsernameExist(String username) {
    return userDao.doesUsernameExist(username);
  }

  @Override
  public boolean doesEmailExist(String email) {
    return userDao.doesEmailExist(email);
  }
}
