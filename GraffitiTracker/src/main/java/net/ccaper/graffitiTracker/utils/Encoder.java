package net.ccaper.graffitiTracker.utils;

import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

public class Encoder {
  private static final ShaPasswordEncoder passwordEncoder = new ShaPasswordEncoder(256);

  
  public static String encodeString(String salt, String stringToEncode) {
    return passwordEncoder.encodePassword(stringToEncode, salt);
  }
}
