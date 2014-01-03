package net.ccaper.GraffitiTracker.serviceImpl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import net.ccaper.GraffitiTracker.objects.WDYLResponse;
import net.ccaper.GraffitiTracker.service.BannedWordService;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("BannedWordService")
public class WdylBannedWordServiceImpl implements BannedWordService {
  // TODO: unit test
  private static final Logger logger = LoggerFactory
      .getLogger(WdylBannedWordServiceImpl.class);

  @Override
  public boolean doesStringContainBannedWord(String string) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      WDYLResponse response = mapper.readValue(new URL(
          "http://www.wdyl.com/profanity?q=" + string), WDYLResponse.class);
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
}
