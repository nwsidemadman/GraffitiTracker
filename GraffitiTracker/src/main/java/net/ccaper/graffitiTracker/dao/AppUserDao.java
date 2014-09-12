package net.ccaper.graffitiTracker.dao;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;

import net.ccaper.graffitiTracker.enums.RoleEnum;
import net.ccaper.graffitiTracker.objects.AppUser;

// TODO(ccaper): check if invalid banned inet submit can be caught on ajax error and notify user
// TODO(ccaper): add key up js to list banned inets on search
// TODO(ccaper): remove sort arrows from all datatable columns that shouldn't be sorted

/**
 * 
 * @author ccaper Data Access Object for the
 *         {@link net.ccaper.graffitiTracker.objects.AppUser}
 */
public interface AppUserDao {

  /**
   * Gets the {@link net.ccaper.graffitiTracker.objects.AppUser} by username.
   * 
   * @param username
   *          the username
   * @return the {@link net.ccaper.graffitiTracker.objects.AppUser} by username
   */
  AppUser getAppUserByUsername(String username);

  /**
   * Adds the {@link net.ccaper.graffitiTracker.objects.AppUser}.
   * 
   * @param appUser
   *          the {@link net.ccaper.graffitiTracker.objects.AppUser}
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
   * Update isActive by userid.
   * 
   * @param userid
   *          the userid
   * @param isActive
   *          the isActive
   */
  void updateIsActiveByUserid(int userid, boolean isActive);

  /**
   * Delete {@link net.ccaper.graffitiTracker.objects.AppUser}s when
   * registration expired.
   */
  void deleteAppUsersWhenRegistrationExpired();

  /**
   * Gets the count of new users.
   * 
   * @param numberOfDays
   *          the number of days for count
   * @return the count of new users
   */
  int getCountNewUsers(int numberOfDays);

  /**
   * Gets the count logins.
   * 
   * @param numberOfDays
   *          the number of days for count
   * @return the count logins
   */
  int getCountLogins(int numberOfDays);

  /**
   * Gets the count of unconfirmed users.
   * 
   * @param numberOfDays
   *          the number of days for count
   * @return the count unconfirmed users
   */
  int getCountUnconfirmedUsers(int numberOfDays);

  /**
   * Gets the super admin emails.
   * 
   * @return the super admin emails
   */
  List<String> getSuperAdminEmails();

  /**
   * Gets the username by email.
   * 
   * @param email
   *          the email
   * @return the username by email
   * @throws EmptyResultDataAccessException
   *           when empty
   */
  String getUsernameByEmail(String email) throws EmptyResultDataAccessException;

  /**
   * Gets the email by username.
   * 
   * @param username
   *          the username
   * @return the email by username
   * @throws EmptyResultDataAccessException
   *           when empty
   */
  String getEmailByUsername(String username)
      throws EmptyResultDataAccessException;

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
   * @return all {@link net.ccaper.graffitiTracker.objects.AppUser}s
   */
  List<AppUser> getAllUsers();

  /**
   * Gets the {@link net.ccaper.graffitiTracker.objects.AppUser} by id.
   * 
   * @param id
   *          the id
   * @return the {@link net.ccaper.graffitiTracker.objects.AppUser} by id
   * @throws EmptyResultDataAccessException
   *           when empty
   */
  AppUser getAppUserById(int id) throws EmptyResultDataAccessException;

  /**
   * Adds the {@link net.ccaper.graffitiTracker.objects.Role}s by userid.
   * 
   * @param id
   *          the id
   * @param roleAdditions
   *          the {@link net.ccaper.graffitiTracker.objects.Role} additions
   */
  void addRolesByUserid(int id, List<RoleEnum> roleAdditions);

  /**
   * Delete {@link net.ccaper.graffitiTracker.objects.Role}s by userid.
   * 
   * @param id
   *          the id
   * @param roleDeletions
   *          the {@link net.ccaper.graffitiTracker.objects.Role} deletions
   */
  void deleteRolesByUserid(int id, List<RoleEnum> roleDeletions);
}
