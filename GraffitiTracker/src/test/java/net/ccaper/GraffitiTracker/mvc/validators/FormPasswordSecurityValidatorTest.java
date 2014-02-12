package net.ccaper.GraffitiTracker.mvc.validators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import net.ccaper.GraffitiTracker.objects.PasswordSecurityForm;
import net.ccaper.GraffitiTracker.service.AppUserService;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

public class FormPasswordSecurityValidatorTest {
  FormPasswordSecurityValidator formPasswordSecurityValidator = new FormPasswordSecurityValidator();

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testValidate_HappyPath() throws Exception {
    PasswordSecurityForm passwordSecurityForm = new PasswordSecurityForm();
    passwordSecurityForm.setUserId(1);
    passwordSecurityForm.setSecurityAnswer("testAnswer");
    String password = "testPassword";
    passwordSecurityForm.setPassword(password);
    passwordSecurityForm.setConfirmPassword(password);
    AppUserService appUserServiceMock = mock(AppUserService.class);
    when(
        appUserServiceMock.getSecurityAnswerByUserId(passwordSecurityForm
            .getUserId())).thenReturn(passwordSecurityForm.getSecurityAnswer());
    formPasswordSecurityValidator.setAppUserService(appUserServiceMock);
    Errors errors = new BeanPropertyBindingResult(passwordSecurityForm,
        "passwordSecurityValid");
    formPasswordSecurityValidator.validate(passwordSecurityForm, errors);
    assertFalse(errors.hasErrors());
    verify(appUserServiceMock).getSecurityAnswerByUserId(
        passwordSecurityForm.getUserId());
  }

  @Test
  public void testValidate_SadPath() throws Exception {
    PasswordSecurityForm passwordSecurityForm = new PasswordSecurityForm();
    passwordSecurityForm.setUserId(1);
    passwordSecurityForm.setSecurityAnswer(null);
    passwordSecurityForm.setPassword("testPassword");
    passwordSecurityForm.setConfirmPassword("differentPassword");
    Errors errors = new BeanPropertyBindingResult(passwordSecurityForm,
        "passwordSecurityValid");
    formPasswordSecurityValidator.validate(passwordSecurityForm, errors);
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("securityAnswer"));
    assertNotNull(errors.getFieldError("password"));
  }

  @Test
  public void testSecurityAnswer_Empty() {
    PasswordSecurityForm passwordSecurityFormSecurityAnswerEmpty = new PasswordSecurityForm();
    passwordSecurityFormSecurityAnswerEmpty
    .setSecurityAnswer(StringUtils.EMPTY);
    passwordSecurityFormSecurityAnswerEmpty.setUserId(1);
    Errors errors = new BeanPropertyBindingResult(
        passwordSecurityFormSecurityAnswerEmpty,
        "passwordSecurityInvalidAnswer");
    formPasswordSecurityValidator.validateSecurityAnswer(errors,
        passwordSecurityFormSecurityAnswerEmpty.getSecurityAnswer(),
        passwordSecurityFormSecurityAnswerEmpty.getUserId());
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("securityAnswer"));
  }

  @Test
  public void testSecurityAnswer_Null() {
    PasswordSecurityForm passwordSecurityFormSecurityAnswerNull = new PasswordSecurityForm();
    passwordSecurityFormSecurityAnswerNull.setSecurityAnswer(null);
    passwordSecurityFormSecurityAnswerNull.setUserId(1);
    Errors errors = new BeanPropertyBindingResult(
        passwordSecurityFormSecurityAnswerNull, "passwordSecurityInvalidAnswer");
    formPasswordSecurityValidator.validateSecurityAnswer(errors,
        passwordSecurityFormSecurityAnswerNull.getSecurityAnswer(),
        passwordSecurityFormSecurityAnswerNull.getUserId());
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("securityAnswer"));
  }

  @Test
  public void testSecurityAnswer_TooLong() {
    PasswordSecurityForm passwordSecurityFormSecurityAnswerTooLong = new PasswordSecurityForm();
    passwordSecurityFormSecurityAnswerTooLong
    .setSecurityAnswer("12345678901234567890123456789012345678901");
    passwordSecurityFormSecurityAnswerTooLong.setUserId(1);
    Errors errors = new BeanPropertyBindingResult(
        passwordSecurityFormSecurityAnswerTooLong,
        "passwordSecurityInvalidAnswer");
    formPasswordSecurityValidator.validateSecurityAnswer(errors,
        passwordSecurityFormSecurityAnswerTooLong.getSecurityAnswer(),
        passwordSecurityFormSecurityAnswerTooLong.getUserId());
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("securityAnswer"));
  }

  @Test
  public void testSecurityAnswer_AnswerNotMatch() {
    String securityAnswer = "testAnswer";
    PasswordSecurityForm passwordSecurityFormSecurityAnswer = new PasswordSecurityForm();
    passwordSecurityFormSecurityAnswer.setSecurityAnswer(securityAnswer);
    passwordSecurityFormSecurityAnswer.setUserId(1);
    Errors errors = new BeanPropertyBindingResult(
        passwordSecurityFormSecurityAnswer, "passwordSecurityInvalidAnswer");
    AppUserService appUserServiceMock = mock(AppUserService.class);
    when(
        appUserServiceMock
        .getSecurityAnswerByUserId(passwordSecurityFormSecurityAnswer
            .getUserId())).thenReturn("differentAnswer");
    formPasswordSecurityValidator.setAppUserService(appUserServiceMock);
    formPasswordSecurityValidator.validateSecurityAnswer(errors,
        passwordSecurityFormSecurityAnswer.getSecurityAnswer(),
        passwordSecurityFormSecurityAnswer.getUserId());
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("securityAnswer"));
    verify(appUserServiceMock).getSecurityAnswerByUserId(
        passwordSecurityFormSecurityAnswer.getUserId());
  }

  @Test
  public void testSecurityAnswer_HappyPath() {
    String securityAnswer = "testAnswer";
    PasswordSecurityForm passwordSecurityFormSecurityAnswer = new PasswordSecurityForm();
    passwordSecurityFormSecurityAnswer.setSecurityAnswer(securityAnswer);
    passwordSecurityFormSecurityAnswer.setUserId(1);
    Errors errors = new BeanPropertyBindingResult(
        passwordSecurityFormSecurityAnswer, "passwordSecurityInvalidAnswer");
    AppUserService appUserServiceMock = mock(AppUserService.class);
    when(
        appUserServiceMock
        .getSecurityAnswerByUserId(passwordSecurityFormSecurityAnswer
            .getUserId())).thenReturn(securityAnswer);
    formPasswordSecurityValidator.setAppUserService(appUserServiceMock);
    formPasswordSecurityValidator.validateSecurityAnswer(errors,
        passwordSecurityFormSecurityAnswer.getSecurityAnswer(),
        passwordSecurityFormSecurityAnswer.getUserId());
    assertFalse(errors.hasErrors());
    verify(appUserServiceMock).getSecurityAnswerByUserId(
        passwordSecurityFormSecurityAnswer.getUserId());
  }
}
