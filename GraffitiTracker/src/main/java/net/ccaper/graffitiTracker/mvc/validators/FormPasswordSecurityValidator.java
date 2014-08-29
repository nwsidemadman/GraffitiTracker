package net.ccaper.graffitiTracker.mvc.validators;

import net.ccaper.graffitiTracker.objects.AppUser;
import net.ccaper.graffitiTracker.objects.PasswordSecurityForm;
import net.ccaper.graffitiTracker.service.AppUserService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * 
 * @author ccaper
 * 
 *         Validator for
 *         {@link net.ccaper.graffitiTracker.objects.PasswordSecurityForm}
 * 
 */
@Component
public class FormPasswordSecurityValidator implements Validator {
  static private final int MAX_SECURITY_ANSWER_LENGTH = 40;
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
    PasswordSecurityForm passwordSecurityForm = (PasswordSecurityForm) target;
    validateSecurityAnswer(errors, passwordSecurityForm.getSecurityAnswer(),
        passwordSecurityForm.getUserid());
    CommonValidator.validatePassword(errors,
        passwordSecurityForm.getPassword(),
        passwordSecurityForm.getConfirmPassword());
  }

  // visible for testing
  /**
   * Validate security answer.
   * 
   * @param errors
   *          the errors
   * @param securityAnswer
   *          the security answer
   * @param userid
   *          the userid
   */
  void validateSecurityAnswer(Errors errors, String securityAnswer, int userid) {
    if (StringUtils.isEmpty(securityAnswer)) {
      errors.rejectValue("securityAnswer", "invalidSecurityAnswer",
          "Security answer can not be empty.");
    } else if (securityAnswer.length() > MAX_SECURITY_ANSWER_LENGTH) {
      errors.rejectValue("securityAnswer", "invalidSecurityAnswer", String
          .format("Security answer must be no longer than %s characters.",
              MAX_SECURITY_ANSWER_LENGTH));
    } else if (!securityAnswer.toLowerCase().equals(
        appUserService.getSecurityAnswerByUserid(userid).toLowerCase())) {
      errors.rejectValue("securityAnswer", "invalidSecurityAnswer",
          "Security answer does not match security answer on record.");
    }
  }
}
