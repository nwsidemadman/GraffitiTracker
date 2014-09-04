package net.ccaper.graffitiTracker.mvc;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.ccaper.graffitiTracker.mvc.validators.BannedInetValidator;
import net.ccaper.graffitiTracker.objects.AppUser;
import net.ccaper.graffitiTracker.objects.BannedInet;
import net.ccaper.graffitiTracker.service.AppUserService;
import net.ccaper.graffitiTracker.service.BannedInetsService;
import net.ccaper.graffitiTracker.service.UserSecurityService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 
 * @author ccaper
 * 
 */
@Controller
@RequestMapping("/banned_inets")
@Scope("session")
public class BannedInetsController {
  private static final Logger logger = LoggerFactory
      .getLogger(BannedInetsController.class);
  @Autowired
  private BannedInetsService bannedInetsService;
  @Autowired
  private AppUserService appUserService;
  @Autowired
  private UserSecurityService userSecurityService;
  @Autowired
  private BannedInetValidator bannedInetValidator;

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
   * Sets the {@link net.ccaper.graffitiTracker.service.UserSecurityService}.
   * 
   * @param userSecurityService
   *          the new
   *          {@link net.ccaper.graffitiTracker.service.UserSecurityService}
   */
  void setUserSecurityService(UserSecurityService userSecurityService) {
    this.userSecurityService = userSecurityService;
  }

  // visible for testing
  /**
   * Sets the
   * {@link net.ccaper.graffitiTracker.mvc.validators.BannedInetValidator}.
   * 
   * @param bannedInetValidator
   *          the new
   *          {@link net.ccaper.graffitiTracker.mvc.validators.BannedInetValidator}
   */
  void setBannedInetValidator(BannedInetValidator bannedInetValidator) {
    this.bannedInetValidator = bannedInetValidator;
  }

  // TODO(ccaper): javadoc
  @RequestMapping(method = RequestMethod.GET)
  public String listBannedInets(Map<String, Object> model) {
    if (!userSecurityService.isUserAnonymous()) {
      String username = userSecurityService.getUsernameFromSecurity();
      AppUser appUser = appUserService.getUserByUsername(username);
      model.put("appUser", appUser);
    }
    return "bannedInets/listBannedInets";
  }

  // TODO(ccaper): javadoc
  // TODO(ccaper): unit test
  @RequestMapping(value = "/editCreateBannedInet", method = RequestMethod.GET)
  public String editCreateBannedInet(Map<String, Object> model,
      BannedInet origBannedInet, HttpServletRequest request) {
    model.put("editedBannedInet", origBannedInet);
    model.put("contextPath", request.getContextPath());
    return "bannedInets/editCreateBannedInet";
  }

  // TODO(ccaper): javadoc
  // TODO(ccaper): unit test
  // TODO(ccaper): confirm needed
  @RequestMapping(value = "/editCreateBannedInet", method = RequestMethod.POST)
  public String editCreateBannedInetSubmit(Map<String, Object> model,
      BannedInet editedBannedInet, BindingResult bindingResult,
      @RequestParam String origInetMinIncl,
      @RequestParam String origInetMaxIncl, @RequestParam boolean origIsActive,
      @RequestParam String origNotes) {
    bannedInetValidator.validate(editedBannedInet, bindingResult);
    if (bindingResult.hasErrors()) {
      return "/banned_inets/editCreateBannedInet";
    }
    BannedInet origBannedInet = new BannedInet();
    origBannedInet.setInetMinIncl(origInetMinIncl);
    origBannedInet.setInetMaxIncl(origInetMaxIncl);
    origBannedInet.setIsActive(origIsActive);
    origBannedInet.setNotes(origNotes);
    logger.info("all good");
    return "redirect:/banned_inets";
  }
}
