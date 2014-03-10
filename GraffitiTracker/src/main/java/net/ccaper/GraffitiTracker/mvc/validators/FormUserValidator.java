package net.ccaper.GraffitiTracker.mvc.validators;

import net.ccaper.GraffitiTracker.objects.AppUser;
import net.ccaper.GraffitiTracker.objects.UserForm;
import net.ccaper.GraffitiTracker.service.AppUserService;
import net.ccaper.GraffitiTracker.service.BannedWordService;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class FormUserValidator implements Validator {
  private static final Logger logger = LoggerFactory
      .getLogger(FormUserValidator.class);
  static private final int MIN_USERNAME_LENGTH = 6;
  static private final int MAX_USERNAME_LENGTH = 20;
  static private final int MAX_EMAIL_LENGTH = 100;
  static private final int MAX_SECURITY_ANSWER_LENGTH = 40;
  static private EmailValidator EMAIL_VALIDATOR = EmailValidator
      .getInstance(false);
  @Autowired
  private AppUserService appUserService;
  @Autowired
  private BannedWordService bannedWordService;

  public void setAppUserService(AppUserService appUserService) {
    this.appUserService = appUserService;
  }

  public void setBannedWordService(BannedWordService bannedWordService) {
    this.bannedWordService = bannedWordService;
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return AppUser.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    UserForm userForm = (UserForm) target;
    validateUsername(errors, userForm.getUsername());
    CommonValidator.validatePassword(errors, userForm.getPassword(),
        userForm.getConfirmPassword());
    validateEmail(errors, userForm.getEmail());
    validateSecurityAnswer(errors, userForm.getSecurityAnswer());
    validateAcceptTerms(errors, userForm.getAcceptTerms());
  }

  // visible for testing
  void validateUsername(Errors errors, String username) {
    if (StringUtils.isEmpty(username)) {
      errors.rejectValue("username", "invalidUsername",
          "Username can not be empty.");
    } else if (username.length() < MIN_USERNAME_LENGTH) {
      errors.rejectValue("username", "invalidUsername", String.format(
          "Username must be longer than %s characters.", MIN_USERNAME_LENGTH));
    } else if (username.length() > MAX_USERNAME_LENGTH) {
      errors.rejectValue("username", "invalidUsername", String
          .format("Username must be no longer than %s characters.",
              MAX_USERNAME_LENGTH));
    } else if (StringUtils.containsWhitespace(username)) {
      errors.rejectValue("username", "invalidUsername",
          "Username can not contain whitespace.");
    } else if (!StringUtils.isAlphanumeric(username)) {
      errors.rejectValue("username", "invalidUsername",
          "Username can only contain alphanumeric characters.");
    } else if (bannedWordService.doesStringContainBannedWord(username)) {
      errors.rejectValue("username", "invalidUsername",
          "Username contains a banned word.");
    } else if (appUserService.doesUsernameExist(username)) {
      errors.rejectValue("username", "invalidUsername",
          "Username already exists, please chose another.");
    }
  }

  // visible for testing
  void validateEmail(Errors errors, String email) {
    if (StringUtils.isEmpty(email)) {
      errors.rejectValue("email", "invalidEmail", "Email can not be empty.");
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
  void validateSecurityAnswer(Errors errors, String securityAnswer) {
    if (StringUtils.isEmpty(securityAnswer)) {
      errors.rejectValue("securityAnswer", "invalidSecurityAnswer",
          "Security answer can not be empty.");
    } else if (securityAnswer.length() > MAX_SECURITY_ANSWER_LENGTH) {
      errors.rejectValue("securityAnswer", "invalidSecurityAnswer", String
          .format("Security answer must be no longer than %s characters.",
              MAX_SECURITY_ANSWER_LENGTH));
    }
  }

  // visible for testing
  void validateAcceptTerms(Errors errors, boolean acceptTerms) {
    if (acceptTerms == false) {
      errors.rejectValue("acceptTerms", "invalidAcceptTerms",
          "You must accept the terms and conditions.");
    }
  }
}
