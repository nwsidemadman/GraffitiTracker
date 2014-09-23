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

  /**
   * Gets the chicago city service graffiti requests from server and store in repo.
   *
   * @param recipients List of emails to notify of process results
   * @param startDate the start date
   * @param endDate the end date
   * @return the chicago city service graffiti requests from server and store in repo
   */
  void getChicagoCityServiceGraffitiRequestsFromServerAndStoreInRepo(List<String> recipients, Date startDate, Date endDate);

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
