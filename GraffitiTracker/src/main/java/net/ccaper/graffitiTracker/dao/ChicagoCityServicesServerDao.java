package net.ccaper.graffitiTracker.dao;

import java.util.Date;
import java.util.List;

import net.ccaper.graffitiTracker.objects.ChicagoCityServiceGraffiti;

/**
 * @author ccaper
 * 
 *         The Interface ChicagoCityServicesServerDao.
 */
public interface ChicagoCityServicesServerDao {

  /**
   * Gets the graffiti from Chicago's City Services
   *
   * @param startDate
   *          the start date, can be null to not specify a start date
   * @param endDate
   *          the end date, can be null to not specify an end date
   * @return the graffiti
   */
  List<ChicagoCityServiceGraffiti> getGraffiti(Date startDate, Date endDate);
}
