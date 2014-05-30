package net.ccaper.GraffitiTracker.utils;

import static org.junit.Assert.*;
import net.ccaper.GraffitiTracker.objects.UserForm;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EncodePasswordTest {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testEncodePassword_HappyPath() throws Exception {
    String password = "testPassword";
    assertFalse(password.equals(EncodePassword.encodePassword("testUsername", password)));
  }
  
  @Test(expected = IllegalStateException.class)
  public void testEncodePassword_UsernameNull() throws Exception {
    String password = "testPassword";
    EncodePassword.encodePassword(null, password);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testEncodePassword_PasswordNull() throws Exception {
    EncodePassword.encodePassword("testUsername", null);
  }
}
