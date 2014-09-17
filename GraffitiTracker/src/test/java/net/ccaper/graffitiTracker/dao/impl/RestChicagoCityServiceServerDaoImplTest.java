package net.ccaper.graffitiTracker.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import net.ccaper.graffitiTracker.dao.ChicagoCityServiceServerDao;
import net.ccaper.graffitiTracker.objects.ChicagoCityServiceGraffiti;
import net.ccaper.graffitiTracker.utils.DateFormats;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RestChicagoCityServiceServerDaoImplTest {
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
    ChicagoCityServiceServerDao classUnderTest = new RestChicagoCityServiceServerDaoImpl();
    classUnderTest.getGraffiti(startDate, endDate);
  }
  
  @Test
  public void testGetGraffiti_happyPathEmptyData() throws Exception {
    class RestChicagoCityServiceServerDaoImplMock extends RestChicagoCityServiceServerDaoImpl {
      @Override
      ChicagoCityServiceGraffiti[] getGraffitiData(
          Map<String, String> urlVariables) {
        return new ChicagoCityServiceGraffiti[0];
      }
    }
    
    RestChicagoCityServiceServerDaoImpl classUnderTest = new RestChicagoCityServiceServerDaoImplMock();
    assertEquals(0, classUnderTest.getGraffiti(null, null).size());
  }

  @Test
  public void testSetDateInChicagoServicesDateRangeUrl_NullDate()
      throws Exception {
    RestChicagoCityServiceServerDaoImpl classUnderTest = new RestChicagoCityServiceServerDaoImpl();
    Map<String, String> vars = new HashMap<String, String>();
    String before = RestChicagoCityServiceServerDaoImpl.chicagoServicesDateRangeUrl;
    classUnderTest.setDateInChicagoServicesDateRangeUrl(null, "test", vars);
    assertEquals(before,
        RestChicagoCityServiceServerDaoImpl.chicagoServicesDateRangeUrl);
    assertTrue(vars.isEmpty());
  }

  @Test
  public void testSetDateInChicagoServicesDateRangeUrl_HappyPath()
      throws Exception {
    RestChicagoCityServiceServerDaoImpl classUnderTest = new RestChicagoCityServiceServerDaoImpl();
    Map<String, String> vars = new HashMap<String, String>();
    String before = RestChicagoCityServiceServerDaoImpl.chicagoServicesDateRangeUrl;
    Calendar cal = GregorianCalendar.getInstance();
    cal.set(2014, 8, 15, 0, 0);
    String arg = "test";
    classUnderTest.setDateInChicagoServicesDateRangeUrl(cal.getTime(), arg,
        vars);
    assertEquals(String.format("%s&%s={%s}", before, arg, arg),
        RestChicagoCityServiceServerDaoImpl.chicagoServicesDateRangeUrl);
    assertEquals(1, vars.size());
    assertTrue(vars.containsKey(arg));
    assertEquals(DateFormats.W3_DATE_FORMAT.format(cal.getTime()),
        vars.get(arg));
  }
}
