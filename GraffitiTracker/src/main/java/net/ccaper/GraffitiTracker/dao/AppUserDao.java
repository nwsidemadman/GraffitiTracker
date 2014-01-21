package net.ccaper.GraffitiTracker.dao;

import net.ccaper.GraffitiTracker.objects.AppUser;

public interface AppUserDao {
  AppUser getAppUserByUsername(String username);

  void addAppUser(AppUser appUser);

  int getCountUsernames(String username);

  int getCountEmails(String email);

  void updateLoginTimestamps(String username);

  void updateAppUserAsActive(int userid);
  
  void deleteAppUsersWhenRegistrationExpired();
  
  int getCountNewUsers(int numberOfDays);
}
