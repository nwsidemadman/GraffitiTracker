package net.ccaper.GraffitiTracker.mvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ccaper.GraffitiTracker.objects.AppUser;
import net.ccaper.GraffitiTracker.objects.BannedInet;
import net.ccaper.GraffitiTracker.objects.LoginInet;
import net.ccaper.GraffitiTracker.service.AppUserService;
import net.ccaper.GraffitiTracker.service.BannedInetsService;
import net.ccaper.GraffitiTracker.service.LoginAddressService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

// TODO: add security
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
  
  //TODO(ccaper): unit test
  @RequestMapping(method = RequestMethod.POST)
  @ResponseBody public BannedInet addBannedInet(@RequestBody BannedInet bannedInet) {
    logger.info("BannedInet: " + bannedInet);
    BannedInet test = new BannedInet();
    test.setInetMinIncl("127.0.0.1");
    test.setInetMaxIncl("127.0.0.1");
    test.setActive(true);
    test.setNotes("test");
    return test;
  }
}
