package net.ccaper.GraffitiTracker.serviceImpl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import net.ccaper.GraffitiTracker.objects.TextCaptcha;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TextCaptchaServiceImplTest {
  private TextCaptcha captcha = new TextCaptcha(
      "The list pink, heart, yellow, sweatshirt and jelly contains how many colours?",
      "b8a9f715dbb64fd5c56e7783c6820a61",
      "c81e728d9d4c2f636f067f89cc14862c");

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testIsCaptchaAnswerCorrect() throws Exception {
    TextCaptchaServiceImpl service = new TextCaptchaServiceImpl();
    assertTrue(service.isCaptchaAnswerCorrect(captcha, "2"));
    assertTrue(service.isCaptchaAnswerCorrect(captcha, "two"));
    assertTrue(service.isCaptchaAnswerCorrect(captcha, "TWO"));
    assertFalse(service.isCaptchaAnswerCorrect(captcha, "3"));
  }
}
