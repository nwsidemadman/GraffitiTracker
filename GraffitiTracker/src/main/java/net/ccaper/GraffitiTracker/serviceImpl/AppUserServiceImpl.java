package net.ccaper.GraffitiTracker.serviceImpl;

import net.ccaper.GraffitiTracker.dao.AppUserDao;
import net.ccaper.GraffitiTracker.dao.RegistrationConfirmationsDao;
import net.ccaper.GraffitiTracker.objects.AppUser;
import net.ccaper.GraffitiTracker.service.AppUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("appUserService")
public class AppUserServiceImpl implements AppUserService {
  @Autowired
  AppUserDao appUserDao;
  @Autowired
  RegistrationConfirmationsDao registrationConfirmationsDao;

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
    registrationConfirmationsDao.addRegistrationConfirmationByUsername(username);
  }

  @Override
  public String getUniqueUrlParam(String username) {
    return registrationConfirmationsDao.getUniqueUrlParam(username);
  }
}
