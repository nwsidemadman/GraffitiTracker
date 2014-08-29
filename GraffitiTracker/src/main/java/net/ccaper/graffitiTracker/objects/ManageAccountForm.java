package net.ccaper.graffitiTracker.objects;

import net.ccaper.graffitiTracker.utils.EncodePassword;

/**
 * 
 * @author ccaper
 * 
 *         POJO for ManagaAccountForm values
 * 
 */
public class ManageAccountForm {
  private String email;
  private String password;
  private String confirmPassword;
  private String securityQuestion;
  private String securityAnswer;

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
   * @param username
   *          the username
   * @return the app user
   */
  public AppUser createAppUserFromUserForm(String username) {
    AppUser appUser = new AppUser();
    appUser.setEmail(email);
    appUser.setPassword(EncodePassword.encodePassword(username, password));
    appUser.setSecurityQuestion(securityQuestion);
    appUser.setSecurityAnswer(securityAnswer);
    return appUser;
  }
}
