package net.ccaper.GraffitiTracker.objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

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
  public void testValidateEmailHappyPath() throws Exception {
    String email = "test@test.com";
    user.setEmail(email);
    assertEquals(email, user.getEmail());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidateEmailSadPath() throws Exception {
    user.setEmail("test@test");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidateEmailLocal() throws Exception {
    user.setEmail("test@localhost");
  }

  @Test
  public void testSetUsernameHappyPath() throws Exception {
    String username = "testUsername";
    user.setUsername(username);
    assertEquals(username, user.getUsername());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetUsernameTooShort() throws Exception {
    user.setUsername("1");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetUsernameTooLong() throws Exception {
    user.setUsername("123456789012345678901");
  }

  @Test
  public void testSetPasswordEncodeClearText() throws Exception {
    String password = "testPassword";
    user.setUsername("testUsername");
    user.setPassword(password);
    user.encodePassword();
    assertFalse(password.equals(user.getPassword()));
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

  @Test(expected = IllegalArgumentException.class)
  public void testSetPasswordEncodeClearTextTooShort() throws Exception {
    user.setPassword("1");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetPasswordEncodeClearTextTooLong() throws Exception {
    user.setPassword(
        "12345678901234567890123456789012345678901234567890123456789012345");
  }
  
  @Test
  public void testSetPasswordEncoded() throws Exception {
    String password = "testPassword";
    user.setPasswordEncoded(password);
    assertEquals(password, user.getPassword());
  }
}
