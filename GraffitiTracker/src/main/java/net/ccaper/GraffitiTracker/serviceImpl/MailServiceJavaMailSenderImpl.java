package net.ccaper.GraffitiTracker.serviceImpl;

import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

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

import net.ccaper.GraffitiTracker.service.MailService;

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
  public void sendRichEmail(List<String> recipients, String subject,
      String content) {
    try {
      mailSender.send(getMimeMessage(
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

  MimeMessage getMimeMessage(String[] recipients, String subject, String content)
      throws MessagingException {
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true);
    helper.setReplyTo(REPLY_TO);
    helper.setTo(recipients);
    helper.setSubject(subject);
    helper.setText(
        "<html><body><div><img src='cid:graffitiTrackerLogo'/></div>" + content
            + "</body></html>", true);
    ClassPathResource logo = new ClassPathResource("../../resources/images/graffiti_tracker_logo.png");
    helper.addInline("graffitiTrackerLogo", logo);
    return message;
  }
}
