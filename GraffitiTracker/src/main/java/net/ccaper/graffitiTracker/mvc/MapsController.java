package net.ccaper.graffitiTracker.mvc;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.ccaper.graffitiTracker.objects.AppUser;
import net.ccaper.graffitiTracker.objects.BannedInet;
import net.ccaper.graffitiTracker.objects.ChicagoCityServiceGraffiti;
import net.ccaper.graffitiTracker.service.AppUserService;
import net.ccaper.graffitiTracker.service.BannedInetsService;
import net.ccaper.graffitiTracker.service.ChicagoCityServicesGraffitiService;
import net.ccaper.graffitiTracker.service.UserSecurityService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 
 * @author ccaper
 * 
 */
@Controller
@RequestMapping("/maps")
@Scope("session")
public class MapsController {
  private static final Logger logger = LoggerFactory
      .getLogger(MapsController.class);
  @Autowired
  private AppUserService appUserService;
  @Autowired
  private UserSecurityService userSecurityService;
  @Autowired
  private ChicagoCityServicesGraffitiService chicagoCityServicesGraffitiService;

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
  void setChicagoCityServicesGraffitiService(
      ChicagoCityServicesGraffitiService chicagoCityServicesGraffitiService) {
    this.chicagoCityServicesGraffitiService = chicagoCityServicesGraffitiService;
  }

  // TODO(ccaper): unit test
  /**
   * Produce a map
   * 
   * @param model
   *          the model
   * @return the view name
   */
  @RequestMapping(value="map", method = RequestMethod.GET)
  public String produceMap(Map<String, Object> model) {
    if (!userSecurityService.isUserAnonymous()) {
      String username = userSecurityService.getUsernameFromSecurity();
      AppUser appUser = appUserService.getUserByUsername(username);
      model.put("appUser", appUser);
    }
    List<ChicagoCityServiceGraffiti> dataList = chicagoCityServicesGraffitiService.getAllGraffiti();
    ChicagoCityServiceGraffiti[] data = new ChicagoCityServiceGraffiti[dataList.size()];
    data = dataList.toArray(data);
    model.put("graffiti", data);
    return "maps/map";
  }
}
