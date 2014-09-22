package net.ccaper.graffitiTracker.objects;

import java.sql.Timestamp;

/**
 * 
 * @author ccaper
 * 
 *         POJO that backs the form holding user's choices for graffiti
 *         criteria.
 *
 */
public class MapForm {
  private String status;
  private long startDate;
  private long endDate;

  /**
   * Gets the status.
   *
   * @return the status
   */
  public String getStatus() {
    return status;
  }

  /**
   * Sets the status.
   *
   * @param status
   *          the new status
   */
  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * Gets the start date.
   *
   * @return the start date
   */
  public long getStartDate() {
    return startDate;
  }

  /**
   * Gets the start date as timestamp.
   *
   * @return the start date as timestamp
   */
  public Timestamp getStartDateAsTimestamp() {
    return new Timestamp(startDate);
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
   * Gets the end date as timestamp.
   *
   * @return the end date as timestamp
   */
  public Timestamp getEndDateAsTimestamp() {
    return new Timestamp(endDate);
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
