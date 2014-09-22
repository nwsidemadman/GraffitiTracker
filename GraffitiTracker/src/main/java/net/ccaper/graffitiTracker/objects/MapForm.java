package net.ccaper.graffitiTracker.objects;

import java.sql.Timestamp;

// TODO(ccaper): javadoc
public class MapForm {
  private String status;
  private long startDate;
  private long endDate;

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public long getStartDate() {
    return startDate;
  }

  public Timestamp getStartDateAsTimestamp() {
    return new Timestamp(startDate);
  }

  public void setStartDate(long startDate) {
    this.startDate = startDate;
  }

  public long getEndDate() {
    return endDate;
  }

  public Timestamp getEndDateAsTimestamp() {
    return new Timestamp(endDate);
  }

  public void setEndDate(long endDate) {
    this.endDate = endDate;
  }
}
