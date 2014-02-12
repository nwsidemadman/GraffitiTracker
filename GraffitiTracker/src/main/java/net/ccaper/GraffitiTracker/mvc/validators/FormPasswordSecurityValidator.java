package net.ccaper.GraffitiTracker.mvc.validators;

import net.ccaper.GraffitiTracker.objects.AppUser;
import net.ccaper.GraffitiTracker.objects.PasswordSecurityForm;
import net.ccaper.GraffitiTracker.service.AppUserService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class FormPasswordSecurityValidator implements Validator {
  static private final int MAX_SECURITY_ANSWER_LENGTH = 40;
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
    PasswordSecurityForm passwordSecurityForm = (PasswordSecurityForm) target;
    validateSecurityAnswer(errors, passwordSecurityForm.getSecurityAnswer(),
        passwordSecurityForm.getUserId());
    CommonValidator.validatePassword(errors, passwordSecurityForm.getPassword(),
        passwordSecurityForm.getConfirmPassword());
  }

  // visible for testing
  void validateSecurityAnswer(Errors errors, String securityAnswer, int userId) {
    if (StringUtils.isEmpty(securityAnswer)) {
      errors.rejectValue("securityAnswer", "invalidSecurityAnswer",
          "Security answer can not be empty.");
    } else if (securityAnswer.length() > MAX_SECURITY_ANSWER_LENGTH) {
      errors.rejectValue("securityAnswer", "invalidSecurityAnswer", String
          .format("Security answer must be no longer than %s characters.",
              MAX_SECURITY_ANSWER_LENGTH));
    } else if (!securityAnswer.toLowerCase().equals(
        appUserService.getSecurityAnswerByUserId(userId).toLowerCase())) {
      errors.rejectValue("securityAnswer", "invalidSecurityAnswer",
          "Security answer does not match security answer on record.");
    }
  }
}
