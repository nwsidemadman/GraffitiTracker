package net.ccaper.GraffitiTracker.mvc.validators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import net.ccaper.GraffitiTracker.objects.User;
import net.ccaper.GraffitiTracker.service.BannedWordService;
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
  public void testValidateAcceptTermsHappyPath() throws Exception {
    User userValidAcceptTerms = new User();
    userValidAcceptTerms.setAcceptTerms(true);
    Errors errors = new BeanPropertyBindingResult(userValidAcceptTerms,
        "userValidAcceptTerms");
    userValidator.validateAcceptTerms(errors,
        userValidAcceptTerms.getAcceptTerms());
    assertFalse(errors.hasErrors());
  }

  @Test
  public void testValidateAcceptTermsSadPath() throws Exception {
    User userInvalidAcceptTerms = new User();
    userInvalidAcceptTerms.setAcceptTerms(false);
    Errors errors = new BeanPropertyBindingResult(userInvalidAcceptTerms,
        "userInvalidAcceptTerms");
    userValidator.validateAcceptTerms(errors,
        userInvalidAcceptTerms.getAcceptTerms());
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("acceptTerms"));
  }

  @Test
  public void testValidateEmail_Empty() throws Exception {
    User userInvalidEmail = new User();
    userInvalidEmail.setEmail(StringUtils.EMPTY);
    Errors errors = new BeanPropertyBindingResult(userInvalidEmail,
        "userInvalidEmail");
    userValidator.validateEmail(errors, userInvalidEmail.getEmail(),
        userInvalidEmail.getAcceptTerms());
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("email"));
  }

  @Test
  public void testValidateEmail_Null() throws Exception {
    User userInvalidEmail = new User();
    userInvalidEmail.setEmail(null);
    Errors errors = new BeanPropertyBindingResult(userInvalidEmail,
        "userInvalidEmail");
    userValidator.validateEmail(errors, userInvalidEmail.getEmail(),
        userInvalidEmail.getAcceptTerms());
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("email"));
  }

  @Test
  public void testValidateEmail_TooLong() throws Exception {
    User userInvalidEmail = new User();
    userInvalidEmail
    .setEmail("12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901");
    Errors errors = new BeanPropertyBindingResult(userInvalidEmail,
        "userInvalidEmail");
    userValidator.validateEmail(errors, userInvalidEmail.getEmail(),
        userInvalidEmail.getAcceptTerms());
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("email"));
  }

  @Test
  public void testValidateEmail_InvalidAddress() throws Exception {
    User userInvalidEmail = new User();
    userInvalidEmail.setEmail("test@test");
    Errors errors = new BeanPropertyBindingResult(userInvalidEmail,
        "userInvalidEmail");
    userValidator.validateEmail(errors, userInvalidEmail.getEmail(),
        userInvalidEmail.getAcceptTerms());
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("email"));
  }

  @Test
  public void testValidateEmail_AcceptTermsFalse() throws Exception {
    User userInvalidEmail = new User();
    userInvalidEmail.setEmail("test@test.com");
    Errors errors = new BeanPropertyBindingResult(userInvalidEmail,
        "userInvalidEmail");
    userValidator.validateEmail(errors, userInvalidEmail.getEmail(),
        userInvalidEmail.getAcceptTerms());
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("email"));
  }

  @Test
  public void testValidateEmail_EmailAlreadyExists() throws Exception {
    UserService userServiceMock = mock(UserService.class);
    User userInvalidEmail = new User();
    userInvalidEmail.setEmail("test@test.com");
    userInvalidEmail.setAcceptTerms(true);
    when(userServiceMock.doesEmailExist(userInvalidEmail.getEmail()))
    .thenReturn(true);
    userValidator.setUserService(userServiceMock);
    Errors errors = new BeanPropertyBindingResult(userInvalidEmail,
        "userInvalidEmail");
    userValidator.validateEmail(errors, userInvalidEmail.getEmail(),
        userInvalidEmail.getAcceptTerms());
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("email"));
    verify(userServiceMock).doesEmailExist(userInvalidEmail.getEmail());
  }

  @Test
  public void testValidateEmail_HappyPath() throws Exception {
    UserService userServiceMock = mock(UserService.class);
    User userValidEmail = new User();
    userValidEmail.setEmail("test@test.com");
    userValidEmail.setAcceptTerms(true);
    when(userServiceMock.doesEmailExist(userValidEmail.getEmail())).thenReturn(
        false);
    userValidator.setUserService(userServiceMock);
    Errors errors = new BeanPropertyBindingResult(userValidEmail,
        "userValidEmail");
    userValidator.validateEmail(errors, userValidEmail.getEmail(),
        userValidEmail.getAcceptTerms());
    assertFalse(errors.hasErrors());
    verify(userServiceMock).doesEmailExist(userValidEmail.getEmail());
  }

  @Test
  public void testValidatePassword_Empty() throws Exception {
    User userInvalidPassword = new User();
    userInvalidPassword.setPassword(StringUtils.EMPTY);
    userInvalidPassword.setConfirmPassword(StringUtils.EMPTY);
    Errors errors = new BeanPropertyBindingResult(userInvalidPassword,
        "userInvalidPassword");
    userValidator.validatePassword(errors, userInvalidPassword.getPassword(),
        userInvalidPassword.getConfirmPassword());
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("password"));
    assertNotNull(errors.getFieldError("confirmPassword"));
  }

  @Test
  public void testValidatePassword_Null() throws Exception {
    User userInvalidPassword = new User();
    userInvalidPassword.setPassword(null);
    userInvalidPassword.setConfirmPassword(null);
    Errors errors = new BeanPropertyBindingResult(userInvalidPassword,
        "userInvalidPassword");
    userValidator.validatePassword(errors, userInvalidPassword.getPassword(),
        userInvalidPassword.getConfirmPassword());
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("password"));
    assertNotNull(errors.getFieldError("confirmPassword"));
  }

  @Test
  public void testValidatePassword_TooShort() throws Exception {
    User userInvalidPassword = new User();
    userInvalidPassword.setPassword("123");
    userInvalidPassword.setConfirmPassword("somePassword");
    Errors errors = new BeanPropertyBindingResult(userInvalidPassword,
        "userInvalidPassword");
    userValidator.validatePassword(errors, userInvalidPassword.getPassword(),
        userInvalidPassword.getConfirmPassword());
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("password"));
  }

  @Test
  public void testValidatePassword_TooLong() throws Exception {
    User userInvalidPassword = new User();
    userInvalidPassword
    .setPassword("12345678901234567890123456789012345678901234567890123456789012345");
    userInvalidPassword.setConfirmPassword("somePassword");
    Errors errors = new BeanPropertyBindingResult(userInvalidPassword,
        "userInvalidPassword");
    userValidator.validatePassword(errors, userInvalidPassword.getPassword(),
        userInvalidPassword.getConfirmPassword());
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("password"));
  }

  @Test
  public void testValidatePassword_ConfirmPasswordNoMatch() throws Exception {
    User userInvalidPassword = new User();
    userInvalidPassword.setPassword("SomeValidPassword");
    userInvalidPassword.setConfirmPassword("SomeValidPassword2");
    Errors errors = new BeanPropertyBindingResult(userInvalidPassword,
        "userInvalidPassword");
    userValidator.validatePassword(errors, userInvalidPassword.getPassword(),
        userInvalidPassword.getConfirmPassword());
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("password"));
    assertNotNull(errors.getFieldError("confirmPassword"));
  }

  @Test
  public void testValidatePassword_HappyPath() throws Exception {
    User userValidPassword = new User();
    userValidPassword.setPassword("SomeValidPassword");
    userValidPassword.setConfirmPassword("SomeValidPassword");
    Errors errors = new BeanPropertyBindingResult(userValidPassword,
        "userValidPassword");
    userValidator.validatePassword(errors, userValidPassword.getPassword(),
        userValidPassword.getConfirmPassword());
    assertFalse(errors.hasErrors());
  }

  @Test
  public void testValidateUsername_Empty() throws Exception {
    User userInvalidUsername = new User();
    userInvalidUsername.setUsername(StringUtils.EMPTY);
    Errors errors = new BeanPropertyBindingResult(userInvalidUsername,
        "userInvalidUsername");
    userValidator.validateUsername(errors, userInvalidUsername.getUsername(),
        userInvalidUsername.getAcceptTerms());
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("username"));
  }

  @Test
  public void testValidateUsername_Null() throws Exception {
    User userInvalidUsername = new User();
    userInvalidUsername.setUsername(null);
    Errors errors = new BeanPropertyBindingResult(userInvalidUsername,
        "userInvalidUsername");
    userValidator.validateUsername(errors, userInvalidUsername.getUsername(),
        userInvalidUsername.getAcceptTerms());
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("username"));
  }

  @Test
  public void testValidateUsername_TooShort() throws Exception {
    User userInvalidUsername = new User();
    userInvalidUsername.setUsername("123");
    Errors errors = new BeanPropertyBindingResult(userInvalidUsername,
        "userInvalidUsername");
    userValidator.validateUsername(errors, userInvalidUsername.getUsername(),
        userInvalidUsername.getAcceptTerms());
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("username"));
  }

  @Test
  public void testValidateUsername_TooLong() throws Exception {
    User userInvalidUsername = new User();
    userInvalidUsername.setUsername("123456789012345678901");
    Errors errors = new BeanPropertyBindingResult(userInvalidUsername,
        "userInvalidUsername");
    userValidator.validateUsername(errors, userInvalidUsername.getUsername(),
        userInvalidUsername.getAcceptTerms());
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("username"));
  }

  @Test
  public void testValidateUsername_ContainsWhitespace() throws Exception {
    User userInvalidUsername = new User();
    userInvalidUsername.setUsername("Some Username");
    Errors errors = new BeanPropertyBindingResult(userInvalidUsername,
        "userInvalidUsername");
    userValidator.validateUsername(errors, userInvalidUsername.getUsername(),
        userInvalidUsername.getAcceptTerms());
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("username"));
  }

  @Test
  public void testValidateUsername_NonAlphaNumeric() throws Exception {
    User userInvalidUsername = new User();
    userInvalidUsername.setUsername("Some$Username");
    Errors errors = new BeanPropertyBindingResult(userInvalidUsername,
        "userInvalidUsername");
    userValidator.validateUsername(errors, userInvalidUsername.getUsername(),
        userInvalidUsername.getAcceptTerms());
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("username"));
  }

  @Test
  public void testValidateUsername_AcceptTermsFalse() throws Exception {
    User userInvalidUsername = new User();
    userInvalidUsername.setUsername("ValidUsername");
    Errors errors = new BeanPropertyBindingResult(userInvalidUsername,
        "userInvalidUsername");
    userValidator.validateUsername(errors, userInvalidUsername.getUsername(),
        userInvalidUsername.getAcceptTerms());
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("username"));
  }

  @Test
  public void testValidateUsername_BannedWord() throws Exception {
    BannedWordService bannedWordServiceMock = mock(BannedWordService.class);
    User userInvalidUsername = new User();
    userInvalidUsername.setUsername("bannedWord");
    userInvalidUsername.setAcceptTerms(true);
    when(
        bannedWordServiceMock.doesStringContainBannedWord(userInvalidUsername
            .getUsername())).thenReturn(true);
    userValidator.setBannedWordService(bannedWordServiceMock);
    Errors errors = new BeanPropertyBindingResult(userInvalidUsername,
        "userInvalidUsername");
    userValidator.validateUsername(errors, userInvalidUsername.getUsername(),
        userInvalidUsername.getAcceptTerms());
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("username"));
    verify(bannedWordServiceMock).doesStringContainBannedWord(
        userInvalidUsername.getUsername());
  }

  @Test
  public void testValidateUsername_UsernameAlreadyExists() throws Exception {
    BannedWordService bannedWordServiceMock = mock(BannedWordService.class);
    UserService userServiceMock = mock(UserService.class);
    User userInvalidUsername = new User();
    userInvalidUsername.setUsername("alreadyExists");
    userInvalidUsername.setAcceptTerms(true);
    when(
        bannedWordServiceMock.doesStringContainBannedWord(userInvalidUsername
            .getUsername())).thenReturn(false);
    when(userServiceMock.doesUsernameExist(userInvalidUsername.getUsername()))
    .thenReturn(true);
    userValidator.setBannedWordService(bannedWordServiceMock);
    userValidator.setUserService(userServiceMock);
    Errors errors = new BeanPropertyBindingResult(userInvalidUsername,
        "userInvalidUsername");
    userValidator.validateUsername(errors, userInvalidUsername.getUsername(),
        userInvalidUsername.getAcceptTerms());
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("username"));
    verify(bannedWordServiceMock).doesStringContainBannedWord(
        userInvalidUsername.getUsername());
    verify(userServiceMock).doesUsernameExist(userInvalidUsername.getUsername());
  }

  @Test
  public void testValidateUsername_HappyPath() throws Exception {
    BannedWordService bannedWordServiceMock = mock(BannedWordService.class);
    UserService userServiceMock = mock(UserService.class);
    User userValidUsername = new User();
    userValidUsername.setUsername("validUsername");
    userValidUsername.setAcceptTerms(true);
    when(
        bannedWordServiceMock.doesStringContainBannedWord(userValidUsername
            .getUsername())).thenReturn(false);
    when(userServiceMock.doesUsernameExist(userValidUsername.getUsername()))
    .thenReturn(false);
    userValidator.setBannedWordService(bannedWordServiceMock);
    userValidator.setUserService(userServiceMock);
    Errors errors = new BeanPropertyBindingResult(userValidUsername,
        "userValidUsername");
    userValidator.validateUsername(errors, userValidUsername.getUsername(),
        userValidUsername.getAcceptTerms());
    assertFalse(errors.hasErrors());
    verify(bannedWordServiceMock).doesStringContainBannedWord(
        userValidUsername.getUsername());
    verify(userServiceMock).doesUsernameExist(userValidUsername.getUsername());
  }

  @Test
  public void testValidate_HappyPath() throws Exception {
    BannedWordService bannedWordServiceMock = mock(BannedWordService.class);
    UserService userServiceMock = mock(UserService.class);
    User userValid = new User();
    userValid.setUsername("validUsername");
    userValid.setPassword("validPassword");
    userValid.setConfirmPassword("validPassword");
    userValid.setEmail("test@test.com");
    userValid.setAcceptTerms(true);
    when(
        bannedWordServiceMock.doesStringContainBannedWord(userValid
            .getUsername())).thenReturn(false);
    when(userServiceMock.doesUsernameExist(userValid.getUsername()))
    .thenReturn(false);
    userValidator.setBannedWordService(bannedWordServiceMock);
    userValidator.setUserService(userServiceMock);
    Errors errors = new BeanPropertyBindingResult(userValid,
        "userValid");
    userValidator.validate(userValid, errors);
    assertFalse(errors.hasErrors());
    verify(bannedWordServiceMock).doesStringContainBannedWord(
        userValid.getUsername());
    verify(userServiceMock).doesUsernameExist(userValid.getUsername());
  }
}
