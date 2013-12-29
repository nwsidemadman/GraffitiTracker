package net.ccaper.GraffitiTracker.dao;

import net.ccaper.GraffitiTracker.objects.User;

public interface UserDao {
  User getUserByUsername(String username);
  
  void addUser(User user);
  
  boolean doesUsernameExist(String username);
  
  boolean doesEmailExist(String email);
}
