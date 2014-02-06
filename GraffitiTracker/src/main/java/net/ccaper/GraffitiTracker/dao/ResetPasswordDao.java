package net.ccaper.GraffitiTracker.dao;

public interface ResetPasswordDao {
  void addResetPassword(String username);

  String getUniqueUrlParamByUsername(String username);

  String getSecurityQuestionByUniqueUrlParam(String uniqueUrlParam);
}
