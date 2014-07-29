package net.ccaper.GraffitiTracker.mvc;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.ccaper.GraffitiTracker.mvc.validators.FormEmailValidator;
import net.ccaper.GraffitiTracker.mvc.validators.FormManageAccountEditValidator;
import net.ccaper.GraffitiTracker.mvc.validators.FormPasswordSecurityValidator;
import net.ccaper.GraffitiTracker.mvc.validators.FormUserValidator;
import net.ccaper.GraffitiTracker.mvc.validators.FormUsernameValidator;
import net.ccaper.GraffitiTracker.objects.AppUser;
import net.ccaper.GraffitiTracker.objects.EmailForm;
import net.ccaper.GraffitiTracker.objects.ManageAccountForm;
import net.ccaper.GraffitiTracker.objects.PasswordSecurityForm;
import net.ccaper.GraffitiTracker.objects.TextCaptcha;
import net.ccaper.GraffitiTracker.objects.UserForm;
import net.ccaper.GraffitiTracker.objects.UserSecurityQuestion;
import net.ccaper.GraffitiTracker.objects.UsernameForm;
import net.ccaper.GraffitiTracker.service.AppUserService;
import net.ccaper.GraffitiTracker.service.BannedInetsService;
import net.ccaper.GraffitiTracker.service.CaptchaService;
import net.ccaper.GraffitiTracker.service.MailService;
import net.ccaper.GraffitiTracker.utils.DateFormats;
import net.ccaper.GraffitiTracker.utils.Encoder;

import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/users")
@Scope("session")
public class UserController {
  private static final Logger logger = LoggerFactory
      .getLogger(UserController.class);
  // visible for testing
  static final String CONFIRMED_EMAIL_LINK_SERVLET_PATH_WITH_PARAM = "/users/confirmed?registrationConfirmationUniqueUrlParam=";
  // visible for testing
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
  BannedInetsService bannedInetsService;
  @Autowired
  FormUserValidator formUserValidator;
  @Autowired
  FormEmailValidator formEmailValidator;
  @Autowired
  FormUsernameValidator formUsernameValidator;
  @Autowired
  FormManageAccountEditValidator formManageAccountEditValidator;
  @Autowired
  FormPasswordSecurityValidator formPasswordSecurityValidator;
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

  public void setFormManageAccountEditValidator(
      FormManageAccountEditValidator formManageAccountEditValidator) {
    this.formManageAccountEditValidator = formManageAccountEditValidator;
  }

  public void setFormPasswordSecuritylValidator(
      FormPasswordSecurityValidator formPasswordSecurityValidator) {
    this.formPasswordSecurityValidator = formPasswordSecurityValidator;
  }

  public void setAppUserService(AppUserService appUserService) {
    this.appUserService = appUserService;
  }

  public void setMailService(MailService mailService) {
    this.mailService = mailService;
  }

  public void setBannedInetsService(BannedInetsService bannedInetsService) {
    this.bannedInetsService = bannedInetsService;
  }

  public void setVelocityEngine(VelocityEngine velocityEngine) {
    this.velocityEngine = velocityEngine;
  }

  @RequestMapping(value = "/new", method = RequestMethod.GET)
  public String createUserProfile(Model model, HttpSession session,
      HttpServletRequest request) {
    String userInet = request.getRemoteAddr();
    if (!isUserAnonymous()) {
      String username = getUsernameFromSecurity();
      AppUser appUser = appUserService.getUser(username);
      model.addAttribute("appUser", appUser);
    }
    if (bannedInetsService.isInetBanned(userInet)) {
      bannedInetsService.updateNumberRegistrationAttemptsInetInRange(userInet);
      return "users/banned";
    }
    TextCaptcha captcha = captchaService.getTextCaptcha();
    session.setAttribute("textCaptcha", captcha);
    UserForm userForm = new UserForm();
    userForm.setTextCaptchaQuestion(captcha.getQuestion());
    model.addAttribute(userForm);
    return "users/create";
  }

  // visible for mocking
  String getUsernameFromSecurity() {
    return SecurityContextHolder.getContext().getAuthentication().getName();
  }

  // visible for mocking
  boolean isUserAnonymous() {
    AuthenticationTrustResolver authenticationTrustResolver = new AuthenticationTrustResolverImpl();
    return authenticationTrustResolver.isAnonymous(SecurityContextHolder
        .getContext().getAuthentication());
  }

  @RequestMapping(value = "/new", method = RequestMethod.POST)
  public String addAppUserFromForm(HttpServletRequest request,
      HttpSession session, UserForm userForm, BindingResult bindingResult,
      Map<String, Object> model) {
    formUserValidator.validate(userForm, bindingResult);
    if (bindingResult.hasErrors()) {
      return handleUserFormErrors(session, userForm, model);
    }
    TextCaptcha captcha = (TextCaptcha) session.getAttribute("textCaptcha");
    if (!captchaService.isCaptchaAnswerCorrect(captcha,
        StringUtils.trimToEmpty(userForm.getCaptchaAnswer()))) {
      return handleIncorrectCaptchaAnswer(session, userForm, bindingResult);
    }
    appUserService.addAppUser(userForm.createAppUserFromUserForm());
    handleSendingConfirmationEmail(userForm, request);
    if (!isUserAnonymous()) {
      String username = getUsernameFromSecurity();
      AppUser appUser = appUserService.getUser(username);
      model.put("appUser", appUser);
    }
    return "redirect:/users/registered";
  }

  private String handleUserFormErrors(HttpSession session, UserForm userForm,
      Map<String, Object> model) {
    TextCaptcha captcha = captchaService.getTextCaptcha();
    session.setAttribute("textCaptcha", captcha);
    userForm.setTextCaptchaQuestion(captcha.getQuestion());
    userForm.setCaptchaAnswer(null);
    if (!isUserAnonymous()) {
      String username = getUsernameFromSecurity();
      AppUser appUser = appUserService.getUser(username);
      model.put("appUser", appUser);
    }
    return "users/create";
  }

  private String handleIncorrectCaptchaAnswer(HttpSession session,
      UserForm userForm, BindingResult bindingResult) {
    TextCaptcha newCaptcha = captchaService.getTextCaptcha();
    session.setAttribute("textCaptcha", newCaptcha);
    userForm.setTextCaptchaQuestion(newCaptcha.getQuestion());
    userForm.setCaptchaAnswer(null);
    bindingResult.rejectValue("captchaAnswer", "incorrectCaptchaAnswer",
        "Incorrect captcha answer.");
    return "users/create";
  }

  private void handleSendingConfirmationEmail(UserForm userForm,
      HttpServletRequest request) {
    List<String> recipients = new ArrayList<String>(2);
    recipients.add(userForm.getEmail());
    mailService.sendVelocityEmail(recipients,
        "GraffitiTracker Registration Confirmation",
        generateConfirmationEmailBodyWithVelocityEngine(userForm, request));
  }

  // visible for mocking
  String generateConfirmationEmailBodyWithVelocityEngine(UserForm userForm,
      HttpServletRequest request) {
    Map<String, Object> model = new HashMap<String, Object>(3);
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
    Map<String, Object> model = new HashMap<String, Object>(3);
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
    Map<String, Object> model = new HashMap<String, Object>(3);
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

  // visible for mocking
  String generateEmailAddressChangeEmailBodyWithVelocityEngine(String oldEmail,
      String newEmail, HttpServletRequest request) {
    Map<String, Object> model = new HashMap<String, Object>(3);
    model.put("copyrightYear", DateFormats.YEAR_FORMAT.format(new Date()));
    model
        .put(
            "content",
            String
                .format(
                    "<p>A change was made to the stored email address, and we thought you might like to know.</p>"
                        + "<p>The old email address was '%s' and the new email address is '%s'.</p>"
                        + "<p>If this change was made in error, please correct your email address on file.</p>",
                    oldEmail, newEmail));
    model.put(
        "home_link",
        getHomeLink(request.getRequestURL().toString(),
            request.getServletPath()));
    return generateHtmlFromVelocityTemplate(model);
  }

  @RequestMapping(value = "/registered", method = RequestMethod.GET)
  public String showRegisteredUser(Map<String, Object> model) {
    if (!isUserAnonymous()) {
      String username = getUsernameFromSecurity();
      AppUser appUser = appUserService.getUser(username);
      model.put("appUser", appUser);
    }
    return "users/registered";
  }

  @RequestMapping(value = "/confirmed", method = RequestMethod.GET)
  public String showConfirmedUser(
      @RequestParam(required = true) String registrationConfirmationUniqueUrlParam,
      Map<String, Object> model) {
    Integer userId = appUserService
        .getUserIdByRegistrationConfirmationUniqueUrlParam(registrationConfirmationUniqueUrlParam);
    if (!isUserAnonymous()) {
      String username = getUsernameFromSecurity();
      AppUser appUser = appUserService.getUser(username);
      model.put("appUser", appUser);
    }
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
    emailForm.setRecoverUsername(true);
    model.addAttribute(emailForm);
    if (!isUserAnonymous()) {
      String username = getUsernameFromSecurity();
      AppUser appUser = appUserService.getUser(username);
      model.addAttribute("appUser", appUser);
    }
    return "users/forgotUsername";
  }

  @RequestMapping(params = "recoverUsername", method = RequestMethod.POST)
  public String sendUsername(EmailForm emailForm, BindingResult bindingResult,
      HttpServletRequest request, Map<String, Object> model) {
    if (!isUserAnonymous()) {
      String username = getUsernameFromSecurity();
      AppUser appUser = appUserService.getUser(username);
      model.put("appUser", appUser);
    }
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
  public String sentUsername(Map<String, Object> model) {
    if (!isUserAnonymous()) {
      String username = getUsernameFromSecurity();
      AppUser appUser = appUserService.getUser(username);
      model.put("appUser", appUser);
    }
    return "users/sentUsername";
  }

  @RequestMapping(method = RequestMethod.GET, params = "forgotPassword")
  public String forgotPassword(Model model) {
    UsernameForm usernameForm = new UsernameForm();
    usernameForm.setRecoverPassword(true);
    model.addAttribute(usernameForm);
    if (!isUserAnonymous()) {
      String username = getUsernameFromSecurity();
      AppUser appUser = appUserService.getUser(username);
      model.addAttribute("appUser", appUser);
    }
    return "users/forgotPassword";
  }

  @RequestMapping(params = "recoverPassword", method = RequestMethod.POST)
  public String sendPasswordLink(UsernameForm usernameForm,
      BindingResult bindingResult, HttpServletRequest request,
      Map<String, Object> model) {
    if (!isUserAnonymous()) {
      String username = getUsernameFromSecurity();
      AppUser appUser = appUserService.getUser(username);
      model.put("appUser", appUser);
    }
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
  public String sentPassword(Map<String, Object> model) {
    if (!isUserAnonymous()) {
      String username = getUsernameFromSecurity();
      AppUser appUser = appUserService.getUser(username);
      model.put("appUser", appUser);
    }
    return "users/sentPassword";
  }

  @RequestMapping(value = "/resetPassword", method = RequestMethod.GET)
  public String resetPassword(
      @RequestParam(required = true) String resetPasswordUniqueUrlParam,
      Map<String, Object> model, HttpServletRequest request) {
    UserSecurityQuestion userSecurityQuestion = appUserService
        .getUserSecurityQuestionByResetPasswordUniqueUrlParam(resetPasswordUniqueUrlParam);
    if (userSecurityQuestion == null) {
      model.put("exists", false);
    } else {
      appUserService
          .deleteResetPasswordByUniqueUrlParam(resetPasswordUniqueUrlParam);
      model.put("exists", true);
      PasswordSecurityForm passwordSecurityForm = new PasswordSecurityForm();
      passwordSecurityForm.setUserId(userSecurityQuestion.getUserid());
      passwordSecurityForm.setSecurityQuestion(userSecurityQuestion
          .getSecurityQuestion());
      passwordSecurityForm.setResetPassword(true);
      model.put("passwordSecurityForm", passwordSecurityForm);
    }
    model.put("contextPath", request.getContextPath());
    if (!isUserAnonymous()) {
      String username = getUsernameFromSecurity();
      AppUser appUser = appUserService.getUser(username);
      model.put("appUser", appUser);
    }
    return "users/resetPassword";
  }

  @RequestMapping(params = "resetPassword", method = RequestMethod.POST)
  public String updatePassword(PasswordSecurityForm passwordSecurityForm,
      BindingResult bindingResult, HttpServletRequest request,
      Map<String, Object> model) {
    if (!isUserAnonymous()) {
      String username = getUsernameFromSecurity();
      AppUser appUser = appUserService.getUser(username);
      model.put("appUser", appUser);
    }
    formPasswordSecurityValidator.validate(passwordSecurityForm, bindingResult);
    if (bindingResult.hasErrors()) {
      model.put("exists", true);
      model.put("passwordSecurityForm", passwordSecurityForm);
      model.put("contextPath", request.getContextPath());
      return "users/resetPassword";
    }
    appUserService.updatePasswordByUserid(passwordSecurityForm.getUserid(),
        Encoder.encodeString(appUserService
            .getUsernameByUserid(passwordSecurityForm.getUserid()),
            passwordSecurityForm.getPassword()));
    return "redirect:/users/passwordUpdated";
  }

  @RequestMapping(value = "/passwordUpdated", method = RequestMethod.GET)
  public String passwordUpdated(Map<String, Object> model) {
    if (!isUserAnonymous()) {
      String username = getUsernameFromSecurity();
      AppUser appUser = appUserService.getUser(username);
      model.put("appUser", appUser);
    }
    return "users/passwordUpdated";
  }

  @RequestMapping(value = "/termsAndConditions", method = RequestMethod.GET)
  public String termsAndConditions() {
    // TODO: can this be static?
    return "users/termsAndConditions";
  }

  @RequestMapping(value = "/manageAccount", method = RequestMethod.GET)
  public String manageAccount(Map<String, Object> model) {
    if (!isUserAnonymous()) {
      String username = getUsernameFromSecurity();
      AppUser appUser = appUserService.getUser(username);
      model.put("appUser", appUser);
    }
    return "users/manageAccount";
  }

  @RequestMapping(value = "/manageAccountEdit", method = RequestMethod.GET)
  public String manageAccountEdit(Map<String, Object> model) {
    if (!isUserAnonymous()) {
      String username = getUsernameFromSecurity();
      AppUser appUser = appUserService.getUser(username);
      model.put("appUser", appUser);
    }
    ManageAccountForm manageAccountForm = new ManageAccountForm();
    model.put("manageAccountForm", manageAccountForm);
    return "users/manageAccountEdit";
  }

  @RequestMapping(value = "/manageAccountEdit", method = RequestMethod.POST)
  public String manageAccountEditSubmit(ManageAccountForm manageAccountForm,
      BindingResult bindingResult, Map<String, Object> model,
      HttpServletRequest request) {
    formManageAccountEditValidator.validate(manageAccountForm, bindingResult);
    String username = getUsernameFromSecurity();
    AppUser appUser = appUserService.getUser(username);
    model.put("appUser", appUser);
    if (bindingResult.hasErrors()) {
      return "users/manageAccountEdit";
    }
    if (StringUtils.isNotEmpty(manageAccountForm.getEmail())) {
      handleSendingEmailAddressChangeEmail(appUser.getEmail(),
          manageAccountForm.getEmail(), request);
      appUserService.updateEmailByUserid(appUser.getUserId(),
          manageAccountForm.getEmail());
    }
    if (StringUtils.isNotEmpty(manageAccountForm.getSecurityQuestion())) {
      appUserService.updateSecurityQuestionByUserid(appUser.getUserId(),
          manageAccountForm.getSecurityQuestion());
    }
    if (StringUtils.isNotEmpty(manageAccountForm.getSecurityAnswer())) {
      appUserService.updateSecurityAnswerByUserid(appUser.getUserId(),
          manageAccountForm.getSecurityAnswer());
    }
    if (StringUtils.isNotEmpty(manageAccountForm.getPassword())) {
      appUserService.updatePasswordByUserid(appUser.getUserId(), Encoder
          .encodeString(
              appUserService.getUsernameByUserid(appUser.getUserId()),
              manageAccountForm.getPassword()));
    }
    model.put("appUser", appUser);
    return "redirect:/users/manageAccount";
  }

  private void handleSendingEmailAddressChangeEmail(String oldEmail,
      String newEmail, HttpServletRequest request) {
    List<String> recipients = new ArrayList<String>(1);
    recipients.add(oldEmail);
    recipients.add(newEmail);
    mailService.sendVelocityEmail(
        recipients,
        "GraffitiTracker Email Address Change Notification",
        generateEmailAddressChangeEmailBodyWithVelocityEngine(oldEmail,
            newEmail, request));
  }
  
  @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
  public @ResponseBody List<AppUser> getAllUsers(Model model, HttpSession session,
      HttpServletRequest request) {
    return appUserService.getAllUsers();
  }
}
