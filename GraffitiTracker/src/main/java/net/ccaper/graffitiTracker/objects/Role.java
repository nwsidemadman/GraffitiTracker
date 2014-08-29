package net.ccaper.graffitiTracker.objects;

import java.sql.Timestamp;

import net.ccaper.graffitiTracker.enums.RoleEnum;

/**
 * 
 * @author ccaper
 * 
 *         POJO for {@link net.ccaper.graffitiTracker.objects.AppUser} roles
 * 
 */
public class Role {
  private RoleEnum role;
  private Timestamp grantedTimestamp;

  /**
   * Gets the role.
   * 
   * @return the {@link net.ccaper.graffitiTracker.enums.RoleEnum}
   */
  public RoleEnum getRole() {
    return role;
  }

  /**
   * Sets the role.
   * 
   * @param role
   *          the new {@link net.ccaper.graffitiTracker.enums.RoleEnum}
   */
  public void setRole(RoleEnum role) {
    this.role = role;
  }

  /**
   * Gets the granted timestamp.
   * 
   * @return the granted timestamp
   */
  public Timestamp getGrantedTimestamp() {
    return grantedTimestamp;
  }

  /**
   * Sets the granted timestamp.
   * 
   * @param grantedTimestamp
   *          the new granted timestamp
   */
  public void setGrantedTimestamp(Timestamp grantedTimestamp) {
    this.grantedTimestamp = grantedTimestamp;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "Role [role=" + role + ", grantedTimestamp=" + grantedTimestamp
        + "]";
  }
}
