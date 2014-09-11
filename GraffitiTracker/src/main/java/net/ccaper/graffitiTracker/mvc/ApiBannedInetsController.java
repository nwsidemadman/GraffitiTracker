package net.ccaper.graffitiTracker.mvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.ccaper.graffitiTracker.objects.BannedInet;
import net.ccaper.graffitiTracker.objects.OriginalEditedBannedInet;
import net.ccaper.graffitiTracker.service.BannedInetsService;
import net.ccaper.graffitiTracker.utils.InetAddressUtils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
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
  public BannedInet addBannedInet(
      @RequestBody OriginalEditedBannedInet originalEditBannedInet,
      HttpServletResponse response) {
    if (!isBannedInetValid(originalEditBannedInet.getEditedBannedInet())) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return originalEditBannedInet.getEditedBannedInet();
    }
    if (originalEditBannedInet.getOriginalBannedInet() != null
        && !StringUtils.isEmpty(originalEditBannedInet.getOriginalBannedInet().getInetMinIncl())
        && didInetsChange(originalEditBannedInet)) {
      bannedInetsService.inetUpdateBannedInets(originalEditBannedInet);
    } else {
      bannedInetsService
          .insertOrNonInetUpdateBannedInets(originalEditBannedInet
              .getEditedBannedInet());
    }
    return originalEditBannedInet.getEditedBannedInet();
  }

  // visible for testing
  /**
   * Checks if banned inet is valid, checking if inet min is not greater than
   * inet max.
   * 
   * @param bannedInet
   *          the banned inet
   * @return true, if banned inet is valid
   */
  static boolean isBannedInetValid(BannedInet bannedInet) {
    if (!InetAddressUtils.isInetValid(bannedInet.getInetMinIncl())) {
      return false;
    }
    if (!InetAddressUtils.isInetValid(bannedInet.getInetMaxIncl())) {
      return false;
    }
    if (InetAddressUtils.stringToNumber(bannedInet.getInetMinIncl()) > InetAddressUtils
        .stringToNumber(bannedInet.getInetMaxIncl())) {
      return false;
    }
    return true;
  }

  // visible for testing
  /**
   * Checks of the inet values changed on a user submission
   * 
   * @param originalEditBannedInet
   *          Holder containing original banned inet and edited banned inet
   * @return true, if values changed
   */
  static boolean didInetsChange(OriginalEditedBannedInet originalEditBannedInet) {
    if (originalEditBannedInet.getOriginalBannedInet().getInetMinIncl()
        .equals(originalEditBannedInet.getEditedBannedInet().getInetMinIncl())
        && originalEditBannedInet
            .getOriginalBannedInet()
            .getInetMaxIncl()
            .equals(
                originalEditBannedInet.getEditedBannedInet().getInetMaxIncl())) {
      return false;
    }
    return true;
  }

  /**
   * Gets the all banned inets.
   * 
   * @return the all banned inets
   */
  @RequestMapping(method = RequestMethod.GET)
  public @ResponseBody
  Map<String, List<BannedInet>> getAllBannedInets() {
    Map<String, List<BannedInet>> data = new HashMap<String, List<BannedInet>>(
        1);
    data.put("data", bannedInetsService.getAllBannedInets());
    return data;
  }

  /**
   * Deletes banned inet.
   *
   * @param minInetIncl the min inet incl
   * @param maxInetIncl the max inet incl
   */
  @RequestMapping(value = "/{minInetIncl}/{maxInetIncl}/", method = RequestMethod.DELETE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteBannedInet(@PathVariable String minInetIncl,
      @PathVariable String maxInetIncl) {
    bannedInetsService.deleteBannedInet(minInetIncl, maxInetIncl);
  }
}
