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
   * Gets the graffiti.
   *
   * @param startDate
   *          the start date
   * @param endDate
   *          the end date
   * @param page
   *          the page number, requests have a max return, so we have to page
   *          through results to get all
   * @return the graffiti
   */
  List<ChicagoCityServiceGraffiti> getGraffiti(Date startDate, Date endDate,
      int page);
}
