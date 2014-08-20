package net.ccaper.GraffitiTracker.objects;

import java.util.List;

import net.ccaper.GraffitiTracker.enums.RoleEnum;

// TODO(ccaper): this will likely go away when roles changed in AppUser
public class ManagerEditAppUser {
  private String email;
  private boolean isActive;
  private List<RoleEnum> roles;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public boolean isActive() {
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
}
