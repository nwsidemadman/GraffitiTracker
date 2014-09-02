package net.ccaper.graffitiTracker.mvc;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
  
  @Test
  public void testGetAllBannedInets() throws Exception {
    BannedInetsService bannedInetsServiceMock = mock(BannedInetsService.class);
    BannedInet bannedInet = new BannedInet();
    bannedInet.setInetMaxIncl("127.0.0.1");
    bannedInet.setInetMaxIncl("127.0.0.1");
    bannedInet.setIsActive(true);
    bannedInet.setNotes("test");
    List<BannedInet> bannedInets = new ArrayList<BannedInet>(1);
    bannedInets.add(bannedInet);
    Map<String, List<BannedInet>> data = new HashMap<String, List<BannedInet>>(1);
    data.put("data", bannedInets);
    when(bannedInetsServiceMock.getAllBannedInets()).thenReturn(bannedInets);
    ApiBannedInetsController classUnderTest = new ApiBannedInetsController();
    classUnderTest.setBannedInetsService(bannedInetsServiceMock);
    assertEquals(data, classUnderTest.getAllBannedInets());
    verify(bannedInetsServiceMock).getAllBannedInets();
  }
}
