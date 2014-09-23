package net.ccaper.graffitiTracker.objects;

import java.util.Date;

/**
 * 
 * @author ccaper
 * 
 *         POJO backing the form for the user choices for updating data from
 *         city services server.
 *
 */
public class CityServiceUpdateForm {
  // visible for testing
  static final long BEGINNING_OF_TIME = 64800000L;
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

  /**
   * Gets the start date as Date.
   *
   * @return the start date as Date
   */
  public Date getStartDateAsDate() {
    if (startDate == BEGINNING_OF_TIME) {
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

  /**
   * Gets the end date as Date.
   *
   * @return the end date as Date
   */
  public Date getEndDateAsDate() {
    if (startDate == BEGINNING_OF_TIME) {
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
