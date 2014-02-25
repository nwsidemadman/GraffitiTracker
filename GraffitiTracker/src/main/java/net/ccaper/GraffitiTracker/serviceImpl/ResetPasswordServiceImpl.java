package net.ccaper.GraffitiTracker.serviceImpl;

import net.ccaper.GraffitiTracker.dao.ResetPasswordDao;
import net.ccaper.GraffitiTracker.service.ResetPasswordService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service("resetPasswordService")
public class ResetPasswordServiceImpl implements ResetPasswordService {
  private static final Logger logger = LoggerFactory
      .getLogger(ResetPasswordServiceImpl.class);

  @Autowired
  ResetPasswordDao resetPasswordDao;

  @Override
  @Scheduled(cron = "0 0 7 * * ?")
  public void deleteResetPasswordWhenTimestampExpired() {
    logger.info("Deleting reset passwords where timestamp expired.");
    resetPasswordDao.deleteResetPasswordWhereTimestampExpired();
  }
}
