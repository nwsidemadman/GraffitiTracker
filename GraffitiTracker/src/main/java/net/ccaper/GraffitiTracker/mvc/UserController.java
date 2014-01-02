package net.ccaper.GraffitiTracker.mvc;

import net.ccaper.GraffitiTracker.mvc.validators.UserValidator;
import net.ccaper.GraffitiTracker.objects.User;
import net.ccaper.GraffitiTracker.service.CaptchaService;
import net.ccaper.GraffitiTracker.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/users")
public class UserController {
  private static final Logger logger = LoggerFactory
      .getLogger(UserController.class);
  @Autowired
  UserService userService;
  @Autowired
  CaptchaService captchaService;
  @Autowired
  UserValidator userValidator;
  
  @RequestMapping(method=RequestMethod.GET, params="new")
  public String createUserProfile(Model model) {
    User user = new User();
    user.setTextCaptcha(captchaService.getTextCaptcha());
    model.addAttribute(user);
    return "users/edit";
  }
  
  @RequestMapping(method=RequestMethod.POST)
  public String addUserFromForm(User user, BindingResult bindingResult) {
    if (user.getTextCaptcha() == null) {
      logger.info("TextCaptcha is null");
    }
    userValidator.validate(user, bindingResult);
    if(bindingResult.hasErrors()) {
      user.setTextCaptcha(captchaService.getTextCaptcha());
      user.setUserCaptchaAnswer(null);
      return "users/edit";
    }
    logger.info("valid input, checking captcha");
    if(!captchaService.isCaptchaAnswerCorrect(user.getTextCaptcha(), user.getUserCaptchaAnswer())) {
      user.setTextCaptcha(captchaService.getTextCaptcha());
      user.setUserCaptchaAnswer(null);
      return "users/edit";
    }
    user.encodePassword();
    userService.addUser(user);
    return "redirect:/home";
  }  
}
