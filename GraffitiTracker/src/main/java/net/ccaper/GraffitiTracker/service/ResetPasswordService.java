package net.ccaper.GraffitiTracker.service;

public interface ResetPasswordService {
  void deleteResetPasswordWhenTimestampExpired();
}
