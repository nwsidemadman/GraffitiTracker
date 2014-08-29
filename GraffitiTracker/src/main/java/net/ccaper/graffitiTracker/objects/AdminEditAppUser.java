package net.ccaper.graffitiTracker.objects;

import java.util.List;

import net.ccaper.graffitiTracker.enums.RoleEnum;

public class AdminEditAppUser {
  private String email;
  private boolean isActive;
  private List<RoleEnum> roles;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public boolean getIsActive() {
    return isActive;
  }

  public void setActive(boolean isActive) {
    this.isActive = isActive;
  }

  public List<RoleEnum> getRoles() {
    return roles;
  }

  public void setRoles(List<RoleEnum> roles) {
    this.roles = roles;
  }

  @Override
  public String toString() {
    return "AdminEditAppUser [email=" + email + ", isActive=" + isActive
        + ", roles=" + roles + "]";
  }
}
