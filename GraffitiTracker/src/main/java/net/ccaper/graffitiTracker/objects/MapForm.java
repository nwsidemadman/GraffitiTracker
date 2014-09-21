package net.ccaper.graffitiTracker.objects;

// TODO(ccaper): javadoc
public class MapForm {
  // TODO(ccaper): make this enum
  private String status;
  // TODO(ccaper): make this timestamp
  private String startDate;
  // TODO(ccaper): make this timestamp
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

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }
}
