package net.ccaper.GraffitiTracker.dao;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;

import net.ccaper.GraffitiTracker.enums.RoleEnum;
import net.ccaper.GraffitiTracker.objects.AppUser;

public interface AppUserDao {
  AppUser getAppUserByUsername(String username);

  void addAppUser(AppUser appUser);

  int getCountUsernames(String username);

  int getCountEmails(String email);

  void updateLoginTimestamps(String username);

  // TODO(ccaper): remove
  void updateAppUserAsActive(int userid);
  
  void updateIsActiveByUserid(int userid, boolean isActive);

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
  
  List<AppUser> getAllUsers();
  
  AppUser getAppUserById(int id) throws EmptyResultDataAccessException;
  
  void addRolesByUserid(int id, List<RoleEnum> roleAdditions);
  
  void deleteRolesByUserid(int id, List<RoleEnum> roleDeletions);
}
