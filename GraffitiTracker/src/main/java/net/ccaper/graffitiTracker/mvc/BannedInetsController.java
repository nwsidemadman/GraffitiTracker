package net.ccaper.graffitiTracker.mvc;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
  public String editCreateBannedInet(Map<String, Object> model, HttpServletRequest request, BannedInet origBannedInet) {
    logger.info("oh hai");
    logger.info("originalBannedInet: " + origBannedInet);
    return "bannedInets/editCreateBannedInet";
  }
}
