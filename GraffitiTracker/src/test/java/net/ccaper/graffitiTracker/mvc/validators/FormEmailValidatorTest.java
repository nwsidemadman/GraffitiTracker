package net.ccaper.graffitiTracker.mvc.validators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import net.ccaper.graffitiTracker.mvc.validators.FormEmailValidator;
import net.ccaper.graffitiTracker.objects.EmailForm;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

public class FormEmailValidatorTest {
  FormEmailValidator formEmailValidator = new FormEmailValidator();

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testValidate_Empty() throws Exception {
    EmailForm emailFormInvalidEmail = new EmailForm();
    emailFormInvalidEmail.setEmail(StringUtils.EMPTY);
    Errors errors = new BeanPropertyBindingResult(emailFormInvalidEmail,
        "emailInvalidEmail");
    formEmailValidator.validate(emailFormInvalidEmail, errors);
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("email"));
  }

  @Test
  public void testValidate_Null() throws Exception {
    EmailForm emailFormInvalidEmail = new EmailForm();
    emailFormInvalidEmail.setEmail(null);
    Errors errors = new BeanPropertyBindingResult(emailFormInvalidEmail,
        "emailInvalidEmail");
    formEmailValidator.validate(emailFormInvalidEmail, errors);
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("email"));
  }

  @Test
  public void testValidateEmail_InvalidAddress() throws Exception {
    EmailForm emailFormInvalidEmail = new EmailForm();
    emailFormInvalidEmail.setEmail("test@test");
    Errors errors = new BeanPropertyBindingResult(emailFormInvalidEmail,
        "userInvalidEmail");
    formEmailValidator.validate(emailFormInvalidEmail, errors);
    assertTrue(errors.hasErrors());
    assertNotNull(errors.getFieldError("email"));
  }

  @Test
  public void testValidateEmail_HappyPath() throws Exception {
    EmailForm emailFormInvalidEmail = new EmailForm();
    emailFormInvalidEmail.setEmail("test@test.com");
    Errors errors = new BeanPropertyBindingResult(emailFormInvalidEmail,
        "userInvalidEmail");
    formEmailValidator.validate(emailFormInvalidEmail, errors);
    assertFalse(errors.hasErrors());
  }
}
