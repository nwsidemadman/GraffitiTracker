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

  // TODO(ccaper): javadoc
  List<ChicagoCityServiceGraffiti> getAllChicagoCityServicesGraffiti(
      List<String> status, Timestamp startDate, Timestamp endDat);
}
