package net.ccaper.graffitiTracker.dao;

import java.sql.Timestamp;
import java.util.List;

import net.ccaper.graffitiTracker.objects.ChicagoCityServiceGraffiti;

/**
 * 
 * @author ccaper
 * 
 *         DAO for ChicagoCityServicesGraffiti
 * 
 */
public interface ChicagoCityServicesGraffitiDao {

  /**
   * Store chicago city services graffiti request in repository.
   * 
   * @param graffiti
   *          the graffiti request
   */
  void storeChicagoCityServicesGraffiti(ChicagoCityServiceGraffiti graffiti);

  /**
   * Gets the all chicago city services graffiti that match criteria.
   *
   * @param status the status
   * @param startDate the start date
   * @param endDat the end date
   * @return the graffiti matching criteria
   */
  List<ChicagoCityServiceGraffiti> getAllChicagoCityServicesGraffiti(
      List<String> status, Timestamp startDate, Timestamp endDate);
}
