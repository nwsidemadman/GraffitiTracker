package net.ccaper.graffitiTracker.mvc.validators;

import net.ccaper.graffitiTracker.objects.AppUser;
import net.ccaper.graffitiTracker.objects.EmailForm;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * 
 * @author ccaper
 * 
 *         Validator for {@link net.ccaper.graffitiTracker.objects.EmailForm}
 * 
 */
@Component
public class FormEmailValidator implements Validator {
  static private EmailValidator EMAIL_VALIDATOR = EmailValidator
      .getInstance(false);

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
    EmailForm emailForm = (EmailForm) target;
    if (StringUtils.isEmpty(emailForm.getEmail())) {
      errors.rejectValue("email", "invalidEmail", "Email can not be empty.");
    } else if (!EMAIL_VALIDATOR.isValid(emailForm.getEmail())) {
      errors.rejectValue("email", "invalidEmail", "Email is not valid.");
    }
  }
}
