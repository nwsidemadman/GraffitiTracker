package net.ccaper.graffitiTracker.objects;

// TODO(ccaper): javadoc
// TODO(ccaper): confirm needed
public class OriginalEditedBannedInet {
  private BannedInet originalBannedInet;
  private BannedInet editedBannedInet;

  public BannedInet getOriginalBannedInet() {
    return originalBannedInet;
  }

  public void setOriginalBannedInet(BannedInet originalBannedInet) {
    this.originalBannedInet = originalBannedInet;
  }

  public BannedInet getEditedBannedInet() {
    return editedBannedInet;
  }

  public void setEditedBannedInet(BannedInet editedBannedInet) {
    this.editedBannedInet = editedBannedInet;
  }
}
