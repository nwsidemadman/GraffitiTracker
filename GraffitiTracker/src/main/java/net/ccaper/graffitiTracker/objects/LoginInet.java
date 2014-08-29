package net.ccaper.graffitiTracker.objects;

import java.sql.Timestamp;

public class LoginInet {
  private String inet;
  private int numberVisits;
  private Timestamp lastVisit;
  private boolean isInetBanned;

  public String getInet() {
    return inet;
  }

  public void setInet(String inet) {
    this.inet = inet;
  }

  public int getNumberVisits() {
    return numberVisits;
  }

  public void setNumberVisits(int numberVisits) {
    this.numberVisits = numberVisits;
  }

  public Timestamp getLastVisit() {
    return lastVisit;
  }

  public void setLastVisit(Timestamp lastVisit) {
    this.lastVisit = lastVisit;
  }
  
  public boolean getIsInetBanned() {
    return isInetBanned;
  }
  
  public void setIsInetBanned(boolean isInetBanned) {
    this.isInetBanned = isInetBanned;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((inet == null) ? 0 : inet.hashCode());
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
    LoginInet other = (LoginInet) obj;
    if (inet == null) {
      if (other.inet != null)
        return false;
    } else if (!inet.equals(other.inet))
      return false;
    return true;
  }
}
