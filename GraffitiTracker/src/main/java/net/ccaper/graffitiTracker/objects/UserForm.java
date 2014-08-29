package net.ccaper.graffitiTracker.objects;

import net.ccaper.graffitiTracker.utils.EncodePassword;

/**
 * 
 * @author ccaper
 * 
 *         POJO for UserForm values
 * 
 */
public class UserForm {
  private String username;
  private String email;
  private String password;
  private String confirmPassword;
  private boolean acceptTerms;
  private String captchaAnswer;
  private String textCaptchaQuestion;
  private String securityQuestion;
  private String securityAnswer;

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
   * Gets the confirm password.
   * 
   * @return the confirm password
   */
  public String getConfirmPassword() {
    return confirmPassword;
  }

  /**
   * Sets the confirm password.
   * 
   * @param confirmPassword
   *          the new confirm password
   */
  public void setConfirmPassword(String confirmPassword) {
    this.confirmPassword = confirmPassword;
  }

  /**
   * Gets the accept terms.
   * 
   * @return the accept terms
   */
  public boolean getAcceptTerms() {
    return acceptTerms;
  }

  /**
   * Sets the accept terms.
   * 
   * @param acceptTerms
   *          the new accept terms
   */
  public void setAcceptTerms(boolean acceptTerms) {
    this.acceptTerms = acceptTerms;
  }

  /**
   * Gets the captcha answer.
   * 
   * @return the captcha answer
   */
  public String getCaptchaAnswer() {
    return captchaAnswer;
  }

  /**
   * Sets the captcha answer.
   * 
   * @param captchaAnswer
   *          the new captcha answer
   */
  public void setCaptchaAnswer(String captchaAnswer) {
    this.captchaAnswer = captchaAnswer;
  }

  /**
   * Gets the text captcha question.
   * 
   * @return the text captcha question
   */
  public String getTextCaptchaQuestion() {
    return textCaptchaQuestion;
  }

  /**
   * Sets the text captcha question.
   * 
   * @param textCaptchaQuestion
   *          the new text captcha question
   */
  public void setTextCaptchaQuestion(String textCaptchaQuestion) {
    this.textCaptchaQuestion = textCaptchaQuestion;
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

  /**
   * Creates the app user from user form.
   * 
   * @return the app user
   */
  public AppUser createAppUserFromUserForm() {
    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    appUser.setEmail(email);
    appUser.setPassword(EncodePassword.encodePassword(username, password));
    appUser.setSecurityQuestion(securityQuestion);
    appUser.setSecurityAnswer(securityAnswer);
    return appUser;
  }
}
