package net.ccaper.graffitiTracker.serviceImpl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import net.ccaper.graffitiTracker.objects.WDYLResponse;
import net.ccaper.graffitiTracker.serviceImpl.WdylBannedWordServiceImpl;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WdylBannedWordServiceImplTest {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testDoesStringContainBannedWord_HappyPath() throws Exception {
    class WdylBannedWordServiceImplMock extends WdylBannedWordServiceImpl {

      @Override
      WDYLResponse getWdylResponse(URL url) throws JsonParseException,
      JsonMappingException, IOException {
        WDYLResponse response = new WDYLResponse();
        response.setResponse(Boolean.toString(true));
        return response;
      }
    }

    WdylBannedWordServiceImpl wdylBannedWordServiceImplMock = new WdylBannedWordServiceImplMock();
    assertTrue(wdylBannedWordServiceImplMock.doesStringContainBannedWord("safeString"));
  }

  @Test
  public void testDoesStringContainBannedWord_JsonParseException() throws Exception {
    class WdylBannedWordServiceImplMock extends WdylBannedWordServiceImpl {

      @Override
      WDYLResponse getWdylResponse(URL url) throws JsonParseException,
      JsonMappingException, IOException {
        throw new JsonParseException("test", null);
      }
    }

    WdylBannedWordServiceImpl wdylBannedWordServiceImplMock = new WdylBannedWordServiceImplMock();
    assertFalse(wdylBannedWordServiceImplMock.doesStringContainBannedWord("safeString"));
  }

  @Test
  public void testDoesStringContainBannedWord_JsonMappingException() throws Exception {
    class WdylBannedWordServiceImplMock extends WdylBannedWordServiceImpl {

      @Override
      WDYLResponse getWdylResponse(URL url) throws JsonParseException,
      JsonMappingException, IOException {
        throw new JsonMappingException("test");
      }
    }

    WdylBannedWordServiceImpl wdylBannedWordServiceImplMock = new WdylBannedWordServiceImplMock();
    assertFalse(wdylBannedWordServiceImplMock.doesStringContainBannedWord("safeString"));
  }

  @Test
  public void testDoesStringContainBannedWord_MalformedURLException() throws Exception {
    class WdylBannedWordServiceImplMock extends WdylBannedWordServiceImpl {

      @Override
      WDYLResponse getWdylResponse(URL url) throws JsonParseException,
      JsonMappingException, IOException {
        throw new MalformedURLException("test");
      }
    }

    WdylBannedWordServiceImpl wdylBannedWordServiceImplMock = new WdylBannedWordServiceImplMock();
    assertFalse(wdylBannedWordServiceImplMock.doesStringContainBannedWord("safeString"));
  }

  @Test
  public void testDoesStringContainBannedWord_IOException() throws Exception {
    class WdylBannedWordServiceImplMock extends WdylBannedWordServiceImpl {

      @Override
      WDYLResponse getWdylResponse(URL url) throws JsonParseException,
      JsonMappingException, IOException {
        throw new IOException("test");
      }
    }

    WdylBannedWordServiceImpl wdylBannedWordServiceImplMock = new WdylBannedWordServiceImplMock();
    assertFalse(wdylBannedWordServiceImplMock.doesStringContainBannedWord("safeString"));
  }
}
