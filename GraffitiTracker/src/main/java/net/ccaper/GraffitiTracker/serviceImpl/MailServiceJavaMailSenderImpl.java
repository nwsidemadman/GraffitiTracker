package net.ccaper.GraffitiTracker.serviceImpl;

import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import net.ccaper.GraffitiTracker.service.MailService;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailServiceJavaMailSenderImpl implements MailService {
  private static final Logger logger = LoggerFactory
      .getLogger(MailServiceJavaMailSenderImpl.class);
  private static final String REPLY_TO = "no-reply@graffiti-tracker.com";
  @Autowired
  private JavaMailSender mailSender;

  public void setMailSender(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

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

  SimpleMailMessage getSimpleMailMessage(String[] recipients, String subject,
      String content) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setReplyTo(REPLY_TO);
    message.setTo(recipients);
    message.setSubject(subject);
    message.setText(content);
    return message;
  }

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

  MimeMessage getVelocityMimeMessage(String[] recipients, String subject, String content)
      throws MessagingException {
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true);
    helper.setReplyTo(REPLY_TO);
    helper.setTo(recipients);
    // TODO: prepend env to subject
    helper.setSubject(subject);
    helper.setText(
        content, true);
    // TODO: consider small logo for emails so it shows inline on email
    // TODO: make copyright line smaller, for emails
    ClassPathResource logo = new ClassPathResource("../../resources/images/graffiti_tracker_logo.png");
    helper.addInline("graffitiTrackerLogo", logo);
    return message;
  }
}
