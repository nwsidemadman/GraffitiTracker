package net.ccaper.GraffitiTracker.serviceImpl;

import java.util.Date;
import java.util.List;

import net.ccaper.GraffitiTracker.dao.AppUserDao;
import net.ccaper.GraffitiTracker.dao.RegistrationConfirmationsDao;
import net.ccaper.GraffitiTracker.dao.ResetPasswordDao;
import net.ccaper.GraffitiTracker.enums.EnvironmentEnum;
import net.ccaper.GraffitiTracker.objects.AppUser;
import net.ccaper.GraffitiTracker.objects.UserSecurityQuestion;
import net.ccaper.GraffitiTracker.service.AppUserService;
import net.ccaper.GraffitiTracker.service.MailService;
import net.ccaper.GraffitiTracker.utils.DateFormats;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service("appUserService")
public class AppUserServiceImpl implements AppUserService {
  private static final Logger logger = LoggerFactory
      .getLogger(AppUserServiceImpl.class);
  @Autowired
  AppUserDao appUserDao;
  @Autowired
  RegistrationConfirmationsDao registrationConfirmationsDao;
  @Autowired
  ResetPasswordDao resetPasswordDao;
  @Autowired
  MailService mailService;

  @Override
  public AppUser getUser(String username) {
    return appUserDao.getAppUserByUsername(username);
  }

  @Override
  public void addAppUser(AppUser appUser) {
    appUserDao.addAppUser(appUser);
  }

  @Override
  public boolean doesUsernameExist(String username) {
    int count = getCountUsernames(username);
    if (count == 0) {
      return false;
    } else {
      return true;
    }
  }

  // visible for testing
  int getCountUsernames(String username) {
    return appUserDao.getCountUsernames(username);
  }

  @Override
  public boolean doesEmailExist(String email) {
    int count = getCountEmails(email);
    if (count == 0) {
      return false;
    } else {
      return true;
    }
  }

  // visible for testing
  int getCountEmails(String email) {
    return appUserDao.getCountEmails(email);
  }

  @Override
  public void updateLoginTimestamps(String username) {
    appUserDao.updateLoginTimestamps(username);
  }

  @Override
  public void addRegistrationConfirmation(String username) {
    registrationConfirmationsDao
    .addRegistrationConfirmationByUsername(username);
  }

  @Override
  public String getRegistrationConfirmationUniqueUrlParamByUsername(
      String username) {
    return registrationConfirmationsDao.getUniqueUrlParamByUsername(username);
  }

  @Override
  public void deleteRegistrationConfirmationByUniqueUrlParam(
      String uniqueUrlParam) {
    registrationConfirmationsDao
    .deleteRegistrationConfirmationByUniqueUrlParam(uniqueUrlParam);
  }

  @Override
  public Integer getUserIdByRegistrationConfirmationUniqueUrlParam(
      String uniqueUrlParam) {
    return registrationConfirmationsDao
        .getUserIdByUniqueUrlParam(uniqueUrlParam);
  }

  @Override
  public void updateAppUserAsActive(int userid) {
    appUserDao.updateAppUserAsActive(userid);
  }

  @Override
  @Scheduled(cron = "0 0 6 * * ?")
  public void deleteAppUsersWhenRegistrationExpired() {
    logger.info("Deleting app users where registration expired.");
    appUserDao.deleteAppUsersWhenRegistrationExpired();
  }

  @Override
  @Scheduled(cron = "0 30 5 * * ?")
  public void emailAdminStatsDaily() {
    logger.info("Sending daily stats to super admins.");
    List<String> recipients = appUserDao.getSuperAdminEmails();
    if (recipients.size() == 0) {
      logger
      .error("There are no users with super admin role to deliver daily stats.");
      return;
    }
    String content = "";
    int numberOfDays = 1;
    content += String.format("New Users Last Day: %s\n",
        appUserDao.getCountNewUsers(numberOfDays));
    content += String.format("Unconfirmed Users Last Day: %s\n",
        appUserDao.getCountUnconfirmedUsers(numberOfDays));
    content += String.format("Logins Last Day: %s\n",
        appUserDao.getCountLogins(numberOfDays));
    mailService.sendSimpleEmail(recipients, String.format(
        "%s GraffitiTracker Daily Stats %s",
        EnvironmentEnum.getEnvironmentEnumFromEnvironmentPropertyString(
            System.getProperty("GRAFFITI_TRACKER_ENV")).getDisplayString(),
            DateFormats.YEAR_SLASH_MONTH_SLASH_DAY_FORMAT.format(new Date())),
            content);
  }

  @Override
  public String getUsernameByEmail(String email) {
    return appUserDao.getUsernameByEmail(email);
  }

  @Override
  public String getEmailByUsername(String username) {
    return appUserDao.getEmailByUsername(username);
  }

  @Override
  public void addResetPassword(String username) {
    resetPasswordDao.addResetPassword(username);
  }

  @Override
  public String getResetPasswordUniqueUrlParamByUsername(String username) {
    return resetPasswordDao.getUniqueUrlParamByUsername(username);
  }

  @Override
  public UserSecurityQuestion getUserSecurityQuestionByResetPasswordUniqueUrlParam(
      String uniqueUrlParam) {
    return resetPasswordDao.getUserSecurityQuestionByUniqueUrlParam(uniqueUrlParam);
  }

  @Override
  public void deleteResetPasswordByUniqueUrlParam(String uniqueUrlParam) {
    resetPasswordDao.deleteResetPasswordByUniqueUrlParam(uniqueUrlParam);
  }

  // TODO: delete reset passwords when expired
}
