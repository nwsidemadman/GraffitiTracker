package net.ccaper.GraffitiTracker.objects;

public class PasswordSecurityForm {
  private int userId;
  private String securityQuestion;
  private String securityAnswer;
  private String password;
  private String confirmPassword;
  private boolean resetPassword;

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
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

  public boolean isResetPassword() {
    return resetPassword;
  }

  public void setResetPassword(boolean resetPassword) {
    this.resetPassword = resetPassword;
  }
}
