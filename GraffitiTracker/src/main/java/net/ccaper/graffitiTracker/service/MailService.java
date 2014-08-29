package net.ccaper.graffitiTracker.service;

import java.util.List;

/**
 * 
 * @author ccaper
 * 
 *         Service for sending mail
 * 
 */
public interface MailService {

  /**
   * Send simple email.
   * 
   * @param recipients
   *          the recipients
   * @param subject
   *          the subject
   * @param content
   *          the content
   */
  void sendSimpleEmail(List<String> recipients, String subject, String content);

  /**
   * Send velocity template email.
   * 
   * @param recipients
   *          the recipients
   * @param subject
   *          the subject
   * @param content
   *          the content
   */
  void sendVelocityEmail(List<String> recipients, String subject, String content);
}
