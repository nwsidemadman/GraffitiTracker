package net.ccaper.GraffitiTracker.service;

import net.ccaper.GraffitiTracker.objects.User;

public interface UserService {
  User getUser(String username);
  
  void addUser(User user);
  
  boolean doesUsernameExist(String username);
  
  boolean doesEmailExist(String email);
}
