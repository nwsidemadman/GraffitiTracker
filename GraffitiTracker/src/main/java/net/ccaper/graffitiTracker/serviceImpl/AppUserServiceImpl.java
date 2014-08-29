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
  void setAppUserDao(AppUserDao appUserDao) {
    this.appUserDao = appUserDao;
  }

  // visible for testing
  void setResetPasswordDao(ResetPasswordDao resetPasswordDao) {
    this.resetPasswordDao = resetPasswordDao;
  }

  // visible for testing
  void setRegistrationConfirmationDao(
      RegistrationConfirmationsDao registrationConfirmationDao) {
    this.registrationConfirmationsDao = registrationConfirmationDao;
  }

  // visible for testing
  void setMailService(MailService mailService) {
    this.mailService = mailService;
  }

  @Override
  public AppUser getUserByUsername(String username) {
    return appUserDao.getAppUserByUsername(username);
  }

  @Override
  public void addAppUser(AppUser appUser) {
    appUserDao.addAppUser(appUser);
  }

  @Override
  public boolean doesUsernameExist(String username) {
    return appUserDao.doesUsernameExist(username);
  }

  @Override
  public boolean doesEmailExist(String email) {
    return appUserDao.doesEmailExist(email);
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
    try {
      return registrationConfirmationsDao
          .getUserIdByUniqueUrlParam(uniqueUrlParam);
    } catch (EmptyResultDataAccessException e) {
      return null;
    }
  }

  @Override
  public void updateAppUserAsActive(int userid) {
    appUserDao.updateIsActiveByUserid(userid, true);
  }

  @Override
  @Scheduled(cron = "0 0 7 * * ?")
  public void deleteAppUsersWhenRegistrationExpired() {
    logger.info("Deleting app users where registration expired.");
    appUserDao.deleteAppUsersWhenRegistrationExpired();
  }

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

  @Override
  public String getUsernameByEmail(String email) {
    try {
      return appUserDao.getUsernameByEmail(email);
    } catch (EmptyResultDataAccessException e) {
      return null;
    }
  }

  @Override
  public String getEmailByUsername(String username) {
    try {
      return appUserDao.getEmailByUsername(username);
    } catch (EmptyResultDataAccessException e) {
      return null;
    }
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
    try {
      return resetPasswordDao
          .getUserSecurityQuestionByUniqueUrlParam(uniqueUrlParam);
    } catch (EmptyResultDataAccessException e) {
      return null;
    }
  }

  @Override
  public void deleteResetPasswordByUniqueUrlParam(String uniqueUrlParam) {
    resetPasswordDao.deleteResetPasswordByUniqueUrlParam(uniqueUrlParam);
  }

  @Override
  public String getSecurityAnswerByUserid(int userid) {
    return appUserDao.getSecurityAnswerByUserid(userid);
  }

  @Override
  public String getUsernameByUserid(int userid) {
    return appUserDao.getUsernameByUserid(userid);
  }

  @Override
  public void updatePasswordByUserid(int userid, String passwordEncoded) {
    appUserDao.updatePasswordByUserid(userid, passwordEncoded);
  }

  @Override
  public void updateEmailByUserid(int userid, String email) {
    appUserDao.updateEmailByUserid(userid, email);
  }

  @Override
  public void updateSecurityQuestionByUserid(int userid, String securityQuestion) {
    appUserDao.updateSecurityQuestionByUserid(userid, securityQuestion);
  }

  @Override
  public void updateSecurityAnswerByUserid(int userid, String securityAnswer) {
    appUserDao.updateSecurityAnswerByUserid(userid, securityAnswer);
  }

  @Override
  public List<AppUser> getAllUsers() {
    return appUserDao.getAllUsers();
  }

  @Override
  public AppUser getUserById(int id) {
    try {
      return appUserDao.getAppUserById(id);
    } catch (DataAccessException e) {
      return null;
    }
  }

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