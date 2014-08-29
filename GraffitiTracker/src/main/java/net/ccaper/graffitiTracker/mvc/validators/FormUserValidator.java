package net.ccaper.graffitiTracker.mvc.validators;

import net.ccaper.graffitiTracker.objects.AppUser;
import net.ccaper.graffitiTracker.objects.UserForm;
import net.ccaper.graffitiTracker.service.AppUserService;
import net.ccaper.graffitiTracker.service.BannedWordService;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * 
 * @author ccaper
 * 
 *         Validator for {@link net.ccaper.graffitiTracker.objects.UserForm}
 * 
 */
@Component
public class FormUserValidator implements Validator {
  private static final Logger logger = LoggerFactory
      .getLogger(FormUserValidator.class);
  static private final int MIN_USERNAME_LENGTH = 6;
  static private final int MAX_USERNAME_LENGTH = 20;
  @Autowired
  private AppUserService appUserService;
  @Autowired
  private BannedWordService bannedWordService;

  // visible for testing
  /**
   * Sets the {@link net.ccaper.graffitiTracker.service.AppUserService}.
   * 
   * @param appUserService
   *          the new {@link net.ccaper.graffitiTracker.service.AppUserService}
   */
  void setAppUserService(AppUserService appUserService) {
    this.appUserService = appUserService;
  }

  // visible for testing
  /**
   * Sets the {@link net.ccaper.graffitiTracker.service.BannedWordService}.
   * 
   * @param bannedWordService
   *          the new
   *          {@link net.ccaper.graffitiTracker.service.BannedWordService}
   */
  void setBannedWordService(BannedWordService bannedWordService) {
    this.bannedWordService = bannedWordService;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.validation.Validator#supports(java.lang.Class)
   */
  @Override
  public boolean supports(Class<?> clazz) {
    return AppUser.class.isAssignableFrom(clazz);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.validation.Validator#validate(java.lang.Object,
   * org.springframework.validation.Errors)
   */
  @Override
  public void validate(Object target, Errors errors) {
    UserForm userForm = (UserForm) target;
    validateUsername(errors, userForm.getUsername());
    CommonValidator.validatePassword(errors, userForm.getPassword(),
        userForm.getConfirmPassword());
    CommonValidator.validateEmail(errors, userForm.getEmail(), appUserService);
    CommonValidator
        .validateSecurityAnswer(errors, userForm.getSecurityAnswer());
    validateAcceptTerms(errors, userForm.getAcceptTerms());
    validateSecurityQuestion(errors, userForm.getSecurityQuestion());
  }

  // visible for testing
  /**
   * Validate username.
   * 
   * @param errors
   *          the errors
   * @param username
   *          the username
   */
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
  /**
   * Validate accept terms.
   * 
   * @param errors
   *          the errors
   * @param acceptTerms
   *          the accept terms
   */
  void validateAcceptTerms(Errors errors, boolean acceptTerms) {
    if (acceptTerms == false) {
      errors.rejectValue("acceptTerms", "invalidAcceptTerms",
          "You must accept the terms and conditions.");
    }
  }

  // visible for testing
  /**
   * Validate security question.
   * 
   * @param errors
   *          the errors
   * @param securityQuestion
   *          the security question
   */
  void validateSecurityQuestion(Errors errors, String securityQuestion) {
    if (StringUtils.isEmpty(securityQuestion)) {
      errors.rejectValue("securityQuestion", "invalidSecurityQuestion",
          "You must chose a security question.");
    }
  }
}
