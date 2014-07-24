package net.ccaper.GraffitiTracker.service;

import java.util.List;

import net.ccaper.GraffitiTracker.objects.AppUser;
import net.ccaper.GraffitiTracker.objects.UserSecurityQuestion;

public interface AppUserService {
  AppUser getUser(String username);

  void addAppUser(AppUser appUser);

  boolean doesUsernameExist(String username);

  boolean doesEmailExist(String email);

  void updateLoginTimestamps(String username);

  void addRegistrationConfirmation(String username);

  String getRegistrationConfirmationUniqueUrlParamByUsername(String username);

  void deleteRegistrationConfirmationByUniqueUrlParam(String uniqueUrlParam);

  Integer getUserIdByRegistrationConfirmationUniqueUrlParam(String uniqueUrlParam);

  void updateAppUserAsActive(int userid);

  void deleteAppUsersWhenRegistrationExpired();

  void emailAdminStatsDaily();

  String getUsernameByEmail(String email);

  String getEmailByUsername(String username);

  void addResetPassword(String username);

  String getResetPasswordUniqueUrlParamByUsername(String username);

  UserSecurityQuestion getUserSecurityQuestionByResetPasswordUniqueUrlParam(String uniqueUrlParam);

  void deleteResetPasswordByUniqueUrlParam(String uniqueUrlParam);

  String getSecurityAnswerByUserid(int userid);

  String getUsernameByUserid(int userid);

  void updatePasswordByUserid(int userid, String passwordEncoded);
  
  void updateEmailByUserid(int userid, String email);
  
  void updateSecurityQuestionByUserid(int userid, String securityQuestion);
  
  void updateSecurityAnswerByUserid(int userid, String securityAnswer);
  
  List<AppUser> getAllUsers();
}
