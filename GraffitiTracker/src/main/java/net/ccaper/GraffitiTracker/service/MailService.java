package net.ccaper.GraffitiTracker.service;

import java.util.List;

public interface MailService {
  void sendSimpleEmail(List<String> recipients, String subject, String content);

  void sendRichEmail(List<String> recipients, String subject, String content);
}
