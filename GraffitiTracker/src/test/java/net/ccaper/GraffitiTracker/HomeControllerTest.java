package net.ccaper.GraffitiTracker;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HomeControllerTest {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void returnsCorrectView() {
    HomeController controller = new HomeController();
    assertEquals("home", controller.showHomePage());
  }
}
