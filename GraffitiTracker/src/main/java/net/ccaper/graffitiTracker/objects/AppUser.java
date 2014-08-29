package net.ccaper.graffitiTracker.objects;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ccaper.graffitiTracker.enums.RoleEnum;

public class AppUser {
  private int userId;
  private String username;
  private String email;
  private boolean isActive;
  private Timestamp registerTimestamp;
  private String password;
  private Timestamp currentLoginTimestamp;
  private Timestamp previousLoginTimestamp;
  private List<Role> roles;
  private int loginCount;
  private String securityQuestion;
  private String securityAnswer;

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

  public List<Role> getRoles() {
    return roles;
  }

  public void setRoles(List<Role> roles) {
    this.roles = roles;
  }

  /**
   * Takes the List of roles and converts it to a map of timestamps to role
   * @return map of timestamps to role
   */
  public Map<RoleEnum, Timestamp> getRolesAsTimestampToRoleEnumMap() {
    if (roles == null) {
      return null;
    }
    Map<RoleEnum, Timestamp> rolesMap = new HashMap<RoleEnum, Timestamp>(
        roles.size());
    for (Role role : roles) {
      rolesMap.put(role.getRole(), role.getGrantedTimestamp());
    }
    return rolesMap;
  }

  public String getSecurityQuestion() {
    return securityQuestion;
  }

  public void setSecurityQuestion(String securityQuestion) {
    this.securityQuestion = securityQuestion;
  }

  public String getSecurityAnswer() {
    return securityAnswer;
  }

  public void setSecurityAnswer(String securityAnswer) {
    this.securityAnswer = securityAnswer;
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
