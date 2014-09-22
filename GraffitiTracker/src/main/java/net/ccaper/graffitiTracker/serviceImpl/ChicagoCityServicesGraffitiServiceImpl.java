package net.ccaper.graffitiTracker.serviceImpl;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

import net.ccaper.graffitiTracker.dao.ChicagoCityServicesGraffitiDao;
import net.ccaper.graffitiTracker.dao.ChicagoCityServicesServerDao;
import net.ccaper.graffitiTracker.objects.ChicagoCityServiceGraffiti;
import net.ccaper.graffitiTracker.service.ChicagoCityServicesGraffitiService;
import net.ccaper.graffitiTracker.spring.AppConfig;

/**
 * 
 * @author ccaper
 * 
 *         Implementation for the ChicagoCityServicesGraffitiService
 * 
 */
@Service("chicagoCityServicesGraffitiService")
public class ChicagoCityServicesGraffitiServiceImpl implements
    ChicagoCityServicesGraffitiService {

  /** The chicago city services server dao. */
  @Autowired
  private ChicagoCityServicesServerDao chicagoCityServicesServerDao;

  /** The chicago city services graffiti dao. */
  @Autowired
  private ChicagoCityServicesGraffitiDao chicagoCityServicesGraffitiDao;

  // visible for testing
  /**
   * Sets the chicago city services server dao.
   * 
   * @param chicagoCityServicesServerDao
   *          the new chicago city services server dao
   */
  void setChicagoCityServicesServerDao(
      ChicagoCityServicesServerDao chicagoCityServicesServerDao) {
    this.chicagoCityServicesServerDao = chicagoCityServicesServerDao;
  }

  // visible for testing
  /**
   * Sets the chicago city services graffiti dao.
   * 
   * @param chicagoCityServicesGraffitiDao
   *          the new chicago city services graffiti dao
   */
  void setChicagoCityServicesGraffitiDao(
      ChicagoCityServicesGraffitiDao chicagoCityServicesGraffitiDao) {
    this.chicagoCityServicesGraffitiDao = chicagoCityServicesGraffitiDao;
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.ccaper.graffitiTracker.service.ChicagoCityServicesGraffitiService#
   * getChicagoCityServiceGraffitiRequestsFromServer(java.util.Date,
   * java.util.Date)
   */
  @Override
  public List<ChicagoCityServiceGraffiti> getChicagoCityServiceGraffitiRequestsFromServer(
      Date startDate, Date endDate) {
    return chicagoCityServicesServerDao.getGraffiti(startDate, endDate);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.ccaper.graffitiTracker.service.ChicagoCityServicesGraffitiService#
   * storeChicagoCityServiceGraffitiRequests(java.util.List)
   */
  @Override
  public int storeChicagoCityServiceGraffitiRequests(
      List<ChicagoCityServiceGraffiti> data) {
    int counter = 0;
    for (ChicagoCityServiceGraffiti datum : data) {
      if (datum.getMediaUrl() != null) {
        chicagoCityServicesGraffitiDao.storeChicagoCityServicesGraffiti(datum);
        ++counter;
      }
    }
    return counter;
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.ccaper.graffitiTracker.service.ChicagoCityServicesGraffitiService#
   * getAllGraffiti(java.util.List, java.sql.Timestamp, java.sql.Timestamp)
   */
  @Override
  public List<ChicagoCityServiceGraffiti> getAllGraffiti(List<String> status,
      Timestamp startDate, Timestamp endDate) {
    return chicagoCityServicesGraffitiDao.getAllChicagoCityServicesGraffiti(
        status, startDate, endDate);
  }
}
