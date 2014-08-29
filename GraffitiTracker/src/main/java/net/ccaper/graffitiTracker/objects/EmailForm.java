package net.ccaper.graffitiTracker.objects;

/**
 * 
 * @author ccaper
 * 
 *         POJO for EmailForm values
 * 
 */
public class EmailForm {
  private String email;
  private boolean recoverUsername;

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
   * Checks if is recover username.
   * 
   * @return true, if is recover username
   */
  public boolean isRecoverUsername() {
    return recoverUsername;
  }

  /**
   * Sets the recover username.
   * 
   * @param recoverUsername
   *          the new recover username
   */
  public void setRecoverUsername(boolean recoverUsername) {
    this.recoverUsername = recoverUsername;
  }
}
