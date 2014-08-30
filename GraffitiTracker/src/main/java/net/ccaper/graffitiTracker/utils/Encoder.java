package net.ccaper.graffitiTracker.utils;

import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

/**
 * 
 * @author ccaper
 * 
 *         Password hash encoder using SHA 256 and salt
 * 
 */
public class Encoder {
  private static final ShaPasswordEncoder encoder = new ShaPasswordEncoder(
      256);

  /**
   * Encode string.
   * 
   * @param salt
   *          the salt
   * @param stringToEncode
   *          the string to encode
   * @return the string encoded
   */
  public static String encodeString(String salt, String stringToEncode) {
    return encoder.encodePassword(stringToEncode, salt);
  }
}
