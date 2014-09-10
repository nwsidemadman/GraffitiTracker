package net.ccaper.graffitiTracker.serviceImpl;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import net.ccaper.graffitiTracker.dao.LoginAddressDao;
import net.ccaper.graffitiTracker.dao.impl.JdbcLoginAddressDaoImpl;
import net.ccaper.graffitiTracker.service.LoginAddressService;
import net.ccaper.graffitiTracker.serviceImpl.LoginAddressServiceImpl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LoginAddressServiceImplTest {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testUpdateLoginAddressByUsername_invalidInet() throws Exception {
    LoginAddressService classUnderTest = new LoginAddressServiceImpl();
    classUnderTest.updateLoginAddressByUsername("testUser", "127.0.0.0.1");
  }
  
  @Test
  public void testUpdateLoginAddressByUsername_validInet() throws Exception {
    LoginAddressServiceImpl classUnderTest = new LoginAddressServiceImpl();
    LoginAddressDao daoMock = mock(JdbcLoginAddressDaoImpl.class);
    classUnderTest.setLoginAddressDao(daoMock);
    String username = "testUser";
    String ip = "127.0.0.1";
    classUnderTest.updateLoginAddressByUsername(username, ip);
    verify(daoMock).updateLoginAddressByUsername(username, ip);
  }
}
