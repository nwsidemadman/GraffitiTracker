package net.ccaper.graffitiTracker.objects;

import java.util.Date;

// TODO(ccaper): javadoc
public class CityServiceUpdateForm {
  private Date startDate;
  private Date endDate;

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }
}
