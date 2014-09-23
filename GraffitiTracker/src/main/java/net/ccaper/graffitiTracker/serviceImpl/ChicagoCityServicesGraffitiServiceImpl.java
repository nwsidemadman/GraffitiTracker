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
import net.ccaper.graffitiTracker.enums.EnvironmentEnum;
import net.ccaper.graffitiTracker.objects.ChicagoCityServiceGraffiti;
import net.ccaper.graffitiTracker.service.ChicagoCityServicesGraffitiService;
import net.ccaper.graffitiTracker.service.MailService;
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
  @Autowired
  private ChicagoCityServicesServerDao chicagoCityServicesServerDao;
  @Autowired
  private ChicagoCityServicesGraffitiDao chicagoCityServicesGraffitiDao;
  @Autowired
  private MailService mailService;

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

  // visible for testing
  /**
   * Sets the mail service.
   * 
   * @param mailService
   *          the new mail service
   */
  void setMailService(MailService mailService) {
    this.mailService = mailService;
  }

  // visible for testing
  /**
   * Store chicago city service graffiti requests in repo.
   *
   * @param data
   *          the data to persist
   * @return number of records saved
   */
  int storeChicagoCityServiceGraffitiRequestsInRepo(
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

  /*
   * (non-Javadoc)
   * 
   * @see net.ccaper.graffitiTracker.service.ChicagoCityServicesGraffitiService#
   * getChicagoCityServiceGraffitiRequestsFromServerAndStoreInRepo
   * (java.util.Date, java.util.Date)
   */
  @Override
  // TODO(ccaper): update test
  public void getChicagoCityServiceGraffitiRequestsFromServerAndStoreInRepo(
      List<String> recipients, Date startDate, Date endDate) {
    int page = 1;
    List<ChicagoCityServiceGraffiti> temp;
    int totalRecordsServer = 0;
    int totalRecordsSaved = 0;
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
      totalRecordsServer += temp.size();
      totalRecordsSaved += numberRecordsSaved;
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
                totalRecordsServer, totalRecordsSaved));
    emailFetchAndStoreResults(recipients, totalRecordsServer, totalRecordsSaved);
  }

  // TODO(ccaper): javadoc
  private void emailFetchAndStoreResults(List<String> recipients,
      int totalRecordsServer, int totalRecordsSaved) {
    mailService
        .sendSimpleEmail(
            recipients,
            String.format(
                "%s GraffitiTracker CityService Data Update %s",
                EnvironmentEnum
                    .getEnvironmentEnumFromEnvironmentPropertyString(
                        System.getProperty("CLASSPATH_PROP_ENV"))
                    .getDisplayString(),
                DateFormats.YEAR_SLASH_MONTH_SLASH_DAY_FORMAT
                    .format(new Date())),
            String
                .format(
                    "Number of records found on city server: %d\nNumber of records inserted/updated in repo: %d",
                    totalRecordsServer, totalRecordsSaved));
  }
}
