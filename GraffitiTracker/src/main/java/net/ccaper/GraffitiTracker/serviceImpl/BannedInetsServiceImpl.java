package net.ccaper.GraffitiTracker.serviceImpl;

import net.ccaper.GraffitiTracker.dao.BannedInetsDao;
import net.ccaper.GraffitiTracker.service.BannedInetsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("bannedInetsService")
public class BannedInetsServiceImpl implements BannedInetsService {
  @Autowired
  BannedInetsDao bannedInetsDao;

  public void setBannedInetsDao(BannedInetsDao bannedInetsDao) {
    this.bannedInetsDao = bannedInetsDao;
  }

  @Override
  public boolean isInetBanned(String inet) {
    if (bannedInetsDao.selectCountInetInRange(inet) == 0) {
      return false;
    } else {
      return true;
    }
  }

  @Override
  public void updateNumberRegistrationAttemptsInetInRange(String inet) {
    bannedInetsDao.updateNumberRegistrationAttemptsInetInRange(inet);
  }
}
