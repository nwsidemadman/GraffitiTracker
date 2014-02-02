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
import net.ccaper.GraffitiTracker.objects.EmailForm;
import net.ccaper.GraffitiTracker.objects.TextCaptcha;
import net.ccaper.GraffitiTracker.objects.UserForm;
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
  static final String EMAIL_LINK_SERVLET_PATH_WITH_PARAM = "/users/confirmed?uniqueUrlParam=";
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
    String uniqueUrlParam = appUserService.getUniqueUrlParam(userForm
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
                        request.getServletPath(), uniqueUrlParam)));
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
      @RequestParam(required = true) String uniqueUrlParam,
      Map<String, Object> model) {
    Integer userid = appUserService.getUseridByUniqueUrlParam(uniqueUrlParam);
    if (userid == null) {
      model.put("confirmed", false);
    } else {
      appUserService.updateAppUserAsActive(userid);
      appUserService
          .deleteRegistrationConfirmationByUniqueUrlParam(uniqueUrlParam);
      model.put("confirmed", true);
    }
    return "users/confirmed";
  }

  // visible for testing
  String getEmailLink(String url, String oldServletPath, String uniqeUrlParam) {
    return url.replace(oldServletPath, EMAIL_LINK_SERVLET_PATH_WITH_PARAM
        + uniqeUrlParam);
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
}
