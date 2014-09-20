package net.ccaper.graffitiTracker.mvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


import net.ccaper.graffitiTracker.objects.AppUser;
import net.ccaper.graffitiTracker.objects.ChicagoCityServiceGraffiti;
import net.ccaper.graffitiTracker.objects.MapForm;
import net.ccaper.graffitiTracker.service.AppUserService;
import net.ccaper.graffitiTracker.service.ChicagoCityServicesGraffitiService;
import net.ccaper.graffitiTracker.service.UserSecurityService;

import org.apache.commons.lang3.StringUtils;
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

  // TODO(ccaper): javadoc
  // TODO(ccaper): unit test
  @RequestMapping(value="map", method = RequestMethod.GET)
  public String mapFilter(Map<String, Object> model) {
    if (!userSecurityService.isUserAnonymous()) {
      String username = userSecurityService.getUsernameFromSecurity();
      AppUser appUser = appUserService.getUserByUsername(username);
      model.put("appUser", appUser);
    }
    MapForm mapForm = new MapForm();
    model.put("mapForm", mapForm);
    model.put("graffiti", new ArrayList<ChicagoCityServiceGraffiti>(0));
    return "maps/mapFilter";
  }

  // TODO(ccaper): javadoc
  // TODO(ccaper): unit test
  @RequestMapping(value="map", method = RequestMethod.POST)
  public String map(MapForm mapForm, Map<String, Object> model) {
    if (!userSecurityService.isUserAnonymous()) {
      String username = userSecurityService.getUsernameFromSecurity();
      AppUser appUser = appUserService.getUserByUsername(username);
      model.put("appUser", appUser);
    }
    List<String> status = new ArrayList<String>(3);
    if (!StringUtils.isEmpty(mapForm.getStatus())) {
      status = new LinkedList<String>(Arrays.asList(StringUtils.split(mapForm.getStatus(), ",")));
      status.remove("NONE");
    }
    model.put("graffiti", chicagoCityServicesGraffitiService.getAllGraffiti(status));
    return "maps/map";
  }
}
