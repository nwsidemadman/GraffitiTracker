package net.ccaper.GraffitiTracker.service;

import net.ccaper.GraffitiTracker.objects.AppUser;

public interface AppUserService {
  AppUser getUser(String username);
  
  void addAppUser(AppUser appUser);
  
  boolean doesUsernameExist(String username);
  
  boolean doesEmailExist(String email);
  
  void updateLoginTimestamps(String username);
}