package net.ccaper.graffitiTracker.dao.impl;

import static org.junit.Assert.assertEquals;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JdbcChicagoCityServicesGraffitiDaoImplTest {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testBuildStatusPortionQueryString_null() throws Exception {
    assertEquals(StringUtils.EMPTY,
        JdbcChicagoCityServicesGraffitiDaoImpl
            .buildStatusPortionQueryString(null));
  }

  @Test
  public void testBuildStatusPortionQueryString_empty() throws Exception {
    assertEquals(StringUtils.EMPTY,
        JdbcChicagoCityServicesGraffitiDaoImpl
            .buildStatusPortionQueryString(new ArrayList<String>(0)));
  }

  @Test
  public void testBuildStatusPortionQueryString_happyPath_1item()
      throws Exception {
    List<String> status = new ArrayList<String>(1);
    status.add("'closed'");
    assertEquals(String.format(" AND %s IN ('closed')",
        JdbcChicagoCityServicesGraffitiDaoImpl.STATUS_COL),
        JdbcChicagoCityServicesGraffitiDaoImpl
            .buildStatusPortionQueryString(status));
  }

  @Test
  public void testBuildStatusPortionQueryString_happyPath_2items()
      throws Exception {
    List<String> status = new ArrayList<String>(2);
    status.add("'closed'");
    status.add("'open'");
    assertEquals(String.format(" AND %s IN ('closed', 'open')",
        JdbcChicagoCityServicesGraffitiDaoImpl.STATUS_COL),
        JdbcChicagoCityServicesGraffitiDaoImpl
            .buildStatusPortionQueryString(status));
  }

  @Test
  public void testbuildQueryStringFromUserCriteria_noStatus() throws Exception {
    Timestamp startDate = new Timestamp(500L);
    Timestamp endDate = new Timestamp(600L);
    assertEquals(
        String.format(
            "%s WHERE %s LIMIT 100",
            JdbcChicagoCityServicesGraffitiDaoImpl.SQL_GET_ALL_GRAFFITI,
            JdbcChicagoCityServicesGraffitiDaoImpl
                .buildRequestDatePortionQueryString(startDate, endDate))
            .toLowerCase(),
        JdbcChicagoCityServicesGraffitiDaoImpl
            .buildQueryStringFromUserCriteria(new ArrayList<String>(0),
                startDate, endDate));
  }

  @Test
  public void testbuildQueryStringFromUserCriteria_status() throws Exception {
    List<String> status = new ArrayList<String>(1);
    status.add("'closed'");
    Timestamp startDate = new Timestamp(500L);
    Timestamp endDate = new Timestamp(600L);
    assertEquals(
        String.format(
            "%s WHERE %s%s LIMIT 100",
            JdbcChicagoCityServicesGraffitiDaoImpl.SQL_GET_ALL_GRAFFITI,
            JdbcChicagoCityServicesGraffitiDaoImpl
                .buildRequestDatePortionQueryString(startDate, endDate),
            JdbcChicagoCityServicesGraffitiDaoImpl
                .buildStatusPortionQueryString(status)).toLowerCase(),
        JdbcChicagoCityServicesGraffitiDaoImpl
            .buildQueryStringFromUserCriteria(status, startDate, endDate));
  }
}
