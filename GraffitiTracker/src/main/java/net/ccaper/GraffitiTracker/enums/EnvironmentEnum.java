package net.ccaper.GraffitiTracker.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author ccaper The Enum for development environments
 * 
 */
public enum EnvironmentEnum {
  LOCAL("", "Local"), DEV("dev", "Development"), STAGE("staging", "Staging"), PROD(
      "prod", "");

  private String environmentPropertyString;
  private String displayString;

  /**
   * Instantiates a new environment enum.
   * 
   * @param environmentPropertyString
   *          the environment property string
   * @param displayString
   *          the display string
   */
  private EnvironmentEnum(String environmentPropertyString, String displayString) {
    this.environmentPropertyString = environmentPropertyString;
    this.displayString = displayString;
  }

  /**
   * Gets the environment property string.
   * 
   * @return the environment property string
   */
  public String getEnvironmentPropertyString() {
    return environmentPropertyString;
  }

  /**
   * Gets the display string.
   * 
   * @return the display string
   */
  public String getDisplayString() {
    return displayString;
  }

  /**
   * Gets the environment enum from environment property string.
   * 
   * @param environmentPropertyString
   *          the environment property string
   * @return the environment enum from environment property string
   */
  public static EnvironmentEnum getEnvironmentEnumFromEnvironmentPropertyString(
      String environmentPropertyString) {
    if (StringUtils.isEmpty(environmentPropertyString)) {
      return EnvironmentEnum.LOCAL;
    }
    for (EnvironmentEnum enrironmentEnum : EnvironmentEnum.values()) {
      if (enrironmentEnum.getEnvironmentPropertyString().equals(
          environmentPropertyString)) {
        return enrironmentEnum;
      }
    }
    throw new IllegalArgumentException(
        String
            .format(
                "The environment property string '%s' does not map to a valid EnvironmentEnum.",
                environmentPropertyString));
  }
}
