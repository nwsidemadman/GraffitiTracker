package net.ccaper.graffitiTracker.objects;

/**
 * 
 * @author ccaper
 * 
 *         POJO for PasswordSecurityForm values
 * 
 */
public class PasswordSecurityForm {
  private int userid;
  private String securityQuestion;
  private String securityAnswer;
  private String password;
  private String confirmPassword;
  private boolean resetPassword;

  /**
   * Gets the userid.
   * 
   * @return the userid
   */
  public int getUserid() {
    return userid;
  }

  /**
   * Sets the userid.
   * 
   * @param userid
   *          the new userid
   */
  public void setUserid(int userid) {
    this.userid = userid;
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
   * Checks if is reset password.
   * 
   * @return true, if is reset password
   */
  public boolean isResetPassword() {
    return resetPassword;
  }

  /**
   * Sets the reset password.
   * 
   * @param resetPassword
   *          the new reset password
   */
  public void setResetPassword(boolean resetPassword) {
    this.resetPassword = resetPassword;
  }
}
