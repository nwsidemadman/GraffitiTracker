package net.ccaper.graffitiTracker.utils;

/**
 * 
 * @author ccaper
 * 
 *         Encodes the user's password
 * 
 */
public class EncodePassword {

  /**
   * Encode password.
   * 
   * @param username
   *          the username used as salt (username can never change)
   * @param password
   *          the password
   * @return the password encoded
   */
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
