package net.ccaper.graffitiTracker.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ccaper.graffitiTracker.dao.ChicagoCityServicesServerDao;
import net.ccaper.graffitiTracker.objects.ChicagoCityServiceGraffiti;
import net.ccaper.graffitiTracker.utils.DateFormats;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RestChicagoCityServicesServerDaoImplTest {
  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetGraffiti_InvalidDate() throws Exception {
    Calendar cal = GregorianCalendar.getInstance();
    cal.set(2014, 8, 15, 0, 0);
    Date startDate = cal.getTime();
    cal.roll(Calendar.DAY_OF_MONTH, -1);
    Date endDate = cal.getTime();
    ChicagoCityServicesServerDao classUnderTest = new RestChicagoCityServicesServerDaoImpl();
    classUnderTest.getGraffiti(startDate, endDate, 1);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGetGraffiti_InvalidPage() throws Exception {
    ChicagoCityServicesServerDao classUnderTest = new RestChicagoCityServicesServerDaoImpl();
    classUnderTest.getGraffiti(null, null, 0);
  }
  
  @Test
  public void testGetGraffiti_happyPathNonNullDates() throws Exception {
    final ChicagoCityServiceGraffiti result = new ChicagoCityServiceGraffiti();
    result.setId(5);
    class RestChicagoCityServiceServerDaoImplMock extends RestChicagoCityServicesServerDaoImpl {
      private int i = 0;
      @Override
      ChicagoCityServiceGraffiti[] getGraffitiData(
          Map<String, String> urlVariables) {
        if (i == 0) {
          ++i;
          ChicagoCityServiceGraffiti[] results = {result};
          return results;
        } else {
          return new ChicagoCityServiceGraffiti[0];
        }
      }
    }
    
    RestChicagoCityServicesServerDaoImpl classUnderTest = new RestChicagoCityServiceServerDaoImplMock();
    Calendar cal = GregorianCalendar.getInstance();
    cal.set(2014, 8, 15, 0, 0);
    List<ChicagoCityServiceGraffiti> results = new ArrayList<ChicagoCityServiceGraffiti>();
    List<ChicagoCityServiceGraffiti> temp;
    int page = 1;
    do {
      temp = classUnderTest.getGraffiti(cal.getTime(), cal.getTime(), page);
      if (temp.size() > 0) {
        ++page;
        results.addAll(temp);
      }
    } while (temp.size() > 0);
    assertEquals(1, results.size());
    assertTrue(results.contains(result));
  }
  
  @Test
  public void testGetGraffiti_happyPathNullStart() throws Exception {
    final ChicagoCityServiceGraffiti result = new ChicagoCityServiceGraffiti();
    result.setId(5);
    class RestChicagoCityServiceServerDaoImplMock extends RestChicagoCityServicesServerDaoImpl {
      private int i = 0;
      @Override
      ChicagoCityServiceGraffiti[] getGraffitiData(
          Map<String, String> urlVariables) {
        if (i == 0) {
          ++i;
          ChicagoCityServiceGraffiti[] results = {result};
          return results;
        } else {
          return new ChicagoCityServiceGraffiti[0];
        }
      }
    }
    
    RestChicagoCityServicesServerDaoImpl classUnderTest = new RestChicagoCityServiceServerDaoImplMock();
    List<ChicagoCityServiceGraffiti> results = new ArrayList<ChicagoCityServiceGraffiti>();
    List<ChicagoCityServiceGraffiti> temp;
    int page = 1;
    do {
      temp = classUnderTest.getGraffiti(null, null, page);
      if (temp.size() > 0) {
        ++page;
        results.addAll(temp);
      }
    } while (temp.size() > 0);
    assertEquals(1, results.size());
    assertTrue(results.contains(result));
  }

  @Test
  public void testSetDateInChicagoServicesDateRangeUrl_NullDate()
      throws Exception {
    RestChicagoCityServicesServerDaoImpl classUnderTest = new RestChicagoCityServicesServerDaoImpl();
    Map<String, String> vars = new HashMap<String, String>();
    String before = RestChicagoCityServicesServerDaoImpl.chicagoServicesDateRangeUrl;
    classUnderTest.setDateInChicagoServicesDateRangeUrl(null, "test", vars);
    assertEquals(before,
        RestChicagoCityServicesServerDaoImpl.chicagoServicesDateRangeUrl);
    assertTrue(vars.isEmpty());
  }

  @Test
  public void testSetDateInChicagoServicesDateRangeUrl_HappyPath()
      throws Exception {
    RestChicagoCityServicesServerDaoImpl classUnderTest = new RestChicagoCityServicesServerDaoImpl();
    Map<String, String> vars = new HashMap<String, String>();
    String before = RestChicagoCityServicesServerDaoImpl.chicagoServicesDateRangeUrl;
    Calendar cal = GregorianCalendar.getInstance();
    cal.set(2014, 8, 15, 0, 0);
    String arg = "test";
    classUnderTest.setDateInChicagoServicesDateRangeUrl(cal.getTime(), arg,
        vars);
    assertEquals(String.format("%s&%s={%s}", before, arg, arg),
        RestChicagoCityServicesServerDaoImpl.chicagoServicesDateRangeUrl);
    assertEquals(1, vars.size());
    assertTrue(vars.containsKey(arg));
    assertEquals(DateFormats.W3_DATE_FORMAT.format(cal.getTime()),
        vars.get(arg));
  }
}
