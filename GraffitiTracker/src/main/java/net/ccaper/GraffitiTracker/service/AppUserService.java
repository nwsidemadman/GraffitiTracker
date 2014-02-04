package net.ccaper.GraffitiTracker.service;

import net.ccaper.GraffitiTracker.objects.AppUser;

public interface AppUserService {
  AppUser getUser(String username);

  void addAppUser(AppUser appUser);

  boolean doesUsernameExist(String username);

  boolean doesEmailExist(String email);

  void updateLoginTimestamps(String username);

  void addRegistrationConfirmation(String username);

  String getRegistrationConfirmationUniqueUrlParamByUsername(String username);

  void deleteRegistrationConfirmationByUniqueUrlParam(String uniqueUrlParam);

  Integer getUseridByRegistrationConfirmationUniqueUrlParam(String uniqueUrlParam);

  void updateAppUserAsActive(int userid);

  void deleteAppUsersWhenRegistrationExpired();

  void emailAdminStatsDaily();

  String getUsernameByEmail(String email);

  String getEmailByUsername(String username);

  void addResetPassword(String username);

  String getResetPasswordUniqueUrlParamByUsername(String username);
}
