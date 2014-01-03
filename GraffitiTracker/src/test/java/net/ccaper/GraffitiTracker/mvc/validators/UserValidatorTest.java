package net.ccaper.GraffitiTracker.mvc.validators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import net.ccaper.GraffitiTracker.objects.User;
import net.ccaper.GraffitiTracker.service.UserService;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

public class UserValidatorTest {
  UserValidator userValidator = new UserValidator();

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testValidateAcceptTermsHappyPath() {
    User userValidAcceptTerms = new User();
    userValidAcceptTerms.setAcceptTerms(true);
    Errors errors = new BeanPropertyBindingResult(userValidAcceptTerms, "userValidAcceptTerms");
    userValidator.validateAcceptTerms(errors, userValidAcceptTerms.getAcceptTerms());
    assertFalse(errors.hasErrors());
  }

  @Test
  public void testValidateAcceptTermsSadPath() {
    User userInvalidAcceptTerms = new User();
    userInvalidAcceptTerms.setAcceptTerms(false);
    Errors errors = new BeanPropertyBindingResult(userInvalidAcceptTerms, "userInvalidAcceptTerms");
    userValidator.validateAcceptTerms(errors, userInvalidAcceptTerms.getAcceptTerms());
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("acceptTerms"));
  }

  @Test
  public void testValidateEmail_Empty() {
    User userInvalidEmail = new User();
    userInvalidEmail.setEmail(StringUtils.EMPTY);
    Errors errors = new BeanPropertyBindingResult(userInvalidEmail, "userInvalidEmail");
    userValidator.validateEmail(errors, userInvalidEmail.getEmail(), userInvalidEmail.getAcceptTerms());
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("email"));
  }

  @Test
  public void testValidateEmail_Null() {
    User userInvalidEmail = new User();
    userInvalidEmail.setEmail(null);
    Errors errors = new BeanPropertyBindingResult(userInvalidEmail, "userInvalidEmail");
    userValidator.validateEmail(errors, userInvalidEmail.getEmail(), userInvalidEmail.getAcceptTerms());
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("email"));
  }

  @Test
  public void testValidateEmail_tooLong() {
    User userInvalidEmail = new User();
    userInvalidEmail.setEmail("12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901");
    Errors errors = new BeanPropertyBindingResult(userInvalidEmail, "userInvalidEmail");
    userValidator.validateEmail(errors, userInvalidEmail.getEmail(), userInvalidEmail.getAcceptTerms());
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("email"));
  }

  @Test
  public void testValidateEmail_invalidAddress() {
    User userInvalidEmail = new User();
    userInvalidEmail.setEmail("test@test");
    Errors errors = new BeanPropertyBindingResult(userInvalidEmail, "userInvalidEmail");
    userValidator.validateEmail(errors, userInvalidEmail.getEmail(), userInvalidEmail.getAcceptTerms());
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("email"));
  }

  @Test
  public void testValidateEmail_acceptTermsFalse() {
    User userInvalidEmail = new User();
    userInvalidEmail.setEmail("test@test.com");
    Errors errors = new BeanPropertyBindingResult(userInvalidEmail, "userInvalidEmail");
    userValidator.validateEmail(errors, userInvalidEmail.getEmail(), userInvalidEmail.getAcceptTerms());
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("email"));
  }

  @Test
  public void testValidateEmail_EmailAlreadyExists() {
    UserService userServiceMock = mock(UserService.class);
    User userInvalidEmail = new User();
    userInvalidEmail.setEmail("test@test.com");
    userInvalidEmail.setAcceptTerms(true);
    when(userServiceMock.doesEmailExist(userInvalidEmail.getEmail())).thenReturn(true);
    userValidator.setUserService(userServiceMock);
    Errors errors = new BeanPropertyBindingResult(userInvalidEmail, "userInvalidEmail");
    userValidator.validateEmail(errors, userInvalidEmail.getEmail(), userInvalidEmail.getAcceptTerms());
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("email"));
    verify(userServiceMock).doesEmailExist(userInvalidEmail.getEmail());
  }

  @Test
  public void testValidateEmail_HappyPath() {
    UserService userServiceMock = mock(UserService.class);
    User userValidEmail = new User();
    userValidEmail.setEmail("test@test.com");
    userValidEmail.setAcceptTerms(true);
    when(userServiceMock.doesEmailExist(userValidEmail.getEmail())).thenReturn(false);
    userValidator.setUserService(userServiceMock);
    Errors errors = new BeanPropertyBindingResult(userValidEmail, "userValidEmail");
    userValidator.validateEmail(errors, userValidEmail.getEmail(), userValidEmail.getAcceptTerms());
    assertFalse(errors.hasErrors());
    verify(userServiceMock).doesEmailExist(userValidEmail.getEmail());
  }
}
