package net.ccaper.graffitiTracker.serviceImpl;

import net.ccaper.graffitiTracker.dao.ResetPasswordDao;
import net.ccaper.graffitiTracker.service.ResetPasswordService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * 
 * @author ccaper
 * 
 *         Implementation for resetting the password
 * 
 */
@Service("resetPasswordService")
public class ResetPasswordServiceImpl implements ResetPasswordService {
  private static final Logger logger = LoggerFactory
      .getLogger(ResetPasswordServiceImpl.class);
  @Autowired
  private ResetPasswordDao resetPasswordDao;

  /*
   * (non-Javadoc)
   * 
   * @see net.ccaper.graffitiTracker.service.ResetPasswordService#
   * deleteResetPasswordWhenTimestampExpired()
   */
  @Override
  @Scheduled(cron = "0 0 7 * * ?")
  public void deleteResetPasswordWhenTimestampExpired() {
    logger.info("Deleting reset passwords where timestamp expired.");
    resetPasswordDao.deleteResetPasswordWhereTimestampExpired();
  }
}
