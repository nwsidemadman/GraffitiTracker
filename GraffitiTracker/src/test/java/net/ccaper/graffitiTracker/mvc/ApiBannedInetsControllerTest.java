package net.ccaper.graffitiTracker.mvc;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.ccaper.graffitiTracker.mvc.ApiBannedInetsController;
import net.ccaper.graffitiTracker.objects.BannedInet;
import net.ccaper.graffitiTracker.objects.OriginalEditedBannedInet;
import net.ccaper.graffitiTracker.service.BannedInetsService;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class ApiBannedInetsControllerTest {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  @Ignore
  // TODO(ccaper): fix test
  public void testAddBannedInet() throws Exception {
    BannedInetsService bannedInetsServiceMock = mock(BannedInetsService.class);
    BannedInet editedBannedInet = new BannedInet();
    editedBannedInet.setInetMaxIncl("127.0.0.1");
    editedBannedInet.setInetMaxIncl("127.0.0.1");
    editedBannedInet.setNotes("test");
    ApiBannedInetsController controller = new ApiBannedInetsController();
    controller.setBannedInetsService(bannedInetsServiceMock);
    OriginalEditedBannedInet origEditedBannedInet = new OriginalEditedBannedInet();
    origEditedBannedInet.setEditedBannedInet(editedBannedInet);
    origEditedBannedInet.setOriginalBannedInet(new BannedInet());
    HttpServletResponse responseMock = mock(HttpServletResponse.class);
    assertEquals(editedBannedInet, controller.addBannedInet(origEditedBannedInet, responseMock));
    verify(bannedInetsServiceMock).insertOrNonInetUpdateBannedInets(editedBannedInet);
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
