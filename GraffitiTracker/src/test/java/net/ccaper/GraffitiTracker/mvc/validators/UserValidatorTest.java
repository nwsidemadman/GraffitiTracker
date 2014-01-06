package net.ccaper.GraffitiTracker.mvc.validators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import net.ccaper.GraffitiTracker.objects.UserForm;
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
    UserForm userFormValidAcceptTerms = new UserForm();
    userFormValidAcceptTerms.setAcceptTerms(true);
    Errors errors = new BeanPropertyBindingResult(userFormValidAcceptTerms,
        "userValidAcceptTerms");
    userValidator.validateAcceptTerms(errors,
        userFormValidAcceptTerms.getAcceptTerms());
    assertFalse(errors.hasErrors());
  }

  @Test
  public void testValidateAcceptTermsSadPath() throws Exception {
    UserForm userFormInvalidAcceptTerms = new UserForm();
    userFormInvalidAcceptTerms.setAcceptTerms(false);
    Errors errors = new BeanPropertyBindingResult(userFormInvalidAcceptTerms,
        "userInvalidAcceptTerms");
    userValidator.validateAcceptTerms(errors,
        userFormInvalidAcceptTerms.getAcceptTerms());
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("acceptTerms"));
  }

  @Test
  public void testValidateEmail_Empty() throws Exception {
    UserForm userFormInvalidEmail = new UserForm();
    userFormInvalidEmail.setEmail(StringUtils.EMPTY);
    Errors errors = new BeanPropertyBindingResult(userFormInvalidEmail,
        "userInvalidEmail");
    userValidator.validateEmail(errors, userFormInvalidEmail.getEmail(),
        userFormInvalidEmail.getAcceptTerms());
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("email"));
  }

  @Test
  public void testValidateEmail_Null() throws Exception {
    UserForm userFormInvalidEmail = new UserForm();
    userFormInvalidEmail.setEmail(null);
    Errors errors = new BeanPropertyBindingResult(userFormInvalidEmail,
        "userInvalidEmail");
    userValidator.validateEmail(errors, userFormInvalidEmail.getEmail(),
        userFormInvalidEmail.getAcceptTerms());
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
    userValidator.validateEmail(errors, userFormInvalidEmail.getEmail(),
        userFormInvalidEmail.getAcceptTerms());
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("email"));
  }

  @Test
  public void testValidateEmail_InvalidAddress() throws Exception {
    UserForm userFormInvalidEmail = new UserForm();
    userFormInvalidEmail.setEmail("test@test");
    Errors errors = new BeanPropertyBindingResult(userFormInvalidEmail,
        "userInvalidEmail");
    userValidator.validateEmail(errors, userFormInvalidEmail.getEmail(),
        userFormInvalidEmail.getAcceptTerms());
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("email"));
  }

  @Test
  public void testValidateEmail_AcceptTermsFalse() throws Exception {
    UserForm userFormInvalidEmail = new UserForm();
    userFormInvalidEmail.setEmail("test@test.com");
    Errors errors = new BeanPropertyBindingResult(userFormInvalidEmail,
        "userInvalidEmail");
    userValidator.validateEmail(errors, userFormInvalidEmail.getEmail(),
        userFormInvalidEmail.getAcceptTerms());
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("email"));
  }

  @Test
  public void testValidateEmail_EmailAlreadyExists() throws Exception {
    UserService userServiceMock = mock(UserService.class);
    UserForm userFormInvalidEmail = new UserForm();
    userFormInvalidEmail.setEmail("test@test.com");
    userFormInvalidEmail.setAcceptTerms(true);
    when(userServiceMock.doesEmailExist(userFormInvalidEmail.getEmail()))
    .thenReturn(true);
    userValidator.setUserService(userServiceMock);
    Errors errors = new BeanPropertyBindingResult(userFormInvalidEmail,
        "userInvalidEmail");
    userValidator.validateEmail(errors, userFormInvalidEmail.getEmail(),
        userFormInvalidEmail.getAcceptTerms());
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("email"));
    verify(userServiceMock).doesEmailExist(userFormInvalidEmail.getEmail());
  }

  @Test
  public void testValidateEmail_HappyPath() throws Exception {
    UserService userServiceMock = mock(UserService.class);
    UserForm userFormValidEmail = new UserForm();
    userFormValidEmail.setEmail("test@test.com");
    userFormValidEmail.setAcceptTerms(true);
    when(userServiceMock.doesEmailExist(userFormValidEmail.getEmail())).thenReturn(
        false);
    userValidator.setUserService(userServiceMock);
    Errors errors = new BeanPropertyBindingResult(userFormValidEmail,
        "userValidEmail");
    userValidator.validateEmail(errors, userFormValidEmail.getEmail(),
        userFormValidEmail.getAcceptTerms());
    assertFalse(errors.hasErrors());
    verify(userServiceMock).doesEmailExist(userFormValidEmail.getEmail());
  }

  @Test
  public void testValidatePassword_Empty() throws Exception {
    UserForm userFormInvalidPassword = new UserForm();
    userFormInvalidPassword.setPassword(StringUtils.EMPTY);
    userFormInvalidPassword.setConfirmPassword(StringUtils.EMPTY);
    Errors errors = new BeanPropertyBindingResult(userFormInvalidPassword,
        "userInvalidPassword");
    userValidator.validatePassword(errors, userFormInvalidPassword.getPassword(),
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
    userValidator.validatePassword(errors, userFormInvalidPassword.getPassword(),
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
    userValidator.validatePassword(errors, userFormInvalidPassword.getPassword(),
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
    userValidator.validatePassword(errors, userFormInvalidPassword.getPassword(),
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
    userValidator.validatePassword(errors, userFormInvalidPassword.getPassword(),
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
    userValidator.validatePassword(errors, userFormValidPassword.getPassword(),
        userFormValidPassword.getConfirmPassword());
    assertFalse(errors.hasErrors());
  }

  @Test
  public void testValidateUsername_Empty() throws Exception {
    UserForm userFormInvalidUsername = new UserForm();
    userFormInvalidUsername.setUsername(StringUtils.EMPTY);
    Errors errors = new BeanPropertyBindingResult(userFormInvalidUsername,
        "userInvalidUsername");
    userValidator.validateUsername(errors, userFormInvalidUsername.getUsername(),
        userFormInvalidUsername.getAcceptTerms());
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("username"));
  }

  @Test
  public void testValidateUsername_Null() throws Exception {
    UserForm userFormInvalidUsername = new UserForm();
    userFormInvalidUsername.setUsername(null);
    Errors errors = new BeanPropertyBindingResult(userFormInvalidUsername,
        "userInvalidUsername");
    userValidator.validateUsername(errors, userFormInvalidUsername.getUsername(),
        userFormInvalidUsername.getAcceptTerms());
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("username"));
  }

  @Test
  public void testValidateUsername_TooShort() throws Exception {
    UserForm userFormInvalidUsername = new UserForm();
    userFormInvalidUsername.setUsername("123");
    Errors errors = new BeanPropertyBindingResult(userFormInvalidUsername,
        "userInvalidUsername");
    userValidator.validateUsername(errors, userFormInvalidUsername.getUsername(),
        userFormInvalidUsername.getAcceptTerms());
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("username"));
  }

  @Test
  public void testValidateUsername_TooLong() throws Exception {
    UserForm userFormInvalidUsername = new UserForm();
    userFormInvalidUsername.setUsername("123456789012345678901");
    Errors errors = new BeanPropertyBindingResult(userFormInvalidUsername,
        "userInvalidUsername");
    userValidator.validateUsername(errors, userFormInvalidUsername.getUsername(),
        userFormInvalidUsername.getAcceptTerms());
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("username"));
  }

  @Test
  public void testValidateUsername_ContainsWhitespace() throws Exception {
    UserForm userFormInvalidUsername = new UserForm();
    userFormInvalidUsername.setUsername("Some Username");
    Errors errors = new BeanPropertyBindingResult(userFormInvalidUsername,
        "userInvalidUsername");
    userValidator.validateUsername(errors, userFormInvalidUsername.getUsername(),
        userFormInvalidUsername.getAcceptTerms());
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("username"));
  }

  @Test
  public void testValidateUsername_NonAlphaNumeric() throws Exception {
    UserForm userFormInvalidUsername = new UserForm();
    userFormInvalidUsername.setUsername("Some$Username");
    Errors errors = new BeanPropertyBindingResult(userFormInvalidUsername,
        "userInvalidUsername");
    userValidator.validateUsername(errors, userFormInvalidUsername.getUsername(),
        userFormInvalidUsername.getAcceptTerms());
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("username"));
  }

  @Test
  public void testValidateUsername_AcceptTermsFalse() throws Exception {
    UserForm userFormInvalidUsername = new UserForm();
    userFormInvalidUsername.setUsername("ValidUsername");
    Errors errors = new BeanPropertyBindingResult(userFormInvalidUsername,
        "userInvalidUsername");
    userValidator.validateUsername(errors, userFormInvalidUsername.getUsername(),
        userFormInvalidUsername.getAcceptTerms());
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("username"));
  }

  @Test
  public void testValidateUsername_BannedWord() throws Exception {
    BannedWordService bannedWordServiceMock = mock(BannedWordService.class);
    UserForm userFormInvalidUsername = new UserForm();
    userFormInvalidUsername.setUsername("bannedWord");
    userFormInvalidUsername.setAcceptTerms(true);
    when(
        bannedWordServiceMock.doesStringContainBannedWord(userFormInvalidUsername
            .getUsername())).thenReturn(true);
    userValidator.setBannedWordService(bannedWordServiceMock);
    Errors errors = new BeanPropertyBindingResult(userFormInvalidUsername,
        "userInvalidUsername");
    userValidator.validateUsername(errors, userFormInvalidUsername.getUsername(),
        userFormInvalidUsername.getAcceptTerms());
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("username"));
    verify(bannedWordServiceMock).doesStringContainBannedWord(
        userFormInvalidUsername.getUsername());
  }

  @Test
  public void testValidateUsername_UsernameAlreadyExists() throws Exception {
    BannedWordService bannedWordServiceMock = mock(BannedWordService.class);
    UserService userServiceMock = mock(UserService.class);
    UserForm userFormInvalidUsername = new UserForm();
    userFormInvalidUsername.setUsername("alreadyExists");
    userFormInvalidUsername.setAcceptTerms(true);
    when(
        bannedWordServiceMock.doesStringContainBannedWord(userFormInvalidUsername
            .getUsername())).thenReturn(false);
    when(userServiceMock.doesUsernameExist(userFormInvalidUsername.getUsername()))
    .thenReturn(true);
    userValidator.setBannedWordService(bannedWordServiceMock);
    userValidator.setUserService(userServiceMock);
    Errors errors = new BeanPropertyBindingResult(userFormInvalidUsername,
        "userInvalidUsername");
    userValidator.validateUsername(errors, userFormInvalidUsername.getUsername(),
        userFormInvalidUsername.getAcceptTerms());
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("username"));
    verify(bannedWordServiceMock).doesStringContainBannedWord(
        userFormInvalidUsername.getUsername());
    verify(userServiceMock).doesUsernameExist(userFormInvalidUsername.getUsername());
  }

  @Test
  public void testValidateUsername_HappyPath() throws Exception {
    BannedWordService bannedWordServiceMock = mock(BannedWordService.class);
    UserService userServiceMock = mock(UserService.class);
    UserForm userFormValidUsername = new UserForm();
    userFormValidUsername.setUsername("validUsername");
    userFormValidUsername.setAcceptTerms(true);
    when(
        bannedWordServiceMock.doesStringContainBannedWord(userFormValidUsername
            .getUsername())).thenReturn(false);
    when(userServiceMock.doesUsernameExist(userFormValidUsername.getUsername()))
    .thenReturn(false);
    userValidator.setBannedWordService(bannedWordServiceMock);
    userValidator.setUserService(userServiceMock);
    Errors errors = new BeanPropertyBindingResult(userFormValidUsername,
        "userValidUsername");
    userValidator.validateUsername(errors, userFormValidUsername.getUsername(),
        userFormValidUsername.getAcceptTerms());
    assertFalse(errors.hasErrors());
    verify(bannedWordServiceMock).doesStringContainBannedWord(
        userFormValidUsername.getUsername());
    verify(userServiceMock).doesUsernameExist(userFormValidUsername.getUsername());
  }

  @Test
  public void testValidate_HappyPath() throws Exception {
    BannedWordService bannedWordServiceMock = mock(BannedWordService.class);
    UserService userServiceMock = mock(UserService.class);
    UserForm userFormValid = new UserForm();
    userFormValid.setUsername("validUsername");
    userFormValid.setPassword("validPassword");
    userFormValid.setConfirmPassword("validPassword");
    userFormValid.setEmail("test@test.com");
    userFormValid.setAcceptTerms(true);
    when(
        bannedWordServiceMock.doesStringContainBannedWord(userFormValid
            .getUsername())).thenReturn(false);
    when(userServiceMock.doesUsernameExist(userFormValid.getUsername()))
    .thenReturn(false);
    userValidator.setBannedWordService(bannedWordServiceMock);
    userValidator.setUserService(userServiceMock);
    Errors errors = new BeanPropertyBindingResult(userFormValid,
        "userValid");
    userValidator.validate(userFormValid, errors);
    assertFalse(errors.hasErrors());
    verify(bannedWordServiceMock).doesStringContainBannedWord(
        userFormValid.getUsername());
    verify(userServiceMock).doesUsernameExist(userFormValid.getUsername());
  }
  
  @Test
  public void testValidate_SadPath() throws Exception {
    UserForm userFormInvalid = new UserForm();
    userFormInvalid.setUsername("one");
    userFormInvalid.setPassword("one");
    userFormInvalid.setConfirmPassword("one");
    userFormInvalid.setEmail("test@test");
    userFormInvalid.setAcceptTerms(false);
    Errors errors = new BeanPropertyBindingResult(userFormInvalid,
        "userInvalid");
    userValidator.validate(userFormInvalid, errors);
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("username"));
    assertNotNull(errors.getFieldError("password"));
    assertNotNull(errors.getFieldError("email"));
    assertNotNull(errors.getFieldError("acceptTerms"));
  }
}
