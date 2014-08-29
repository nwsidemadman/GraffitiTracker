package net.ccaper.graffitiTracker.dao;

import org.springframework.dao.EmptyResultDataAccessException;

/**
 * 
 * @author ccaper
 * Data Access Object for handling registration confirmations
 *
 */
public interface RegistrationConfirmationsDao {
  
  /**
   * Adds the registration confirmation by username.
   *
   * @param username the username
   */
  void addRegistrationConfirmationByUsername(String username);

  /**
   * Gets the unique url param by username.
   *
   * @param username the username
   * @return the unique url param by username
   * @throws EmptyResultDataAccessException when empty
   */
  String getUniqueUrlParamByUsername(String username)
      throws EmptyResultDataAccessException;

  /**
   * Delete registration confirmation by unique url param.
   *
   * @param uniqueUrlParam the unique url param
   */
  void deleteRegistrationConfirmationByUniqueUrlParam(String uniqueUrlParam);

  /**
   * Gets the user id by unique url param.
   *
   * @param uniqueUrlParam the unique url param
   * @return the user id by unique url param
   */
  Integer getUserIdByUniqueUrlParam(String uniqueUrlParam);
}
