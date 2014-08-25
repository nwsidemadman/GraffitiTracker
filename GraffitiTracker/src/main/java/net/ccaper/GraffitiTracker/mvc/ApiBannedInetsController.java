package net.ccaper.GraffitiTracker.mvc;

import net.ccaper.GraffitiTracker.objects.BannedInet;
import net.ccaper.GraffitiTracker.service.BannedInetsService;

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

@RestController
@RequestMapping("/api/banned_inets")
@Scope("session")
public class ApiBannedInetsController {
  private static final Logger logger = LoggerFactory
      .getLogger(ApiBannedInetsController.class);
  
  @Autowired
  BannedInetsService bannedInetsService;
  
  public void setBannedInetsService(BannedInetsService bannedInetsService) {
    this.bannedInetsService = bannedInetsService;
  }
  
  @RequestMapping(method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody public BannedInet addBannedInet(@RequestBody BannedInet bannedInet) {
    bannedInetsService.insertOrUpdateBannedInets(bannedInet);
    return bannedInet;
  }
}
