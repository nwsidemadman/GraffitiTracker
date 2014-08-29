package net.ccaper.graffitiTracker.enums;

import static org.junit.Assert.assertEquals;
import net.ccaper.graffitiTracker.enums.EnvironmentEnum;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EnvironmentEnumTest {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testGetEnvironmentEnumFromEnvironmentPropertyString_HappyPath() throws Exception{
    for (EnvironmentEnum environmentEnum : EnvironmentEnum.values()) {
      assertEquals(environmentEnum.getDisplayString(),
          EnvironmentEnum
          .getEnvironmentEnumFromEnvironmentPropertyString(environmentEnum
              .getEnvironmentPropertyString()).getDisplayString());
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetEnvironmentEnumFromEnvironmentPropertyString_SadPath() throws Exception{
    EnvironmentEnum
    .getEnvironmentEnumFromEnvironmentPropertyString("test");
  }
}
