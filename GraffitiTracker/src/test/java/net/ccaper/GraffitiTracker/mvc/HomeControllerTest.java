package net.ccaper.GraffitiTracker.mvc;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import net.ccaper.GraffitiTracker.mvc.HomeController;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class HomeControllerTest {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  @Ignore
  public void returnsCorrectView() {
    // TODO: fix test, broken
    HomeController controller = new HomeController();
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("home", controller.showHomePage(model));
  }
}
