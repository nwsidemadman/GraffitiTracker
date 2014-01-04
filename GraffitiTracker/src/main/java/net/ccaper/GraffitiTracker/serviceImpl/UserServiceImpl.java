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
    int count = getCountUsernames(username);
    if (count == 0) {
      return false;
    } else {
      return true;
    }
  }

  // visible for testing
  int getCountUsernames(String username) {
    return userDao.getCountUsernames(username);
  }

  @Override
  public boolean doesEmailExist(String email) {
    int count = getCountEmails(email);
    if (count == 0) {
      return false;
    } else {
      return true;
    }
  }

  // visible for testing
  int getCountEmails(String email) {
    return userDao.getCountEmails(email);
  }
}
