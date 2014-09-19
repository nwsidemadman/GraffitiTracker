package net.ccaper.graffitiTracker.service;

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
   * Gets the chicago city service requests for graffiti from server.
   *
   * @param startDate
   *          the start date
   * @param endDate
   *          the end date
   * @return the chicago city service requests from server
   */
  List<ChicagoCityServiceGraffiti> getChicagoCityServiceGraffitiRequestsFromServer(
      Date startDate, Date endDate);

  /**
   * Store chicago city service graffiti data.
   *
   * @param data
   *          the data
   */
  void storeChicagoCityServiceGraffitiRequests(
      List<ChicagoCityServiceGraffiti> data);

  // TODO(ccaper): javadoc
  List<ChicagoCityServiceGraffiti> getAllGraffiti(List<String> status);
}
