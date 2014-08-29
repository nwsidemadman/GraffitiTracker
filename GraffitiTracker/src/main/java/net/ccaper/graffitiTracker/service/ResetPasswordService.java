package net.ccaper.graffitiTracker.service;

public interface ResetPasswordService {
  void deleteResetPasswordWhenTimestampExpired();
}
