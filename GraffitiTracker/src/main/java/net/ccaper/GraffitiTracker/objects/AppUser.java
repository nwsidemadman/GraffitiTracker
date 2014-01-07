package net.ccaper.GraffitiTracker.objects;

import java.sql.Timestamp;
import java.util.Set;

public class AppUser {
  private int userId;
  private String username;
  private String email;
  private boolean isActive;
  private Timestamp registerTimestamp;
  private String password;
  private Timestamp currentLoginTimestamp;
  private Timestamp previousLoginTimestamp;
  private Set<Role> roles;
  private int loginCount;

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(boolean isActive) {
    this.isActive = isActive;
  }

  public Timestamp getRegisterTimestamp() {
    return registerTimestamp;
  }

  public void setRegisterTimestamp(Timestamp registerTimestamp) {
    this.registerTimestamp = registerTimestamp;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Timestamp getCurrentLoginTimestamp() {
    return currentLoginTimestamp;
  }

  public void setCurrentLoginTimestamp(Timestamp currentLoginTimestamp) {
    this.currentLoginTimestamp = currentLoginTimestamp;
  }
  
  public Timestamp getPreviousLoginTimestamp() {
    return previousLoginTimestamp;
  }

  public void setPreviousLoginTimestamp(Timestamp previousLoginTimestamp) {
    this.previousLoginTimestamp = previousLoginTimestamp;
  }
  
  public int getLoginCount() {
    return loginCount;
  }
  
  public void setLoginCount(int loginCount) {
    this.loginCount = loginCount;
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + userId;
    result = prime * result + ((username == null) ? 0 : username.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    AppUser other = (AppUser) obj;
    if (userId != other.userId)
      return false;
    if (username == null) {
      if (other.username != null)
        return false;
    } else if (!username.equals(other.username))
      return false;
    return true;
  }
}