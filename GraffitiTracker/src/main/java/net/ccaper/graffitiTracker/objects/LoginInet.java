package net.ccaper.graffitiTracker.objects;

import java.sql.Timestamp;

/**
 * 
 * @author ccaper
 * 
 *         POJO for LoginInet
 * 
 */
public class LoginInet {
  private String inet;
  private int numberVisits;
  private Timestamp lastVisit;
  private boolean isInetBanned;

  /**
   * Gets the inet.
   * 
   * @return the inet
   */
  public String getInet() {
    return inet;
  }

  /**
   * Sets the inet.
   * 
   * @param inet
   *          the new inet
   */
  public void setInet(String inet) {
    this.inet = inet;
  }

  /**
   * Gets the number visits.
   * 
   * @return the number visits
   */
  public int getNumberVisits() {
    return numberVisits;
  }

  /**
   * Sets the number visits.
   * 
   * @param numberVisits
   *          the new number visits
   */
  public void setNumberVisits(int numberVisits) {
    this.numberVisits = numberVisits;
  }

  /**
   * Gets the last visit.
   * 
   * @return the last visit
   */
  public Timestamp getLastVisit() {
    return lastVisit;
  }

  /**
   * Sets the last visit.
   * 
   * @param lastVisit
   *          the new last visit
   */
  public void setLastVisit(Timestamp lastVisit) {
    this.lastVisit = lastVisit;
  }

  /**
   * Gets the checks if is inet banned.
   * 
   * @return the checks if is inet banned
   */
  public boolean getIsInetBanned() {
    return isInetBanned;
  }

  /**
   * Sets the checks if is inet banned.
   * 
   * @param isInetBanned
   *          the new checks if is inet banned
   */
  public void setIsInetBanned(boolean isInetBanned) {
    this.isInetBanned = isInetBanned;
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
    result = prime * result + ((inet == null) ? 0 : inet.hashCode());
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
    LoginInet other = (LoginInet) obj;
    if (inet == null) {
      if (other.inet != null)
        return false;
    } else if (!inet.equals(other.inet))
      return false;
    return true;
  }
}
