package net.ccaper.GraffitiTracker.mvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.ccaper.GraffitiTracker.mvc.validators.FormUserValidator;
import net.ccaper.GraffitiTracker.objects.TextCaptcha;
import net.ccaper.GraffitiTracker.objects.UserForm;
import net.ccaper.GraffitiTracker.service.AppUserService;
import net.ccaper.GraffitiTracker.service.CaptchaService;
import net.ccaper.GraffitiTracker.service.MailService;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/users")
@SessionAttributes("textCaptcha")
public class UserController {
  private static final Logger logger = LoggerFactory
      .getLogger(UserController.class);
  // visible for testing
  static final String EMAIL_LINK_SERVLET_PATH_WITH_PARAM = "/users/confirmed?uniqueUrlParam=";
  @Autowired
  AppUserService appUserService;
  @Autowired
  CaptchaService captchaService;
  @Autowired
  MailService mailService;
  @Autowired
  FormUserValidator formUserValidator;

  public void setCaptchaService(CaptchaService captchaService) {
    this.captchaService = captchaService;
  }

  public void setFormUserValidator(FormUserValidator formUserValidator) {
    this.formUserValidator = formUserValidator;
  }

  public void setAppUserService(AppUserService appUserService) {
    this.appUserService = appUserService;
  }

  public void setMailService(MailService mailService) {
    this.mailService = mailService;
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
      TextCaptcha captcha = captchaService.getTextCaptcha();
      session.setAttribute("textCaptcha", captcha);
      userForm.setTextCaptchaQuestion(captcha.getQuestion());
      userForm.setCaptchaAnswer(null);
      return "users/edit";
    }
    TextCaptcha captcha = (TextCaptcha) session.getAttribute("textCaptcha");
    if (!captchaService.isCaptchaAnswerCorrect(captcha,
        StringUtils.trimToEmpty(userForm.getCaptchaAnswer()))) {
      TextCaptcha newCaptcha = captchaService.getTextCaptcha();
      session.setAttribute("textCaptcha", newCaptcha);
      userForm.setTextCaptchaQuestion(newCaptcha.getQuestion());
      userForm.setCaptchaAnswer(null);
      bindingResult.rejectValue("captchaAnswer", "incorrectCaptchaAnswer",
          "Incorrect captcha answer.");
      return "users/edit";
    }
    appUserService.addAppUser(userForm.createAppUserFromUserForm());
    appUserService.addRegistrationConfirmation(userForm.getUsername());
    String uniqueUrlParam = appUserService.getUniqueUrlParam(userForm
        .getUsername());
    List<String> recipients = new ArrayList<String>(1);
    recipients.add(userForm.getEmail());
    String link = getEmailLink(request.getRequestURL().toString(),
        request.getServletPath(), uniqueUrlParam);
    mailService
        .sendRichEmail(
            recipients,
            "GraffitiTracker Registration Confirmation",
            String
                .format(
                    "<div>"
                    + "<p>Thank you for registering at GraffitiTracker.</p>"
                    + "<p>To complete your registration, please click the following link with 48 hours of receiving this email.</p>"
                    + "<p><a href='%s'>%s</a></p>"
                    + "</div>",
                    link, link));
    return "redirect:/users/registered";
  }

  @RequestMapping(value = "/registered", method = RequestMethod.GET)
  public String showRegisteredUser() {
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
}
