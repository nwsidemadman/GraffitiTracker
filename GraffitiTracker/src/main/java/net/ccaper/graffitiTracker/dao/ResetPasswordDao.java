package net.ccaper.graffitiTracker.dao;

import org.springframework.dao.EmptyResultDataAccessException;

import net.ccaper.graffitiTracker.objects.UserSecurityQuestion;

/**
 * 
 * @author ccaper
 * Data Access Object for resetting a password
 *
 */
public interface ResetPasswordDao {
  
  /**
   * Adds the unique url param for resetting a password.
   *
   * @param username the username
   */
  void addResetPassword(String username);

  /**
   * Gets the unique url param by username.
   *
   * @param username the username
   * @return the unique url param by username
   */
  String getUniqueUrlParamByUsername(String username);

  /**
   * Gets the {@link net.ccaper.graffitiTracker.objects.UserSecurityQuestion} by unique url param.
   *
   * @param uniqueUrlParam the unique url param
   * @return the {@link net.ccaper.graffitiTracker.objects.UserSecurityQuestion} by unique url param
   * @throws EmptyResultDataAccessException when empty
   */
  UserSecurityQuestion getUserSecurityQuestionByUniqueUrlParam(
      String uniqueUrlParam) throws EmptyResultDataAccessException;

  /**
   * Delete reset password by unique url param.
   *
   * @param uniqueUrlParam the unique url param
   */
  void deleteResetPasswordByUniqueUrlParam(String uniqueUrlParam);

  /**
   * Delete reset password where timestamp expired.
   */
  void deleteResetPasswordWhereTimestampExpired();
}
