package net.ccaper.GraffitiTracker.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import net.ccaper.GraffitiTracker.service.MailService;

@Service
public class MailServiceMailSenderImpl implements MailService {
  @Autowired
  private MailSender mailSender;

  public void setMailSender(MailSender mailSender) {
    this.mailSender = mailSender;
  }

  @Override
  public void sendEmail(List<String> recipients, String subject, String content) {
    mailSender.send(getSimpleMailMessage(
        recipients.toArray(new String[recipients.size()]), subject, content));
  }

  SimpleMailMessage getSimpleMailMessage(String[] recipients, String subject,
      String content) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(recipients);
    message.setSubject(subject);
    message.setText(content);
    return message;
  }
}
