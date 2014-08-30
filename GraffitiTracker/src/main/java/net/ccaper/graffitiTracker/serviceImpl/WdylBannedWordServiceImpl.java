package net.ccaper.graffitiTracker.serviceImpl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import net.ccaper.graffitiTracker.objects.WDYLResponse;
import net.ccaper.graffitiTracker.service.BannedWordService;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 
 * @author ccaper
 * 
 *         Implementation of Google's WDYL API for the Banned Words Service
 * 
 */
@Service("BannedWordService")
public class WdylBannedWordServiceImpl implements BannedWordService {
  private static final Logger logger = LoggerFactory
      .getLogger(WdylBannedWordServiceImpl.class);
  private static final String WDYL_URL = "http://www.wdyl.com/profanity?q=";

  /*
   * (non-Javadoc)
   * 
   * @see net.ccaper.graffitiTracker.service.BannedWordService#
   * doesStringContainBannedWord(java.lang.String)
   */
  @Override
  public boolean doesStringContainBannedWord(String string) {
    try {
      WDYLResponse response = getWdylResponse(new URL(WDYL_URL + string));
      return response.getResponse();
    } catch (JsonParseException e) {
      logger.error(String.format(
          "JSONParseException when checking WDYL for string %s.", string), e);
      return false;
    } catch (JsonMappingException e) {
      logger.error(String.format(
          "JsonMappingException when checking WDYL for string %s.", string), e);
      return false;
    } catch (MalformedURLException e) {
      logger
          .error(String
              .format(
                  "MalformedURLException when checking WDYL for string %s.",
                  string), e);
      return false;
    } catch (IOException e) {
      logger.error(String.format(
          "IOException when checking WDYL for string %s.", string), e);
      return false;
    }
  }

  // visible for mocking
  /**
   * Gets the wdyl response.
   * 
   * @param url
   *          the url
   * @return the wdyl response
   * @throws JsonParseException
   *           the json parse exception
   * @throws JsonMappingException
   *           the json mapping exception
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   */
  WDYLResponse getWdylResponse(URL url) throws JsonParseException,
      JsonMappingException, IOException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(url, WDYLResponse.class);
  }
}
