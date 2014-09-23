package net.ccaper.graffitiTracker.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import net.ccaper.graffitiTracker.dao.ChicagoCityServicesServerDao;
import net.ccaper.graffitiTracker.objects.ChicagoCityServiceGraffiti;
import net.ccaper.graffitiTracker.utils.DateFormats;

/**
 * 
 * @author ccaper
 * 
 *         REST implementation of the ChicagoCityServicesServerDao.
 * 
 */
@Repository("chicagoCityServicesServerDao")
public class RestChicagoCityServicesServerDaoImpl implements
    ChicagoCityServicesServerDao {
  private static final String GRAFFITI_SERVICE_CODE = "4fd3b167e750846744000005";
  private static final String SERVICE_CODE_ARG = "service_code";
  private static final String PAGE_ARG = "page";
  private static final String PAGE_SIZE_ARG = "page_size";
  private static final String START_DATE_ARG = "start_date";
  private static final String END_DATE_ARG = "end_date";

  // visible for testing
  static String chicagoServicesDateRangeUrl = String.format(
      "http://311api.cityofchicago.org/open311/v2/requests.json?"
          + "%s={%s}&%s={%s}&%s={%s}", SERVICE_CODE_ARG, SERVICE_CODE_ARG,
      PAGE_SIZE_ARG, PAGE_SIZE_ARG, PAGE_ARG, PAGE_ARG);
  private static final String PAGE_SIZE = "500";

  /* (non-Javadoc)
   * @see net.ccaper.graffitiTracker.dao.ChicagoCityServicesServerDao#getGraffiti(java.util.Date, java.util.Date, int)
   */
  @Override
  public List<ChicagoCityServiceGraffiti> getGraffiti(Date startDate,
      Date endDate, int page) {
    if (page < 1) {
      throw new IllegalArgumentException(String.format("page %d must be greater than 1", page));
    }
    if ((startDate != null && endDate != null) && endDate.before(startDate)) {
      throw new IllegalArgumentException(String.format(
          "startDate '%s' must be <= to endDate '%s'",
          DateFormats.W3_DATE_FORMAT.format(startDate),
          DateFormats.W3_DATE_FORMAT.format(endDate)));
    }
    Map<String, String> urlVariables = new HashMap<String, String>(5);
    urlVariables.put(SERVICE_CODE_ARG, GRAFFITI_SERVICE_CODE);
    urlVariables.put(PAGE_SIZE_ARG, PAGE_SIZE);
    setDateInChicagoServicesDateRangeUrl(startDate, START_DATE_ARG,
        urlVariables);
    setDateInChicagoServicesDateRangeUrl(endDate, END_DATE_ARG, urlVariables);
    List<ChicagoCityServiceGraffiti> results = new ArrayList<ChicagoCityServiceGraffiti>();
    urlVariables.put(PAGE_ARG, Integer.toString(page));
    List<ChicagoCityServiceGraffiti> temp = Arrays
        .asList(getGraffitiData(urlVariables));
    if (temp.size() != 0) {
      ++page;
      results.addAll(temp);
    }
    return results;
  }

  // isolated for testing
  // visible for testing
  /**
   * REST template call that gets the graffiti data.
   * 
   * @param urlVariables
   *          the url variables for substitution in the url query string
   * @return the graffiti data
   */
  ChicagoCityServiceGraffiti[] getGraffitiData(Map<String, String> urlVariables) {
    return new RestTemplate().getForObject(chicagoServicesDateRangeUrl,
        ChicagoCityServiceGraffiti[].class, urlVariables);
  }

  // visible for testing
  /**
   * Sets the date in chicago services date range url.
   * 
   * @param date
   *          the date
   * @param arg
   *          the argument/key used url query string
   * @param vars
   *          the variable values used for substitution in the url query string
   */
  void setDateInChicagoServicesDateRangeUrl(Date date, String arg,
      Map<String, String> vars) {
    if (date != null) {
      chicagoServicesDateRangeUrl += String.format("&%s={%s}", arg, arg);
      vars.put(arg, DateFormats.W3_DATE_FORMAT.format(date));
    }
  }
}
