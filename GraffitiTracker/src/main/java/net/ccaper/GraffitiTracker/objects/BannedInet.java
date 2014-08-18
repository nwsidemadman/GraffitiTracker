package net.ccaper.GraffitiTracker.objects;

public class BannedInet {
  private String inetMinIncl;
  private String inetMaxIncl;
  private boolean isActive;
  private int numberRegistrationAttempts;
  private String notes;

  public String getInetMinIncl() {
    return inetMinIncl;
  }

  public void setInetMinIncl(String inetMinIncl) {
    this.inetMinIncl = inetMinIncl;
  }

  public String getInetMaxIncl() {
    return inetMaxIncl;
  }

  public void setInetMaxIncl(String inetMaxIncl) {
    this.inetMaxIncl = inetMaxIncl;
  }

  public boolean isActive() {
    return isActive;
  }

  public void setActive(boolean isActive) {
    this.isActive = isActive;
  }

  public int getNumberRegistrationAttempts() {
    return numberRegistrationAttempts;
  }

  public void setNumberRegistrationAttempts(int numberRegistrationAttempts) {
    this.numberRegistrationAttempts = numberRegistrationAttempts;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result
        + ((inetMaxIncl == null) ? 0 : inetMaxIncl.hashCode());
    result = prime * result
        + ((inetMinIncl == null) ? 0 : inetMinIncl.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    BannedInet other = (BannedInet) obj;
    if (inetMaxIncl == null) {
      if (other.inetMaxIncl != null)
        return false;
    } else if (!inetMaxIncl.equals(other.inetMaxIncl))
      return false;
    if (inetMinIncl == null) {
      if (other.inetMinIncl != null)
        return false;
    } else if (!inetMinIncl.equals(other.inetMinIncl))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "BannedInet [inetMinIncl=" + inetMinIncl + ", inetMaxIncl="
        + inetMaxIncl + ", isActive=" + isActive
        + ", numberRegistrationAttempts=" + numberRegistrationAttempts
        + ", notes=" + notes + "]";
  }
}
