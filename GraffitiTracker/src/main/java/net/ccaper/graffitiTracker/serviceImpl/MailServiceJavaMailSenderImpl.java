package net.ccaper.graffitiTracker.serviceImpl;

import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import net.ccaper.graffitiTracker.enums.EnvironmentEnum;
import net.ccaper.graffitiTracker.service.MailService;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * 
 * @author ccaper
 * 
 *         Implementation for the JavaMail version of the mail service
 * 
 */
@Service
public class MailServiceJavaMailSenderImpl implements MailService {
  private static final Logger logger = LoggerFactory
      .getLogger(MailServiceJavaMailSenderImpl.class);
  private static final String REPLY_TO = "no-reply@graffiti-tracker.com";
  @Autowired
  private JavaMailSender mailSender;

  // visible for testing
  /**
   * Sets the mail sender.
   * 
   * @param mailSender
   *          the new mail sender
   */
  void setMailSender(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * net.ccaper.graffitiTracker.service.MailService#sendSimpleEmail(java.util
   * .List, java.lang.String, java.lang.String)
   */
  @Override
  public void sendSimpleEmail(List<String> recipients, String subject,
      String content) {
    try {
      mailSender.send(getSimpleMailMessage(
          recipients.toArray(new String[recipients.size()]), subject, content));
    } catch (MailException e) {
      logger.error(String.format(
          "Error sending simple email, to='%s', subject='%s, content='%s'",
          StringUtils.join(recipients, ", "), subject, content), e);
    }
  }

  /**
   * Gets the simple mail message.
   * 
   * @param recipients
   *          the recipients
   * @param subject
   *          the subject
   * @param content
   *          the content
   * @return the simple mail message
   */
  SimpleMailMessage getSimpleMailMessage(String[] recipients, String subject,
      String content) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setReplyTo(REPLY_TO);
    message.setTo(recipients);
    message.setSubject(subject);
    message.setText(content);
    return message;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * net.ccaper.graffitiTracker.service.MailService#sendVelocityEmail(java.util
   * .List, java.lang.String, java.lang.String)
   */
  @Override
  public void sendVelocityEmail(List<String> recipients, String subject,
      String content) {
    try {
      mailSender.send(getVelocityMimeMessage(
          recipients.toArray(new String[recipients.size()]), subject, content));
    } catch (MailException e) {
      logger.error(String.format(
          "Error sending rich email, to='%s', subject='%s, content='%s'",
          StringUtils.join(recipients, ", "), subject, content), e);
    } catch (MessagingException e) {
      logger.error(String.format(
          "Error sending rich email, to='%s', subject='%s, content='%s'",
          StringUtils.join(recipients, ", "), subject, content), e);
    }
  }

  /**
   * Gets the velocity mime message.
   * 
   * @param recipients
   *          the recipients
   * @param subject
   *          the subject
   * @param content
   *          the content
   * @return the velocity mime message
   * @throws MessagingException
   *           the messaging exception
   */
  MimeMessage getVelocityMimeMessage(String[] recipients, String subject,
      String content) throws MessagingException {
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true);
    helper.setReplyTo(REPLY_TO);
    helper.setTo(recipients);
    helper.setSubject(String.format(
        "%s %s",
        EnvironmentEnum.getEnvironmentEnumFromEnvironmentPropertyString(
            System.getProperty("CLASSPATH_PROP_ENV")).getDisplayString(),
        subject));
    helper.setText(content, true);
    return message;
  }
}
