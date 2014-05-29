package net.ccaper.GraffitiTracker.mvc.validators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import net.ccaper.GraffitiTracker.objects.UserForm;
import net.ccaper.GraffitiTracker.service.AppUserService;

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
  
  @Test
  public void testValidateEmail_Empty() throws Exception {
    UserForm userFormInvalidEmail = new UserForm();
    userFormInvalidEmail.setEmail(StringUtils.EMPTY);
    Errors errors = new BeanPropertyBindingResult(userFormInvalidEmail,
        "userInvalidEmail");
    CommonValidator.validateEmail(errors, userFormInvalidEmail.getEmail(), null);
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("email"));
  }

  @Test
  public void testValidateEmail_Null() throws Exception {
    UserForm userFormInvalidEmail = new UserForm();
    userFormInvalidEmail.setEmail(null);
    Errors errors = new BeanPropertyBindingResult(userFormInvalidEmail,
        "userInvalidEmail");
    CommonValidator.validateEmail(errors, userFormInvalidEmail.getEmail(), null);
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("email"));
  }

  @Test
  public void testValidateEmail_TooLong() throws Exception {
    UserForm userFormInvalidEmail = new UserForm();
    userFormInvalidEmail
    .setEmail("12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901");
    Errors errors = new BeanPropertyBindingResult(userFormInvalidEmail,
        "userInvalidEmail");
    CommonValidator.validateEmail(errors, userFormInvalidEmail.getEmail(), null);
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("email"));
  }

  @Test
  public void testValidateEmail_InvalidAddress() throws Exception {
    UserForm userFormInvalidEmail = new UserForm();
    userFormInvalidEmail.setEmail("test@test");
    Errors errors = new BeanPropertyBindingResult(userFormInvalidEmail,
        "userInvalidEmail");
    CommonValidator.validateEmail(errors, userFormInvalidEmail.getEmail(), null);
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("email"));
  }

  @Test
  public void testValidateEmail_EmailAlreadyExists() throws Exception {
    AppUserService appUserServiceMock = mock(AppUserService.class);
    UserForm userFormInvalidEmail = new UserForm();
    userFormInvalidEmail.setEmail("test@test.com");
    userFormInvalidEmail.setAcceptTerms(true);
    when(appUserServiceMock.doesEmailExist(userFormInvalidEmail.getEmail()))
    .thenReturn(true);
    Errors errors = new BeanPropertyBindingResult(userFormInvalidEmail,
        "userInvalidEmail");
    CommonValidator.validateEmail(errors, userFormInvalidEmail.getEmail(), appUserServiceMock);
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("email"));
    verify(appUserServiceMock).doesEmailExist(userFormInvalidEmail.getEmail());
  }

  @Test
  public void testValidateEmail_HappyPath() throws Exception {
    AppUserService appUserServiceMock = mock(AppUserService.class);
    UserForm userFormValidEmail = new UserForm();
    userFormValidEmail.setEmail("test@test.com");
    userFormValidEmail.setAcceptTerms(true);
    when(appUserServiceMock.doesEmailExist(userFormValidEmail.getEmail())).thenReturn(
        false);
    Errors errors = new BeanPropertyBindingResult(userFormValidEmail,
        "userValidEmail");
    CommonValidator.validateEmail(errors, userFormValidEmail.getEmail(), appUserServiceMock);
    assertFalse(errors.hasErrors());
    verify(appUserServiceMock).doesEmailExist(userFormValidEmail.getEmail());
  }
  
  @Test
  public void testValidateSecurityAnswer_Empty() throws Exception {
    UserForm userFormInvalidSecurityAnswer = new UserForm();
    userFormInvalidSecurityAnswer.setSecurityAnswer(StringUtils.EMPTY);
    Errors errors = new BeanPropertyBindingResult(userFormInvalidSecurityAnswer,
        "userInvalidSecurityAnswer");
    CommonValidator.validateSecurityAnswer(errors, userFormInvalidSecurityAnswer.getSecurityAnswer());
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("securityAnswer"));
  }

  @Test
  public void testValidateSecurityAnswer_Null() throws Exception {
    UserForm userFormInvalidSecurityAnswer = new UserForm();
    userFormInvalidSecurityAnswer.setSecurityAnswer(null);
    Errors errors = new BeanPropertyBindingResult(userFormInvalidSecurityAnswer,
        "userInvalidSecurityAnswer");
    CommonValidator.validateSecurityAnswer(errors, userFormInvalidSecurityAnswer.getSecurityAnswer());
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("securityAnswer"));
  }

  @Test
  public void testValidateSecurityAnswer_TooLong() throws Exception {
    UserForm userFormInvalidSecurityAnswer = new UserForm();
    userFormInvalidSecurityAnswer.setSecurityAnswer("12345678901234567890123456789012345678901");
    Errors errors = new BeanPropertyBindingResult(userFormInvalidSecurityAnswer,
        "userInvalidSecurityAnswer");
    CommonValidator.validateSecurityAnswer(errors, userFormInvalidSecurityAnswer.getSecurityAnswer());
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("securityAnswer"));
  }

  @Test
  public void testValidateSecurityAnswer_HappyPath() throws Exception {
    UserForm userFormInvalidSecurityAnswer = new UserForm();
    userFormInvalidSecurityAnswer.setSecurityAnswer("testAnswer");
    Errors errors = new BeanPropertyBindingResult(userFormInvalidSecurityAnswer,
        "userInvalidSecurityAnswer");
    CommonValidator.validateSecurityAnswer(errors, userFormInvalidSecurityAnswer.getSecurityAnswer());
    assertFalse(errors.hasErrors());
  }
}
