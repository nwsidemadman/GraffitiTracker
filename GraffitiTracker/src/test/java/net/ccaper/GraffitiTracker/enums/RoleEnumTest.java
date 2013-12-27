package net.ccaper.GraffitiTracker.enums;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RoleEnumTest {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testDbStringHappyPath() throws Exception {
    for (RoleEnum roleEnum : RoleEnum.values()) {
      assertEquals(roleEnum, RoleEnum.getRoleEnumFromDbString(roleEnum.getDbString()));
    }
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testDbStringSadPath() throws Exception {
    RoleEnum.getRoleEnumFromDbString("test");
  }
}
