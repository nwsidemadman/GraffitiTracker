package net.ccaper.graffitiTracker.serviceImpl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.ccaper.graffitiTracker.dao.ChicagoCityServicesGraffitiDao;
import net.ccaper.graffitiTracker.dao.ChicagoCityServicesServerDao;
import net.ccaper.graffitiTracker.objects.ChicagoCityServiceGraffiti;
import net.ccaper.graffitiTracker.service.ChicagoCityServicesGraffitiService;
import net.ccaper.graffitiTracker.utils.DateFormats;

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
  private static final Logger logger = LoggerFactory
      .getLogger(ChicagoCityServicesGraffitiServiceImpl.class);

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

  // TODO(ccaper): javadoc
  // TODO(ccaper): unit test?
  private int storeChicagoCityServiceGraffitiRequestsInRepo(
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
  public List<ChicagoCityServiceGraffiti> getAllGraffitiFromRepo(
      List<String> status, Timestamp startDate, Timestamp endDate) {
    return chicagoCityServicesGraffitiDao.getAllChicagoCityServicesGraffiti(
        status, startDate, endDate);
  }

  // TODO(ccaper): javadoc
  // TODO(ccaper): unit test
  @Override
  public void getChicagoCityServiceGraffitiRequestsFromServerAndStoreInRepo(
      Date startDate, Date endDate) {
    int page = 1;
    List<ChicagoCityServiceGraffiti> temp;
    int totalItemsServer = 0;
    int totalItemsSaved = 0;
    do {
      temp = chicagoCityServicesServerDao.getGraffiti(startDate, endDate, page);
      logger.info(String.format(
          "Page %d fetched from city services server with " + "%d items.",
          page, temp.size()));
      if (temp.size() != 0) {
        ++page;
        logger.info(String.format(
            "First item in page %d batch has a requested datetime of %s.",
            page, DateFormats.W3_DATE_FORMAT.format(temp.get(0)
                .getRequestedDateTime())));
      }
      int numberRecordsSaved = storeChicagoCityServiceGraffitiRequestsInRepo(temp);
      totalItemsServer += temp.size();
      totalItemsSaved += numberRecordsSaved;
      logger
          .info(String
              .format(
                  "For page %d batch, %d items had media URL's and were saved to repo.",
                  page, numberRecordsSaved));
    } while (temp.size() != 0);
    logger
        .info(String
            .format(
                "Fetch from city services server and save to repo "
                    + "complete, %d total records found on server, %d total records saved to repo.",
                totalItemsServer, totalItemsSaved));
  }
}
