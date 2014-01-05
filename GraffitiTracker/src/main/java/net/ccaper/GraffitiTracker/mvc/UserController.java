package net.ccaper.GraffitiTracker.mvc;

import javax.servlet.http.HttpSession;

import net.ccaper.GraffitiTracker.mvc.validators.UserValidator;
import net.ccaper.GraffitiTracker.objects.TextCaptcha;
import net.ccaper.GraffitiTracker.objects.User;
import net.ccaper.GraffitiTracker.service.CaptchaService;
import net.ccaper.GraffitiTracker.service.UserService;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/users")
@SessionAttributes("textCaptcha")
public class UserController {
  // TODO: unit test
  private static final Logger logger = LoggerFactory
      .getLogger(UserController.class);
  @Autowired
  UserService userService;
  @Autowired
  CaptchaService captchaService;
  @Autowired
  UserValidator userValidator;
  
  public void setCaptchaService(CaptchaService captchaService) {
    this.captchaService = captchaService;
  }
  
  public void setUserValidator(UserValidator userValidator) {
    this.userValidator = userValidator;
  }
  
  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  @RequestMapping(method = RequestMethod.GET, params = "new")
  public String createUserProfile(Model model, HttpSession session) {
    TextCaptcha captcha = captchaService.getTextCaptcha();
    session.setAttribute("textCaptcha", captcha);
    User user = new User();
    user.setTextCaptchaQuestion(captcha.getQuestion());
    model.addAttribute(user);
    return "users/edit";
  }

  @RequestMapping(method = RequestMethod.POST)
  public String addUserFromForm(HttpSession session, User user,
      BindingResult bindingResult) {
    userValidator.validate(user, bindingResult);
    if (bindingResult.hasErrors()) {
      TextCaptcha captcha = captchaService.getTextCaptcha();
      session.setAttribute("textCaptcha", captcha);
      user.setTextCaptchaQuestion(captcha.getQuestion());
      user.setCaptchaAnswer(null);
      return "users/edit";
    }
    TextCaptcha captcha = (TextCaptcha) session.getAttribute("textCaptcha");
    if (!captchaService.isCaptchaAnswerCorrect(captcha,
        StringUtils.trimToEmpty(user.getCaptchaAnswer()))) {
      TextCaptcha newCaptcha = captchaService.getTextCaptcha();
      session.setAttribute("textCaptcha", newCaptcha);
      user.setTextCaptchaQuestion(newCaptcha.getQuestion());
      user.setCaptchaAnswer(null);
      bindingResult.rejectValue("captchaAnswer", "incorrectCaptchaAnswer",
          "Incorrect captcha answer.");
      return "users/edit";
    }
    user.encodePassword();
    userService.addUser(user);
    return "redirect:/home";
  }
}
