package net.ccaper.graffitiTracker.mvc.validators;

import net.ccaper.graffitiTracker.objects.AppUser;
import net.ccaper.graffitiTracker.objects.UsernameForm;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * 
 * @author ccaper
 * 
 *         Validator for {@link net.ccaper.graffitiTracker.object.UserNameForm}
 * 
 */
@Component
public class FormUsernameValidator implements Validator {

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
    UsernameForm usernameForm = (UsernameForm) target;
    if (StringUtils.isEmpty(usernameForm.getUsername())) {
      errors.rejectValue("username", "invalidUsername",
          "Username can not be empty.");
    }
  }
}
