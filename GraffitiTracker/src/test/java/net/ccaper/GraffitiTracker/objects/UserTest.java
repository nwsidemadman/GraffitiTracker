package net.ccaper.GraffitiTracker.objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UserTest {
  User user;

  @Before
  public void setUp() throws Exception {
    user = new User();
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testEncodePassword() throws Exception {
    String password = "testPassword";
    user.setUsername("testUsername");
    user.setPassword(password);
    user.setConfirmPassword(password);
    user.encodePassword();
    assertFalse(password.equals(user.getPassword()));
    assertNull(user.getPassword());
    assertNull(user.getConfirmPassword());
  }

  @Test(expected = IllegalStateException.class)
  public void testEncodePassword_UsernameNull() throws Exception {
    user.setPassword("testPassword");
    user.encodePassword();
  }

  @Test(expected = IllegalStateException.class)
  public void testEncodePassword_PasswordNull() throws Exception {
    user.setUsername("testUsername");
    user.encodePassword();
  }

  @Test
  public void testSetPasswordEncoded() throws Exception {
    String password = "testPassword";
    user.setPasswordEncoded(password);
    assertEquals(password, user.getPasswordEncoded());
  }
}
