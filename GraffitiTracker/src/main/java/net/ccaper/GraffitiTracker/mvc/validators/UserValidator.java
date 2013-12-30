package net.ccaper.GraffitiTracker.mvc.validators;

import net.ccaper.GraffitiTracker.objects.User;
import net.ccaper.GraffitiTracker.service.UserService;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class UserValidator implements Validator {
  static private final int MIN_USERNAME_LENGTH = 6;
  static private final int MAX_USERNAME_LENGTH = 20;
  static private final int MIN_PASSWORD_LENGTH = 6;
  static private final int MAX_PASSWORD_LENGTH = 64;
  static private final int MAX_EMAIL_LENGTH = 100;
  static private EmailValidator EMAIL_VALIDATOR = EmailValidator
      .getInstance(false);
  private UserService userService;

  public UserValidator(UserService userService) {
    this.userService = userService;
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return User.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    // TODO: unit test
    User user = (User) target;
    if (StringUtils.isEmpty(user.getUsername())) {
      errors.rejectValue("username", "invalidUsername",
          "Username can not be empty.");
    } else if (user.getUsername().length() < MIN_USERNAME_LENGTH) {
      errors.rejectValue("username", "invalidUsername", String.format(
          "Username must be longer than %s characters.", MIN_USERNAME_LENGTH));
    } else if (user.getUsername().length() > MAX_USERNAME_LENGTH) {
      errors.rejectValue("username", "invalidUsername", String
          .format("Username must be no longer than %s characters.",
              MAX_USERNAME_LENGTH));
    } else if (StringUtils.containsWhitespace(user.getUsername())) {
      errors.rejectValue("username", "invalidUsername",
          "Username can not contain whitespace.");
    } else if (!StringUtils.isAlphanumeric(user.getUsername())) {
      errors.rejectValue("username", "invalidUsername",
          "Username can only contain alphanumeric characters.");
    } else if (user.getAcceptTerms() == true
        && userService.doesUsernameExist(user.getUsername())) {
      errors.rejectValue("username", "invalidUsername",
          "Username already exists, please chose another.");
    }
    if (StringUtils.isEmpty(user.getPassword())) {
      errors.rejectValue("password", "invalidPassword",
          "Password can not be empty.");
    } else if (user.getPassword().length() < MIN_PASSWORD_LENGTH) {
      errors.rejectValue("password", "invalidPassword", String.format(
          "Password must be longer than %s characters.", MIN_PASSWORD_LENGTH));
    } else if (user.getPassword().length() > MAX_PASSWORD_LENGTH) {
      errors.rejectValue("password", "invalidPassword", String
          .format("Password must be no longer than %s characters.",
              MAX_PASSWORD_LENGTH));
    } else if (!user.getPassword().equals(user.getConfirmPassword())) {
      errors.rejectValue("password", "invalidPassword",
          "Passwords do not match");
      errors.rejectValue("confirmPassword", "invalidPassword",
          "Passwords do not match");
    }
    if (StringUtils.isEmpty(user.getConfirmPassword())) {
      errors.rejectValue("confirmPassword", "invalidConfirmPassword",
          "Confirm password can not be empty.");
    }
    if (StringUtils.isEmpty(user.getEmail())) {
      errors.rejectValue("email", "invalidEmail", "Email can not be empty.");
    } else if (user.getEmail().length() > MAX_EMAIL_LENGTH) {
      errors.rejectValue("email", "invalidemail", String.format(
          "Email must be no longer than %s characters.", MAX_EMAIL_LENGTH));
    } else if (!EMAIL_VALIDATOR.isValid(user.getEmail())) {
      errors.rejectValue("email", "invalidemail", "Email is not valid.");
    } else if (user.getAcceptTerms() == true
        && userService.doesEmailExist(user.getEmail())) {
      errors.rejectValue("email", "invalidEmail",
          "Email already exists, one email per user.");
    }
    if (user.getAcceptTerms() == false) {
      errors.rejectValue("acceptTerms", "invalidAcceptTerms",
          "You must accept the terms and conditions.");
    }
  }
}
