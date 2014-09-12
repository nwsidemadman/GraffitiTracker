package net.ccaper.graffitiTracker.objects;

import java.sql.Timestamp;

/**
 * 
 * @author ccaper
 * 
 *         POJO for BannedInet
 * 
 */
public class BannedInet {
  private String inetMinIncl;
  private String inetMaxIncl;
  private boolean isActive = true;
  private int numberRegistrationAttempts;
  private Timestamp createdTimestamp;
  private String notes;

  /**
   * Gets the inet min incl.
   * 
   * @return the inet min incl
   */
  public String getInetMinIncl() {
    return inetMinIncl;
  }

  /**
   * Sets the inet min incl.
   * 
   * @param inetMinIncl
   *          the new inet min incl
   */
  public void setInetMinIncl(String inetMinIncl) {
    this.inetMinIncl = inetMinIncl;
  }

  /**
   * Gets the inet max incl.
   * 
   * @return the inet max incl
   */
  public String getInetMaxIncl() {
    return inetMaxIncl;
  }

  /**
   * Sets the inet max incl.
   * 
   * @param inetMaxIncl
   *          the new inet max incl
   */
  public void setInetMaxIncl(String inetMaxIncl) {
    this.inetMaxIncl = inetMaxIncl;
  }

  /**
   * Gets the checks if is active.
   * 
   * @return the checks if is active
   */
  public boolean getIsActive() {
    return isActive;
  }

  /**
   * Sets the checks if is active.
   * 
   * @param isActive
   *          the new checks if is active
   */
  public void setIsActive(boolean isActive) {
    this.isActive = isActive;
  }

  /**
   * Gets the number registration attempts.
   * 
   * @return the number registration attempts
   */
  public int getNumberRegistrationAttempts() {
    return numberRegistrationAttempts;
  }

  /**
   * Sets the number registration attempts.
   * 
   * @param numberRegistrationAttempts
   *          the new number registration attempts
   */
  public void setNumberRegistrationAttempts(int numberRegistrationAttempts) {
    this.numberRegistrationAttempts = numberRegistrationAttempts;
  }

  /**
   * Gets the notes.
   * 
   * @return the notes
   */
  public String getNotes() {
    return notes;
  }

  /**
   * Sets the notes.
   * 
   * @param notes
   *          the new notes
   */
  public void setNotes(String notes) {
    this.notes = notes;
  }
  
  
  /**
   * Sets the created timestamp.
   *
   * @param createdTimestamp the new created timestamp
   */
  public void setCreatedTimestamp(Timestamp createdTimestamp) {
    this.createdTimestamp = createdTimestamp;
  }
  
  /**
   * Gets the created timestamp.
   *
   * @return the created timestamp
   */
  public Timestamp getCreatedTimestamp() {
    return createdTimestamp;
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#hashCode()
   */
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

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
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

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "BannedInet [inetMinIncl=" + inetMinIncl + ", inetMaxIncl="
        + inetMaxIncl + ", isActive=" + isActive
        + ", numberRegistrationAttempts=" + numberRegistrationAttempts
        + ", notes=" + notes + ", createdTimestamp=" + createdTimestamp + "]";
  }
}
