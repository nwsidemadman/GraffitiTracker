package net.ccaper.graffitiTracker.mvc.validators;

import net.ccaper.graffitiTracker.objects.AppUser;
import net.ccaper.graffitiTracker.objects.ManageAccountForm;
import net.ccaper.graffitiTracker.service.AppUserService;

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
 *         Validator for
 *         {@link net.ccaper.graffitiTracker.objects.ManageAccountForm}
 * 
 */
@Component
public class FormManageAccountEditValidator implements Validator {
  private static final Logger logger = LoggerFactory
      .getLogger(FormManageAccountEditValidator.class);
  @Autowired
  private AppUserService appUserService;

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
    ManageAccountForm manageAccountForm = (ManageAccountForm) target;
    validatePassword(errors, manageAccountForm.getPassword(),
        manageAccountForm.getConfirmPassword());
    validateEmail(errors, manageAccountForm.getEmail());
    validateSecurityAnswer(errors, manageAccountForm.getSecurityAnswer());
  }

  // visible for testing
  /**
   * Validate password.
   * 
   * @param errors
   *          the errors
   * @param password
   *          the password
   * @param confirmPassword
   *          the confirm password
   */
  void validatePassword(Errors errors, String password, String confirmPassword) {
    if (StringUtils.isEmpty(password)) {
      return;
    }
    CommonValidator.validatePassword(errors, password, confirmPassword);
  }

  // visible for testing
  /**
   * Validate email.
   * 
   * @param errors
   *          the errors
   * @param email
   *          the email
   */
  void validateEmail(Errors errors, String email) {
    if (StringUtils.isEmpty(email)) {
      return;
    }
    CommonValidator.validateEmail(errors, email, appUserService);
  }

  // visible for testing
  /**
   * Validate security answer.
   * 
   * @param errors
   *          the errors
   * @param securityAnswer
   *          the security answer
   */
  void validateSecurityAnswer(Errors errors, String securityAnswer) {
    if (StringUtils.isEmpty(securityAnswer)) {
      return;
    }
    CommonValidator.validateSecurityAnswer(errors, securityAnswer);
  }
}
