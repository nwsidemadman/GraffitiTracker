package net.ccaper.graffitiTracker.objects;

import java.util.Date;

// TODO(ccaper): javadoc
public class CityServiceUpdateForm {
  private long startDate;
  private long endDate;

  /**
   * Gets the start date.
   *
   * @return the start date
   */
  public long getStartDate() {
    return startDate;
  }

  // TODO(ccaper): unit test
  /**
   * Gets the start date as Date.
   *
   * @return the start date as Date
   */
  public Date getStartDateAsDate() {
    if (startDate == 0L) {
      return null;
    }
    return new Date(startDate);
  }

  /**
   * Sets the start date.
   *
   * @param startDate
   *          the new start date
   */
  public void setStartDate(long startDate) {
    this.startDate = startDate;
  }

  /**
   * Gets the end date.
   *
   * @return the end date
   */
  public long getEndDate() {
    return endDate;
  }

  // TODO(ccaper): unit test
  /**
   * Gets the end date as Date.
   *
   * @return the end date as Date
   */
  public Date getEndDateAsDate() {
    if (startDate == 0L) {
      return null;
    }
    return new Date(endDate);
  }

  /**
   * Sets the end date.
   *
   * @param endDate
   *          the new end date
   */
  public void setEndDate(long endDate) {
    this.endDate = endDate;
  }
}
