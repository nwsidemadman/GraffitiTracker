package net.ccaper.graffitiTracker.dao;

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
}
