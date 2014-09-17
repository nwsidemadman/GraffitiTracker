package net.ccaper.graffitiTracker.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.client.RestTemplate;

import net.ccaper.graffitiTracker.dao.ChicagoCityServiceServerDao;
import net.ccaper.graffitiTracker.objects.ChicagoCityServiceGraffiti;

public class RestChicagoCityServiceServerDaoImpl implements
    ChicagoCityServiceServerDao {
  private static final String GRAFFITI_SERVICE_CODE = "4fd3b167e750846744000005";
  private static final String SERVICE_CODE_ARG = "service_code";
  private static final String PAGE_ARG = "page";
  private static final String PAGE_SIZE_ARG = "page_size";
  private static final String START_DATE_ARG = "start_date";
  private static final String END_DATE_ARG = "end_date";

  private static final String CHICAGO_SERVICES_DATE_RANGE_URL = String.format(
      "http://311api.cityofchicago.org/open311/v2/requests.json?"
          + "%s={%s}&%s={%s}&%s={%s}&%s={%s}&%s={%s}", SERVICE_CODE_ARG,
      SERVICE_CODE_ARG, PAGE_SIZE_ARG, PAGE_SIZE_ARG, PAGE_ARG, PAGE_ARG,
      START_DATE_ARG, START_DATE_ARG, END_DATE_ARG, END_DATE_ARG);
  private static final String PAGE_SIZE = "500";

  @Override
  public List<ChicagoCityServiceGraffiti> getGraffiti() {
    Map<String, String> urlVariables = new HashMap<String, String>(5);
    urlVariables.put(SERVICE_CODE_ARG, GRAFFITI_SERVICE_CODE);
    urlVariables.put(START_DATE_ARG, "2014-09-13T00:00:00Z");
    urlVariables.put(END_DATE_ARG, "2014-09-14T00:00:00Z");
    urlVariables.put(PAGE_SIZE_ARG, PAGE_SIZE);
    List<ChicagoCityServiceGraffiti> results = new ArrayList<ChicagoCityServiceGraffiti>();
    int page = 1;
    List<ChicagoCityServiceGraffiti> temp;
    do {
      urlVariables.put(PAGE_ARG, Integer.toString(page));
      temp = Arrays.asList(new RestTemplate().getForObject(
          CHICAGO_SERVICES_DATE_RANGE_URL, ChicagoCityServiceGraffiti[].class,
          urlVariables));
      if (temp.size() != 0) {
        ++page;
        results.addAll(temp);
      }
    } while (temp.size() != 0);
    return results;
  }

  public static void main(String[] args) {
    RestChicagoCityServiceServerDaoImpl dao = new RestChicagoCityServiceServerDaoImpl();
    System.out.println("start");
    List<ChicagoCityServiceGraffiti> data = dao.getGraffiti();
    for (ChicagoCityServiceGraffiti datum : data) {
      System.out.println(datum);
    }
    System.out.println(String.format("%s items retrieved.", data.size()));
    System.out.println("end");
  }
}
