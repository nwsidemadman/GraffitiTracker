package net.ccaper.GraffitiTracker.serviceImpl;

import net.ccaper.GraffitiTracker.dao.BannedInetsDao;
import net.ccaper.GraffitiTracker.service.BannedInetsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("bannedInetsService")
public class BannedInetsServiceImpl implements BannedInetsService {
  @Autowired
  BannedInetsDao bannedInetsDao;

  @Override
  public int getCountInetInRange(String inet) {
    return bannedInetsDao.selectCountInetInRange(inet);
  }

  @Override
  public void updateNumberRegistrationAttemptsInetInRange(String inet) {
    bannedInetsDao.updateNumberRegistrationAttemptsInetInRange(inet);
  }

}
