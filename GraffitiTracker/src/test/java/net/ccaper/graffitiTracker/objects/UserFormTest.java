package net.ccaper.graffitiTracker.objects;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import net.ccaper.graffitiTracker.objects.AppUser;
import net.ccaper.graffitiTracker.objects.UserForm;

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
