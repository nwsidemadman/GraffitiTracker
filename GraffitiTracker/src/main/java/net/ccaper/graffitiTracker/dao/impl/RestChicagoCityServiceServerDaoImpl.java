package net.ccaper.graffitiTracker.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.client.RestTemplate;

import net.ccaper.graffitiTracker.dao.ChicagoCityServiceServerDao;
import net.ccaper.graffitiTracker.objects.ChicagoCityServiceGraffiti;
import net.ccaper.graffitiTracker.utils.DateFormats;

public class RestChicagoCityServiceServerDaoImpl implements
    ChicagoCityServiceServerDao {
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

  // TODO(ccaper): unit test
  // TODO(ccaper): javadoc
  @Override
  public List<ChicagoCityServiceGraffiti> getGraffiti(Date startDate,
      Date endDate) {
    if (startDate != null && endDate != null && endDate.before(startDate)) {
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
    int page = 1;
    List<ChicagoCityServiceGraffiti> temp;
    do {
      urlVariables.put(PAGE_ARG, Integer.toString(page));
      temp = Arrays.asList(getGraffitiData(urlVariables));
      if (temp.size() != 0) {
        ++page;
        results.addAll(temp);
      }
    } while (temp.size() != 0);
    return results;
  }

  // isolated for testing
  // visisble for testing
  ChicagoCityServiceGraffiti[] getGraffitiData(
      Map<String, String> urlVariables) {
    return new RestTemplate().getForObject(chicagoServicesDateRangeUrl,
        ChicagoCityServiceGraffiti[].class, urlVariables);
  }

  // TODO(ccaper): javadoc
  // visible for testing
  void setDateInChicagoServicesDateRangeUrl(Date date, String arg,
      Map<String, String> vars) {
    if (date != null) {
      chicagoServicesDateRangeUrl += String.format("&%s={%s}", arg, arg);
      vars.put(arg, DateFormats.W3_DATE_FORMAT.format(date));
    }
  }

  public static void main(String[] args) {
    RestChicagoCityServiceServerDaoImpl dao = new RestChicagoCityServiceServerDaoImpl();
    System.out.println("start");
    Calendar cal = GregorianCalendar.getInstance();
    cal.set(2014, 8, 13, 0, 0);
    Date startDate = cal.getTime();
    cal.set(2014, 8, 14, 0, 0);
    Date endDate = cal.getTime();
    System.out.println("StartDate: "
        + DateFormats.W3_DATE_FORMAT.format(startDate));
    System.out
        .println("EndDate: " + DateFormats.W3_DATE_FORMAT.format(endDate));
    List<ChicagoCityServiceGraffiti> data = dao.getGraffiti(startDate, endDate);
    for (ChicagoCityServiceGraffiti datum : data) {
      System.out.println(datum);
    }
    System.out.println(String.format("%s items retrieved.", data.size()));
    System.out.println("end");
  }
}
