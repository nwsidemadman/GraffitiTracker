package net.ccaper.GraffitiTracker.mvc;

import javax.servlet.http.HttpSession;

import net.ccaper.GraffitiTracker.mvc.validators.FormUserValidator;
import net.ccaper.GraffitiTracker.objects.TextCaptcha;
import net.ccaper.GraffitiTracker.objects.UserForm;
import net.ccaper.GraffitiTracker.service.AppUserService;
import net.ccaper.GraffitiTracker.service.CaptchaService;

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
  @Autowired
  AppUserService appUserService;
  @Autowired
  CaptchaService captchaService;
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
  public String addAppUserFromForm(HttpSession session, UserForm userForm,
      BindingResult bindingResult) {
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
    // TODO: url encode email param
    return String.format("redirect:/users/registered?username=%s&email=%s",
        userForm.getUsername(), userForm.getEmail());
  }

  @RequestMapping(value = "/registered", method = RequestMethod.GET)
  public String showRegisteredUser(
      @RequestParam(required = true) String username,
      @RequestParam(required = true) String email) {
    if (StringUtils.isEmpty(username) || StringUtils.isEmpty(email)) {
      return "redirect:/home";
    }
    return "users/registered";
  }
}
