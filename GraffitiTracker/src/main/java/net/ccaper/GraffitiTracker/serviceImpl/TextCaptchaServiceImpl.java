package net.ccaper.GraffitiTracker.serviceImpl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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

import net.ccaper.GraffitiTracker.objects.TextCaptcha;
import net.ccaper.GraffitiTracker.service.CaptchaService;

@Service("captchaService")
public class TextCaptchaServiceImpl implements CaptchaService {
  // TODO: unit test
  private static final Logger logger = LoggerFactory
      .getLogger(TextCaptchaServiceImpl.class);
  private static final String TEXT_CAPTCHA_URL = "http://api.textcaptcha.com/";
  private static final Random RANDOM = new Random(System.currentTimeMillis());
  @Autowired
  @Qualifier("captchaKey")
  private String captchaKey;

  @Override
  public TextCaptcha getTextCaptcha() {
    try {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db;
      db = dbf.newDocumentBuilder();
      Document doc = db.parse(new URL(TEXT_CAPTCHA_URL + captchaKey)
          .openStream());
      NodeList questionNodes = doc.getElementsByTagName("question");
      NodeList answerNodes = doc.getElementsByTagName("answer");
      if (questionNodes.getLength() != 1) {
        logger.error(String.format(
            "Text captcha contained %s questions, should only be 1.",
            questionNodes.getLength()));
        return generateRandomDefaultCaptcha();
      }
      String[] answers = new String[answerNodes.getLength()];
      for (int i = 0; i < answerNodes.getLength(); ++i) {
        answers[i] = answerNodes.item(i).getTextContent();
      }
      return new TextCaptcha(questionNodes.item(0).getTextContent(), answers);
    } catch (ParserConfigurationException e) {
      logger
          .error("ParserConfigurationException while parsing TextCaptcha.", e);
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

  private TextCaptcha generateRandomDefaultCaptcha() {
    int defaultCaptchasSize = 10;
    List<TextCaptcha> defaultCaptchas = new ArrayList<TextCaptcha>(
        defaultCaptchasSize);
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
    return defaultCaptchas.get(RANDOM.nextInt(defaultCaptchasSize));
  }
  
  @Override
  public boolean isCaptchaAnswerCorrect(TextCaptcha textCaptcha, String answer) {
    if (StringUtils.isEmpty(answer)) {
      return false;
    }
    return textCaptcha.getAnswers().contains(
        DigestUtils.md5Hex(answer.toLowerCase()));
  }
}
