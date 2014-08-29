package net.ccaper.graffitiTracker.utils;

public class EncodePassword {
  public static String encodePassword(String username, String password) {
    if (username == null) {
      throw new IllegalStateException("Username needed for salting is null.");
    }
    if (password == null) {
      throw new IllegalStateException("Password is null.");
    }
    return Encoder.encodeString(username, password);
  }
}
