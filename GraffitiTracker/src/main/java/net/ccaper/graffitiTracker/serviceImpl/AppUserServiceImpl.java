package net.ccaper.graffitiTracker.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.ccaper.graffitiTracker.dao.AppUserDao;
import net.ccaper.graffitiTracker.dao.RegistrationConfirmationsDao;
import net.ccaper.graffitiTracker.dao.ResetPasswordDao;
import net.ccaper.graffitiTracker.enums.EnvironmentEnum;
import net.ccaper.graffitiTracker.enums.RoleEnum;
import net.ccaper.graffitiTracker.objects.AdminEditAppUser;
import net.ccaper.graffitiTracker.objects.AppUser;
import net.ccaper.graffitiTracker.objects.Role;
import net.ccaper.graffitiTracker.objects.UserSecurityQuestion;
import net.ccaper.graffitiTracker.service.AppUserService;
import net.ccaper.graffitiTracker.service.MailService;
import net.ccaper.graffitiTracker.utils.DateFormats;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * 
 * @author ccaper
 * 
 *         Implementation of the app user service
 * 
 */
@Service("appUserService")
public class AppUserServiceImpl implements AppUserService {
  private static final Logger logger = LoggerFactory
      .getLogger(AppUserServiceImpl.class);
  @Autowired
  private AppUserDao appUserDao;
  @Autowired
  private RegistrationConfirmationsDao registrationConfirmationsDao;
  @Autowired
  private ResetPasswordDao resetPasswordDao;
  @Autowired
  private MailService mailService;

  // visible for testing
  /**
   * Sets the app user dao.
   * 
   * @param appUserDao
   *          the new app user dao
   */
  void setAppUserDao(AppUserDao appUserDao) {
    this.appUserDao = appUserDao;
  }

  // visible for testing
  /**
   * Sets the reset password dao.
   * 
   * @param resetPasswordDao
   *          the new reset password dao
   */
  void setResetPasswordDao(ResetPasswordDao resetPasswordDao) {
    this.resetPasswordDao = resetPasswordDao;
  }

  // visible for testing
  /**
   * Sets the registration confirmation dao.
   * 
   * @param registrationConfirmationDao
   *          the new registration confirmation dao
   */
  void setRegistrationConfirmationDao(
      RegistrationConfirmationsDao registrationConfirmationDao) {
    this.registrationConfirmationsDao = registrationConfirmationDao;
  }

  // visible for testing
  /**
   * Sets the mail service.
   * 
   * @param mailService
   *          the new mail service
   */
  void setMailService(MailService mailService) {
    this.mailService = mailService;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * net.ccaper.graffitiTracker.service.AppUserService#getUserByUsername(java
   * .lang.String)
   */
  @Override
  public AppUser getUserByUsername(String username) {
    return appUserDao.getAppUserByUsername(username);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * net.ccaper.graffitiTracker.service.AppUserService#addAppUser(net.ccaper
   * .graffitiTracker.objects.AppUser)
   */
  @Override
  public void addAppUser(AppUser appUser) {
    appUserDao.addAppUser(appUser);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * net.ccaper.graffitiTracker.service.AppUserService#doesUsernameExist(java
   * .lang.String)
   */
  @Override
  public boolean doesUsernameExist(String username) {
    return appUserDao.doesUsernameExist(username);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * net.ccaper.graffitiTracker.service.AppUserService#doesEmailExist(java.lang
   * .String)
   */
  @Override
  public boolean doesEmailExist(String email) {
    return appUserDao.doesEmailExist(email);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * net.ccaper.graffitiTracker.service.AppUserService#updateLoginTimestamps
   * (java.lang.String)
   */
  @Override
  public void updateLoginTimestamps(String username) {
    appUserDao.updateLoginTimestamps(username);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * net.ccaper.graffitiTracker.service.AppUserService#addRegistrationConfirmation
   * (java.lang.String)
   */
  @Override
  public void addRegistrationConfirmation(String username) {
    registrationConfirmationsDao
        .addRegistrationConfirmationByUsername(username);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.ccaper.graffitiTracker.service.AppUserService#
   * getRegistrationConfirmationUniqueUrlParamByUsername(java.lang.String)
   */
  @Override
  public String getRegistrationConfirmationUniqueUrlParamByUsername(
      String username) {
    return registrationConfirmationsDao.getUniqueUrlParamByUsername(username);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.ccaper.graffitiTracker.service.AppUserService#
   * deleteRegistrationConfirmationByUniqueUrlParam(java.lang.String)
   */
  @Override
  public void deleteRegistrationConfirmationByUniqueUrlParam(
      String uniqueUrlParam) {
    registrationConfirmationsDao
        .deleteRegistrationConfirmationByUniqueUrlParam(uniqueUrlParam);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.ccaper.graffitiTracker.service.AppUserService#
   * getUserIdByRegistrationConfirmationUniqueUrlParam(java.lang.String)
   */
  @Override
  public Integer getUserIdByRegistrationConfirmationUniqueUrlParam(
      String uniqueUrlParam) {
    try {
      return registrationConfirmationsDao
          .getUserIdByUniqueUrlParam(uniqueUrlParam);
    } catch (EmptyResultDataAccessException e) {
      return null;
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * net.ccaper.graffitiTracker.service.AppUserService#updateAppUserAsActive
   * (int)
   */
  @Override
  public void updateAppUserAsActive(int userid) {
    appUserDao.updateIsActiveByUserid(userid, true);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.ccaper.graffitiTracker.service.AppUserService#
   * deleteAppUsersWhenRegistrationExpired()
   */
  @Override
  @Scheduled(cron = "0 0 7 * * ?")
  public void deleteAppUsersWhenRegistrationExpired() {
    logger.info("Deleting app users where registration expired.");
    appUserDao.deleteAppUsersWhenRegistrationExpired();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * net.ccaper.graffitiTracker.service.AppUserService#emailAdminStatsDaily()
   */
  @Override
  @Scheduled(cron = "0 01 6 * * ?")
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
    content += String.format("New Users Last Day: %d\n",
        appUserDao.getCountNewUsers(numberOfDays));
    content += String.format("Unconfirmed Users Last Day: %d\n",
        appUserDao.getCountUnconfirmedUsers(numberOfDays));
    content += String.format("Number Users Logged In Last Day: %d\n",
        appUserDao.getCountLogins(numberOfDays));
    mailService.sendSimpleEmail(recipients, String.format(
        "%s GraffitiTracker Daily Stats %s",
        EnvironmentEnum.getEnvironmentEnumFromEnvironmentPropertyString(
            System.getProperty("CLASSPATH_PROP_ENV")).getDisplayString(),
        DateFormats.YEAR_SLASH_MONTH_SLASH_DAY_FORMAT.format(new Date())),
        content);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * net.ccaper.graffitiTracker.service.AppUserService#getUsernameByEmail(java
   * .lang.String)
   */
  @Override
  public String getUsernameByEmail(String email) {
    try {
      return appUserDao.getUsernameByEmail(email);
    } catch (EmptyResultDataAccessException e) {
      return null;
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * net.ccaper.graffitiTracker.service.AppUserService#getEmailByUsername(java
   * .lang.String)
   */
  @Override
  public String getEmailByUsername(String username) {
    try {
      return appUserDao.getEmailByUsername(username);
    } catch (EmptyResultDataAccessException e) {
      return null;
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * net.ccaper.graffitiTracker.service.AppUserService#addResetPassword(java
   * .lang.String)
   */
  @Override
  public void addResetPassword(String username) {
    resetPasswordDao.addResetPassword(username);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.ccaper.graffitiTracker.service.AppUserService#
   * getResetPasswordUniqueUrlParamByUsername(java.lang.String)
   */
  @Override
  public String getResetPasswordUniqueUrlParamByUsername(String username) {
    return resetPasswordDao.getUniqueUrlParamByUsername(username);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.ccaper.graffitiTracker.service.AppUserService#
   * getUserSecurityQuestionByResetPasswordUniqueUrlParam(java.lang.String)
   */
  @Override
  public UserSecurityQuestion getUserSecurityQuestionByResetPasswordUniqueUrlParam(
      String uniqueUrlParam) {
    try {
      return resetPasswordDao
          .getUserSecurityQuestionByUniqueUrlParam(uniqueUrlParam);
    } catch (EmptyResultDataAccessException e) {
      return null;
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.ccaper.graffitiTracker.service.AppUserService#
   * deleteResetPasswordByUniqueUrlParam(java.lang.String)
   */
  @Override
  public void deleteResetPasswordByUniqueUrlParam(String uniqueUrlParam) {
    resetPasswordDao.deleteResetPasswordByUniqueUrlParam(uniqueUrlParam);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * net.ccaper.graffitiTracker.service.AppUserService#getSecurityAnswerByUserid
   * (int)
   */
  @Override
  public String getSecurityAnswerByUserid(int userid) {
    return appUserDao.getSecurityAnswerByUserid(userid);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * net.ccaper.graffitiTracker.service.AppUserService#getUsernameByUserid(int)
   */
  @Override
  public String getUsernameByUserid(int userid) {
    return appUserDao.getUsernameByUserid(userid);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * net.ccaper.graffitiTracker.service.AppUserService#updatePasswordByUserid
   * (int, java.lang.String)
   */
  @Override
  public void updatePasswordByUserid(int userid, String passwordEncoded) {
    appUserDao.updatePasswordByUserid(userid, passwordEncoded);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * net.ccaper.graffitiTracker.service.AppUserService#updateEmailByUserid(int,
   * java.lang.String)
   */
  @Override
  public void updateEmailByUserid(int userid, String email) {
    appUserDao.updateEmailByUserid(userid, email);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.ccaper.graffitiTracker.service.AppUserService#
   * updateSecurityQuestionByUserid(int, java.lang.String)
   */
  @Override
  public void updateSecurityQuestionByUserid(int userid, String securityQuestion) {
    appUserDao.updateSecurityQuestionByUserid(userid, securityQuestion);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * net.ccaper.graffitiTracker.service.AppUserService#updateSecurityAnswerByUserid
   * (int, java.lang.String)
   */
  @Override
  public void updateSecurityAnswerByUserid(int userid, String securityAnswer) {
    appUserDao.updateSecurityAnswerByUserid(userid, securityAnswer);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.ccaper.graffitiTracker.service.AppUserService#getAllUsers()
   */
  @Override
  public List<AppUser> getAllUsers() {
    return appUserDao.getAllUsers();
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.ccaper.graffitiTracker.service.AppUserService#getUserById(int)
   */
  @Override
  public AppUser getUserById(int id) {
    try {
      return appUserDao.getAppUserById(id);
    } catch (DataAccessException e) {
      return null;
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * net.ccaper.graffitiTracker.service.AppUserService#updateAppUser(net.ccaper
   * .graffitiTracker.objects.AppUser,
   * net.ccaper.graffitiTracker.objects.AdminEditAppUser)
   */
  @Override
  public void updateAppUser(AppUser uneditedUser, AdminEditAppUser edits) {
    if (uneditedUser.getEmail() != edits.getEmail()) {
      appUserDao
          .updateEmailByUserid(uneditedUser.getUserId(), edits.getEmail());
    }
    if (uneditedUser.getIsActive() != edits.getIsActive()) {
      appUserDao.updateIsActiveByUserid(uneditedUser.getUserId(),
          edits.getIsActive());
    }
    if (!areRolesEqual(uneditedUser, edits)) {
      List<RoleEnum> roleAdditions = getRoleAdditions(uneditedUser, edits);
      if (roleAdditions.size() > 0) {
        appUserDao.addRolesByUserid(uneditedUser.getUserId(), roleAdditions);
      }
      List<RoleEnum> roleDeletions = getRoleDeletions(uneditedUser, edits);
      if (roleDeletions.size() > 0) {
        appUserDao.deleteRolesByUserid(uneditedUser.getUserId(), roleDeletions);
      }
    }
  }

  // visible for testing
  /**
   * Are roles equal.
   * 
   * @param uneditedUser
   *          the unedited user
   * @param edits
   *          the edits
   * @return true, if successful
   */
  static boolean areRolesEqual(AppUser uneditedUser, AdminEditAppUser edits) {
    if (uneditedUser.getRoles() == null && edits.getRoles() != null) {
      return false;
    }
    if (uneditedUser.getRoles() != null && edits.getRoles() == null) {
      return false;
    }
    if (uneditedUser.getRoles() == null && edits.getRoles() == null) {
      return true;
    }
    List<RoleEnum> uneditedRolesEnum = new ArrayList<RoleEnum>(uneditedUser
        .getRoles().size());
    for (Role role : uneditedUser.getRoles()) {
      uneditedRolesEnum.add(role.getRole());
    }
    return uneditedRolesEnum.equals(edits.getRoles());
  }

  // visible for testing
  /**
   * Gets the role additions.
   * 
   * @param uneditedUser
   *          the unedited user
   * @param edits
   *          the edits
   * @return the role additions
   */
  static List<RoleEnum> getRoleAdditions(AppUser uneditedUser,
      AdminEditAppUser edits) {
    List<RoleEnum> roleAdditions = new ArrayList<RoleEnum>(
        RoleEnum.values().length);
    List<RoleEnum> uneditedRolesEnum = new ArrayList<RoleEnum>(uneditedUser
        .getRoles().size());
    for (Role role : uneditedUser.getRoles()) {
      uneditedRolesEnum.add(role.getRole());
    }
    for (RoleEnum roleInEdits : edits.getRoles()) {
      if (!uneditedRolesEnum.contains(roleInEdits)) {
        roleAdditions.add(roleInEdits);
      }
    }
    return roleAdditions;
  }

  // visible for testing
  /**
   * Gets the role deletions.
   * 
   * @param uneditedUser
   *          the unedited user
   * @param edits
   *          the edits
   * @return the role deletions
   */
  static List<RoleEnum> getRoleDeletions(AppUser uneditedUser,
      AdminEditAppUser edits) {
    List<RoleEnum> roleDeletions = new ArrayList<RoleEnum>(
        RoleEnum.values().length);
    List<RoleEnum> uneditedRolesEnum = new ArrayList<RoleEnum>(uneditedUser
        .getRoles().size());
    for (Role role : uneditedUser.getRoles()) {
      uneditedRolesEnum.add(role.getRole());
    }
    for (RoleEnum roleInUnedits : uneditedRolesEnum) {
      if (!edits.getRoles().contains(roleInUnedits)) {
        roleDeletions.add(roleInUnedits);
      }
    }
    return roleDeletions;
  }
}
