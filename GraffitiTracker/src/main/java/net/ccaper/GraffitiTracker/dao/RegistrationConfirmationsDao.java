package net.ccaper.GraffitiTracker.dao;

import org.springframework.dao.EmptyResultDataAccessException;

public interface RegistrationConfirmationsDao {
  void addRegistrationConfirmationByUsername(String username);

  String getUniqueUrlParamByUsername(String username)
      throws EmptyResultDataAccessException;

  void deleteRegistrationConfirmationByUniqueUrlParam(String uniqueUrlParam);

  Integer getUserIdByUniqueUrlParam(String uniqueUrlParam);
}
