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
 *         Controller for maps.
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

  /**
   * Sets the chicago city services graffiti service.
   *
   * @param chicagoCityServicesGraffitiService
   *          the new chicago city services graffiti service
   */
  void setChicagoCityServicesGraffitiService(
      ChicagoCityServicesGraffitiService chicagoCityServicesGraffitiService) {
    this.chicagoCityServicesGraffitiService = chicagoCityServicesGraffitiService;
  }

  /**
   * Gets the map form for selecting criteria to map.
   *
   * @param model
   *          the model
   * @return the view name
   */
  @RequestMapping(value = "map", method = RequestMethod.GET)
  public String mapFilter(Map<String, Object> model) {
    if (!userSecurityService.isUserAnonymous()) {
      String username = userSecurityService.getUsernameFromSecurity();
      AppUser appUser = appUserService.getUserByUsername(username);
      model.put("appUser", appUser);
    }
    model.put("mapForm", new MapForm());
    model.put("graffiti", new ArrayList<ChicagoCityServiceGraffiti>(0));
    return "maps/mapFilter";
  }

  /**
   * Based on options selected on the map form, generates a Graffiti map.
   *
   * @param mapForm
   *          the map form holding the user's selections
   * @param model
   *          the model
   * @return the the view name
   */
  @RequestMapping(value = "map", method = RequestMethod.POST)
  public String graffitiMap(MapForm mapForm, Map<String, Object> model) {
    if (!userSecurityService.isUserAnonymous()) {
      String username = userSecurityService.getUsernameFromSecurity();
      AppUser appUser = appUserService.getUserByUsername(username);
      model.put("appUser", appUser);
    }
    List<String> status = stripNoneChoice(mapForm.getStatus());
    model
        .put(
            "graffiti",
            chicagoCityServicesGraffitiService.getAllGraffiti(status,
                mapForm.getStartDateAsTimestamp(),
                mapForm.getEndDateAsTimestamp()));
    return "maps/map";
  }
  
  // visible for testing
  /**
   * Strip none choice from multi select.
   *
   * @param choicesString the multi select choices as string
   * @return the list of choices with NONE stripped
   */
  List<String> stripNoneChoice(String choicesString) {
    List<String> choices = new ArrayList<String>(0);
    if (!StringUtils.isEmpty(choicesString)) {
      choices = new LinkedList<String>(Arrays.asList(StringUtils.split(
          choicesString, ",")));
      choices.remove("NONE");
    }
    return choices;
  }
}
