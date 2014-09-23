package net.ccaper.graffitiTracker.mvc;

import java.util.List;
import java.util.Map;

import net.ccaper.graffitiTracker.objects.ChicagoCityServiceGraffiti;
import net.ccaper.graffitiTracker.objects.CityServiceUpdateForm;
import net.ccaper.graffitiTracker.service.AppUserService;
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
 *         Controller for city services actions
 * 
 */
// TODO(ccaper): add security for super admin only
@Controller
@RequestMapping("/city_services")
@Scope("session")
public class CityServicesController {
  private static final Logger logger = LoggerFactory
      .getLogger(CityServicesController.class);
  @Autowired
  private ChicagoCityServicesGraffitiService chicagoCityServicesGraffitiService;
  @Autowired
  private AppUserService appUserService;
  @Autowired
  private UserSecurityService userSecurityService;

  // visible for testing
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

  // TODO(ccaper): unit test
  // TODO(ccaper): javadoc
  @RequestMapping(value = "/update", method = RequestMethod.GET)
  public String getUpdateForm(Map<String, Object> model) {
    if (!userSecurityService.isUserAnonymous()) {
      model.put("appUser", appUserService.getUserByUsername(userSecurityService
          .getUsernameFromSecurity()));
    }
    CityServiceUpdateForm cityServiceUpdateForm = new CityServiceUpdateForm();
    model.put("cityServiceUpdateForm", cityServiceUpdateForm);
    return "city_services/update";
  }

  // TODO(ccaper): unit test
  // TODO(ccaper): javadoc
  @RequestMapping(value = "/update", method = RequestMethod.POST)
  public String updateCityServiceData(
      CityServiceUpdateForm citySerivceUpdateForm, Map<String, Object> model) {
    if (!userSecurityService.isUserAnonymous()) {
      model.put("appUser", appUserService.getUserByUsername(userSecurityService
          .getUsernameFromSecurity()));
    }
    List<ChicagoCityServiceGraffiti> data = chicagoCityServicesGraffitiService
        .getChicagoCityServiceGraffitiRequestsFromServer(
            citySerivceUpdateForm.getStartDateAsDate(),
            citySerivceUpdateForm.getEndDateAsDate());
    model.put("dataSizeStored", chicagoCityServicesGraffitiService.storeChicagoCityServiceGraffitiRequests(data));
    model.put("dataSizeServer", data.size());
    return "city_services/updateStatus";
  }
}
