package net.ccaper.GraffitiTracker.mvc;

import net.ccaper.GraffitiTracker.service.BannedInetsService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bannedInets")
@Scope("session")
public class ApiBannedInetController {
  private static final Logger logger = LoggerFactory
      .getLogger(ApiBannedInetController.class);
  
  @Autowired
  BannedInetsService bannedInetsService;
  
  public void setAppUserService(BannedInetsService bannedInetsService) {
    this.bannedInetsService = bannedInetsService;
  }
  
  // TODO(ccaper): unite test
  @RequestMapping(value = "/{inet}/is_banned", method = RequestMethod.GET, produces = "application/json")
  public @ResponseBody boolean isInetBanned(@PathVariable String inet) {
    return bannedInetsService.isInetBanned(inet);
  }
}
