package net.ccaper.GraffitiTracker.enums;

/**
 * 
 * @author ccaper Enum for Roles
 * 
 */
public enum RoleEnum {
  SUPERADMIN("ROLE_SUPERADMIN", "Super Admin"), ADMIN("ROLE_ADMIN", "Admin"), BASIC(
      "ROLE_BASIC", "Basic"), SUBSCRIPTION("ROLE_SUBSCRIPTION", "Subscription"), LICENSED(
      "ROLE_LICENSED", "Licensed"), TRIAL("ROLE_TRIAL", "Trial");

  private String dbString;
  private String displayString;

  /**
   * Instantiates a new role enum.
   * 
   * @param dbString
   *          the db string
   * @param displayString
   *          the display string
   */
  private RoleEnum(String dbString, String displayString) {
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

  /**
   * Gets the role enum from db string.
   * 
   * @param roleDbString
   *          the role db string
   * @return the role enum from db string
   */
  public static RoleEnum getRoleEnumFromDbString(String roleDbString) {
    for (RoleEnum roleEnum : RoleEnum.values()) {
      if (roleEnum.getDbString().equals(roleDbString)) {
        return roleEnum;
      }
    }
    throw new IllegalArgumentException(String.format(
        "The db string %s does not map to a valid RoleEnum.", roleDbString));
  }
}
