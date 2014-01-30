package net.ccaper.GraffitiTracker.objects;

public class EmailForm {
  private String email;
  private boolean recoverUsername;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public boolean isRecoverUsername() {
    return recoverUsername;
  }

  public void setRecoverUsername(boolean recoverUsername) {
    this.recoverUsername = recoverUsername;
  }
}
