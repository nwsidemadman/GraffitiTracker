package net.ccaper.graffitiTracker.utils;

import org.apache.commons.lang3.StringUtils;

// TODO(ccaper): confirm if needed
public class InetAddressUtils {
  // TODO(ccaper): javadoc
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
}
