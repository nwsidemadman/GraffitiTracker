package net.ccaper.graffitiTracker.objects;

/**
 * 
 * @author ccaper
 * 
 *         POJO holder for an unedited (original) banned inet and an edited
 *         banned inet.
 * 
 */
public class OriginalEditedBannedInet {
  private BannedInet originalBannedInet;
  private BannedInet editedBannedInet;

  /**
   * Gets the original banned inet.
   * 
   * @return the original banned inet
   */
  public BannedInet getOriginalBannedInet() {
    return originalBannedInet;
  }

  /**
   * Sets the original banned inet.
   * 
   * @param originalBannedInet
   *          the new original banned inet
   */
  public void setOriginalBannedInet(BannedInet originalBannedInet) {
    this.originalBannedInet = originalBannedInet;
  }

  /**
   * Gets the edited banned inet.
   * 
   * @return the edited banned inet
   */
  public BannedInet getEditedBannedInet() {
    return editedBannedInet;
  }

  /**
   * Sets the edited banned inet.
   * 
   * @param editedBannedInet
   *          the new edited banned inet
   */
  public void setEditedBannedInet(BannedInet editedBannedInet) {
    this.editedBannedInet = editedBannedInet;
  }
}
