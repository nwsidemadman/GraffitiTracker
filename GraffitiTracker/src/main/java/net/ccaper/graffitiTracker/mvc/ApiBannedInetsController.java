package net.ccaper.graffitiTracker.mvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ccaper.graffitiTracker.objects.BannedInet;
import net.ccaper.graffitiTracker.objects.OriginalEditedBannedInet;
import net.ccaper.graffitiTracker.service.BannedInetsService;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
  // TODO(ccaper): update tests
  public BannedInet addBannedInet(
      @RequestBody OriginalEditedBannedInet originalEditBannedInet) {
    if (originalEditBannedInet.getOriginalBannedInet() != null
        && originalEditBannedInet.getOriginalBannedInet().getInetMinIncl() != null
        && !originalEditBannedInet.getOriginalBannedInet().getInetMinIncl()
            .equals(StringUtils.EMPTY)
        && didInetsChange(originalEditBannedInet)) {
      bannedInetsService.inetUpdateBannedInets(originalEditBannedInet);
    } else {
      bannedInetsService
          .insertOrNonInetUpdateBannedInets(originalEditBannedInet
              .getEditedBannedInet());
    }
    return originalEditBannedInet.getEditedBannedInet();
  }

  // TODO(ccaper): unit test
  // TODO(ccaper): javadoc
  private boolean didInetsChange(OriginalEditedBannedInet originalEditBannedInet) {
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

  // TODO(ccaper): javadoc
  // TODO(ccaper): unit test
  @RequestMapping(value = "{min_inet_incl}/{max_inet_incl}", method = RequestMethod.DELETE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void deleteBannedInet(@PathVariable String minInetIncl,
      @PathVariable String maxInetIncl) {
    bannedInetsService.deleteBannedInet(minInetIncl, maxInetIncl);
  }
}
