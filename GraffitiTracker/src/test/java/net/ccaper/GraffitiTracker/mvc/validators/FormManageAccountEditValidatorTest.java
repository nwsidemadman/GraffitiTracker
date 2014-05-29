package net.ccaper.GraffitiTracker.mvc.validators;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import net.ccaper.GraffitiTracker.objects.ManageAccountForm;
import net.ccaper.GraffitiTracker.service.AppUserService;

import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

public class FormManageAccountEditValidatorTest {
  FormManageAccountEditValidator formManageAccountEditValidator = new FormManageAccountEditValidator();

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testValidatePasswordEmpty() {
    ManageAccountForm manageAccountFormEmptyPassword = new ManageAccountForm();
    manageAccountFormEmptyPassword.setPassword(StringUtils.EMPTY);
    Errors errors = new BeanPropertyBindingResult(
        manageAccountFormEmptyPassword, "emptyPassword");
    formManageAccountEditValidator.validatePassword(errors,
        manageAccountFormEmptyPassword.getPassword(), null);
    assertFalse(errors.hasErrors());
  }

  @Test
  public void testValidatePasswordNotEmpty() {
    ManageAccountForm manageAccountFormNotEmptyPassword = new ManageAccountForm();
    manageAccountFormNotEmptyPassword.setPassword("somePassword");
    manageAccountFormNotEmptyPassword.setConfirmPassword("somePassword");
    Errors errors = new BeanPropertyBindingResult(
        manageAccountFormNotEmptyPassword, "notEmptyPassword");
    formManageAccountEditValidator.validatePassword(errors,
        manageAccountFormNotEmptyPassword.getPassword(),
        manageAccountFormNotEmptyPassword.getConfirmPassword());
    assertFalse(errors.hasErrors());
  }

  @Test
  public void testValidateEmailEmpty() {
    ManageAccountForm manageAccountFormEmptyEmail = new ManageAccountForm();
    manageAccountFormEmptyEmail.setEmail(StringUtils.EMPTY);
    Errors errors = new BeanPropertyBindingResult(manageAccountFormEmptyEmail,
        "emptyEmail");
    formManageAccountEditValidator.validateEmail(errors,
        manageAccountFormEmptyEmail.getEmail());
    assertFalse(errors.hasErrors());
  }
  
  @Test
  public void testValidateEmailNotEmpty() {
    AppUserService appUserServiceMock = mock(AppUserService.class);
    ManageAccountForm manageAccountFormNotEmptyEmail = new ManageAccountForm();
    manageAccountFormNotEmptyEmail.setEmail("test@test.com");
    Errors errors = new BeanPropertyBindingResult(manageAccountFormNotEmptyEmail,
        "notEmptyEmail");
    when(appUserServiceMock.doesEmailExist(manageAccountFormNotEmptyEmail.getEmail())).thenReturn(
        false);
    formManageAccountEditValidator.setAppUserService(appUserServiceMock);
    formManageAccountEditValidator.validateEmail(errors,
        manageAccountFormNotEmptyEmail.getEmail());
    assertFalse(errors.hasErrors());
    verify(appUserServiceMock).doesEmailExist(manageAccountFormNotEmptyEmail.getEmail());
  }
  
  @Test
  public void testValidateSecurityAnswerEmpty() {
    ManageAccountForm manageAccountFormEmptySecurityAnswer = new ManageAccountForm();
    manageAccountFormEmptySecurityAnswer.setSecurityAnswer(StringUtils.EMPTY);
    Errors errors = new BeanPropertyBindingResult(
        manageAccountFormEmptySecurityAnswer, "emptySecurityAnswer");
    formManageAccountEditValidator.validateSecurityAnswer(errors,
        manageAccountFormEmptySecurityAnswer.getSecurityAnswer());
    assertFalse(errors.hasErrors());
  }

  @Test
  public void testValidateSecurityAnswerNotEmpty() {
    ManageAccountForm manageAccountFormNotEmptySecurityAnswer = new ManageAccountForm();
    manageAccountFormNotEmptySecurityAnswer.setSecurityAnswer("Some security answer");
    Errors errors = new BeanPropertyBindingResult(
        manageAccountFormNotEmptySecurityAnswer, "notEmptySecurityAnswer");
    formManageAccountEditValidator.validateSecurityAnswer(errors,
        manageAccountFormNotEmptySecurityAnswer.getSecurityAnswer());
    assertFalse(errors.hasErrors());
  }
}
