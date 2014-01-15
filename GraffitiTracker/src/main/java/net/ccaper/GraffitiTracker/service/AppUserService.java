package net.ccaper.GraffitiTracker.service;

import net.ccaper.GraffitiTracker.objects.AppUser;

public interface AppUserService {
  AppUser getUser(String username);

  void addAppUser(AppUser appUser);

  boolean doesUsernameExist(String username);

  boolean doesEmailExist(String email);

  void updateLoginTimestamps(String username);

  void addRegistrationConfirmation(String username);

  String getUniqueUrlParam(String username);

  boolean doesUniqueUrlParamExist(String uniqueUrlParam);

  void deleteRegistrationConfirmationByUniqueUrlParam(String uniqueUrlParam);

  int getUseridByUniqueUrlParam(String uniqueUrlParam);

  void updateAppUserAsActive(int userid);
}
