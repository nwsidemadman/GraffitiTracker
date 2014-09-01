package net.ccaper.graffitiTracker.mvc;

import net.ccaper.graffitiTracker.objects.BannedInet;
import net.ccaper.graffitiTracker.service.BannedInetsService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @author ccaper
 * 
 *         Handles the api actions for the
 *         {@link net.ccaper.graffitiTracker.objects.BannedInet}
 * 
 */
@RestController
@RequestMapping("/api/banned_inets")
@Scope("session")
public class ApiBannedInetsController {
  private static final Logger logger = LoggerFactory
      .getLogger(ApiBannedInetsController.class);
  @Autowired
  private BannedInetsService bannedInetsService;

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

  /**
   * Adds the {@link net.ccaper.graffitiTracker.objects.BannedInet}.
   * 
   * @param bannedInet
   *          the {@link net.ccaper.graffitiTracker.objects.BannedInet}
   * @return the {@link net.ccaper.graffitiTracker.objects.BannedInet}
   */
  @RequestMapping(method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public BannedInet addBannedInet(@RequestBody BannedInet bannedInet) {
    bannedInetsService.insertOrUpdateBannedInets(bannedInet);
    return bannedInet;
  }
}
