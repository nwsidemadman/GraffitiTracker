package net.ccaper.GraffitiTracker.mvc.validators;

import net.ccaper.GraffitiTracker.objects.AppUser;
import net.ccaper.GraffitiTracker.objects.ManageAccountForm;
import net.ccaper.GraffitiTracker.service.AppUserService;

import org.apache.commons.lang3.StringUtils;
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
  @Autowired
  private AppUserService appUserService;

  // TODO(ccaper): make default visibility
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

  // visible for testing
  void validatePassword(Errors errors, String password, String confirmPassword) {
    if (StringUtils.isEmpty(password)) {
      return;
    }
    CommonValidator.validatePassword(errors, password, confirmPassword);
  }

  // visible for testing
  void validateEmail(Errors errors, String email) {
    if (StringUtils.isEmpty(email)) {
      return;
    }
    CommonValidator.validateEmail(errors, email, appUserService);
  }

  // visible for testing
  void validateSecurityAnswer(Errors errors, String securityAnswer) {
    if (StringUtils.isEmpty(securityAnswer)) {
      return;
    }
    CommonValidator.validateSecurityAnswer(errors, securityAnswer);
  }
}
