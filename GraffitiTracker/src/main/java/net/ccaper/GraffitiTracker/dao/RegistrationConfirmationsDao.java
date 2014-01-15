package net.ccaper.GraffitiTracker.dao;

public interface RegistrationConfirmationsDao {
  void addRegistrationConfirmationByUsername(String username);

  String getUniqueUrlParamByUsername(String username);

  int getCountUniqueUrlParam(String uniqueUrlParam);

  void deleteRegistrationConfirmationByUniqueUrlParam(String uniqueUrlParam);

  int getUseridByUniqueUrlParam(String uniqueUrlParam);
}
