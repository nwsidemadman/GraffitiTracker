package net.ccaper.GraffitiTracker.mvc.validators;

import net.ccaper.GraffitiTracker.objects.AppUser;
import net.ccaper.GraffitiTracker.objects.UsernameForm;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class FormUsernameValidator implements Validator {
  @Override
  public boolean supports(Class<?> clazz) {
    return AppUser.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    UsernameForm usernameForm = (UsernameForm) target;
    if (StringUtils.isEmpty(usernameForm.getUsername())) {
      errors.rejectValue("username", "invalidUsername", "Username can not be empty.");
    }
  }
}
