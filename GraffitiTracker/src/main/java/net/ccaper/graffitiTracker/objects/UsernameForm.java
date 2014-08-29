package net.ccaper.graffitiTracker.objects;

public class UsernameForm {
  private String username;
  private boolean recoverPassword;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }
  
  public boolean isRecoverPassword() {
    return recoverPassword;
  }

  public void setRecoverPassword(boolean recoverPassword) {
    this.recoverPassword = recoverPassword;
  }
}
