package net.ccaper.graffitiTracker.service;

import net.ccaper.graffitiTracker.objects.TextCaptcha;

/**
 * 
 * @author ccaper
 * 
 *         Service for handling text captcha
 * 
 */
public interface CaptchaService {

  /**
   * Gets the text captcha.
   * 
   * @return the text captcha
   */
  TextCaptcha getTextCaptcha();

  /**
   * Checks if is captcha answer correct.
   * 
   * @param textCaptcha
   *          the text captcha
   * @param answer
   *          the answer
   * @return true, if captcha answer is correct
   */
  boolean isCaptchaAnswerCorrect(TextCaptcha textCaptcha, String answer);
}
