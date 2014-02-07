package net.ccaper.GraffitiTracker.mvc;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.ccaper.GraffitiTracker.mvc.validators.FormEmailValidator;
import net.ccaper.GraffitiTracker.mvc.validators.FormUserValidator;
import net.ccaper.GraffitiTracker.mvc.validators.FormUsernameValidator;
import net.ccaper.GraffitiTracker.objects.EmailForm;
import net.ccaper.GraffitiTracker.objects.PasswordSecurityForm;
import net.ccaper.GraffitiTracker.objects.TextCaptcha;
import net.ccaper.GraffitiTracker.objects.UserForm;
import net.ccaper.GraffitiTracker.objects.UsernameForm;
import net.ccaper.GraffitiTracker.service.AppUserService;
import net.ccaper.GraffitiTracker.service.CaptchaService;
import net.ccaper.GraffitiTracker.service.MailService;
import net.ccaper.GraffitiTracker.utils.DateFormats;

import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/users")
@Scope("session")
public class UserController {
  private static final Logger logger = LoggerFactory
      .getLogger(UserController.class);
  // visible for testing
  static final String CONFIRMED_EMAIL_LINK_SERVLET_PATH_WITH_PARAM = "/users/confirmed?registrationConfirmationUniqueUrlParam=";
  // visible for testing
  // TODO: fix link
  static final String RESET_PASSWORD_EMAIL_LINK_SERVLET_PATH_WITH_PARAM = "/users/resetPassword?resetPasswordUniqueUrlParam=";
  // visible for testing
  static final String HOME_LINK = "/home";
  @Autowired
  AppUserService appUserService;
  @Autowired
  CaptchaService captchaService;
  @Autowired
  MailService mailService;
  @Autowired
  FormUserValidator formUserValidator;
  @Autowired
  FormEmailValidator formEmailValidator;
  @Autowired
  FormUsernameValidator formUsernameValidator;
  @Autowired
  VelocityEngine velocityEngine;

  public void setCaptchaService(CaptchaService captchaService) {
    this.captchaService = captchaService;
  }

  public void setFormUserValidator(FormUserValidator formUserValidator) {
    this.formUserValidator = formUserValidator;
  }

  public void setFormEmailValidator(FormEmailValidator formEmailValidator) {
    this.formEmailValidator = formEmailValidator;
  }

  public void setFormUsernamelValidator(
      FormUsernameValidator formUsernameValidator) {
    this.formUsernameValidator = formUsernameValidator;
  }

  public void setAppUserService(AppUserService appUserService) {
    this.appUserService = appUserService;
  }

  public void setMailService(MailService mailService) {
    this.mailService = mailService;
  }

  public void setVelocityEngine(VelocityEngine velocityEngine) {
    this.velocityEngine = velocityEngine;
  }

  @RequestMapping(method = RequestMethod.GET, params = "new")
  public String createUserProfile(Model model, HttpSession session) {
    TextCaptcha captcha = captchaService.getTextCaptcha();
    session.setAttribute("textCaptcha", captcha);
    UserForm userForm = new UserForm();
    userForm.setTextCaptchaQuestion(captcha.getQuestion());
    model.addAttribute(userForm);
    return "users/edit";
  }

  @RequestMapping(method = RequestMethod.POST)
  public String addAppUserFromForm(HttpServletRequest request,
      HttpSession session, UserForm userForm, BindingResult bindingResult) {
    formUserValidator.validate(userForm, bindingResult);
    if (bindingResult.hasErrors()) {
      return handleUserFormErrors(session, userForm);
    }
    TextCaptcha captcha = (TextCaptcha) session.getAttribute("textCaptcha");
    if (!captchaService.isCaptchaAnswerCorrect(captcha,
        StringUtils.trimToEmpty(userForm.getCaptchaAnswer()))) {
      return handleIncorrectCaptchaAnswer(session, userForm, bindingResult);
    }
    appUserService.addAppUser(userForm.createAppUserFromUserForm());
    handleSendingConfirmationEmail(userForm, request);
    return "redirect:/users/registered";
  }

  private String handleUserFormErrors(HttpSession session, UserForm userForm) {
    TextCaptcha captcha = captchaService.getTextCaptcha();
    session.setAttribute("textCaptcha", captcha);
    userForm.setTextCaptchaQuestion(captcha.getQuestion());
    userForm.setCaptchaAnswer(null);
    return "users/edit";
  }

  private String handleIncorrectCaptchaAnswer(HttpSession session,
      UserForm userForm, BindingResult bindingResult) {
    TextCaptcha newCaptcha = captchaService.getTextCaptcha();
    session.setAttribute("textCaptcha", newCaptcha);
    userForm.setTextCaptchaQuestion(newCaptcha.getQuestion());
    userForm.setCaptchaAnswer(null);
    bindingResult.rejectValue("captchaAnswer", "incorrectCaptchaAnswer",
        "Incorrect captcha answer.");
    return "users/edit";
  }

  private void handleSendingConfirmationEmail(UserForm userForm,
      HttpServletRequest request) {
    appUserService.addRegistrationConfirmation(userForm.getUsername());
    List<String> recipients = new ArrayList<String>(1);
    recipients.add(userForm.getEmail());
    mailService.sendVelocityEmail(recipients,
        "GraffitiTracker Registration Confirmation",
        generateConfirmationEmailBodyWithVelocityEngine(userForm, request));
  }

  // visible for mocking
  String generateConfirmationEmailBodyWithVelocityEngine(UserForm userForm,
      HttpServletRequest request) {
    Map<String, Object> model = new HashMap<String, Object>();
    model.put("copyrightYear", DateFormats.YEAR_FORMAT.format(new Date()));
    String registrationConfirmationUniqueUrlParam = appUserService
        .getRegistrationConfirmationUniqueUrlParamByUsername(userForm
            .getUsername());
    model
    .put(
        "content",
        String
        .format(
            "<p>Thank you for registering at GraffitiTracker.</p>"
                + "<p>To complete your registration, please click the following link within 48 hours of receiving this email:</p>"
                + "<p><a href='%s'>Confirm Registration</a></p>",
                getEmailLink(request.getRequestURL().toString(),
                    request.getServletPath(),
                    CONFIRMED_EMAIL_LINK_SERVLET_PATH_WITH_PARAM,
                    registrationConfirmationUniqueUrlParam)));
    model.put(
        "home_link",
        getHomeLink(request.getRequestURL().toString(),
            request.getServletPath()));
    return generateHtmlFromVelocityTemplate(model);
  }

  // visible for mocking
  String generateForgotUsernameEmailBodyWithVelocityEngine(String username,
      HttpServletRequest request) {
    Map<String, Object> model = new HashMap<String, Object>();
    model.put("copyrightYear", DateFormats.YEAR_FORMAT.format(new Date()));
    model.put("content", String.format(
        "<p>You requested to recover your username.</p>"
            + "<p>Your username is:</p>" + "<p>%s</p>", username));
    model.put(
        "home_link",
        getHomeLink(request.getRequestURL().toString(),
            request.getServletPath()));
    return generateHtmlFromVelocityTemplate(model);
  }

  // visible for mocking
  String generateForgotPasswordEmailBodyWithVelocityEngine(
      String resetPassowrdUniqueUrlParam, HttpServletRequest request) {
    Map<String, Object> model = new HashMap<String, Object>();
    model.put("copyrightYear", DateFormats.YEAR_FORMAT.format(new Date()));
    model
    .put(
        "content",
        String
        .format(
            "<p>You requested to reset your password.</p>"
                + "<p>To reset your password, please click the following link within 24 hours of receiving this email:</p>"
                + "<p><a href='%s'>ResetPassword</a></p>",
                getEmailLink(request.getRequestURL().toString(),
                    request.getServletPath(),
                    RESET_PASSWORD_EMAIL_LINK_SERVLET_PATH_WITH_PARAM,
                    resetPassowrdUniqueUrlParam)));
    model.put(
        "home_link",
        getHomeLink(request.getRequestURL().toString(),
            request.getServletPath()));
    return generateHtmlFromVelocityTemplate(model);
  }

  // visible for mocking
  String generateHtmlFromVelocityTemplate(Map<String, Object> model) {
    return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,
        "../../resources/velocityTemplates/emailTemplate.vm", "UTF-8", model);
  }

  @RequestMapping(value = "/registered", method = RequestMethod.GET)
  public String showRegisteredUser() {
    // TODO: can this be static?
    return "users/registered";
  }

  @RequestMapping(value = "/confirmed", method = RequestMethod.GET)
  public String showConfirmedUser(
      @RequestParam(required = true) String registrationConfirmationUniqueUrlParam,
      Map<String, Object> model) {
    Integer userId = appUserService
        .getUserIdByRegistrationConfirmationUniqueUrlParam(registrationConfirmationUniqueUrlParam);
    if (userId == null) {
      model.put("confirmed", false);
    } else {
      appUserService.updateAppUserAsActive(userId);
      appUserService
      .deleteRegistrationConfirmationByUniqueUrlParam(registrationConfirmationUniqueUrlParam);
      model.put("confirmed", true);
    }
    return "users/confirmed";
  }

  // visible for testing
  String getEmailLink(String url, String oldServletPath, String newServletPath,
      String uniqeUrlParam) {
    return url.replace(oldServletPath, newServletPath + uniqeUrlParam);
  }

  // visible for testing
  String getHomeLink(String url, String oldServletPath) {
    return url.replace(oldServletPath, HOME_LINK);
  }

  @RequestMapping(method = RequestMethod.GET, params = "forgotUsername")
  public String forgotUsername(Model model) {
    EmailForm emailForm = new EmailForm();
    model.addAttribute(emailForm);
    return "users/forgotUsername";
  }

  @RequestMapping(params = "recoverUsername", method = RequestMethod.POST)
  public String sendUsername(EmailForm emailForm, BindingResult bindingResult,
      HttpServletRequest request) {
    formEmailValidator.validate(emailForm, bindingResult);
    if (bindingResult.hasErrors()) {
      return "users/forgotUsername";
    }
    String username = appUserService.getUsernameByEmail(emailForm.getEmail());
    if (username != null) {
      List<String> recipients = new ArrayList<String>(1);
      recipients.add(emailForm.getEmail());
      mailService.sendVelocityEmail(recipients, "Recover Username",
          generateForgotUsernameEmailBodyWithVelocityEngine(username, request));
    }
    return "redirect:/users/sentUsername";
  }

  @RequestMapping(value = "/sentUsername", method = RequestMethod.GET)
  public String sentUsername() {
    // TODO: can this be static?
    return "users/sentUsername";
  }

  @RequestMapping(method = RequestMethod.GET, params = "forgotPassword")
  public String forgotPassword(Model model) {
    UsernameForm usernameForm = new UsernameForm();
    model.addAttribute(usernameForm);
    return "users/forgotPassword";
  }

  @RequestMapping(params = "recoverPassword", method = RequestMethod.POST)
  public String sendPasswordLink(UsernameForm usernameForm,
      BindingResult bindingResult, HttpServletRequest request) {
    formUsernameValidator.validate(usernameForm, bindingResult);
    if (bindingResult.hasErrors()) {
      return "users/forgotPassword";
    }
    String email = appUserService
        .getEmailByUsername(usernameForm.getUsername());
    if (email != null) {
      List<String> recipients = new ArrayList<String>(1);
      recipients.add(email);
      appUserService.addResetPassword(usernameForm.getUsername());
      mailService.sendVelocityEmail(
          recipients,
          "Recover Password",
          generateForgotPasswordEmailBodyWithVelocityEngine(appUserService
              .getResetPasswordUniqueUrlParamByUsername(usernameForm
                  .getUsername()), request));
    }
    return "redirect:/users/sentPassword";
  }

  @RequestMapping(value = "/sentPassword", method = RequestMethod.GET)
  public String sentPassword() {
    // TODO: can this be static?
    return "users/sentPassword";
  }

  @RequestMapping(value = "/resetPassword", method = RequestMethod.GET)
  public String resetPassword(
      @RequestParam(required = true) String resetPasswordUniqueUrlParam,
      Map<String, Object> model) {
    String securityQuestion = appUserService
        .getSecurityQuestionByResetPasswordUniqueUrlParam(resetPasswordUniqueUrlParam);
    if (securityQuestion == null) {
      model.put("exists", false);
    } else {
      // TODO: uncomment line once bug fixed
      //appUserService.deleteResetPasswordByUniqueUrlParam(resetPasswordUniqueUrlParam);
      model.put("exists", true);
      // TODO: create security form, put on model, update test
      PasswordSecurityForm passwordSecurityForm = new PasswordSecurityForm();
      passwordSecurityForm.setSecurityQuestion(securityQuestion);
      model.put("passwordSecurityForm", passwordSecurityForm);
    }
    // TODO: this will need to direct to https
    return "users/resetPassword";
  }

  @RequestMapping(params = "resetPassword", method = RequestMethod.POST)
  public String updatePassword(PasswordSecurityForm passwordSecurityForm,
      BindingResult bindingResult, HttpServletRequest request) {
    logger.info("updatePassword hit");
    // TODO: unit test
    // TODO: validate
    // TODO: update password
    return "redirect:/users/passwordUpdated";
  }
}
