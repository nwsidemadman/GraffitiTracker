package net.ccaper.GraffitiTracker.mvc.validators;

import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;

public class CommonValidator {
  static private final int MIN_PASSWORD_LENGTH = 6;
  static private final int MAX_PASSWORD_LENGTH = 64;

  public static void validatePassword(Errors errors, String password, String confirmPassword) {
    if (StringUtils.isEmpty(password)) {
      errors.rejectValue("password", "invalidPassword",
          "Password can not be empty.");
    } else if (password.length() < MIN_PASSWORD_LENGTH) {
      errors.rejectValue("password", "invalidPassword", String.format(
          "Password must be longer than %s characters.", MIN_PASSWORD_LENGTH));
    } else if (password.length() > MAX_PASSWORD_LENGTH) {
      errors.rejectValue("password", "invalidPassword", String
          .format("Password must be no longer than %s characters.",
              MAX_PASSWORD_LENGTH));
    } else if (!password.equals(confirmPassword)) {
      errors.rejectValue("password", "invalidPassword",
          "Passwords do not match");
      errors.rejectValue("confirmPassword", "invalidPassword",
          "Passwords do not match");
    }
    if (StringUtils.isEmpty(confirmPassword)) {
      errors.rejectValue("confirmPassword", "invalidConfirmPassword",
          "Confirm password can not be empty.");
    }
  }
}
