package net.ccaper.graffitiTracker.objects;

import java.util.List;

import net.ccaper.graffitiTracker.enums.RoleEnum;

/**
 * 
 * @author ccaper
 * 
 *         POJO for AdminEditAppUser
 * 
 */
public class AdminEditAppUser {
  private String email;
  private boolean isActive;
  private List<RoleEnum> roles;

  /**
   * Gets the email.
   * 
   * @return the email
   */
  public String getEmail() {
    return email;
  }

  /**
   * Sets the email.
   * 
   * @param email
   *          the new email
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Gets the checks if is active.
   * 
   * @return the checks if is active
   */
  public boolean getIsActive() {
    return isActive;
  }

  /**
   * Sets the active.
   * 
   * @param isActive
   *          the new active
   */
  public void setActive(boolean isActive) {
    this.isActive = isActive;
  }

  /**
   * Gets the {@link net.ccaper.graffitiTracker.enums.RoleEnum}s.
   * 
   * @return the {@link net.ccaper.graffitiTracker.enums.RoleEnum}s
   */
  public List<RoleEnum> getRoles() {
    return roles;
  }

  /**
   * Sets the {@link net.ccaper.graffitiTracker.enums.RoleEnum}s.
   * 
   * @param roles
   *          the new {@link net.ccaper.graffitiTracker.enums.RoleEnum}s
   */
  public void setRoles(List<RoleEnum> roles) {
    this.roles = roles;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "AdminEditAppUser [email=" + email + ", isActive=" + isActive
        + ", roles=" + roles + "]";
  }
}
