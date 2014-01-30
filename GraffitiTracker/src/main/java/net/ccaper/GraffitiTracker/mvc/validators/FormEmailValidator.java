package net.ccaper.GraffitiTracker.mvc.validators;

import net.ccaper.GraffitiTracker.objects.AppUser;
import net.ccaper.GraffitiTracker.objects.EmailForm;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class FormEmailValidator implements Validator {
  static private EmailValidator EMAIL_VALIDATOR = EmailValidator
      .getInstance(false);

  @Override
  public boolean supports(Class<?> clazz) {
    return AppUser.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    // TODO: unit test
    EmailForm emailForm = (EmailForm) target;
    if (StringUtils.isEmpty(emailForm.getEmail())) {
      errors.rejectValue("email", "invalidEmail", "Email can not be empty.");
    } else if (!EMAIL_VALIDATOR.isValid(emailForm.getEmail())) {
      errors.rejectValue("email", "invalidemail", "Email is not valid.");
    }
  }
}
