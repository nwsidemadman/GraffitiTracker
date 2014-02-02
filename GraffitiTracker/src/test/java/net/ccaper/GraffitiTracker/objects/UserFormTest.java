package net.ccaper.GraffitiTracker.objects;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UserFormTest {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testEncodePassword_HappyPath() throws Exception {
    UserForm userForm = new UserForm();
    userForm.setUsername("testUsername");
    userForm.setPassword("testPassword");
    assertFalse(userForm.getPassword().equals(userForm.encodePassword()));
  }
  
  @Test(expected = IllegalStateException.class)
  public void testEncodePassword_UsernameNull() throws Exception {
    UserForm userForm = new UserForm();
    userForm.setPassword("testPassword");
    userForm.getPassword().equals(userForm.encodePassword());
  }
  
  @Test(expected = IllegalStateException.class)
  public void testEncodePassword_PasswordNull() throws Exception {
    UserForm userForm = new UserForm();
    userForm.setUsername("testUsername");
    userForm.getPassword().equals(userForm.encodePassword());
  }
  
  @Test
  public void testCreateUserFromUserForm() throws Exception {
    UserForm userForm = new UserForm();
    userForm.setUsername("testUsername");
    userForm.setPassword("testPassword");
    userForm.setEmail("test@test.com");
    userForm.setCaptchaAnswer("testAnswer");
    userForm.setSecurityQuestion("testQuestion");
    AppUser appUser = userForm.createAppUserFromUserForm();
    assertEquals(userForm.getUsername(), appUser.getUsername());
    assertEquals(userForm.getEmail(), appUser.getEmail());
    assertFalse(userForm.getPassword().equals(appUser.getPassword()));
    assertEquals(userForm.getSecurityAnswer(), appUser.getSecurityAnswer());
    assertEquals(userForm.getSecurityQuestion(), appUser.getSecurityQuestion());
  }
}
