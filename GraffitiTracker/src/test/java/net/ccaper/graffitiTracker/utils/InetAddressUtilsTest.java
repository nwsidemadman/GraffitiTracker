package net.ccaper.graffitiTracker.utils;

import static org.junit.Assert.*;
import net.ccaper.graffitiTracker.serviceImpl.LoginAddressServiceImpl;

import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

// TODO(ccaper): confirm if needed
public class InetAddressUtilsTest {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testStringToNumber_happyPath() throws Exception {
    assertEquals(167773449d, InetAddressUtils.stringToNumber("10.0.5.9"),
        0.001d);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStringToNumber_notEnoughSubnets() throws Exception {
    InetAddressUtils.stringToNumber("10.0.5");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStringToNumber_subnetRangeTooBig() throws Exception {
    InetAddressUtils.stringToNumber("10.0.300.9");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStringToNumber_subnetRangeTooSmall() throws Exception {
    InetAddressUtils.stringToNumber("10.0.-5.9");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStringToNumber_subnetInvalidNotNumber() throws Exception {
    InetAddressUtils.stringToNumber("10.0.x.9");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStringToNumber_ipNull() throws Exception {
    InetAddressUtils.stringToNumber(null);
  }

  @Test
  public void testIsInetValid_HappyPath() throws Exception {
    assertTrue(InetAddressUtils.isInetValid("127.0.0.1"));
  }

  @Test
  public void testIsInetValid_NoDot() throws Exception {
    assertFalse(InetAddressUtils.isInetValid("0:0:0:0:0:0:0:1"));
  }

  @Test
  public void testIsInetValid_TooFewDots() throws Exception {
    assertFalse(InetAddressUtils.isInetValid("127.0"));
  }

  @Test
  public void testIsInetValid_TooManyDots() throws Exception {
    assertFalse(InetAddressUtils.isInetValid("127.0.0.0.1"));
  }

  @Test
  public void testIsInetValid_NonNumber() throws Exception {
    assertFalse(InetAddressUtils.isInetValid("127.Test.0.1"));
  }

  @Test
  public void testIsInetValid_NumberTooSmall() throws Exception {
    assertFalse(InetAddressUtils.isInetValid("127.-1.0.1"));
  }

  @Test
  public void testIsInetValid_NumberTooBig() throws Exception {
    assertFalse(InetAddressUtils.isInetValid("127.300.0.1"));
  }

  @Test
  public void testIsInetValid_null() throws Exception {
    assertFalse(InetAddressUtils.isInetValid(null));
  }

  @Test
  public void testIsInetValid_empty() throws Exception {
    assertFalse(InetAddressUtils.isInetValid(StringUtils.EMPTY));
  }
}
