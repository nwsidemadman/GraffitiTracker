package net.ccaper.graffitiTracker.service;

import java.util.List;

public interface MailService {
  void sendSimpleEmail(List<String> recipients, String subject, String content);

  void sendVelocityEmail(List<String> recipients, String subject, String content);
}
