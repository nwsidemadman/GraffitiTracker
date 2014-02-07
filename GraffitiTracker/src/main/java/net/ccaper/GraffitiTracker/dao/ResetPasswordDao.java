package net.ccaper.GraffitiTracker.dao;

import net.ccaper.GraffitiTracker.objects.UserSecurityQuestion;

public interface ResetPasswordDao {
  void addResetPassword(String username);

  String getUniqueUrlParamByUsername(String username);

  UserSecurityQuestion getUserSecurityQuestionByUniqueUrlParam(String uniqueUrlParam);

  void deleteResetPasswordByUniqueUrlParam(String uniqueUrlParam);
}
