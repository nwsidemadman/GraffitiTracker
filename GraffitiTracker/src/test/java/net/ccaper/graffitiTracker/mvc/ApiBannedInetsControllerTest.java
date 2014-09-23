package net.ccaper.graffitiTracker.mvc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.ccaper.graffitiTracker.mvc.ApiBannedInetsController;
import net.ccaper.graffitiTracker.objects.BannedInet;
import net.ccaper.graffitiTracker.objects.OriginalEditedBannedInet;
import net.ccaper.graffitiTracker.service.BannedInetsService;

import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

public class ApiBannedInetsControllerTest {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testAddBannedInet_invalidEditedBannedInet() throws Exception {
    BannedInet origBannedInet = new BannedInet();
    origBannedInet.setInetMinIncl("127.0.0.2");
    origBannedInet.setInetMaxIncl("127.0.0.2");
    BannedInet editedBannedInet = new BannedInet();
    editedBannedInet.setInetMinIncl("127.0.0.2");
    editedBannedInet.setInetMaxIncl("127.0.0.1");
    OriginalEditedBannedInet origEditBannedInet = new OriginalEditedBannedInet();
    origEditBannedInet.setOriginalBannedInet(origBannedInet);
    origEditBannedInet.setEditedBannedInet(editedBannedInet);
    MockHttpServletResponse response = new MockHttpServletResponse();
    ApiBannedInetsController classUnderTest = new ApiBannedInetsController();
    assertEquals(origEditBannedInet.getEditedBannedInet(),
        classUnderTest.addBannedInet(origEditBannedInet, response));
    assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
  }

  @Test
  public void testAddBannedInet_origBannedInetNull() throws Exception {
    BannedInet editedBannedInet = new BannedInet();
    editedBannedInet.setInetMinIncl("127.0.0.2");
    editedBannedInet.setInetMaxIncl("127.0.0.2");
    OriginalEditedBannedInet origEditBannedInet = new OriginalEditedBannedInet();
    origEditBannedInet.setOriginalBannedInet(null);
    origEditBannedInet.setEditedBannedInet(editedBannedInet);
    MockHttpServletResponse response = new MockHttpServletResponse();
    ApiBannedInetsController classUnderTest = new ApiBannedInetsController();
    BannedInetsService bannedInetsServiceMock = mock(BannedInetsService.class);
    classUnderTest.setBannedInetsService(bannedInetsServiceMock);
    assertEquals(origEditBannedInet.getEditedBannedInet(),
        classUnderTest.addBannedInet(origEditBannedInet, response));
    verify(bannedInetsServiceMock).insertOrNonInetUpdateBannedInets(
        origEditBannedInet.getEditedBannedInet());
  }
  
  @Test
  public void testAddBannedInet_origBannedInetMinNull() throws Exception {
    BannedInet origBannedInet = new BannedInet();
    origBannedInet.setInetMinIncl(null);
    origBannedInet.setInetMaxIncl("127.0.0.2");
    BannedInet editedBannedInet = new BannedInet();
    editedBannedInet.setInetMinIncl("127.0.0.2");
    editedBannedInet.setInetMaxIncl("127.0.0.2");
    OriginalEditedBannedInet origEditBannedInet = new OriginalEditedBannedInet();
    origEditBannedInet.setOriginalBannedInet(origBannedInet);
    origEditBannedInet.setEditedBannedInet(editedBannedInet);
    MockHttpServletResponse response = new MockHttpServletResponse();
    ApiBannedInetsController classUnderTest = new ApiBannedInetsController();
    BannedInetsService bannedInetsServiceMock = mock(BannedInetsService.class);
    classUnderTest.setBannedInetsService(bannedInetsServiceMock);
    assertEquals(origEditBannedInet.getEditedBannedInet(),
        classUnderTest.addBannedInet(origEditBannedInet, response));
    verify(bannedInetsServiceMock).insertOrNonInetUpdateBannedInets(
        origEditBannedInet.getEditedBannedInet());
  }
  
  @Test
  public void testAddBannedInet_origBannedInetMinEmpty() throws Exception {
    BannedInet origBannedInet = new BannedInet();
    origBannedInet.setInetMinIncl(StringUtils.EMPTY);
    origBannedInet.setInetMaxIncl("127.0.0.2");
    BannedInet editedBannedInet = new BannedInet();
    editedBannedInet.setInetMinIncl("127.0.0.2");
    editedBannedInet.setInetMaxIncl("127.0.0.2");
    OriginalEditedBannedInet origEditBannedInet = new OriginalEditedBannedInet();
    origEditBannedInet.setOriginalBannedInet(origBannedInet);
    origEditBannedInet.setEditedBannedInet(editedBannedInet);
    MockHttpServletResponse response = new MockHttpServletResponse();
    ApiBannedInetsController classUnderTest = new ApiBannedInetsController();
    BannedInetsService bannedInetsServiceMock = mock(BannedInetsService.class);
    classUnderTest.setBannedInetsService(bannedInetsServiceMock);
    assertEquals(origEditBannedInet.getEditedBannedInet(),
        classUnderTest.addBannedInet(origEditBannedInet, response));
    verify(bannedInetsServiceMock).insertOrNonInetUpdateBannedInets(
        origEditBannedInet.getEditedBannedInet());
  }
  
  @Test
  public void testAddBannedInet_InetsDidNotChange() throws Exception {
    BannedInet origBannedInet = new BannedInet();
    origBannedInet.setInetMinIncl("127.0.0.2");
    origBannedInet.setInetMaxIncl("127.0.0.2");
    BannedInet editedBannedInet = new BannedInet();
    editedBannedInet.setInetMinIncl("127.0.0.2");
    editedBannedInet.setInetMaxIncl("127.0.0.2");
    OriginalEditedBannedInet origEditBannedInet = new OriginalEditedBannedInet();
    origEditBannedInet.setOriginalBannedInet(origBannedInet);
    origEditBannedInet.setEditedBannedInet(editedBannedInet);
    MockHttpServletResponse response = new MockHttpServletResponse();
    ApiBannedInetsController classUnderTest = new ApiBannedInetsController();
    BannedInetsService bannedInetsServiceMock = mock(BannedInetsService.class);
    classUnderTest.setBannedInetsService(bannedInetsServiceMock);
    assertEquals(origEditBannedInet.getEditedBannedInet(),
        classUnderTest.addBannedInet(origEditBannedInet, response));
    verify(bannedInetsServiceMock).insertOrNonInetUpdateBannedInets(
        origEditBannedInet.getEditedBannedInet());
  }
  
  @Test
  public void testAddBannedInet_InetsDidChange() throws Exception {
    BannedInet origBannedInet = new BannedInet();
    origBannedInet.setInetMinIncl("127.0.0.2");
    origBannedInet.setInetMaxIncl("127.0.0.2");
    BannedInet editedBannedInet = new BannedInet();
    editedBannedInet.setInetMinIncl("127.0.0.2");
    editedBannedInet.setInetMaxIncl("127.0.0.3");
    OriginalEditedBannedInet origEditBannedInet = new OriginalEditedBannedInet();
    origEditBannedInet.setOriginalBannedInet(origBannedInet);
    origEditBannedInet.setEditedBannedInet(editedBannedInet);
    MockHttpServletResponse response = new MockHttpServletResponse();
    ApiBannedInetsController classUnderTest = new ApiBannedInetsController();
    BannedInetsService bannedInetsServiceMock = mock(BannedInetsService.class);
    classUnderTest.setBannedInetsService(bannedInetsServiceMock);
    assertEquals(origEditBannedInet.getEditedBannedInet(),
        classUnderTest.addBannedInet(origEditBannedInet, response));
    verify(bannedInetsServiceMock).inetUpdateBannedInets(origEditBannedInet);
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
    Map<String, List<BannedInet>> data = new HashMap<String, List<BannedInet>>(
        1);
    data.put("data", bannedInets);
    when(bannedInetsServiceMock.getAllBannedInets()).thenReturn(bannedInets);
    ApiBannedInetsController classUnderTest = new ApiBannedInetsController();
    classUnderTest.setBannedInetsService(bannedInetsServiceMock);
    assertEquals(data, classUnderTest.getAllBannedInets());
    verify(bannedInetsServiceMock).getAllBannedInets();
  }

  @Test
  public void testDidInetsChange_maxChanged() throws Exception {
    BannedInet origBannedInet = new BannedInet();
    origBannedInet.setInetMinIncl("127.0.0.1");
    origBannedInet.setInetMaxIncl("127.0.0.1");
    BannedInet editedBannedInet = new BannedInet();
    editedBannedInet.setInetMinIncl("127.0.0.1");
    editedBannedInet.setInetMaxIncl("127.0.0.2");
    OriginalEditedBannedInet origEditBannedInet = new OriginalEditedBannedInet();
    origEditBannedInet.setOriginalBannedInet(origBannedInet);
    origEditBannedInet.setEditedBannedInet(editedBannedInet);
    assertTrue(ApiBannedInetsController.didInetsChange(origEditBannedInet));
  }

  @Test
  public void testDidInetsChange_minChanged() throws Exception {
    BannedInet origBannedInet = new BannedInet();
    origBannedInet.setInetMinIncl("127.0.0.2");
    origBannedInet.setInetMaxIncl("127.0.0.2");
    BannedInet editedBannedInet = new BannedInet();
    editedBannedInet.setInetMinIncl("127.0.0.1");
    editedBannedInet.setInetMaxIncl("127.0.0.2");
    OriginalEditedBannedInet origEditBannedInet = new OriginalEditedBannedInet();
    origEditBannedInet.setOriginalBannedInet(origBannedInet);
    origEditBannedInet.setEditedBannedInet(editedBannedInet);
    assertTrue(ApiBannedInetsController.didInetsChange(origEditBannedInet));
  }

  @Test
  public void testDidInetsChange_noCHange() throws Exception {
    BannedInet origBannedInet = new BannedInet();
    origBannedInet.setInetMinIncl("127.0.0.2");
    origBannedInet.setInetMaxIncl("127.0.0.2");
    BannedInet editedBannedInet = new BannedInet();
    editedBannedInet.setInetMinIncl("127.0.0.2");
    editedBannedInet.setInetMaxIncl("127.0.0.2");
    OriginalEditedBannedInet origEditBannedInet = new OriginalEditedBannedInet();
    origEditBannedInet.setOriginalBannedInet(origBannedInet);
    origEditBannedInet.setEditedBannedInet(editedBannedInet);
    assertFalse(ApiBannedInetsController.didInetsChange(origEditBannedInet));
  }

  @Test
  public void testIsBannedInetValid_happyPath() throws Exception {
    BannedInet bannedInet = new BannedInet();
    bannedInet.setInetMinIncl("127.0.0.2");
    bannedInet.setInetMaxIncl("127.0.0.2");
    assertTrue(ApiBannedInetsController.isBannedInetValid(bannedInet));
  }

  @Test
  public void testIsBannedInetValid_minInvalid() throws Exception {
    BannedInet bannedInet = new BannedInet();
    bannedInet.setInetMinIncl(null);
    bannedInet.setInetMaxIncl("127.0.0.2");
    assertFalse(ApiBannedInetsController.isBannedInetValid(bannedInet));
  }

  @Test
  public void testIsBannedInetValid_maxInvalid() throws Exception {
    BannedInet bannedInet = new BannedInet();
    bannedInet.setInetMinIncl("127.0.0.2");
    bannedInet.setInetMaxIncl(null);
    assertFalse(ApiBannedInetsController.isBannedInetValid(bannedInet));
  }

  @Test
  public void testIsBannedInetValid_minGreaterThanMax() throws Exception {
    BannedInet bannedInet = new BannedInet();
    bannedInet.setInetMinIncl("127.0.0.2");
    bannedInet.setInetMaxIncl("127.0.0.1");
    assertFalse(ApiBannedInetsController.isBannedInetValid(bannedInet));
  }
}
