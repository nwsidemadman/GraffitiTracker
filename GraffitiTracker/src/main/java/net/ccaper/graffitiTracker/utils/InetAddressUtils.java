package net.ccaper.graffitiTracker.utils;

import org.apache.commons.lang3.StringUtils;

// TODO(ccaper): confirm if needed
public class InetAddressUtils {
  // TODO(ccaper): javadoc
  /**
   * String to number.
   *
   * @param ip the ip
   * @return the double
   */
  public static double stringToNumber(String ip) {
    if (ip == null) {
      throw new IllegalArgumentException(String.format(
          "The IPv4 address %s is not valid.", ip));
    }
    String[] addresses = StringUtils.split(ip, ".");
    if (addresses.length != 4) {
      throw new IllegalArgumentException(String.format(
          "The IPv4 address %s is not valid.", ip));
    }
    double power = 3d;
    double result = 0;
    for (String subnetAddress : addresses) {
      int subnet;
      try {
        subnet = Integer.parseInt(subnetAddress);
      } catch (NumberFormatException e) {
        throw new IllegalArgumentException(String.format(
            "The IPv4 address %s is not valid.", ip));
      }
      if (subnet < 0 || subnet > 256) {
        throw new IllegalArgumentException(String.format(
            "The IPv4 address %s is not valid.", ip));
      }
      result += subnet * Math.pow(256d, power--);
    }
    return result;
  }
  
  
  /**
   * Checks if is inet valid.
   *
   * @param inet the inet
   * @return true, if is inet is valid
   */
  public static boolean isInetValid(String inet) {
    if (StringUtils.isEmpty(inet)) {
      return false;
    }
    String[] subnets = StringUtils.split(inet, ".");
    if (subnets.length != 4) {
      return false;
    }
    for (String subnet : subnets) {
      try {
        int number = Integer.parseInt(subnet);
        if (number < 0 || number > 255) {
          return false;
        }
      } catch (NumberFormatException e) {
        return false;
      }
    }
    return true;
  }
}
