package net.ccaper.GraffitiTracker.service;

import java.util.List;

public interface MailService {
  void sendEmail(List<String> recipients, String subject, String content);
}
