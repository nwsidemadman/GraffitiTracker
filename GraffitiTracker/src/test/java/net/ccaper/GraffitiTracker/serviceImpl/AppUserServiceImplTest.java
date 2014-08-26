package net.ccaper.GraffitiTracker.serviceImpl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.ccaper.GraffitiTracker.dao.AppUserDao;
import net.ccaper.GraffitiTracker.dao.impl.JdbcAppUserDaoImpl;
import net.ccaper.GraffitiTracker.enums.EnvironmentEnum;
import net.ccaper.GraffitiTracker.enums.RoleEnum;
import net.ccaper.GraffitiTracker.objects.AdminEditAppUser;
import net.ccaper.GraffitiTracker.objects.AppUser;
import net.ccaper.GraffitiTracker.objects.Role;
import net.ccaper.GraffitiTracker.service.AppUserService;
import net.ccaper.GraffitiTracker.service.MailService;
import net.ccaper.GraffitiTracker.utils.DateFormats;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.EmptyResultDataAccessException;

public class AppUserServiceImplTest {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testGetUserById_HappyPath() throws Exception {
    AppUser appUser = new AppUser();
    appUser.setUserId(5);
    AppUserDao daoMock = mock(JdbcAppUserDaoImpl.class);
    when(daoMock.getAppUserById(appUser.getUserId())).thenReturn(appUser);
    AppUserServiceImpl service = new AppUserServiceImpl();
    service.setAppUserDao(daoMock);
    assertEquals(appUser, service.getUserById(appUser.getUserId()));
    verify(daoMock).getAppUserById(appUser.getUserId());
  }

  @Test
  public void testGetUserById_NotFound() throws Exception {
    AppUser appUser = new AppUser();
    appUser.setUserId(5);
    AppUserDao daoMock = mock(JdbcAppUserDaoImpl.class);
    when(daoMock.getAppUserById(appUser.getUserId())).thenThrow(
        new EmptyResultDataAccessException("test", 0));
    AppUserServiceImpl service = new AppUserServiceImpl();
    service.setAppUserDao(daoMock);
    assertNull(service.getUserById(appUser.getUserId()));
    verify(daoMock).getAppUserById(appUser.getUserId());
  }

  @Test
  public void testUpdateAppUser_noEdits() throws Exception {
    AppUser uneditedUser = new AppUser();
    uneditedUser.setEmail("test@test.com");
    uneditedUser.setIsActive(true);
    List<Role> uneditedRoles = new ArrayList<Role>(1);
    Role role1 = new Role();
    role1.setRole(RoleEnum.BASIC);
    uneditedRoles.add(role1);
    uneditedUser.setRoles(uneditedRoles);
    AdminEditAppUser editedUser = new AdminEditAppUser();
    editedUser.setEmail(uneditedUser.getEmail());
    editedUser.setActive(uneditedUser.getIsActive());
    editedUser.setRoles(new ArrayList<RoleEnum>(uneditedUser
        .getRolesAsTimestampToRoleEnumMap().keySet()));
    AppUserService service = new AppUserServiceImpl();
    service.updateAppUser(uneditedUser, editedUser);
  }

  @Test
  public void testUpdateAppUser_AllEdits() throws Exception {
    AppUser uneditedUser = new AppUser();
    uneditedUser.setUserId(5);
    uneditedUser.setEmail("test@test.com");
    uneditedUser.setIsActive(true);
    List<Role> uneditedRoles = new ArrayList<Role>(1);
    Role role1 = new Role();
    role1.setRole(RoleEnum.BASIC);
    uneditedRoles.add(role1);
    Role role2 = new Role();
    role2.setRole(RoleEnum.SUPERADMIN);
    uneditedRoles.add(role2);
    uneditedUser.setRoles(uneditedRoles);
    AdminEditAppUser editedUser = new AdminEditAppUser();
    editedUser.setEmail("test2@test.com");
    editedUser.setActive(false);
    List<RoleEnum> editedRoles = new ArrayList<RoleEnum>(1);
    editedRoles.add(RoleEnum.BASIC);
    editedRoles.add(RoleEnum.ADMIN);
    editedUser.setRoles(editedRoles);
    AppUserServiceImpl service = new AppUserServiceImpl();
    AppUserDao daoMock = mock(JdbcAppUserDaoImpl.class);
    service.setAppUserDao(daoMock);
    service.updateAppUser(uneditedUser, editedUser);
    List<RoleEnum> additions = AppUserServiceImpl.getRoleAdditions(
        uneditedUser, editedUser);
    List<RoleEnum> deletions = AppUserServiceImpl.getRoleDeletions(
        uneditedUser, editedUser);
    verify(daoMock).updateEmailByUserid(uneditedUser.getUserId(),
        editedUser.getEmail());
    verify(daoMock).updateIsActiveByUserid(uneditedUser.getUserId(),
        editedUser.getIsActive());
    verify(daoMock).addRolesByUserid(uneditedUser.getUserId(), additions);
    verify(daoMock).deleteRolesByUserid(uneditedUser.getUserId(), deletions);
  }

  @Test
  public void testUpdateAppUser_AllEditsWithOnlyRoleAdditions()
      throws Exception {
    AppUser uneditedUser = new AppUser();
    uneditedUser.setUserId(5);
    uneditedUser.setEmail("test@test.com");
    uneditedUser.setIsActive(true);
    List<Role> uneditedRoles = new ArrayList<Role>(1);
    Role role1 = new Role();
    role1.setRole(RoleEnum.BASIC);
    uneditedRoles.add(role1);
    uneditedUser.setRoles(uneditedRoles);
    AdminEditAppUser editedUser = new AdminEditAppUser();
    editedUser.setEmail("test2@test.com");
    editedUser.setActive(false);
    List<RoleEnum> editedRoles = new ArrayList<RoleEnum>(1);
    editedRoles.add(RoleEnum.BASIC);
    editedRoles.add(RoleEnum.ADMIN);
    editedUser.setRoles(editedRoles);
    AppUserServiceImpl service = new AppUserServiceImpl();
    AppUserDao daoMock = mock(JdbcAppUserDaoImpl.class);
    service.setAppUserDao(daoMock);
    service.updateAppUser(uneditedUser, editedUser);
    List<RoleEnum> additions = AppUserServiceImpl.getRoleAdditions(
        uneditedUser, editedUser);
    verify(daoMock).updateEmailByUserid(uneditedUser.getUserId(),
        editedUser.getEmail());
    verify(daoMock).updateIsActiveByUserid(uneditedUser.getUserId(),
        editedUser.getIsActive());
    verify(daoMock).addRolesByUserid(uneditedUser.getUserId(), additions);
  }

  @Test
  public void testUpdateAppUser_AllEditsWithOnlyRoleDeletions()
      throws Exception {
    AppUser uneditedUser = new AppUser();
    uneditedUser.setUserId(5);
    uneditedUser.setEmail("test@test.com");
    uneditedUser.setIsActive(true);
    List<Role> uneditedRoles = new ArrayList<Role>(1);
    Role role1 = new Role();
    role1.setRole(RoleEnum.BASIC);
    uneditedRoles.add(role1);
    Role role2 = new Role();
    role2.setRole(RoleEnum.SUPERADMIN);
    uneditedRoles.add(role2);
    uneditedUser.setRoles(uneditedRoles);
    AdminEditAppUser editedUser = new AdminEditAppUser();
    editedUser.setEmail("test2@test.com");
    editedUser.setActive(false);
    List<RoleEnum> editedRoles = new ArrayList<RoleEnum>(1);
    editedRoles.add(RoleEnum.BASIC);
    editedUser.setRoles(editedRoles);
    AppUserServiceImpl service = new AppUserServiceImpl();
    AppUserDao daoMock = mock(JdbcAppUserDaoImpl.class);
    service.setAppUserDao(daoMock);
    service.updateAppUser(uneditedUser, editedUser);
    List<RoleEnum> deletions = AppUserServiceImpl.getRoleDeletions(
        uneditedUser, editedUser);
    verify(daoMock).updateEmailByUserid(uneditedUser.getUserId(),
        editedUser.getEmail());
    verify(daoMock).updateIsActiveByUserid(uneditedUser.getUserId(),
        editedUser.getIsActive());
    verify(daoMock).deleteRolesByUserid(uneditedUser.getUserId(), deletions);
  }

  @Test
  public void testAreRolesEqual_bothNull() throws Exception {
    AppUser uneditedUser = new AppUser();
    uneditedUser.setRoles(null);
    AdminEditAppUser editedUser = new AdminEditAppUser();
    editedUser.setRoles(null);
    assertTrue(AppUserServiceImpl.areRolesEqual(uneditedUser, editedUser));
  }

  @Test
  public void testAreRolesEqual_EditedRolesNull() throws Exception {
    AppUser uneditedUser = new AppUser();
    List<Role> uneditedRoles = new ArrayList<Role>(1);
    Role role1 = new Role();
    role1.setRole(RoleEnum.BASIC);
    uneditedRoles.add(role1);
    uneditedUser.setRoles(uneditedRoles);
    AdminEditAppUser editedUser = new AdminEditAppUser();
    editedUser.setRoles(null);
    assertFalse(AppUserServiceImpl.areRolesEqual(uneditedUser, editedUser));
  }

  @Test
  public void testAreRolesEqual_UneditedRolesNull() throws Exception {
    AppUser uneditedUser = new AppUser();
    uneditedUser.setRoles(null);
    AdminEditAppUser editedUser = new AdminEditAppUser();
    List<RoleEnum> editedRoles = new ArrayList<RoleEnum>(1);
    editedRoles.add(RoleEnum.BASIC);
    editedUser.setRoles(editedRoles);
    assertFalse(AppUserServiceImpl.areRolesEqual(uneditedUser, editedUser));
  }

  @Test
  public void testAreRolesEqual_NeitherNull() throws Exception {
    AppUser uneditedUser = new AppUser();
    List<Role> uneditedRoles = new ArrayList<Role>(1);
    Role role1 = new Role();
    role1.setRole(RoleEnum.BASIC);
    uneditedRoles.add(role1);
    uneditedUser.setRoles(uneditedRoles);
    AdminEditAppUser editedUser = new AdminEditAppUser();
    List<RoleEnum> editedRoles = new ArrayList<RoleEnum>(1);
    editedRoles.add(RoleEnum.BASIC);
    editedUser.setRoles(editedRoles);
    assertTrue(AppUserServiceImpl.areRolesEqual(uneditedUser, editedUser));
  }

  @Test
  public void testGetRoleAdditions() throws Exception {
    AppUser uneditedUser = new AppUser();
    List<Role> uneditedRoles = new ArrayList<Role>(1);
    Role role1 = new Role();
    role1.setRole(RoleEnum.BASIC);
    uneditedRoles.add(role1);
    uneditedUser.setRoles(uneditedRoles);
    AdminEditAppUser editedUser = new AdminEditAppUser();
    List<RoleEnum> editedRoles = new ArrayList<RoleEnum>(2);
    editedRoles.add(RoleEnum.BASIC);
    editedRoles.add(RoleEnum.ADMIN);
    editedUser.setRoles(editedRoles);
    List<RoleEnum> rolesToAdd = new ArrayList<RoleEnum>(1);
    rolesToAdd.add(RoleEnum.ADMIN);
    assertEquals(rolesToAdd,
        AppUserServiceImpl.getRoleAdditions(uneditedUser, editedUser));
  }

  @Test
  public void testGetRoleDeletions() throws Exception {
    AppUser uneditedUser = new AppUser();
    List<Role> uneditedRoles = new ArrayList<Role>(2);
    Role role1 = new Role();
    role1.setRole(RoleEnum.ADMIN);
    uneditedRoles.add(role1);
    Role role2 = new Role();
    role2.setRole(RoleEnum.BASIC);
    uneditedRoles.add(role2);
    uneditedUser.setRoles(uneditedRoles);
    AdminEditAppUser editedUser = new AdminEditAppUser();
    List<RoleEnum> editedRoles = new ArrayList<RoleEnum>(1);
    editedRoles.add(RoleEnum.BASIC);
    editedUser.setRoles(editedRoles);
    List<RoleEnum> rolesToDelete = new ArrayList<RoleEnum>(1);
    rolesToDelete.add(RoleEnum.ADMIN);
    assertEquals(rolesToDelete,
        AppUserServiceImpl.getRoleDeletions(uneditedUser, editedUser));
  }

  @Test
  public void testEmailAdminDailyStats_noAdminUsers() throws Exception {
    AppUserDao daoMock = mock(JdbcAppUserDaoImpl.class);
    when(daoMock.getSuperAdminEmails()).thenReturn(new ArrayList<String>());
    AppUserServiceImpl service = new AppUserServiceImpl();
    service.setAppUserDao(daoMock);
    service.emailAdminStatsDaily();
    verify(daoMock).getSuperAdminEmails();
  }

  @Test
  public void testEmailAdminDailyStats_hasAdminUsers() throws Exception {
    AppUserDao daoMock = mock(JdbcAppUserDaoImpl.class);
    List<String> superAdmins = new ArrayList<String>(1);
    superAdmins.add("test@test.com");
    when(daoMock.getSuperAdminEmails()).thenReturn(superAdmins);
    int numberOfDays = 1;
    int newUsers = 3;
    int unconfirmedUsers = 4;
    int logins = 5;
    when(daoMock.getCountNewUsers(numberOfDays)).thenReturn(newUsers);
    when(daoMock.getCountUnconfirmedUsers(numberOfDays)).thenReturn(
        unconfirmedUsers);
    when(daoMock.getCountLogins(numberOfDays)).thenReturn(logins);
    MailService mailServiceMock = mock(MailServiceJavaMailSenderImpl.class);
    AppUserServiceImpl service = new AppUserServiceImpl();
    service.setAppUserDao(daoMock);
    service.setMailService(mailServiceMock);
    service.emailAdminStatsDaily();
    verify(daoMock).getSuperAdminEmails();
    verify(daoMock).getCountNewUsers(numberOfDays);
    verify(daoMock).getCountUnconfirmedUsers(numberOfDays);
    verify(daoMock).getCountLogins(numberOfDays);
    String subject = String.format(
        "%s GraffitiTracker Daily Stats %s",
        EnvironmentEnum.getEnvironmentEnumFromEnvironmentPropertyString(
            System.getProperty("CLASSPATH_PROP_ENV")).getDisplayString(),
        DateFormats.YEAR_SLASH_MONTH_SLASH_DAY_FORMAT.format(new Date()));
    String content = String
        .format(
            "New Users Last Day: %d\nUnconfirmed Users Last Day: %d\nNumber Users Logged In Last Day: %d\n",
            newUsers, unconfirmedUsers, logins);
    verify(mailServiceMock).sendSimpleEmail(superAdmins, subject, content);
  }
}
