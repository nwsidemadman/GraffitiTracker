package net.ccaper.graffitiTracker.service;

import net.ccaper.graffitiTracker.objects.TextCaptcha;

public interface CaptchaService {
  TextCaptcha getTextCaptcha();
  
  boolean isCaptchaAnswerCorrect(TextCaptcha textCaptcha, String answer);
}
