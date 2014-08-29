package net.ccaper.graffitiTracker.mvc;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.ccaper.graffitiTracker.mvc.validators.FormEmailValidator;
import net.ccaper.graffitiTracker.mvc.validators.FormManageAccountEditValidator;
import net.ccaper.graffitiTracker.mvc.validators.FormPasswordSecurityValidator;
import net.ccaper.graffitiTracker.mvc.validators.FormUserValidator;
import net.ccaper.graffitiTracker.mvc.validators.FormUsernameValidator;
import net.ccaper.graffitiTracker.objects.AppUser;
import net.ccaper.graffitiTracker.objects.EmailForm;
import net.ccaper.graffitiTracker.objects.ManageAccountForm;
import net.ccaper.graffitiTracker.objects.PasswordSecurityForm;
import net.ccaper.graffitiTracker.objects.TextCaptcha;
import net.ccaper.graffitiTracker.objects.UserForm;
import net.ccaper.graffitiTracker.objects.UserSecurityQuestion;
import net.ccaper.graffitiTracker.objects.UsernameForm;
import net.ccaper.graffitiTracker.service.AppUserService;
import net.ccaper.graffitiTracker.service.BannedInetsService;
import net.ccaper.graffitiTracker.service.CaptchaService;
import net.ccaper.graffitiTracker.service.LoginAddressService;
import net.ccaper.graffitiTracker.service.MailService;
import net.ccaper.graffitiTracker.service.UserSecurityService;
import net.ccaper.graffitiTracker.utils.DateFormats;
import net.ccaper.graffitiTracker.utils.Encoder;

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

/**
 * 
 * @author ccaper Controller for all user actions.
 * 
 */
@Controller
@RequestMapping("/users")
@Scope("session")
public class UserController {
  private static final Logger logger = LoggerFactory
      .getLogger(UserController.class);
  // visible for testing
  static final String CONFIRMED_EMAIL_LINK_SERVLET_PATH_WITH_PARAM = "/users/confirmed?registrationConfirmationUniqueUrlParam=";
  // visible for testing
  static final String RESET_PASSWORD_EMAIL_LINK_SERVLET_PATH_WITH_PARAM = "/users/forgotPassword/resetPassword?resetPasswordUniqueUrlParam=";
  // visible for testing
  static final String HOME_LINK = "/home";
  @Autowired
  private AppUserService appUserService;
  @Autowired
  private CaptchaService captchaService;
  @Autowired
  private MailService mailService;
  @Autowired
  private BannedInetsService bannedInetsService;
  @Autowired
  private LoginAddressService loginAddressService;
  @Autowired
  private FormUserValidator formUserValidator;
  @Autowired
  private FormEmailValidator formEmailValidator;
  @Autowired
  private FormUsernameValidator formUsernameValidator;
  @Autowired
  private FormManageAccountEditValidator formManageAccountEditValidator;
  @Autowired
  private FormPasswordSecurityValidator formPasswordSecurityValidator;
  @Autowired
  private VelocityEngine velocityEngine;
  @Autowired
  private UserSecurityService userSecurityService;

  // visible for testing
  /**
   * Sets the {@link net.ccaper.graffitiTracker.service.CaptchaService}.
   * 
   * @param captchaService
   *          the new {@link net.ccaper.graffitiTracker.service.CaptchaService}
   */
  void setCaptchaService(CaptchaService captchaService) {
    this.captchaService = captchaService;
  }

  // visible for testing
  /**
   * Sets the
   * {@link net.ccaper.graffitiTracker.mvc.validators.FormUserValidator}.
   * 
   * @param formUserValidator
   *          the new
   *          {@link net.ccaper.graffitiTracker.mvc.validators.FormUserValidator}
   */
  void setFormUserValidator(FormUserValidator formUserValidator) {
    this.formUserValidator = formUserValidator;
  }

  // visible for testing
  /**
   * Sets the
   * {@link net.ccaper.graffitiTracker.mvc.validators.FormEmailValidator}.
   * 
   * @param formEmailValidator
   *          the new
   *          {@link net.ccaper.graffitiTracker.mvc.validators.FormEmailValidator}
   */
  void setFormEmailValidator(FormEmailValidator formEmailValidator) {
    this.formEmailValidator = formEmailValidator;
  }

  // visible for testing
  /**
   * Sets the
   * {@link net.ccaper.graffitiTracker.mvc.validators.FormUsernameValidator}.
   * 
   * @param formUsernameValidator
   *          the new
   *          {@link net.ccaper.graffitiTracker.mvc.validators.FormUsernameValidator}
   */
  void setFormUsernameValidator(FormUsernameValidator formUsernameValidator) {
    this.formUsernameValidator = formUsernameValidator;
  }

  // visible for testing
  /**
   * Sets the
   * {@link net.ccaper.graffitiTracker.mvc.validators.FormManageAccountEditValidator}
   * .
   * 
   * @param formManageAccountEditValidator
   *          the new
   *          {@link net.ccaper.graffitiTracker.mvc.validators.FormManageAccountFormValidator}
   */
  void setFormManageAccountEditValidator(
      FormManageAccountEditValidator formManageAccountEditValidator) {
    this.formManageAccountEditValidator = formManageAccountEditValidator;
  }

  // visible for testing
  /**
   * Sets the
   * {@link net.ccaper.graffitiTracker.mvc.validators.FormPasswordSecurityValidator}
   * .
   * 
   * @param formPasswordSecurityValidator
   *          the new
   *          {@link net.ccaper.graffitiTracker.mvc.validators.FormPasswordSecurityValidator}
   */
  void setFormPasswordSecurityValidator(
      FormPasswordSecurityValidator formPasswordSecurityValidator) {
    this.formPasswordSecurityValidator = formPasswordSecurityValidator;
  }

  // visible for testing
  /**
   * Sets the {@link net.ccaper.graffitiTracker.service.AppUserService}.
   * 
   * @param appUserService
   *          the new {@link net.ccaper.graffitiTracker.service.AppUserService}
   */
  void setAppUserService(AppUserService appUserService) {
    this.appUserService = appUserService;
  }

  // visible for testing
  /**
   * Sets the {@link net.ccaper.graffitiTracker.service.LoginAddressService}.
   * 
   * @param loginAddressService
   *          the new
   *          {@link net.ccaper.graffitiTracker.service.LoginAddressService}
   */
  void setLoginAddressService(LoginAddressService loginAddressService) {
    this.loginAddressService = loginAddressService;
  }

  // visible for testing
  /**
   * Sets the {@link net.ccaper.graffitiTracker.service.MailService}.
   * 
   * @param mailService
   *          the new {@link net.ccaper.graffitiTracker.service.MailService}
   */
  void setMailService(MailService mailService) {
    this.mailService = mailService;
  }

  // visible for testing
  /**
   * Sets the {@link net.ccaper.graffitiTracker.service.BannedInetsService}.
   * 
   * @param bannedInetsService
   *          the new
   *          {@link net.ccaper.graffitiTracker.service.BannedInetsService}
   */
  void setBannedInetsService(BannedInetsService bannedInetsService) {
    this.bannedInetsService = bannedInetsService;
  }

  // visible for testing
  /**
   * Sets the velocity engine.
   * 
   * @param velocityEngine
   *          the new velocity engine
   */
  void setVelocityEngine(VelocityEngine velocityEngine) {
    this.velocityEngine = velocityEngine;
  }

  // visible for testing
  /**
   * Sets the {@link net.ccaper.graffitiTracker.service.UserSecurityService}.
   * 
   * @param userSecurityService
   *          the new
   *          {@link net.ccaper.graffitiTracker.service.UserSecurityService}
   */
  void setUserSecurityService(UserSecurityService userSecurityService) {
    this.userSecurityService = userSecurityService;
  }

  /**
   * Initial create user screen
   * 
   * @param model
   *          the model
   * @param session
   *          the session
   * @param request
   *          the request
   * @return the view name
   */
  @RequestMapping(value = "/new", method = RequestMethod.GET)
  public String createUserProfile(Model model, HttpSession session,
      HttpServletRequest request) {
    String userInet = request.getRemoteAddr();
    if (!userSecurityService.isUserAnonymous()) {
      String username = userSecurityService.getUsernameFromSecurity();
      AppUser appUser = appUserService.getUserByUsername(username);
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

  /**
   * Handles user response from initial create user screen redirects user to
   * screen stating confirmation registration email sent
   * 
   * @param request
   *          the request
   * @param session
   *          the session
   * @param userForm
   *          the user form
   * @param bindingResult
   *          the binding result
   * @param model
   *          the model
   * @return the view name
   */
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
    if (!userSecurityService.isUserAnonymous()) {
      String username = userSecurityService.getUsernameFromSecurity();
      AppUser appUser = appUserService.getUserByUsername(username);
      model.put("appUser", appUser);
    }
    return "redirect:/users/registered";
  }

  /**
   * Handle user form errors.
   * 
   * @param session
   *          the session
   * @param userForm
   *          the user form
   * @param model
   *          the model
   * @return the view name
   */
  private String handleUserFormErrors(HttpSession session, UserForm userForm,
      Map<String, Object> model) {
    TextCaptcha captcha = captchaService.getTextCaptcha();
    session.setAttribute("textCaptcha", captcha);
    userForm.setTextCaptchaQuestion(captcha.getQuestion());
    userForm.setCaptchaAnswer(null);
    if (!userSecurityService.isUserAnonymous()) {
      String username = userSecurityService.getUsernameFromSecurity();
      AppUser appUser = appUserService.getUserByUsername(username);
      model.put("appUser", appUser);
    }
    return "users/create";
  }

  /**
   * Handle incorrect captcha answer.
   * 
   * @param session
   *          the session
   * @param userForm
   *          the user form
   * @param bindingResult
   *          the binding result
   * @return the view name
   */
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

  /**
   * Handle sending confirmation email.
   * 
   * @param userForm
   *          the user form
   * @param request
   *          the request
   */
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
  /**
   * Generate confirmation email body with velocity engine.
   * 
   * @param userForm
   *          the user form
   * @param request
   *          the request
   * @return the html from the velocity template
   */
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
  /**
   * Generate forgot username email body with velocity engine.
   * 
   * @param username
   *          the username
   * @param request
   *          the request
   * @return the html from the velocity template
   */
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
  /**
   * Generate forgot password email body with velocity engine.
   * 
   * @param resetPassowrdUniqueUrlParam
   *          the reset passowrd unique url param
   * @param request
   *          the request
   * @return the html from the velocity template
   */
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
  /**
   * Generate html from velocity template.
   * 
   * @param model
   *          the model
   * @return the html from the velocity template
   */
  String generateHtmlFromVelocityTemplate(Map<String, Object> model) {
    return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,
        "../../resources/velocityTemplates/emailTemplate.vm", "UTF-8", model);
  }

  // visible for mocking
  /**
   * Generate email address change email body with velocity engine.
   * 
   * @param oldEmail
   *          the old email
   * @param newEmail
   *          the new email
   * @param request
   *          the request
   * @return the html from the velocity template
   */
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

  /**
   * When creating new user, handles redirect to give user messages that
   * confirmation registration email sent
   * 
   * @param model
   *          the model
   * @return the view name
   */
  @RequestMapping(value = "/registered", method = RequestMethod.GET)
  public String showRegisteredUser(Map<String, Object> model) {
    if (!userSecurityService.isUserAnonymous()) {
      String username = userSecurityService.getUsernameFromSecurity();
      AppUser appUser = appUserService.getUserByUsername(username);
      model.put("appUser", appUser);
    }
    return "users/registered";
  }

  /**
   * When creating new {@linkg net.ccaper.graffitiTracker.objects.AppUser},
   * handles click in email confirming the registration of a new user.
   * 
   * @param registrationConfirmationUniqueUrlParam
   *          the registration confirmation unique url param
   * @param model
   *          the model
   * @return the view name
   */
  @RequestMapping(value = "/confirmed", method = RequestMethod.GET)
  public String showConfirmedUser(
      @RequestParam(required = true) String registrationConfirmationUniqueUrlParam,
      Map<String, Object> model) {
    Integer userId = appUserService
        .getUserIdByRegistrationConfirmationUniqueUrlParam(registrationConfirmationUniqueUrlParam);
    if (!userSecurityService.isUserAnonymous()) {
      String username = userSecurityService.getUsernameFromSecurity();
      AppUser appUser = appUserService.getUserByUsername(username);
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
  /**
   * Gets the email link.
   * 
   * @param url
   *          the url
   * @param oldServletPath
   *          the old servlet path
   * @param newServletPath
   *          the new servlet path
   * @param uniqeUrlParam
   *          the uniqe url param
   * @return the email link
   */
  String getEmailLink(String url, String oldServletPath, String newServletPath,
      String uniqeUrlParam) {
    return url.replace(oldServletPath, newServletPath + uniqeUrlParam);
  }

  // visible for testing
  /**
   * Gets the home link.
   * 
   * @param url
   *          the url
   * @param oldServletPath
   *          the old servlet path
   * @return the home link
   */
  String getHomeLink(String url, String oldServletPath) {
    return url.replace(oldServletPath, HOME_LINK);
  }

  // initial forgot username screen
  /**
   * Initial forgot username screen
   * 
   * @param model
   *          the model
   * @return the view name
   */
  @RequestMapping(value = "/forgotUsername", method = RequestMethod.GET, params = "forgotUsername")
  public String forgotUsername(Model model) {
    EmailForm emailForm = new EmailForm();
    emailForm.setRecoverUsername(true);
    model.addAttribute(emailForm);
    if (!userSecurityService.isUserAnonymous()) {
      String username = userSecurityService.getUsernameFromSecurity();
      AppUser appUser = appUserService.getUserByUsername(username);
      model.addAttribute("appUser", appUser);
    }
    return "users/forgotUsername";
  }

  /**
   * Handles user entry of email address for recovering user name.
   * 
   * @param emailForm
   *          the {@link net.ccaper.graffitiTracker.objects.EmailForm}
   * @param bindingResult
   *          the binding result
   * @param request
   *          the request
   * @param model
   *          the model
   * @return the view name
   */
  @RequestMapping(value = "/forgotUsername", params = "recoverUsername", method = RequestMethod.POST)
  public String sendUsername(EmailForm emailForm, BindingResult bindingResult,
      HttpServletRequest request, Map<String, Object> model) {
    if (!userSecurityService.isUserAnonymous()) {
      String username = userSecurityService.getUsernameFromSecurity();
      AppUser appUser = appUserService.getUserByUsername(username);
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

  /**
   * For forgotten username, confirmation that email containing username sent.
   * 
   * @param model
   *          the model
   * @return the view name
   */
  @RequestMapping(value = "/sentUsername", method = RequestMethod.GET)
  public String sentUsername(Map<String, Object> model) {
    if (!userSecurityService.isUserAnonymous()) {
      String username = userSecurityService.getUsernameFromSecurity();
      AppUser appUser = appUserService.getUserByUsername(username);
      model.put("appUser", appUser);
    }
    return "users/sentUsername";
  }

  /**
   * Gives user initial forgot password screen prompting for username to reset
   * password.
   * 
   * @param model
   *          the model
   * @return the view name
   */
  @RequestMapping(value = "/forgotPassword", method = RequestMethod.GET)
  public String forgotPassword(Model model) {
    UsernameForm usernameForm = new UsernameForm();
    usernameForm.setRecoverPassword(true);
    model.addAttribute(usernameForm);
    if (!userSecurityService.isUserAnonymous()) {
      String username = userSecurityService.getUsernameFromSecurity();
      AppUser appUser = appUserService.getUserByUsername(username);
      model.addAttribute("appUser", appUser);
    }
    return "users/forgotPassword";
  }

  /**
   * Looks up email for username, sends user an email, and redirects user to
   * email sent message to reset password.
   * 
   * @param usernameForm
   *          the {@link net.ccaper.graffitiTracker.objects.UsernameForm}
   * @param bindingResult
   *          the binding result
   * @param request
   *          the request
   * @param model
   *          the model
   * @return the view name
   */
  @RequestMapping(value = "/forgotPassword", params = "recoverPassword", method = RequestMethod.POST)
  public String sendPasswordLink(UsernameForm usernameForm,
      BindingResult bindingResult, HttpServletRequest request,
      Map<String, Object> model) {
    if (!userSecurityService.isUserAnonymous()) {
      String username = userSecurityService.getUsernameFromSecurity();
      AppUser appUser = appUserService.getUserByUsername(username);
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
    return "redirect:/users/forgotPassword/sentPassword";
  }

  /**
   * Handles catching redirect to give user message that email was set to reset
   * password.
   * 
   * @param model
   *          the model
   * @return the view name
   */
  @RequestMapping(value = "/forgotPassword/sentPassword", method = RequestMethod.GET)
  public String sentPassword(Map<String, Object> model) {
    if (!userSecurityService.isUserAnonymous()) {
      String username = userSecurityService.getUsernameFromSecurity();
      AppUser appUser = appUserService.getUserByUsername(username);
      model.put("appUser", appUser);
    }
    return "users/sentPassword";
  }

  /**
   * Reset password page after user clicks link in email prompting user for
   * security question and new password deletes unique token after hitting this
   * page expiring link in email.
   * 
   * @param resetPasswordUniqueUrlParam
   *          the reset password unique url param
   * @param model
   *          the model
   * @param request
   *          the request
   * @return the view name
   */
  @RequestMapping(value = "/forgotPassword/resetPassword", method = RequestMethod.GET)
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
      passwordSecurityForm.setUserid(userSecurityQuestion.getUserid());
      passwordSecurityForm.setSecurityQuestion(userSecurityQuestion
          .getSecurityQuestion());
      passwordSecurityForm.setResetPassword(true);
      model.put("passwordSecurityForm", passwordSecurityForm);
    }
    model.put("contextPath", request.getContextPath());
    if (!userSecurityService.isUserAnonymous()) {
      String username = userSecurityService.getUsernameFromSecurity();
      AppUser appUser = appUserService.getUserByUsername(username);
      model.put("appUser", appUser);
    }
    return "users/resetPassword";
  }

  /**
   * Updates the password from the user for lost password, redirects to
   * confirmation page.
   * 
   * @param passwordSecurityForm
   *          the
   *          {@link net.ccaper.graffitiTracker.objects.PasswordSecurityForm}
   * @param bindingResult
   *          the binding result
   * @param request
   *          the request
   * @param model
   *          the model
   * @return the view name
   */
  @RequestMapping(value = "/forgotPassword/resetPassword", method = RequestMethod.POST)
  public String updatePassword(PasswordSecurityForm passwordSecurityForm,
      BindingResult bindingResult, HttpServletRequest request,
      Map<String, Object> model) {
    if (!userSecurityService.isUserAnonymous()) {
      String username = userSecurityService.getUsernameFromSecurity();
      AppUser appUser = appUserService.getUserByUsername(username);
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
    return "redirect:/users/forgotPassword/passwordUpdated";
  }

  /**
   * Gives the user a confirmation screen that the password has been reset,
   * completing the forgot password workflow.
   * 
   * @param model
   *          the model
   * @return the view name
   */
  @RequestMapping(value = "/forgotPassword/passwordUpdated", method = RequestMethod.GET)
  public String passwordUpdated(Map<String, Object> model) {
    if (!userSecurityService.isUserAnonymous()) {
      String username = userSecurityService.getUsernameFromSecurity();
      AppUser appUser = appUserService.getUserByUsername(username);
      model.put("appUser", appUser);
    }
    return "users/passwordUpdated";
  }

  /**
   * Terms and conditions.
   * 
   * @return the view name
   */
  @RequestMapping(value = "/termsAndConditions", method = RequestMethod.GET)
  public String termsAndConditions() {
    return "users/termsAndConditions";
  }

  /**
   * Manage account.
   * 
   * @param model
   *          the model
   * @return the view name
   */
  @RequestMapping(value = "/manageAccount", method = RequestMethod.GET)
  public String manageAccount(Map<String, Object> model) {
    if (!userSecurityService.isUserAnonymous()) {
      String username = userSecurityService.getUsernameFromSecurity();
      AppUser appUser = appUserService.getUserByUsername(username);
      model.put("appUser", appUser);
    }
    return "users/manageAccount";
  }

  /**
   * Manage account edit.
   * 
   * @param model
   *          the model
   * @return the view name
   */
  @RequestMapping(value = "/manageAccountEdit", method = RequestMethod.GET)
  public String manageAccountEdit(Map<String, Object> model) {
    if (!userSecurityService.isUserAnonymous()) {
      String username = userSecurityService.getUsernameFromSecurity();
      AppUser appUser = appUserService.getUserByUsername(username);
      model.put("appUser", appUser);
    }
    ManageAccountForm manageAccountForm = new ManageAccountForm();
    model.put("manageAccountForm", manageAccountForm);
    return "users/manageAccountEdit";
  }

  /**
   * Manage account edit submit.
   * 
   * @param manageAccountForm
   *          the {@link net.ccaper.graffitiTracker.objects.ManageAccountForm}
   * @param bindingResult
   *          the binding result
   * @param model
   *          the model
   * @param request
   *          the request
   * @return the view name
   */
  @RequestMapping(value = "/manageAccountEdit", method = RequestMethod.POST)
  public String manageAccountEditSubmit(ManageAccountForm manageAccountForm,
      BindingResult bindingResult, Map<String, Object> model,
      HttpServletRequest request) {
    formManageAccountEditValidator.validate(manageAccountForm, bindingResult);
    String username = userSecurityService.getUsernameFromSecurity();
    AppUser appUser = appUserService.getUserByUsername(username);
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

  /**
   * Handle sending email address change email.
   * 
   * @param oldEmail
   *          the old email
   * @param newEmail
   *          the new email
   * @param request
   *          the request
   */
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

  /**
   * Manage users.
   * 
   * @param model
   *          the model
   * @return the view name
   */
  @RequestMapping(value = "/manageUsers", method = RequestMethod.GET)
  public String manageUsers(Map<String, Object> model) {
    if (!userSecurityService.isUserAnonymous()) {
      String username = userSecurityService.getUsernameFromSecurity();
      AppUser appUser = appUserService.getUserByUsername(username);
      model.put("appUser", appUser);
    }
    return "users/manageUsers";
  }

  /**
   * Gets the user.
   * 
   * @param userId
   *          the user id
   * @param model
   *          the model
   * @return the view name
   */
  @RequestMapping(method = RequestMethod.GET)
  public String getUser(@RequestParam(required = true) int userId,
      Map<String, Object> model) {
    AppUser user = appUserService.getUserById(userId);
    model.put("appUser", user);
    return "users/user";
  }

  /**
   * Gets the shared inets.
   * 
   * @param inet
   *          the inet
   * @param model
   *          the model
   * @return the view name
   */
  @RequestMapping(value = "/usersSharingInets", method = RequestMethod.GET)
  public String getSharedInets(@RequestParam(required = true) String inet,
      Map<String, Object> model) {
    SortedMap<String, Integer> usersSharingInets = loginAddressService
        .getUsersSharingInet(inet);
    model.put("usersSharingInets", usersSharingInets);
    model.put("displayUsernamesStringLength",
        StringUtils.join(usersSharingInets.keySet(), " ").length());
    return "users/usersSharingInets";
  }
}
