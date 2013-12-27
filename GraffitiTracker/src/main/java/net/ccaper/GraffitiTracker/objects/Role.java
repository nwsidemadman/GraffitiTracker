package net.ccaper.GraffitiTracker.objects;

import java.sql.Timestamp;

import net.ccaper.GraffitiTracker.enums.RoleEnum;

public class Role {
  private RoleEnum role;
  private Timestamp grantedTimestamp;

  public RoleEnum getRole() {
    return role;
  }

  public void setRole(RoleEnum role) {
    this.role = role;
  }

  public Timestamp getGrantedTimestamp() {
    return grantedTimestamp;
  }

  public void setGrantedTimestamp(Timestamp grantedTimestamp) {
    this.grantedTimestamp = grantedTimestamp;
  }

  @Override
  public String toString() {
    return "Role [role=" + role + ", grantedTimestamp=" + grantedTimestamp
        + "]";
  }
}
