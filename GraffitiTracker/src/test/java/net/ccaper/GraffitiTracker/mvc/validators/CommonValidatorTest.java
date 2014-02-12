package net.ccaper.GraffitiTracker.mvc.validators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import net.ccaper.GraffitiTracker.objects.UserForm;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

public class CommonValidatorTest {
  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testValidatePassword_Empty() throws Exception {
    UserForm userFormInvalidPassword = new UserForm();
    userFormInvalidPassword.setPassword(StringUtils.EMPTY);
    userFormInvalidPassword.setConfirmPassword(StringUtils.EMPTY);
    Errors errors = new BeanPropertyBindingResult(userFormInvalidPassword,
        "userInvalidPassword");
    CommonValidator.validatePassword(errors,
        userFormInvalidPassword.getPassword(),
        userFormInvalidPassword.getConfirmPassword());
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("password"));
    assertNotNull(errors.getFieldError("confirmPassword"));
  }

  @Test
  public void testValidatePassword_Null() throws Exception {
    UserForm userFormInvalidPassword = new UserForm();
    userFormInvalidPassword.setPassword(null);
    userFormInvalidPassword.setConfirmPassword(null);
    Errors errors = new BeanPropertyBindingResult(userFormInvalidPassword,
        "userInvalidPassword");
    CommonValidator.validatePassword(errors,
        userFormInvalidPassword.getPassword(),
        userFormInvalidPassword.getConfirmPassword());
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("password"));
    assertNotNull(errors.getFieldError("confirmPassword"));
  }

  @Test
  public void testValidatePassword_TooShort() throws Exception {
    UserForm userFormInvalidPassword = new UserForm();
    userFormInvalidPassword.setPassword("123");
    userFormInvalidPassword.setConfirmPassword("somePassword");
    Errors errors = new BeanPropertyBindingResult(userFormInvalidPassword,
        "userInvalidPassword");
    CommonValidator.validatePassword(errors,
        userFormInvalidPassword.getPassword(),
        userFormInvalidPassword.getConfirmPassword());
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("password"));
  }

  @Test
  public void testValidatePassword_TooLong() throws Exception {
    UserForm userFormInvalidPassword = new UserForm();
    userFormInvalidPassword
    .setPassword("12345678901234567890123456789012345678901234567890123456789012345");
    userFormInvalidPassword.setConfirmPassword("somePassword");
    Errors errors = new BeanPropertyBindingResult(userFormInvalidPassword,
        "userInvalidPassword");
    CommonValidator.validatePassword(errors,
        userFormInvalidPassword.getPassword(),
        userFormInvalidPassword.getConfirmPassword());
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("password"));
  }

  @Test
  public void testValidatePassword_ConfirmPasswordNoMatch() throws Exception {
    UserForm userFormInvalidPassword = new UserForm();
    userFormInvalidPassword.setPassword("SomeValidPassword");
    userFormInvalidPassword.setConfirmPassword("SomeValidPassword2");
    Errors errors = new BeanPropertyBindingResult(userFormInvalidPassword,
        "userInvalidPassword");
    CommonValidator.validatePassword(errors,
        userFormInvalidPassword.getPassword(),
        userFormInvalidPassword.getConfirmPassword());
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("password"));
    assertNotNull(errors.getFieldError("confirmPassword"));
  }

  @Test
  public void testValidatePassword_HappyPath() throws Exception {
    UserForm userFormValidPassword = new UserForm();
    userFormValidPassword.setPassword("SomeValidPassword");
    userFormValidPassword.setConfirmPassword("SomeValidPassword");
    Errors errors = new BeanPropertyBindingResult(userFormValidPassword,
        "userValidPassword");
    CommonValidator.validatePassword(errors,
        userFormValidPassword.getPassword(),
        userFormValidPassword.getConfirmPassword());
    assertFalse(errors.hasErrors());
  }
}
