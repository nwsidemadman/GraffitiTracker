package net.ccaper.graffitiTracker.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import net.ccaper.graffitiTracker.objects.ChicagoCityServiceGraffiti;

/**
 * 
 * @author ccaper
 * 
 *         Service for handling Chicago city service requests for graffiti.
 * 
 */
public interface ChicagoCityServicesGraffitiService {

  // TODO(ccaper): javadoc
  void getChicagoCityServiceGraffitiRequestsFromServerAndStoreInRepo(Date startDate, Date endDate);

  /**
   * Gets the all graffiti from repository matching criteria.
   *
   * @param status the status (open/closed)
   * @param startDate the start date
   * @param endDate the end date
   * @return the graffiti matching criteria
   */
  List<ChicagoCityServiceGraffiti> getAllGraffitiFromRepo(List<String> status,
      Timestamp startDate, Timestamp endDate);
}
