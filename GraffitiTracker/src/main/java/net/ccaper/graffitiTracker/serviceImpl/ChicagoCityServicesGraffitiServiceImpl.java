package net.ccaper.graffitiTracker.serviceImpl;

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
  @Autowired
  private ChicagoCityServicesServerDao chicagoCityServicesServerDao;
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
  public void storeChicagoCityServiceGraffitiRequests(
      List<ChicagoCityServiceGraffiti> data) {
    for (ChicagoCityServiceGraffiti datum : data) {
      if (datum.getMediaUrl() != null) {
        chicagoCityServicesGraffitiDao.storeChicagoCityServicesGraffiti(datum);
      }
    }
  }

  /**
   * The main method.
   *
   * @param args
   *          the arguments
   */
  // TODO(ccaper): remove after feature added to web app
  public static void main(String[] args) {
    AnnotationConfigApplicationContext context = null;
    try {
      context = new AnnotationConfigApplicationContext(AppConfig.class);
      ChicagoCityServicesGraffitiService service = context
          .getBean(ChicagoCityServicesGraffitiServiceImpl.class);
      Calendar cal = GregorianCalendar.getInstance();
      cal.set(2014, 7, 1, 0, 0);
      Date startDate = cal.getTime();
      cal.set(2014, 8, 18, 0, 0);
      Date endDate = cal.getTime();
      List<ChicagoCityServiceGraffiti> results = service
          .getChicagoCityServiceGraffitiRequestsFromServer(startDate, endDate);
      service.storeChicagoCityServiceGraffitiRequests(results);
      ((ConfigurableApplicationContext) context).close();
    } catch (Exception e) {
      System.out.println("Error.");
      e.printStackTrace();
      System.exit(1);
    } finally {
      context.close();
    }
  }
}
