package net.ccaper.GraffitiTracker.serviceImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.ccaper.GraffitiTracker.objects.TextCaptcha;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class TextCaptchaServiceImplTest {
  private String xmlValid = "<captcha><question>Snake, knee, dog, face, shark and hand: how many body parts in the list?</question><answer>eccbc87e4b5ce2fe28308fd9f2a7baf3</answer><answer>35d6d33467aae9a2e3dccb4b6b027878</answer></captcha>";
  private TextCaptcha captchaForXmlValid = new TextCaptcha(
      "Snake, knee, dog, face, shark and hand: how many body parts in the list?",
      "eccbc87e4b5ce2fe28308fd9f2a7baf3", "35d6d33467aae9a2e3dccb4b6b027878");

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testIsCaptchaAnswerCorrect() throws Exception {
    TextCaptchaServiceImpl service = new TextCaptchaServiceImpl();
    assertTrue(service.isCaptchaAnswerCorrect(captchaForXmlValid, "3"));
    assertTrue(service.isCaptchaAnswerCorrect(captchaForXmlValid, "three"));
    assertTrue(service.isCaptchaAnswerCorrect(captchaForXmlValid, "THREE"));
    assertFalse(service.isCaptchaAnswerCorrect(captchaForXmlValid, "2"));
    assertFalse(service.isCaptchaAnswerCorrect(captchaForXmlValid, ""));
    assertFalse(service.isCaptchaAnswerCorrect(captchaForXmlValid, null));
  }

  @Test
  public void testGetTextCaptchaFromDocument_HappyPath() throws Exception {
    DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
        .newInstance();
    DocumentBuilder docBuilder;
    docBuilder = docBuilderFactory.newDocumentBuilder();
    Document doc = docBuilder.parse(new InputSource(new ByteArrayInputStream(
        xmlValid.getBytes("utf-8"))));
    TextCaptchaServiceImpl captchaService = new TextCaptchaServiceImpl();
    assertEquals(captchaForXmlValid,
        captchaService.getTextCaptchaFromDocument(doc));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetTextCaptchaFromDocument_MissingQuestion() throws Exception {
    String xmlMissingQuestion = "<captcha><answer>eccbc87e4b5ce2fe28308fd9f2a7baf3</answer><answer>35d6d33467aae9a2e3dccb4b6b027878</answer></captcha>";
    DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
        .newInstance();
    DocumentBuilder docBuilder;
    docBuilder = docBuilderFactory.newDocumentBuilder();
    Document doc = docBuilder.parse(new InputSource(new ByteArrayInputStream(
        xmlMissingQuestion.getBytes("utf-8"))));
    TextCaptchaServiceImpl captchaService = new TextCaptchaServiceImpl();
    captchaService.getTextCaptchaFromDocument(doc);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetTextCaptchaFromDocument_TwoQuestions() throws Exception {
    String xmlTwoQuestions = "<captcha><question>Snake, knee, dog, face, shark and hand: how many body parts in the list?</question><question>Some other question?</question><answer>eccbc87e4b5ce2fe28308fd9f2a7baf3</answer><answer>35d6d33467aae9a2e3dccb4b6b027878</answer></captcha>";
    DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
        .newInstance();
    DocumentBuilder docBuilder;
    docBuilder = docBuilderFactory.newDocumentBuilder();
    Document doc = docBuilder.parse(new InputSource(new ByteArrayInputStream(
        xmlTwoQuestions.getBytes("utf-8"))));
    TextCaptchaServiceImpl captchaService = new TextCaptchaServiceImpl();
    captchaService.getTextCaptchaFromDocument(doc);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetTextCaptchaFromDocument_NoAnswers() throws Exception {
    String xmlMissingAnswers = "<captcha><question>Snake, knee, dog, face, shark and hand: how many body parts in the list?</question></captcha>";
    DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
        .newInstance();
    DocumentBuilder docBuilder;
    docBuilder = docBuilderFactory.newDocumentBuilder();
    Document doc = docBuilder.parse(new InputSource(new ByteArrayInputStream(
        xmlMissingAnswers.getBytes("utf-8"))));
    TextCaptchaServiceImpl captchaService = new TextCaptchaServiceImpl();
    captchaService.getTextCaptchaFromDocument(doc);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetTextCaptchaFromDocument_QuestionTextContentEmpty()
      throws Exception {
    String xmlQuestionTextContentEmpty = "<captcha><question></question><answer>eccbc87e4b5ce2fe28308fd9f2a7baf3</answer><answer>35d6d33467aae9a2e3dccb4b6b027878</answer></captcha>";
    DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
        .newInstance();
    DocumentBuilder docBuilder;
    docBuilder = docBuilderFactory.newDocumentBuilder();
    Document doc = docBuilder.parse(new InputSource(new ByteArrayInputStream(
        xmlQuestionTextContentEmpty.getBytes("utf-8"))));
    TextCaptchaServiceImpl captchaService = new TextCaptchaServiceImpl();
    captchaService.getTextCaptchaFromDocument(doc);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetTextCaptchaFromDocument_FirstAnswerTextContentEmpty()
      throws Exception {
    String xmlFirstAnswerTextContentEmpty = "<captcha><question>Snake, knee, dog, face, shark and hand: how many body parts in the list?</question><answer></answer><answer>35d6d33467aae9a2e3dccb4b6b027878</answer></captcha>";
    DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
        .newInstance();
    DocumentBuilder docBuilder;
    docBuilder = docBuilderFactory.newDocumentBuilder();
    Document doc = docBuilder.parse(new InputSource(new ByteArrayInputStream(
        xmlFirstAnswerTextContentEmpty.getBytes("utf-8"))));
    TextCaptchaServiceImpl captchaService = new TextCaptchaServiceImpl();
    captchaService.getTextCaptchaFromDocument(doc);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetTextCaptchaFromDocument_SecondAnswerTextContentEmpty()
      throws Exception {
    String xmlSecondAnswerTextContentEmpty = "<captcha><question>Snake, knee, dog, face, shark and hand: how many body parts in the list?</question><answer>eccbc87e4b5ce2fe28308fd9f2a7baf3</answer><answer></answer></captcha>";
    DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
        .newInstance();
    DocumentBuilder docBuilder;
    docBuilder = docBuilderFactory.newDocumentBuilder();
    Document doc = docBuilder.parse(new InputSource(new ByteArrayInputStream(
        xmlSecondAnswerTextContentEmpty.getBytes("utf-8"))));
    TextCaptchaServiceImpl captchaService = new TextCaptchaServiceImpl();
    captchaService.getTextCaptchaFromDocument(doc);
  }

  @Test
  public void testGetTextCaptcha_HappyPath() throws Exception {
    class TextCaptchaServiceImplMock extends TextCaptchaServiceImpl {
      @Override
      Document getDocumentFromUrl(URL url) throws ParserConfigurationException,
          SAXException, IOException {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
            .newInstance();
        DocumentBuilder docBuilder;
        docBuilder = docBuilderFactory.newDocumentBuilder();
        return docBuilder.parse(new InputSource(new ByteArrayInputStream(
            xmlValid.getBytes("utf-8"))));
      }
    }

    TextCaptchaServiceImpl captchaServiceMock = new TextCaptchaServiceImplMock();
    captchaServiceMock.setMaxNumberGetCaptchaTries(3);
    assertEquals(captchaForXmlValid, captchaServiceMock.getTextCaptcha());
  }

  @Test
  public void testGetTextCaptcha_IllegalArgumentException() throws Exception {
    class TextCaptchaServiceImplMock extends TextCaptchaServiceImpl {
      @Override
      Document getDocumentFromUrl(URL url) throws ParserConfigurationException,
          SAXException, IOException {
        throw new IllegalArgumentException("test");
      }
    }

    TextCaptchaServiceImpl captchaServiceMock = new TextCaptchaServiceImplMock();
    captchaServiceMock.setMaxNumberGetCaptchaTries(3);
    assertFalse(captchaForXmlValid.equals(captchaServiceMock.getTextCaptcha()));
  }

  @Test
  public void testGetTextCaptcha_ParserConfigurationException()
      throws Exception {
    class TextCaptchaServiceImplMock extends TextCaptchaServiceImpl {
      @Override
      Document getDocumentFromUrl(URL url) throws ParserConfigurationException,
          SAXException, IOException {
        throw new ParserConfigurationException("test");
      }
    }

    TextCaptchaServiceImpl captchaServiceMock = new TextCaptchaServiceImplMock();
    captchaServiceMock.setMaxNumberGetCaptchaTries(3);
    assertFalse(captchaForXmlValid.equals(captchaServiceMock.getTextCaptcha()));
  }

  @Test
  public void testGetTextCaptcha_MalformedURLException() throws Exception {
    class TextCaptchaServiceImplMock extends TextCaptchaServiceImpl {
      @Override
      Document getDocumentFromUrl(URL url) throws ParserConfigurationException,
          SAXException, IOException {
        throw new MalformedURLException("test");
      }
    }

    TextCaptchaServiceImpl captchaServiceMock = new TextCaptchaServiceImplMock();
    captchaServiceMock.setMaxNumberGetCaptchaTries(3);
    assertFalse(captchaForXmlValid.equals(captchaServiceMock.getTextCaptcha()));
  }

  @Test
  public void testGetTextCaptcha_SAXException() throws Exception {
    class TextCaptchaServiceImplMock extends TextCaptchaServiceImpl {
      @Override
      Document getDocumentFromUrl(URL url) throws ParserConfigurationException,
          SAXException, IOException {
        throw new SAXException("test");
      }
    }

    TextCaptchaServiceImpl captchaServiceMock = new TextCaptchaServiceImplMock();
    captchaServiceMock.setMaxNumberGetCaptchaTries(3);
    assertFalse(captchaForXmlValid.equals(captchaServiceMock.getTextCaptcha()));
  }

  @Test
  public void testGetTextCaptcha_IOException() throws Exception {
    class TextCaptchaServiceImplMock extends TextCaptchaServiceImpl {
      @Override
      Document getDocumentFromUrl(URL url) throws ParserConfigurationException,
          SAXException, IOException {
        throw new IOException("test");
      }
    }

    TextCaptchaServiceImpl captchaServiceMock = new TextCaptchaServiceImplMock();
    captchaServiceMock.setMaxNumberGetCaptchaTries(3);
    assertFalse(captchaForXmlValid.equals(captchaServiceMock.getTextCaptcha()));
  }

  @Test
  public void testGetTextCaptcha_FileNotFoundExceptionLessThanMaxTries()
      throws Exception {
    class TextCaptchaServiceImplMock extends TextCaptchaServiceImpl {
      private int FileNotFoundExceptionCounter = 0;
      
      @Override
      Document getDocumentFromUrl(URL url) throws ParserConfigurationException,
          SAXException, IOException {
        if (FileNotFoundExceptionCounter == 0) {
          ++FileNotFoundExceptionCounter;
          throw new FileNotFoundException("test");
        } else {
          DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
              .newInstance();
          DocumentBuilder docBuilder;
          docBuilder = docBuilderFactory.newDocumentBuilder();
          return docBuilder.parse(new InputSource(new ByteArrayInputStream(
              xmlValid.getBytes("utf-8"))));
        }
      }
    }

    TextCaptchaServiceImpl captchaServiceMock = new TextCaptchaServiceImplMock();
    captchaServiceMock.setMaxNumberGetCaptchaTries(3);
    assertTrue(captchaForXmlValid.equals(captchaServiceMock.getTextCaptcha()));
  }
  
  @Test
  public void testGetTextCaptcha_FileNotFoundException() throws Exception {
    class TextCaptchaServiceImplMock extends TextCaptchaServiceImpl {
      @Override
      Document getDocumentFromUrl(URL url) throws ParserConfigurationException,
          SAXException, IOException {
        throw new FileNotFoundException("test");
      }
    }

    TextCaptchaServiceImpl captchaServiceMock = new TextCaptchaServiceImplMock();
    captchaServiceMock.setMaxNumberGetCaptchaTries(3);
    assertFalse(captchaForXmlValid.equals(captchaServiceMock.getTextCaptcha()));
  }
}
