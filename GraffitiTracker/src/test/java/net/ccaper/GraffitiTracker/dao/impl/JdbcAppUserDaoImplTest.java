package net.ccaper.GraffitiTracker.dao.impl;

import static org.junit.Assert.assertEquals;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.ccaper.GraffitiTracker.dao.AppUserDao;
import net.ccaper.GraffitiTracker.enums.RoleEnum;
import net.ccaper.GraffitiTracker.objects.AppUser;
import net.ccaper.GraffitiTracker.objects.Role;

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

  @Test
  public void testGetUsernameByEmail_HappyPath() {
    final String username = "testUsername";

    class JdbcAppUserDaoImplMock extends JdbcAppUserDaoImpl {
      @Override
      String getUsernameByEmail(Map<String, String> emailParamMap) {
        return username;
      }
    }

    AppUserDao jdbcAppUserMock = new JdbcAppUserDaoImplMock();
    assertEquals(username, jdbcAppUserMock.getUsernameByEmail("test@test.com"));
  }

  @Test
  public void testGetUsernameByEmail_EmptyResults() {
    class JdbcAppUserDaoImplMock extends JdbcAppUserDaoImpl {
      @Override
      String getUsernameByEmail(Map<String, String> emailParamMap) {
        throw new EmptyResultDataAccessException("test", 0);
      }
    }

    AppUserDao jdbcAppUserMock = new JdbcAppUserDaoImplMock();
    assertEquals(null, jdbcAppUserMock.getUsernameByEmail("test@test.com"));
  }
  
  @Test
  public void testGetEmailByUsername_HappyPath() {
    final String email = "test@test.com";

    class JdbcAppUserDaoImplMock extends JdbcAppUserDaoImpl {
      @Override
      String getEmailByUsername(Map<String, String> usernameParamMap) {
        return email;
      }
    }

    AppUserDao jdbcAppUserMock = new JdbcAppUserDaoImplMock();
    assertEquals(email, jdbcAppUserMock.getEmailByUsername("test@test.com"));
  }
  
  @Test
  public void testGetEmailByUsername_EmptyResults() {
    class JdbcAppUserDaoImplMock extends JdbcAppUserDaoImpl {
      @Override
      String getEmailByUsername(Map<String, String> usernameParamMap) {
        throw new EmptyResultDataAccessException("test", 0);
      }
    }

    AppUserDao jdbcAppUserMock = new JdbcAppUserDaoImplMock();
    assertEquals(null, jdbcAppUserMock.getEmailByUsername("testUsername"));
  }
  
  @Test(expected = EmptyResultDataAccessException.class)
  public void testGetAppUserById_userDoesNotExist() throws Exception {
    class JdbcAppUserDaoImplMock extends JdbcAppUserDaoImpl {
      @Override
      AppUser getAppUserByIdNoRoles(int id) throws EmptyResultDataAccessException {
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
      AppUser getAppUserByIdNoRoles(int id) throws EmptyResultDataAccessException {
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
}
