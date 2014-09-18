package net.ccaper.graffitiTracker.serviceImpl;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.ccaper.graffitiTracker.dao.ChicagoCityServicesGraffitiDao;
import net.ccaper.graffitiTracker.dao.ChicagoCityServicesServerDao;
import net.ccaper.graffitiTracker.dao.impl.RestChicagoCityServicesServerDaoImpl;
import net.ccaper.graffitiTracker.objects.ChicagoCityServiceGraffiti;
import net.ccaper.graffitiTracker.service.ChicagoCityServicesGraffitiService;

@Service("chicagoCityServicesGraffitiService")
public class ChicagoCityServicesGraffitiServiceImpl implements
    ChicagoCityServicesGraffitiService {
  @Autowired
  private ChicagoCityServicesServerDao chicagoCityServicesServerDao;
  @Autowired
  private ChicagoCityServicesGraffitiDao chicagoCityServicesGraffitiDao;

  // visible for testing
  // TODO(ccaper): javadoc
  void setChicagoCityServicesServerDao(
      ChicagoCityServicesServerDao chicagoCityServicesServerDao) {
    this.chicagoCityServicesServerDao = chicagoCityServicesServerDao;
  }

  // visible for testing
  // TODO(ccaper): javadoc
  void setChicagoCityServicesGraffitiDao(
      ChicagoCityServicesGraffitiDao chicagoCityServicesGraffitiDao) {
    this.chicagoCityServicesGraffitiDao = chicagoCityServicesGraffitiDao;
  }

  @Override
  public List<ChicagoCityServiceGraffiti> getChicagoCityServiceDataFromServer(
      Date startDate, Date endDate) {
    return chicagoCityServicesServerDao.getGraffiti(startDate, endDate);
  }

  public static void main(String[] args) {
    ChicagoCityServicesGraffitiServiceImpl service = new ChicagoCityServicesGraffitiServiceImpl();
    ChicagoCityServicesServerDao dao = new RestChicagoCityServicesServerDaoImpl();
    service.setChicagoCityServicesServerDao(dao);
    Calendar cal = GregorianCalendar.getInstance();
    cal.set(2014, 8, 13, 0, 0);
    Date startDate = cal.getTime();
    cal.roll(Calendar.DAY_OF_MONTH, 1);
    Date endDate = cal.getTime();
    List<ChicagoCityServiceGraffiti> results = service
        .getChicagoCityServiceDataFromServer(startDate, endDate);
    for (ChicagoCityServiceGraffiti result : results) {
      System.out.println(result);
    }
    System.out.println("Number of records: " + results.size());
  }

  @Override
  public void storeChicagoCityServiceData(List<ChicagoCityServiceGraffiti> data) {
    // TODO Auto-generated method stub

  }
}
