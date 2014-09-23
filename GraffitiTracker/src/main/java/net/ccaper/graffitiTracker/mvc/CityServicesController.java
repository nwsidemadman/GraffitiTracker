package net.ccaper.graffitiTracker.mvc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.ccaper.graffitiTracker.objects.CityServiceUpdateForm;
import net.ccaper.graffitiTracker.service.AppUserService;
import net.ccaper.graffitiTracker.service.ChicagoCityServicesGraffitiService;
import net.ccaper.graffitiTracker.service.UserSecurityService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
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

  /**
   * Gets the data update form.
   *
   * @param model
   *          the model
   * @return the view name
   */
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

  /**
   * Update city service data from user choices.
   *
   * @param cityServiceUpdateForm
   *          the city service update form
   * @param model
   *          the model
   * @return the view name
   */
  // TODO(ccaper): update test
  @RequestMapping(value = "/update", method = RequestMethod.POST)
  public String updateCityServiceData(
      CityServiceUpdateForm cityServiceUpdateForm, Map<String, Object> model) {
    String userEmail = null;
    if (!userSecurityService.isUserAnonymous()) {
      String username = userSecurityService.getUsernameFromSecurity();
      userEmail = appUserService.getEmailByUsername(username);
      model.put("appUser", username);
    }
    getDateFromServerAndStoreInRepoAsynch(userEmail,
        cityServiceUpdateForm.getStartDateAsDate(),
        cityServiceUpdateForm.getEndDateAsDate());
    return "city_services/updateStatus";
  }

  // TODO(ccaper): javadoc
  // TODO(ccaper): unit test
  @Async
  private void getDateFromServerAndStoreInRepoAsynch(String userEmail,
      Date startDate, Date endDate) {
    List<String> recipients = new ArrayList<String>(1);
    if (userEmail != null) {
      recipients.add(userEmail);
    }
    chicagoCityServicesGraffitiService
        .getChicagoCityServiceGraffitiRequestsFromServerAndStoreInRepo(
            recipients, startDate, endDate);
  }
}
