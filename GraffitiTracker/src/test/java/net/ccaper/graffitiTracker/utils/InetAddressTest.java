package net.ccaper.graffitiTracker.utils;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

// TODO(ccaper): confirm if needed
public class InetAddressTest {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testStringToNumber_happyPath() throws Exception {
    assertEquals(167773449d, InetAddressUtils.stringToNumber("10.0.5.9"), 0.001d);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testStringToNumber_notEnoughSubnets() throws Exception {
    InetAddressUtils.stringToNumber("10.0.5");
  }
  
  @Test (expected = IllegalArgumentException.class)
  public void testStringToNumber_subnetRangeTooBig() throws Exception {
    InetAddressUtils.stringToNumber("10.0.300.9");
  }
  
  @Test (expected = IllegalArgumentException.class)
  public void testStringToNumber_subnetRangeTooSmall() throws Exception {
    InetAddressUtils.stringToNumber("10.0.-5.9");
  }
  
  @Test (expected = IllegalArgumentException.class)
  public void testStringToNumber_subnetInvalidNotNumber() throws Exception {
    InetAddressUtils.stringToNumber("10.0.x.9");
  }
  
  @Test (expected = IllegalArgumentException.class)
  public void testStringToNumber_ipNull() throws Exception {
    InetAddressUtils.stringToNumber(null);
  }
}
