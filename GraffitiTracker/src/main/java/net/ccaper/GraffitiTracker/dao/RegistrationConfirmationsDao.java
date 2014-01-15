package net.ccaper.GraffitiTracker.dao;

public interface RegistrationConfirmationsDao {
  void addRegistrationConfirmationByUsername(String username);

  String getUniqueUrlParamByUsername(String username);
}
