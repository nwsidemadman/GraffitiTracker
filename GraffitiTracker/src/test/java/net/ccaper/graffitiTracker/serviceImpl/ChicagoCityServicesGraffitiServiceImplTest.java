package net.ccaper.graffitiTracker.serviceImpl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.ccaper.graffitiTracker.dao.ChicagoCityServicesGraffitiDao;
import net.ccaper.graffitiTracker.dao.ChicagoCityServicesServerDao;
import net.ccaper.graffitiTracker.objects.ChicagoCityServiceGraffiti;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ChicagoCityServicesGraffitiServiceImplTest {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testStoreChicagoCityServiceGraffitiRequestsInRepo()
      throws Exception {
    List<ChicagoCityServiceGraffiti> data = new ArrayList<ChicagoCityServiceGraffiti>(
        2);
    ChicagoCityServiceGraffiti datum1 = new ChicagoCityServiceGraffiti();
    datum1.setMediaUrl("test");
    data.add(datum1);
    ChicagoCityServiceGraffiti datum2 = new ChicagoCityServiceGraffiti();
    data.add(datum2);
    ChicagoCityServicesGraffitiDao mockDao = mock(ChicagoCityServicesGraffitiDao.class);
    ChicagoCityServicesGraffitiServiceImpl classUnderTest = new ChicagoCityServicesGraffitiServiceImpl();
    classUnderTest.setChicagoCityServicesGraffitiDao(mockDao);
    assertEquals(1,
        classUnderTest.storeChicagoCityServiceGraffitiRequestsInRepo(data));
    verify(mockDao).storeChicagoCityServicesGraffiti(datum1);
  }

  @Test
  public void testGetChicagoCityServiceGraffitiRequestsFromServerAndStoreInRepo()
      throws Exception {
    class ChicagoCityServicesGraffitiServiceImplMock extends
        ChicagoCityServicesGraffitiServiceImpl {
      @Override
      int storeChicagoCityServiceGraffitiRequestsInRepo(
          List<ChicagoCityServiceGraffiti> data) {
        return 1;
      }
    }

    Date startDate = null;
    Date endDate = null;
    ChicagoCityServicesGraffitiServiceImplMock classUnderTest = new ChicagoCityServicesGraffitiServiceImplMock();
    List<ChicagoCityServiceGraffiti> data = new ArrayList<ChicagoCityServiceGraffiti>(
        2);
    ChicagoCityServiceGraffiti datum1 = new ChicagoCityServiceGraffiti();
    datum1.setRequestedDateTime(new Timestamp((new Date()).getTime()));
    datum1.setMediaUrl("test");
    data.add(datum1);
    ChicagoCityServiceGraffiti datum2 = new ChicagoCityServiceGraffiti();
    data.add(datum2);
    ChicagoCityServicesServerDao mockDao = mock(ChicagoCityServicesServerDao.class);
    when(mockDao.getGraffiti(startDate, endDate, 1)).thenReturn(data);
    when(mockDao.getGraffiti(startDate, endDate, 2)).thenReturn(
        new ArrayList<ChicagoCityServiceGraffiti>(0));
    classUnderTest.setChicagoCityServicesServerDao(mockDao);
    classUnderTest
        .getChicagoCityServiceGraffitiRequestsFromServerAndStoreInRepo(
            startDate, endDate);
    verify(mockDao).getGraffiti(startDate, endDate, 1);
    verify(mockDao).getGraffiti(startDate, endDate, 2);
  }
}
