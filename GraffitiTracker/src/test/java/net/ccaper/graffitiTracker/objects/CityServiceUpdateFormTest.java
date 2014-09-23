package net.ccaper.graffitiTracker.objects;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CityServiceUpdateFormTest {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testGetDateAsDate_notNUll() {
    CityServiceUpdateForm form = new CityServiceUpdateForm();
    form.setStartDate(500L);
    form.setEndDate(600L);
    assertNotNull(form.getStartDateAsDate());
    assertNotNull(form.getEndDateAsDate());
  }
  
  @Test
  public void testGetDateAsDate_nUll() {
    CityServiceUpdateForm form = new CityServiceUpdateForm();
    form.setStartDate(CityServiceUpdateForm.BEGINNING_OF_TIME);
    form.setEndDate(600L);
    assertNull(form.getStartDateAsDate());
    assertNull(form.getEndDateAsDate());
  }
}
