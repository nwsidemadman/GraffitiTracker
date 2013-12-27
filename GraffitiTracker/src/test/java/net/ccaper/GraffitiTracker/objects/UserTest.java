package net.ccaper.GraffitiTracker.objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

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
    String email = "name@domain.com";
    user.setEmail(email);
    assertEquals(email, user.getEmail());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidateEmailSadPath() throws Exception {
    user.setEmail("blah@blah");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidateEmailLocal() throws Exception {
    user.setEmail("blah@localhost");
  }

  @Test
  public void testSetUsernameHappyPath() throws Exception {
    String username = "someUser";
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
    user.setPasswordEncoder(new ShaPasswordEncoder(256));
    user.setPasswordEncodeClearText(password, "testUserName");
    assertFalse(password.equals(user.getPassword()));
  }

  @Test(expected = IllegalStateException.class)
  public void testSetPasswordEncodeClearText_EncoderNull() throws Exception {
    user.setPasswordEncodeClearText("testPassword", "testUserName");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetPasswordEncodeClearTextTooShort() throws Exception {
    user.setPasswordEncodeClearText("1", "testUserName");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetPasswordEncodeClearTextTooLong() throws Exception {
    user.setPasswordEncodeClearText(
        "12345678901234567890123456789012345678901234567890123456789012345",
        "testUserName");
  }
}
