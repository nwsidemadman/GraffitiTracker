package net.ccaper.graffitiTracker.serviceImpl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.ccaper.graffitiTracker.objects.TextCaptcha;
import net.ccaper.graffitiTracker.service.CaptchaService;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 
 * @author ccaper
 * 
 *         Implementation of the TextCaptcha version of the captcha service
 * 
 */
@Service("captchaService")
public class TextCaptchaServiceImpl implements CaptchaService {
  private static final Logger logger = LoggerFactory
      .getLogger(TextCaptchaServiceImpl.class);
  private static final String TEXT_CAPTCHA_URL = "http://api.textcaptcha.com/";
  private static final Random RANDOM = new Random(System.currentTimeMillis());
  @Autowired
  @Qualifier("captchaKey")
  private String captchaKey;
  @Autowired
  @Qualifier("maxNumberCaptchaFetchRetries")
  private Integer maxNumberGetCaptchaTries;

  /**
   * Sets the max number get captcha tries.
   * 
   * @param maxNumberGetCaptchaTries
   *          the new max number get captcha tries
   */
  public void setMaxNumberGetCaptchaTries(int maxNumberGetCaptchaTries) {
    this.maxNumberGetCaptchaTries = maxNumberGetCaptchaTries;
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.ccaper.graffitiTracker.service.CaptchaService#getTextCaptcha()
   */
  @Override
  public TextCaptcha getTextCaptcha() {
    int numberOfTries = 0;
    while (numberOfTries < maxNumberGetCaptchaTries) {
      try {
        Document doc = getDocumentFromUrl(new URL(TEXT_CAPTCHA_URL + captchaKey));
        return getTextCaptchaFromDocument(doc);
      } catch (FileNotFoundException e) {
        ++numberOfTries;
        logger.info(String.format("Try #%s failed fetching text captcha.",
            numberOfTries));
      } catch (IllegalArgumentException e) {
        logger.error("IllegalArgumentException while parsing TextCaptcha.", e);
        return generateRandomDefaultCaptcha();
      } catch (ParserConfigurationException e) {
        logger.error("ParserConfigurationException while parsing TextCaptcha.",
            e);
        return generateRandomDefaultCaptcha();
      } catch (MalformedURLException e) {
        logger.error("MalformedURLException while parsing TextCaptcha.", e);
        return generateRandomDefaultCaptcha();
      } catch (SAXException e) {
        logger.error("SAXException while parsing TextCaptcha.", e);
        return generateRandomDefaultCaptcha();
      } catch (IOException e) {
        logger.warn("IOException while parsing TextCaptcha.", e);
        return generateRandomDefaultCaptcha();
      }
    }
    return generateRandomDefaultCaptcha();
  }

  // visible for mocking
  /**
   * Gets the document from url.
   * 
   * @param url
   *          the url
   * @return the document from url
   * @throws ParserConfigurationException
   *           the parser configuration exception
   * @throws SAXException
   *           the SAX exception
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   */
  Document getDocumentFromUrl(URL url) throws ParserConfigurationException,
      SAXException, IOException {
    DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
        .newInstance();
    DocumentBuilder docBuilder;
    docBuilder = docBuilderFactory.newDocumentBuilder();
    return docBuilder.parse(url.openStream());
  }

  // visible for testing
  /**
   * Gets the text captcha from document.
   * 
   * @param doc
   *          the doc
   * @return the text captcha from document
   * @throws IllegalArgumentException
   *           the illegal argument exception
   */
  TextCaptcha getTextCaptchaFromDocument(Document doc)
      throws IllegalArgumentException {
    NodeList questionNodes = doc.getElementsByTagName("question");
    if (questionNodes.getLength() != 1) {
      throw new IllegalArgumentException(String.format(
          "Text captcha contained %s questions, should only be 1.",
          questionNodes.getLength()));
    }
    String question = questionNodes.item(0).getTextContent();
    if (StringUtils.isEmpty(question)) {
      throw new IllegalArgumentException("Question text content empty");
    }
    NodeList answerNodes = doc.getElementsByTagName("answer");
    String[] answers = new String[answerNodes.getLength()];
    if (answers.length <= 0) {
      throw new IllegalArgumentException(
          "Text captcha contained 0 questions, should 1 or more.");
    }
    for (int i = 0; i < answerNodes.getLength(); ++i) {
      answers[i] = answerNodes.item(i).getTextContent();
      if (StringUtils.isEmpty(answers[i])) {
        throw new IllegalArgumentException("Answer text content empty");
      }
    }
    return new TextCaptcha(question, answers);
  }

  // visible for testing
  /**
   * Generate random default captcha for when we can't connect to API.
   * 
   * @return the text captcha
   */
  TextCaptcha generateRandomDefaultCaptcha() {
    int defaultCaptchasSize = 10;
    List<TextCaptcha> defaultCaptchas = generateDefaultCaptchas();
    return defaultCaptchas.get(RANDOM.nextInt(defaultCaptchasSize));
  }

  /**
   * Generate default captchas for when we can't connect to API
   * 
   * @return the list
   */
  private List<TextCaptcha> generateDefaultCaptchas() {
    List<TextCaptcha> defaultCaptchas = new ArrayList<TextCaptcha>(10);
    defaultCaptchas
        .add(new TextCaptcha("Fourteen minus 7 is what?",
            "8f14e45fceea167a5a36dedd4bea2543",
            "bb3aec0fdcdbc2974890f805c585d432"));
    defaultCaptchas
        .add(new TextCaptcha("What is seventeen minus 1?",
            "c74d97b01eae257e44aa9d5bade97baf",
            "bd2c775d9eaf5f71da52b55ade9989a4"));
    defaultCaptchas.add(new TextCaptcha("What is Chris' name?",
        "6b34fe24ac2ff8103f6fce1f0da2ef57"));
    defaultCaptchas
        .add(new TextCaptcha(
            "How many colours in the list white, red, yellow and dress?",
            "35d6d33467aae9a2e3dccb4b6b027878",
            "eccbc87e4b5ce2fe28308fd9f2a7baf3"));
    defaultCaptchas
        .add(new TextCaptcha(
            "The list pink, brown, purple, hospital and bee contains how many colours?",
            "35d6d33467aae9a2e3dccb4b6b027878",
            "eccbc87e4b5ce2fe28308fd9f2a7baf3"));
    defaultCaptchas
        .add(new TextCaptcha(
            "35, 24 or twenty eight: which of these is the biggest?",
            "d8a1e6b3abf174b09407948ad6a8f099",
            "1c383cd30b7c298ab50293adfecb7b18"));
    defaultCaptchas
        .add(new TextCaptcha("What is eighteen - two?",
            "c74d97b01eae257e44aa9d5bade97baf",
            "bd2c775d9eaf5f71da52b55ade9989a4"));
    defaultCaptchas.add(new TextCaptcha(
        "What is eighteen thousand three hundred and fourteen as a number?",
        "eb98676e8ee16adce38796051a5cc7ff"));
    defaultCaptchas
        .add(new TextCaptcha("Which of 85, 85, 35, 6 or 20 is the biggest?",
            "4b4bd456e0c4dbefa62f445d8b2bf73b",
            "3ef815416f775098fe977004015c6193"));
    defaultCaptchas
        .add(new TextCaptcha(
            "The list pink, heart, yellow, sweatshirt and jelly contains how many colours?",
            "b8a9f715dbb64fd5c56e7783c6820a61",
            "c81e728d9d4c2f636f067f89cc14862c"));
    return defaultCaptchas;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * net.ccaper.graffitiTracker.service.CaptchaService#isCaptchaAnswerCorrect
   * (net.ccaper.graffitiTracker.objects.TextCaptcha, java.lang.String)
   */
  @Override
  public boolean isCaptchaAnswerCorrect(TextCaptcha textCaptcha, String answer) {
    if (StringUtils.isEmpty(answer)) {
      return false;
    }
    return textCaptcha.getAnswers().contains(
        DigestUtils.md5Hex(answer.toLowerCase()));
  }
}
