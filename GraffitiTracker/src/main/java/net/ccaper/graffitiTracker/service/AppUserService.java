package net.ccaper.graffitiTracker.service;

import java.util.List;

import net.ccaper.graffitiTracker.objects.AdminEditAppUser;
import net.ccaper.graffitiTracker.objects.AppUser;
import net.ccaper.graffitiTracker.objects.UserSecurityQuestion;

/**
 * 
 * @author ccaper
 * 
 *         Service for handling the
 *         {@link net.ccaper.graffitiTracker.objects.AppUser}
 * 
 */
public interface AppUserService {

  /**
   * Gets the user by username.
   * 
   * @param username
   *          the username
   * @return the {@link net.ccaper.graffitiTracker.objects.AppUser} by username
   */
  AppUser getUserByUsername(String username);

  /**
   * Gets the user by id.
   * 
   * @param id
   *          the id
   * @return the {@link net.ccaper.graffitiTracker.objects.AppUser} by id
   */
  AppUser getUserById(int id);

  /**
   * Adds the {@link net.ccaper.graffitiTracker.objects.AppUser} user.
   * 
   * @param appUser
   *          the app {@link net.ccaper.graffitiTracker.objects.AppUser}
   */
  void addAppUser(AppUser appUser);

  /**
   * Does username exist.
   * 
   * @param username
   *          the username
   * @return true, if exists
   */
  boolean doesUsernameExist(String username);

  /**
   * Does email exist.
   * 
   * @param email
   *          the email
   * @return true, if exists
   */
  boolean doesEmailExist(String email);

  /**
   * Update login timestamps.
   * 
   * @param username
   *          the username
   */
  void updateLoginTimestamps(String username);

  /**
   * Adds the registration confirmation.
   * 
   * @param username
   *          the username
   */
  void addRegistrationConfirmation(String username);

  /**
   * Gets the registration confirmation unique url param by username.
   * 
   * @param username
   *          the username
   * @return the registration confirmation unique url param by username
   */
  String getRegistrationConfirmationUniqueUrlParamByUsername(String username);

  /**
   * Delete registration confirmation by unique url param.
   * 
   * @param uniqueUrlParam
   *          the unique url param
   */
  void deleteRegistrationConfirmationByUniqueUrlParam(String uniqueUrlParam);

  /**
   * Gets the user id by registration confirmation unique url param.
   * 
   * @param uniqueUrlParam
   *          the unique url param
   * @return the user id by registration confirmation unique url param
   */
  Integer getUserIdByRegistrationConfirmationUniqueUrlParam(
      String uniqueUrlParam);

  /**
   * Update app user as active.
   * 
   * @param userid
   *          the userid
   */
  void updateAppUserAsActive(int userid);

  /**
   * Delete app users when registration expired.
   */
  void deleteAppUsersWhenRegistrationExpired();

  /**
   * Email admin stats daily.
   */
  void emailAdminStatsDaily();

  /**
   * Gets the username by email.
   * 
   * @param email
   *          the email
   * @return the username by email
   */
  String getUsernameByEmail(String email);

  /**
   * Gets the email by username.
   * 
   * @param username
   *          the username
   * @return the email by username
   */
  String getEmailByUsername(String username);

  /**
   * Adds the reset password.
   * 
   * @param username
   *          the username
   */
  void addResetPassword(String username);

  /**
   * Gets the reset password unique url param by username.
   * 
   * @param username
   *          the username
   * @return the reset password unique url param by username
   */
  String getResetPasswordUniqueUrlParamByUsername(String username);

  /**
   * Gets the {@link net.ccaper.graffitiTracker.objects.UserSecurityQuestion} by
   * reset password unique url param.
   * 
   * @param uniqueUrlParam
   *          the unique url param
   * @return the {@link net.ccaper.graffitiTracker.objects.UserSecurityQuestion}
   *         by reset password unique url param
   */
  UserSecurityQuestion getUserSecurityQuestionByResetPasswordUniqueUrlParam(
      String uniqueUrlParam);

  /**
   * Delete reset password by unique url param.
   * 
   * @param uniqueUrlParam
   *          the unique url param
   */
  void deleteResetPasswordByUniqueUrlParam(String uniqueUrlParam);

  /**
   * Gets the security answer by userid.
   * 
   * @param userid
   *          the userid
   * @return the security answer by userid
   */
  String getSecurityAnswerByUserid(int userid);

  /**
   * Gets the username by userid.
   * 
   * @param userid
   *          the userid
   * @return the username by userid
   */
  String getUsernameByUserid(int userid);

  /**
   * Update password by userid.
   * 
   * @param userid
   *          the userid
   * @param passwordEncoded
   *          the password encoded
   */
  void updatePasswordByUserid(int userid, String passwordEncoded);

  /**
   * Update email by userid.
   * 
   * @param userid
   *          the userid
   * @param email
   *          the email
   */
  void updateEmailByUserid(int userid, String email);

  /**
   * Update security question by userid.
   * 
   * @param userid
   *          the userid
   * @param securityQuestion
   *          the security question
   */
  void updateSecurityQuestionByUserid(int userid, String securityQuestion);

  /**
   * Update security answer by userid.
   * 
   * @param userid
   *          the userid
   * @param securityAnswer
   *          the security answer
   */
  void updateSecurityAnswerByUserid(int userid, String securityAnswer);

  /**
   * Gets the all {@link net.ccaper.graffitiTracker.objects.AppUser}s.
   * 
   * @return the all {@link net.ccaper.graffitiTracker.objects.AppUser}s
   */
  List<AppUser> getAllUsers();

  /**
   * Update {@link net.ccaper.graffitiTracker.objects.AppUser}.
   * 
   * @param uneditedUser
   *          the unedited {@link net.ccaper.graffitiTracker.objects.AppUser}
   * @param edits
   *          the {@link net.ccaper.graffitiTracker.objects.AdminEditAppUser}
   */
  void updateAppUser(AppUser uneditedUser, AdminEditAppUser edits);
}
