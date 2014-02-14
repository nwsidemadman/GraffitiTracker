package net.ccaper.GraffitiTracker.serviceImpl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import net.ccaper.GraffitiTracker.dao.BannedInetsDao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BannedInetsServiceImplTest {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testIsInetBanned_False() {
    String inet = "127.0.0.1";
    BannedInetsDao bannedInetsDaoMock = mock(BannedInetsDao.class);
    when(bannedInetsDaoMock.selectCountInetInRange(inet)).thenReturn(0);
    BannedInetsServiceImpl service = new BannedInetsServiceImpl();
    service.setBannedInetsDao(bannedInetsDaoMock);
    assertFalse(service.isInetBanned(inet));
    verify(bannedInetsDaoMock).selectCountInetInRange(inet);
  }

  @Test
  public void testIsInetBanned_True() {
    String inet = "127.0.0.1";
    BannedInetsDao bannedInetsDaoMock = mock(BannedInetsDao.class);
    when(bannedInetsDaoMock.selectCountInetInRange(inet)).thenReturn(2);
    BannedInetsServiceImpl service = new BannedInetsServiceImpl();
    service.setBannedInetsDao(bannedInetsDaoMock);
    assertTrue(service.isInetBanned(inet));
    verify(bannedInetsDaoMock).selectCountInetInRange(inet);
  }
}
