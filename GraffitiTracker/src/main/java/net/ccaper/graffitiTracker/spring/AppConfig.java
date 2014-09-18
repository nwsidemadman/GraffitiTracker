package net.ccaper.graffitiTracker.spring;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * 
 * @author ccaper
 * 
 *         Java config for the Spring framework
 * 
 */
@Configuration
// only needed for temp running of loader, remove when sorted, and removde spring congfig xml
//@ImportResource("classpath:/net/ccaper/graffitiTracker/${CLASSPATH_PROP_ENV:local}/GraffitiTracker-config.xml")
@PropertySource("classpath:/net/ccaper/graffitiTracker/${CLASSPATH_PROP_ENV:local}/GraffitiTracker.properties")
public class AppConfig {
  @Autowired
  private Environment properties;

  /**
   * Data source.
   * 
   * @return the data source
   */
  @Bean(destroyMethod = "close")
  public DataSource dataSource() {
    BasicDataSource dataSource = new BasicDataSource();
    dataSource.setDriverClassName(properties.getProperty("db.driverClassName"));
    dataSource.setUrl(properties.getProperty("db.url"));
    dataSource.setUsername(properties.getProperty("db.user"));
    dataSource.setPassword(properties.getProperty("db.password"));
    dataSource.setInitialSize(Integer.parseInt(properties
        .getProperty("db.pool.initialSize")));
    dataSource.setMaxActive(Integer.parseInt(properties
        .getProperty("db.pool.maxActive")));
    dataSource.setValidationQuery("SELECT 1");
    return dataSource;
  }

  /**
   * Captcha key.
   * 
   * @return the string
   */
  @Bean(name = "captchaKey")
  public String captchaKey() {
    return properties.getProperty("captcha.key");
  }

  /**
   * Max number captcha fetch retries.
   * 
   * @return the integer
   */
  @Bean(name = "maxNumberCaptchaFetchRetries")
  public Integer maxNumberCaptchaFetchRetries() {
    return Integer.parseInt(properties
        .getProperty("captcha.maxNumberFetchRetries"));
  }

  /**
   * Mail sender.
   * 
   * @return the java mail sender
   */
  @Bean(name = "mailSender")
  public JavaMailSender mailSender() {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost(properties.getProperty("mailserver.host"));
    mailSender.setPort(Integer.parseInt(properties
        .getProperty("mailserver.port")));
    mailSender.setUsername(properties.getProperty("mailserver.username"));
    mailSender.setPassword(properties.getProperty("mailserver.password"));
    Properties javaMailProperties = new Properties();
    javaMailProperties.setProperty("mail.smtp.auth", "true");
    javaMailProperties.setProperty("mail.smtp.starttls.enable", "true");
    javaMailProperties.setProperty("mail.smtp.quitwait", "false");
    javaMailProperties.setProperty("mail.smtp.starttls.enable", "true");
    mailSender.setJavaMailProperties(javaMailProperties);
    return mailSender;
  }
}
