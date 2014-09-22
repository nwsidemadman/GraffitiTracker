package net.ccaper.graffitiTracker.objects;

import java.sql.Timestamp;

// TODO(ccaper): javadoc
public class MapForm {
  private String status;
  private String startDate;
  private String endDate;

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getStartDate() {
    return startDate;
  }

//  public Timestamp getStartDateAsTimestamp() {
//    return new Timestamp(startDate);
//  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public String getEndDate() {
    return endDate;
  }

//  public Timestamp getEndDateAsTimestamp() {
//    return new Timestamp(endDate);
//  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }
}
