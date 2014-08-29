package net.ccaper.graffitiTracker.serviceImpl;

import net.ccaper.graffitiTracker.dao.BannedInetsDao;
import net.ccaper.graffitiTracker.objects.BannedInet;
import net.ccaper.graffitiTracker.service.BannedInetsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("bannedInetsService")
public class BannedInetsServiceImpl implements BannedInetsService {
  @Autowired
  private BannedInetsDao bannedInetsDao;

  // visible for testing
  void setBannedInetsDao(BannedInetsDao bannedInetsDao) {
    this.bannedInetsDao = bannedInetsDao;
  }

  @Override
  public boolean isInetBanned(String inet) {
    return bannedInetsDao.isInetInRange(inet);
  }

  @Override
  public void updateNumberRegistrationAttemptsInetInRange(String inet) {
    bannedInetsDao.updateNumberRegistrationAttemptsInetInRange(inet);
  }

  @Override
  public void insertOrUpdateBannedInets(BannedInet bannedInet) {
    bannedInetsDao.insertOrUpdateBannedInets(bannedInet);
  }
}
