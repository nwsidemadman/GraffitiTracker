package net.ccaper.GraffitiTracker.objects;

import java.sql.Timestamp;
import java.util.Set;

import org.apache.commons.validator.routines.EmailValidator;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Length.List;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

public class User {
  static private final int MIN_USERNAME_LENGTH = 6;
  static private final int MAX_USERNAME_LENGTH = 20;
  static private final int MIN_PASSWORD_LENGTH = 6;
  static private final int MAX_PASSWORD_LENGTH = 64;
  static private EmailValidator emailValidator = EmailValidator
      .getInstance(false);
  private int userId;
  @NotBlank(message = "Username must be supplied.")
  @List({
    @Length(min = 6, message = "Username must be at least 6 characters long."),
    @Length(max = 20, message = "Username must less than 21 characters long.")
  })
  private String username;
  @NotBlank(message = "Email must be supplied.")
  @Email(message = "Email must be valid.")
  private String email;
  private boolean isActive;
  private Timestamp registerDate;
  @NotBlank(message = "Password must be supplied.")
  @List({
    @Length(min = 6, message = "Password must be at least 6 characters long."),
    @Length(max = 64, message = "Password must less than 65 characters long.")
  })
  private String password;
  private String passwordEncoded;
  private Timestamp lastLogin;
  private Set<Role> roles;
  
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
    if (username.length() < MIN_USERNAME_LENGTH
        || username.length() > MAX_USERNAME_LENGTH) {
      throw new IllegalArgumentException(
          String
              .format(
                  "Invalid username. The username %s must be longer than %s characters and less than %s characters",
                  username, MIN_USERNAME_LENGTH, MAX_USERNAME_LENGTH));
    }
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    if (!emailValidator.isValid(email)) {
      throw new IllegalArgumentException(String.format(
          "The email address %s is not valid.", email));
    }
    this.email = email;
  }

  public boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(boolean isActive) {
    this.isActive = isActive;
  }

  public Timestamp getRegisterDate() {
    return registerDate;
  }

  public void setRegisterDate(Timestamp registerDate) {
    this.registerDate = registerDate;
  }

  public String getPassword() {
    return passwordEncoded;
  }

  public void setPassword(String password) {
    if (password.length() < MIN_PASSWORD_LENGTH
        || password.length() > MAX_PASSWORD_LENGTH) {
      throw new IllegalArgumentException(
          String
              .format(
                  "Invalid password. The password must be longer than %s characters and less than %s characters",
                   MIN_PASSWORD_LENGTH, MAX_PASSWORD_LENGTH));
    }
    this.password = password;
  }
  
  public void setPasswordEncoded(String passwordEncoded) {
    this.passwordEncoded = passwordEncoded;
  }
  
  public void encodePassword() {
    if (username == null) {
      throw new IllegalStateException("Username needed for salting is null."); 
    }
    if (password == null) {
      throw new IllegalStateException("Password is null."); 
    }
    ShaPasswordEncoder passwordEncoder = new ShaPasswordEncoder(256);
    passwordEncoded = passwordEncoder.encodePassword(password, username);
    password = null;
  }

  public Timestamp getLastLogin() {
    return lastLogin;
  }

  public void setLastLogin(Timestamp lastLogin) {
    this.lastLogin = lastLogin;
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
    result = prime * result + ((email == null) ? 0 : email.hashCode());
    result = prime * result + (isActive ? 1231 : 1237);
    result = prime * result + ((lastLogin == null) ? 0 : lastLogin.hashCode());
    result = prime * result + ((passwordEncoded == null) ? 0 : passwordEncoded.hashCode());
    result = prime * result
        + ((registerDate == null) ? 0 : registerDate.hashCode());
    result = prime * result + ((roles == null) ? 0 : roles.hashCode());
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
    User other = (User) obj;
    if (email == null) {
      if (other.email != null)
        return false;
    } else if (!email.equals(other.email))
      return false;
    if (isActive != other.isActive)
      return false;
    if (lastLogin == null) {
      if (other.lastLogin != null)
        return false;
    } else if (!lastLogin.equals(other.lastLogin))
      return false;
    if (passwordEncoded == null) {
      if (other.passwordEncoded != null)
        return false;
    } else if (!passwordEncoded.equals(other.passwordEncoded))
      return false;
    if (registerDate == null) {
      if (other.registerDate != null)
        return false;
    } else if (!registerDate.equals(other.registerDate))
      return false;
    if (roles == null) {
      if (other.roles != null)
        return false;
    } else if (!roles.equals(other.roles))
      return false;
    if (userId != other.userId)
      return false;
    if (username == null) {
      if (other.username != null)
        return false;
    } else if (!username.equals(other.username))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "User [userId=" + userId + ", username=" + username + ", email="
        + email + ", isActive=" + isActive + ", registerDate=" + registerDate
        + ", lastLogin=" + lastLogin + ", role=" + roles + "]";
  }
}
