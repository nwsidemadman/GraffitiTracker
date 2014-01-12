package net.ccaper.GraffitiTracker.serviceImpl;


import static org.junit.Assert.assertTrue;

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
}
