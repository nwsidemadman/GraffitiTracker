package net.ccaper.GraffitiTracker.dao.impl;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import net.ccaper.GraffitiTracker.dao.AppUserDao;
import net.ccaper.GraffitiTracker.enums.RoleEnum;
import net.ccaper.GraffitiTracker.objects.AppUser;
import net.ccaper.GraffitiTracker.objects.Role;

import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.EmptyResultDataAccessException;

public class JdbcAppUserDaoImplTest {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test(expected = EmptyResultDataAccessException.class)
  public void testGetAppUserById_userDoesNotExist() throws Exception {
    class JdbcAppUserDaoImplMock extends JdbcAppUserDaoImpl {
      @Override
      AppUser getAppUserByIdNoRoles(int id)
          throws EmptyResultDataAccessException {
        throw new EmptyResultDataAccessException(1);
      }
    }

    AppUserDao daoMock = new JdbcAppUserDaoImplMock();
    daoMock.getAppUserById(5);
  }

  @Test
  public void testGetAppUserById_userExists() throws Exception {
    final AppUser appUser = new AppUser();
    appUser.setUserId(5);

    class JdbcAppUserDaoImplMock extends JdbcAppUserDaoImpl {
      @Override
      AppUser getAppUserByIdNoRoles(int id)
          throws EmptyResultDataAccessException {
        return appUser;
      }

      @Override
      List<Role> getRolesById(int id) {
        return new ArrayList<Role>(0);
      }
    }

    AppUserDao daoMock = new JdbcAppUserDaoImplMock();
    assertEquals(appUser, daoMock.getAppUserById(5));
  }

  @Test
  public void testGenerateDeleteRolesIns_oneRole() throws Exception {
    List<RoleEnum> roles = new ArrayList<RoleEnum>(1);
    roles.add(RoleEnum.BASIC);
    assertEquals(String.format("('%s')", RoleEnum.BASIC.getDbString()),
        JdbcAppUserDaoImpl.generateDeleteRolesIns(roles));
  }

  @Test
  public void testGenerateDeleteRolesIns_twoRoles() throws Exception {
    List<RoleEnum> roles = new ArrayList<RoleEnum>(1);
    roles.add(RoleEnum.BASIC);
    roles.add(RoleEnum.ADMIN);
    assertEquals(String.format("('%s', '%s')", RoleEnum.BASIC.getDbString(),
        RoleEnum.ADMIN.getDbString()),
        JdbcAppUserDaoImpl.generateDeleteRolesIns(roles));
  }

  @Test
  public void testGenerateDeleteRolesIns_Empty() throws Exception {
    List<RoleEnum> roles = new ArrayList<RoleEnum>(1);
    assertEquals("()", JdbcAppUserDaoImpl.generateDeleteRolesIns(roles));
  }

  @Test
  public void testGenerateDeleteRolesIns_null() throws Exception {
    assertEquals("()", JdbcAppUserDaoImpl.generateDeleteRolesIns(null));
  }

  @Test
  public void testGenerateInsertRolesValues_oneRole() throws Exception {
    List<RoleEnum> roles = new ArrayList<RoleEnum>(1);
    roles.add(RoleEnum.BASIC);
    int id = 5;
    assertEquals(String.format("(%d, '%s')", id, RoleEnum.BASIC.getDbString()),
        JdbcAppUserDaoImpl.generateInsertRolesValues(id, roles));
  }

  @Test
  public void testGenerateInsertRolesValues_twoRoles() throws Exception {
    List<RoleEnum> roles = new ArrayList<RoleEnum>(1);
    roles.add(RoleEnum.BASIC);
    roles.add(RoleEnum.ADMIN);
    int id = 5;
    assertEquals(
        String.format("(%d, '%s'), (%d, '%s')", id,
            RoleEnum.BASIC.getDbString(), id, RoleEnum.ADMIN.getDbString()),
        JdbcAppUserDaoImpl.generateInsertRolesValues(id, roles));
  }

  @Test
  public void testGenerateInsertRolesValues_Empty() throws Exception {
    List<RoleEnum> roles = new ArrayList<RoleEnum>(1);
    assertEquals(StringUtils.EMPTY,
        JdbcAppUserDaoImpl.generateInsertRolesValues(5, roles));
  }

  @Test
  public void testGenerateInsertRolesValues_null() throws Exception {
    assertEquals(StringUtils.EMPTY,
        JdbcAppUserDaoImpl.generateInsertRolesValues(5, null));
  }
}
