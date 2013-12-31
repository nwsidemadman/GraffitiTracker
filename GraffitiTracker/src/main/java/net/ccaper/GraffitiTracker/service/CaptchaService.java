package net.ccaper.GraffitiTracker.service;

import net.ccaper.GraffitiTracker.objects.TextCaptcha;

public interface CaptchaService {
  TextCaptcha getTextCaptcha();
  
  boolean isCaptchaAnswerCorrect(TextCaptcha textCaptcha, String answer);
}
