package net.ccaper.graffitiTracker.serviceImpl;

import net.ccaper.graffitiTracker.dao.BannedInetsDao;
import net.ccaper.graffitiTracker.objects.BannedInet;
import net.ccaper.graffitiTracker.service.BannedInetsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * @author ccaper
 * 
 *         Implementation of the banned inet service
 * 
 */
@Service("bannedInetsService")
public class BannedInetsServiceImpl implements BannedInetsService {
  @Autowired
  private BannedInetsDao bannedInetsDao;

  // visible for testing
  /**
   * Sets the banned inets dao.
   * 
   * @param bannedInetsDao
   *          the new banned inets dao
   */
  void setBannedInetsDao(BannedInetsDao bannedInetsDao) {
    this.bannedInetsDao = bannedInetsDao;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * net.ccaper.graffitiTracker.service.BannedInetsService#isInetBanned(java
   * .lang.String)
   */
  @Override
  public boolean isInetBanned(String inet) {
    return bannedInetsDao.isInetInRange(inet);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.ccaper.graffitiTracker.service.BannedInetsService#
   * updateNumberRegistrationAttemptsInetInRange(java.lang.String)
   */
  @Override
  public void updateNumberRegistrationAttemptsInetInRange(String inet) {
    bannedInetsDao.updateNumberRegistrationAttemptsInetInRange(inet);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * net.ccaper.graffitiTracker.service.BannedInetsService#insertOrUpdateBannedInets
   * (net.ccaper.graffitiTracker.objects.BannedInet)
   */
  @Override
  public void insertOrUpdateBannedInets(BannedInet bannedInet) {
    bannedInetsDao.insertOrUpdateBannedInets(bannedInet);
  }
}
