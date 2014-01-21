package net.ccaper.GraffitiTracker.enums;

import org.apache.commons.lang3.StringUtils;

public enum EnvironmentEnum {
  LOCAL("", "Local"), DEV("DEV", "Development"), STAGE("STAGE", "Stage"), PROD(
      "PROD", "");

  private String environmentPropertyString;
  private String displayString;

  private EnvironmentEnum(String environmentPropertyString, String displayString) {
    this.environmentPropertyString = environmentPropertyString;
    this.displayString = displayString;
  }

  public String getEnvironmentPropertyString() {
    return environmentPropertyString;
  }

  public String getDisplayString() {
    return displayString;
  }

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
            "The environment property string  %s does not map to a valid EnvironmentEnum.",
            environmentPropertyString));
  }
}
