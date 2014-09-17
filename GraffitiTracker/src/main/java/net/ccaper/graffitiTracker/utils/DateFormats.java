package net.ccaper.graffitiTracker.utils;

import java.text.SimpleDateFormat;

/**
 * 
 * @author ccaper
 * 
 *         Holder for date formats
 * 
 */
public class DateFormats {
  // Example: 2014
  public static final SimpleDateFormat YEAR_FORMAT = new SimpleDateFormat(
      "yyyy");
  // Example: 2014/05/03
  public static final SimpleDateFormat YEAR_SLASH_MONTH_SLASH_DAY_FORMAT = new SimpleDateFormat(
      "yyyy/MM/dd");
  // Example 2010-01-01T00:00:00Z
  public static final SimpleDateFormat W3_DATE_FORMAT = new SimpleDateFormat(
      "yyyy-MM-dd'T'HH:mm:ssZ");
}
