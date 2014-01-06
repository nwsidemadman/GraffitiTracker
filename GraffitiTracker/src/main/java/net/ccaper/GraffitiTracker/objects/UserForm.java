package net.ccaper.GraffitiTracker.objects;

import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

public class UserForm {
  private String username;
  private String email;
  private String password;
  private String confirmPassword;
  private boolean acceptTerms;
  private String captchaAnswer;
  private String textCaptchaQuestion;

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

  // visible for testing
  String encodePassword() {
    if (username == null) {
      throw new IllegalStateException("Username needed for salting is null.");
    }
    if (password == null) {
      throw new IllegalStateException("Password is null.");
    }
    ShaPasswordEncoder passwordEncoder = new ShaPasswordEncoder(256);
    return passwordEncoder.encodePassword(password, username);
  }

  public boolean getAcceptTerms() {
    return acceptTerms;
  }

  public void setAcceptTerms(boolean acceptTerms) {
    this.acceptTerms = acceptTerms;
  }

  public String getCaptchaAnswer() {
    return captchaAnswer;
  }

  public void setCaptchaAnswer(String captchaAnswer) {
    this.captchaAnswer = captchaAnswer;
  }

  public String getTextCaptchaQuestion() {
    return textCaptchaQuestion;
  }

  public void setTextCaptchaQuestion(String textCaptchaQuestion) {
    this.textCaptchaQuestion = textCaptchaQuestion;
  }
  
  public AppUser createAppUserFromUserForm() {
    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    appUser.setEmail(email);
    appUser.setPassword(encodePassword());
    return appUser;
  }
}
