package net.ccaper.GraffitiTracker.enums;

public enum RoleEnum {
  SUPERADMIN("ROLE_SUPERADMIN", "Super Admin"), ADMIN("ROLE_ADMIN", "Admin"), BASIC(
      "ROLE_BASIC", "Basic"), SUBSCRIPTION("ROLE_SUBSCRIPTION", "Subscription"), LICENSED(
      "ROLE_LICENSED", "Licensed"), TRIAL("ROLE_TRIAL", "Trial");

  private String dbString;
  private String displayString;

  private RoleEnum(String dbString, String displayString) {
    this.dbString = dbString;
    this.displayString = displayString;
  }

  public String getDbString() {
    return dbString;
  }

  public String getDisplayString() {
    return displayString;
  }

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
