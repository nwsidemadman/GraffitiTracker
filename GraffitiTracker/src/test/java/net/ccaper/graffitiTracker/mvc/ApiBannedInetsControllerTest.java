package net.ccaper.graffitiTracker.mvc;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import net.ccaper.graffitiTracker.mvc.ApiBannedInetsController;
import net.ccaper.graffitiTracker.objects.BannedInet;
import net.ccaper.graffitiTracker.service.BannedInetsService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ApiBannedInetsControllerTest {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testAddBannedInet() throws Exception {
    BannedInetsService bannedInetsServiceMock = mock(BannedInetsService.class);
    BannedInet bannedInet = new BannedInet();
    bannedInet.setInetMaxIncl("127.0.0.1");
    bannedInet.setInetMaxIncl("127.0.0.1");
    bannedInet.setNotes("test");
    ApiBannedInetsController controller = new ApiBannedInetsController();
    controller.setBannedInetsService(bannedInetsServiceMock);
    assertEquals(bannedInet, controller.addBannedInet(bannedInet));
    verify(bannedInetsServiceMock).insertOrUpdateBannedInets(bannedInet);
  }
}
