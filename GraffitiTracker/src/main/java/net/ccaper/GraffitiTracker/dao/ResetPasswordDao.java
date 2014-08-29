package net.ccaper.GraffitiTracker.dao;

import org.springframework.dao.EmptyResultDataAccessException;

import net.ccaper.GraffitiTracker.objects.UserSecurityQuestion;

public interface ResetPasswordDao {
  void addResetPassword(String username);

  String getUniqueUrlParamByUsername(String username);

  UserSecurityQuestion getUserSecurityQuestionByUniqueUrlParam(
      String uniqueUrlParam) throws EmptyResultDataAccessException;

  void deleteResetPasswordByUniqueUrlParam(String uniqueUrlParam);

  void deleteResetPasswordWhereTimestampExpired();
}
