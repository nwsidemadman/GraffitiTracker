package net.ccaper.GraffitiTracker.mvc.validators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import net.ccaper.GraffitiTracker.objects.UsernameForm;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

public class FormUsernameValidatorTest {
  FormUsernameValidator formUsernameValidator = new FormUsernameValidator();

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testValidate_Empty() throws Exception {
    UsernameForm usernameFormInvalidUsername = new UsernameForm();
    usernameFormInvalidUsername.setUsername(StringUtils.EMPTY);
    Errors errors = new BeanPropertyBindingResult(usernameFormInvalidUsername,
        "usernameInvalidUsername");
    formUsernameValidator.validate(usernameFormInvalidUsername, errors);
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("username"));
  }

  @Test
  public void testValidate_Null() throws Exception {
    UsernameForm usernameFormInvalidUsername = new UsernameForm();
    usernameFormInvalidUsername.setUsername(null);
    Errors errors = new BeanPropertyBindingResult(usernameFormInvalidUsername,
        "usernameInvalidUsername");
    formUsernameValidator.validate(usernameFormInvalidUsername, errors);
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("username"));
  }

  @Test
  public void testValidate_HappyPath() throws Exception {
    UsernameForm usernameFormInvalidUsername = new UsernameForm();
    usernameFormInvalidUsername.setUsername("testUser");
    Errors errors = new BeanPropertyBindingResult(usernameFormInvalidUsername,
        "usernameInvalidUsername");
    formUsernameValidator.validate(usernameFormInvalidUsername, errors);
    assertFalse(errors.hasErrors());
  }
}
