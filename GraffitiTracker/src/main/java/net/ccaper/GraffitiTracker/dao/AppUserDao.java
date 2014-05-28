package net.ccaper.GraffitiTracker.dao;

import java.util.List;

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

  int getCountLogins(int numberOfDays);

  int getCountUnconfirmedUsers(int numberOfDays);

  List<String> getSuperAdminEmails();

  String getUsernameByEmail(String email);

  String getEmailByUsername(String username);

  String getSecurityAnswerByUserid(int userid);

  String getUsernameByUserid(int userid);

  void updatePasswordByUserid(int userid, String passwordEncoded);
  
  void updateEmailByUserid(int userid, String email);
  
  void updateSecurityQuestionByUserid(int userid, String securityQuestion);
  
  void updateSecurityAnswerByUserid(int userid, String securityAnswer);
}
