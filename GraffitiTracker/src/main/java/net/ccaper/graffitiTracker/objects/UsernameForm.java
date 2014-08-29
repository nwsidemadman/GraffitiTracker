package net.ccaper.graffitiTracker.objects;

/**
 * 
 * @author ccaper
 * 
 *         POJO for UsernameForm values
 * 
 */
public class UsernameForm {
  private String username;
  private boolean recoverPassword;

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
   * Checks if is recover password.
   * 
   * @return true, if is recover password
   */
  public boolean isRecoverPassword() {
    return recoverPassword;
  }

  /**
   * Sets the recover password.
   * 
   * @param recoverPassword
   *          the new recover password
   */
  public void setRecoverPassword(boolean recoverPassword) {
    this.recoverPassword = recoverPassword;
  }
}
