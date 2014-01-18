package net.ccaper.GraffitiTracker.spring;

import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:/net/ccaper/GraffitiTracker/spring/spring-config-test.xml" })
public class AppConfigTest {
  private ApplicationContext context;

  @Before
  public void setUp() throws Exception {
    context = new AnnotationConfigApplicationContext(AppConfig.class);
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testWiring() {
    assertNotNull(context.getBean("dataSource"));
    assertNotNull(context.getBean("captchaKey"));
    assertNotNull(context.getBean("maxNumberCaptchaFetchRetries"));
    assertNotNull(context.getBean("mailSender"));
  }
}
