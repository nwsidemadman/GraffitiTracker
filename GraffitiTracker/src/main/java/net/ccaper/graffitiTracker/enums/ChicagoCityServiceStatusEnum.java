package net.ccaper.graffitiTracker.enums;

/**
 * 
 * @author ccaper
 * 
 * Enum for the Chicago City Service Status.
 *
 */
public enum ChicagoCityServiceStatusEnum {
  OPEN("open", "Open"), CLOSED("closed", "Closed");

  private String dbString;
  private String displayString;

  /**
   * Instantiates a new chicago city service status enum.
   *
   * @param dbString the db string
   * @param displayString the display string
   */
  private ChicagoCityServiceStatusEnum(String dbString, String displayString) {
    this.dbString = dbString;
    this.displayString = displayString;
  }

  /**
   * Gets the db string.
   *
   * @return the db string
   */
  public String getDbString() {
    return dbString;
  }

  /**
   * Gets the display string.
   *
   * @return the display string
   */
  public String getDisplayString() {
    return displayString;
  }

  // TODO(ccaper): unit test
  /**
   * Gets the chicago city service status enum from db string.
   *
   * @param dbString the db string
   * @return the chicago city service status enum from db string
   */
  public static ChicagoCityServiceStatusEnum getChicagoCityServiceStatusEnumFromDbOrServerString(
      String dbString) {
    for (ChicagoCityServiceStatusEnum chicagoCityServiceStatusEnum : ChicagoCityServiceStatusEnum
        .values()) {
      if (chicagoCityServiceStatusEnum.getDbString().equals(dbString)) {
        return chicagoCityServiceStatusEnum;
      }
    }
    throw new IllegalArgumentException(
        String
            .format(
                "The db string '%s' does not map to a valid ChicagoCityServiceStatusEnum.",
                dbString));
  }
}
