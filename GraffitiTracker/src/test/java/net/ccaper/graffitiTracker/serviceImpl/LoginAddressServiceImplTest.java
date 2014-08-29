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
  public void testIsIpAddressValid_HappyPath()  throws Exception {
    LoginAddressServiceImpl loginService = new LoginAddressServiceImpl();
    assertTrue(loginService.isIpAddressValid("127.0.0.1"));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testIsIpAddressValid_NoDot()  throws Exception {
    LoginAddressServiceImpl loginService = new LoginAddressServiceImpl();
    loginService.isIpAddressValid("0:0:0:0:0:0:0:1");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testIsIpAddressValid_TooFewDots()  throws Exception {
    LoginAddressServiceImpl loginService = new LoginAddressServiceImpl();
    loginService.isIpAddressValid("127.0");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testIsIpAddressValid_TooManyDots()  throws Exception {
    LoginAddressServiceImpl loginService = new LoginAddressServiceImpl();
    loginService.isIpAddressValid("127.0.0.0.1");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testIsIpAddressValid_NonNumber()  throws Exception {
    LoginAddressServiceImpl loginService = new LoginAddressServiceImpl();
    loginService.isIpAddressValid("127.Test.0.1");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testIsIpAddressValid_NumberTooSmall()  throws Exception {
    LoginAddressServiceImpl loginService = new LoginAddressServiceImpl();
    loginService.isIpAddressValid("127.-1.0.1");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testIsIpAddressValid_NumberTooBig()  throws Exception {
    LoginAddressServiceImpl loginService = new LoginAddressServiceImpl();
    loginService.isIpAddressValid("127.300.0.1");
  }
  
  @Test
  public void testUpdateLoginAddressByUsername_invalidInet() throws Exception {
    class LoginAddressServiceImplMock extends LoginAddressServiceImpl {
      @Override
      boolean isIpAddressValid(String ipAddress) {
        return false;
      }
    }
    
    LoginAddressService serviceMock = new LoginAddressServiceImplMock();
    serviceMock.updateLoginAddressByUsername("testUser", "127.0.0.0.1");
  }
  
  @Test
  public void testUpdateLoginAddressByUsername_validInet() throws Exception {
    class LoginAddressServiceImplMock extends LoginAddressServiceImpl {
      @Override
      boolean isIpAddressValid(String ipAddress) {
        return true;
      }
    }
    
    LoginAddressServiceImpl serviceMock = new LoginAddressServiceImplMock();
    LoginAddressDao daoMock = mock(JdbcLoginAddressDaoImpl.class);
    serviceMock.setLoginAddressDao(daoMock);
    String username = "testUser";
    String ip = "127.0.0.1";
    serviceMock.updateLoginAddressByUsername(username, ip);
    verify(daoMock).updateLoginAddressByUsername(username, ip);
  }
}
