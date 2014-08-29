package net.ccaper.graffitiTracker.objects;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ccaper.graffitiTracker.enums.RoleEnum;

/**
 * 
 * @author ccaper
 * 
 *         POJO for the AppUser
 * 
 */
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

  /**
   * Gets the user id.
   * 
   * @return the user id
   */
  public int getUserId() {
    return userId;
  }

  /**
   * Sets the user id.
   * 
   * @param userId
   *          the new user id
   */
  public void setUserId(int userId) {
    this.userId = userId;
  }

  /**
   * Gets the username.
   * 
   * @return the username
   */
  public String getUsername() {
    return username;
  }

  /**
   * Sets the username.
   * 
   * @param username
   *          the new username
   */
  public void setUsername(String username) {
    this.username = username;
  }

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
   * Sets the checks if is active.
   * 
   * @param isActive
   *          the new checks if is active
   */
  public void setIsActive(boolean isActive) {
    this.isActive = isActive;
  }

  /**
   * Gets the register timestamp.
   * 
   * @return the register timestamp
   */
  public Timestamp getRegisterTimestamp() {
    return registerTimestamp;
  }

  /**
   * Sets the register timestamp.
   * 
   * @param registerTimestamp
   *          the new register timestamp
   */
  public void setRegisterTimestamp(Timestamp registerTimestamp) {
    this.registerTimestamp = registerTimestamp;
  }

  /**
   * Gets the password.
   * 
   * @return the password
   */
  public String getPassword() {
    return password;
  }

  /**
   * Sets the password.
   * 
   * @param password
   *          the new password
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Gets the current login timestamp.
   * 
   * @return the current login timestamp
   */
  public Timestamp getCurrentLoginTimestamp() {
    return currentLoginTimestamp;
  }

  /**
   * Sets the current login timestamp.
   * 
   * @param currentLoginTimestamp
   *          the new current login timestamp
   */
  public void setCurrentLoginTimestamp(Timestamp currentLoginTimestamp) {
    this.currentLoginTimestamp = currentLoginTimestamp;
  }

  /**
   * Gets the previous login timestamp.
   * 
   * @return the previous login timestamp
   */
  public Timestamp getPreviousLoginTimestamp() {
    return previousLoginTimestamp;
  }

  /**
   * Sets the previous login timestamp.
   * 
   * @param previousLoginTimestamp
   *          the new previous login timestamp
   */
  public void setPreviousLoginTimestamp(Timestamp previousLoginTimestamp) {
    this.previousLoginTimestamp = previousLoginTimestamp;
  }

  /**
   * Gets the login count.
   * 
   * @return the login count
   */
  public int getLoginCount() {
    return loginCount;
  }

  /**
   * Sets the login count.
   * 
   * @param loginCount
   *          the new login count
   */
  public void setLoginCount(int loginCount) {
    this.loginCount = loginCount;
  }

  /**
   * Gets the {@link net.ccaper.graffitiTracker.objects.Role}.
   * 
   * @return the {@link net.ccaper.graffitiTracker.objects.Role}s
   */
  public List<Role> getRoles() {
    return roles;
  }

  /**
   * Sets the {@link net.ccaper.graffitiTracker.objects.Role}s.
   * 
   * @param roles
   *          the new {@link net.ccaper.graffitiTracker.objects.Role}s
   */
  public void setRoles(List<Role> roles) {
    this.roles = roles;
  }

  /**
   * Takes the List of roles and converts it to a map of timestamps to role.
   * 
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

  /**
   * Gets the security question.
   * 
   * @return the security question
   */
  public String getSecurityQuestion() {
    return securityQuestion;
  }

  /**
   * Sets the security question.
   * 
   * @param securityQuestion
   *          the new security question
   */
  public void setSecurityQuestion(String securityQuestion) {
    this.securityQuestion = securityQuestion;
  }

  /**
   * Gets the security answer.
   * 
   * @return the security answer
   */
  public String getSecurityAnswer() {
    return securityAnswer;
  }

  /**
   * Sets the security answer.
   * 
   * @param securityAnswer
   *          the new security answer
   */
  public void setSecurityAnswer(String securityAnswer) {
    this.securityAnswer = securityAnswer;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + userId;
    result = prime * result + ((username == null) ? 0 : username.hashCode());
    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
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
