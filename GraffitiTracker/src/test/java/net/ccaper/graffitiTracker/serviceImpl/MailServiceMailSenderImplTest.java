package net.ccaper.graffitiTracker.serviceImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import net.ccaper.graffitiTracker.serviceImpl.MailServiceJavaMailSenderImpl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mail.SimpleMailMessage;

public class MailServiceMailSenderImplTest {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testGetSimpleMailMessage() throws Exception {
    MailServiceJavaMailSenderImpl mailService = new MailServiceJavaMailSenderImpl();
    String[] recipients = new String[]{"test@test.com"};
    String subject = "testSubject";
    String content = "testContent";
    SimpleMailMessage simpleMailMessage = mailService.getSimpleMailMessage(recipients, subject, content);
    assertTrue(Arrays.equals(recipients, simpleMailMessage.getTo()));
    assertEquals(subject, simpleMailMessage.getSubject());
    assertEquals(content, simpleMailMessage.getText());
  }
}
