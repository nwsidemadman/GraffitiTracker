package net.ccaper.GraffitiTracker.mvc.validators;

import net.ccaper.GraffitiTracker.objects.AppUser;
import net.ccaper.GraffitiTracker.objects.ManageAccountForm;
import net.ccaper.GraffitiTracker.service.AppUserService;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class FormManageAccountEditValidator implements Validator {
  private static final Logger logger = LoggerFactory
      .getLogger(FormManageAccountEditValidator.class);
  static private final int MAX_EMAIL_LENGTH = 100;
  static private final int MAX_SECURITY_ANSWER_LENGTH = 40;
  static private EmailValidator EMAIL_VALIDATOR = EmailValidator
      .getInstance(false);
  @Autowired
  private AppUserService appUserService;

  public void setAppUserService(AppUserService appUserService) {
    this.appUserService = appUserService;
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return AppUser.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    ManageAccountForm manageAccountForm = (ManageAccountForm) target;
    validatePassword(errors, manageAccountForm.getPassword(),
        manageAccountForm.getConfirmPassword());
    validateEmail(errors, manageAccountForm.getEmail());
    validateSecurityAnswer(errors, manageAccountForm.getSecurityAnswer());
  }
  
  // TODO(ccaper): unit test
  public void validatePassword(Errors errors, String password, String confirmPassword) {
    if (StringUtils.isEmpty(password)) {
      return;
    }
    CommonValidator.validatePassword(errors, password, confirmPassword);
  }

  // visible for testing
  // TODO(ccaper): unit test
  void validateEmail(Errors errors, String email) {
    if (StringUtils.isEmpty(email)) {
      return;
    } else if (email.length() > MAX_EMAIL_LENGTH) {
      errors.rejectValue("email", "invalidemail", String.format(
          "Email must be no longer than %s characters.", MAX_EMAIL_LENGTH));
    } else if (!EMAIL_VALIDATOR.isValid(email)) {
      errors.rejectValue("email", "invalidemail", "Email is not valid.");
    } else if (appUserService.doesEmailExist(email)) {
      errors.rejectValue("email", "invalidEmail",
          "Email already exists, one email per user.");
    }
  }

  // visible for testing
  // TODO(ccaper): unit test
  void validateSecurityAnswer(Errors errors, String securityAnswer) {
    if (StringUtils.isEmpty(securityAnswer)) {
      return;
    } else if (securityAnswer.length() > MAX_SECURITY_ANSWER_LENGTH) {
      errors.rejectValue("securityAnswer", "invalidSecurityAnswer", String
          .format("Security answer must be no longer than %s characters.",
              MAX_SECURITY_ANSWER_LENGTH));
    }
  }
}
