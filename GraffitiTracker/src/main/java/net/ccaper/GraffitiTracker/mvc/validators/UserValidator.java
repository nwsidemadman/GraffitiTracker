package net.ccaper.GraffitiTracker.mvc.validators;

import net.ccaper.GraffitiTracker.objects.User;
import net.ccaper.GraffitiTracker.service.UserService;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.util.StringUtils;
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
          "The username can not be empty.");
    } else if (user.getUsername().length() < MIN_USERNAME_LENGTH) {
      errors.rejectValue("username", "invalidUsername", String.format(
          "The username \"%s\" must be longer than %s characters.",
          user.getUsername(), MIN_USERNAME_LENGTH));
    } else if (user.getUsername().length() > MAX_USERNAME_LENGTH) {
      errors.rejectValue("username", "invalidUsername", String.format(
          "The username \"%s\" must be no longer than %s characters.",
          user.getUsername(), MAX_USERNAME_LENGTH));
    } else if (StringUtils.containsWhitespace(user.getUsername())) {
      errors.rejectValue(
          "username",
          "invalidUsername",
          String.format("The username \"%s\" can not contain whitespace.",
              user.getUsername()));
    } else if (user.getAcceptTerms() == true && userService.doesUsernameExist(user.getUsername())) {
      errors.rejectValue(
          "username",
          "invalidUsername",
          String.format("The username \"%s\" already exists, please chose another.",
              user.getUsername()));
    }
    if (StringUtils.isEmpty(user.getPassword())) {
      errors.rejectValue("password", "invalidPassword",
          "The password can not be empty.");
    } else if (user.getPassword().length() < MIN_PASSWORD_LENGTH) {
      errors.rejectValue("password", "invalidPassword", String.format(
          "The password must be longer than %s characters.",
          MIN_PASSWORD_LENGTH));
    } else if (user.getPassword().length() > MAX_PASSWORD_LENGTH) {
      errors.rejectValue("password", "invalidPassword", String.format(
          "The password must be no longer than %s characters.",
          MAX_PASSWORD_LENGTH));
    } else if (!user.getPassword().equals(user.getConfirmPassword())) {
      errors.rejectValue("password", "invalidPassword",
          "The passwords do not match");
      errors.rejectValue("confirmPassword", "invalidPassword",
          "The passwords do not match");
    }
    if (StringUtils.isEmpty(user.getConfirmPassword())) {
      errors.rejectValue("confirmPassword", "invalidConfirmPassword",
          "The confirm password can not be empty.");
    }
    if (StringUtils.isEmpty(user.getEmail())) {
      errors.rejectValue("email", "invalidEmail",
          "The email can not be empty.");
    } else if (user.getEmail().length() > MAX_EMAIL_LENGTH) {
      errors.rejectValue("email", "invalidemail", String.format(
          "The email \"%s\" must be no longer than %s characters.",
          user.getEmail(), MAX_EMAIL_LENGTH));
    } else if (!EMAIL_VALIDATOR.isValid(user.getEmail())) {
      errors.rejectValue("email", "invalidemail",
          String.format("The email \"%s\" is not valid.", user.getEmail()));
    } else if (user.getAcceptTerms() == true && userService.doesEmailExist(user.getEmail())) {
      errors.rejectValue(
          "email",
          "invalidEmail",
          String.format("The email \"%s\" already exists, one email per user.",
              user.getEmail()));
    }
    if (user.getAcceptTerms() == false) {
      errors.rejectValue(
          "acceptTerms",
          "invalidAcceptTerms",
          "You must accept the terms and conditions.");
    }
  }
}
