package net.ccaper.GraffitiTracker.mvc;

import net.ccaper.GraffitiTracker.objects.User;
import net.ccaper.GraffitiTracker.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/users")
public class UserController {
  private static final Logger logger = LoggerFactory
      .getLogger(UserController.class);
  @Autowired
  UserService userService;
  
  @RequestMapping(method=RequestMethod.GET, params="new")
  public String createUserProfile(Model model) {
    logger.info("Create user profile hit in UserController!");
    model.addAttribute(new User());
    return "users/edit";
  }
  
  @RequestMapping(method=RequestMethod.POST)
  public String addUserFromForm(@Validated User user, BindingResult bindingResult) {
    if(bindingResult.hasErrors()) {
      return "users/edit";
    }
    userService.addUser(user);
    return "redirect:/home";
  }  
}
