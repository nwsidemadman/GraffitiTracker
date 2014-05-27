package net.ccaper.GraffitiTracker.objects;

import net.ccaper.GraffitiTracker.utils.Encoder;

public class ManageAccountForm {
  private String email;
  private String password;
  private String confirmPassword;
  private String securityQuestion;
  private String securityAnswer;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getConfirmPassword() {
    return confirmPassword;
  }

  public void setConfirmPassword(String confirmPassword) {
    this.confirmPassword = confirmPassword;
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

  public AppUser createAppUserFromUserForm(String username) {
    AppUser appUser = new AppUser();
    appUser.setEmail(email);
    appUser.setPassword(encodePassword(username));
    appUser.setSecurityQuestion(securityQuestion);
    appUser.setSecurityAnswer(securityAnswer);
    return appUser;
  }
  
  // visible for testing
  // TODO(ccaper): unit test
  String encodePassword(String username) {
    if (username == null) {
      throw new IllegalStateException("Username needed for salting is null.");
    }
    if (password == null) {
      throw new IllegalStateException("Password is null.");
    }
    return Encoder.encodeString(username, password);
  }
}
