package net.ccaper.graffitiTracker.enums;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ChicagoCityServiceStatusEnumTest {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testDbString_happyPath() throws Exception {
    for (ChicagoCityServiceStatusEnum chicagoCityServiceStatusEnum : ChicagoCityServiceStatusEnum
        .values()) {
      assertEquals(
          chicagoCityServiceStatusEnum,
          ChicagoCityServiceStatusEnum
              .getChicagoCityServiceStatusEnumFromDbOrServerString(chicagoCityServiceStatusEnum
                  .getDbString()));
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDbString_sadPath() throws Exception {
    ChicagoCityServiceStatusEnum
        .getChicagoCityServiceStatusEnumFromDbOrServerString("test");
  }
}
