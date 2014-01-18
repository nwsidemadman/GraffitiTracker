package net.ccaper.GraffitiTracker.mvc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.ccaper.GraffitiTracker.mvc.validators.FormUserValidator;
import net.ccaper.GraffitiTracker.objects.TextCaptcha;
import net.ccaper.GraffitiTracker.objects.UserForm;
import net.ccaper.GraffitiTracker.service.AppUserService;
import net.ccaper.GraffitiTracker.service.BannedWordService;
import net.ccaper.GraffitiTracker.service.CaptchaService;
import net.ccaper.GraffitiTracker.serviceImpl.TextCaptchaServiceImpl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mail.MailSender;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

public class UserControllerTest {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testCreateUserProfile() {
    TextCaptcha captcha = new TextCaptcha("Some quesiton", "answer");
    CaptchaService captchaServiceMock = mock(CaptchaService.class);
    when(captchaServiceMock.getTextCaptcha()).thenReturn(captcha);
    UserController controller = new UserController();
    controller.setCaptchaService(captchaServiceMock);
    Model model = new ExtendedModelMap();
    HttpSession session = new MockHttpSession();
    assertEquals("users/edit", controller.createUserProfile(model, session));
    UserForm userForm = (UserForm) model.asMap().get("userForm");
    assertNull(userForm.getUsername());
    assertEquals(captcha.getQuestion(), userForm.getTextCaptchaQuestion());
    assertEquals(captcha, session.getAttribute("textCaptcha"));
    verify(captchaServiceMock).getTextCaptcha();
  }

  @Test
  public void testAddAppUserFromForm_HappyPath() throws Exception {
    TextCaptcha captcha = new TextCaptcha("What is Chris' name?",
        "6b34fe24ac2ff8103f6fce1f0da2ef57");
    UserForm userForm = new UserForm();
    userForm.setUsername("testUsername");
    userForm.setPassword("testPassword");
    userForm.setConfirmPassword("testPassword");
    userForm.setEmail("test@test.com");
    userForm.setAcceptTerms(true);
    userForm.setCaptchaAnswer("Chris");
    AppUserService appUserServiceMock = mock(AppUserService.class);
    when(appUserServiceMock.doesEmailExist(userForm.getEmail())).thenReturn(false);
    when(appUserServiceMock.doesUsernameExist(userForm.getUsername())).thenReturn(
        false);
    BannedWordService bannedWordServiceMock = mock(BannedWordService.class);
    when(bannedWordServiceMock.doesStringContainBannedWord(userForm.getUsername()))
    .thenReturn(false);
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    String servletPath = "/Servlet/servletPath1/servletPath2";
    when(requestMock.getRequestURL()).thenReturn(new StringBuffer("http:domain:port" + servletPath));
    when(requestMock.getServletPath()).thenReturn(servletPath);
    MailSender mailSenderMock = mock(MailSender.class);
    CaptchaService captchaService = new TextCaptchaServiceImpl();
    FormUserValidator formUserValidator = new FormUserValidator();
    formUserValidator.setAppUserService(appUserServiceMock);
    formUserValidator.setBannedWordService(bannedWordServiceMock);
    HttpSession session = new MockHttpSession();
    session.setAttribute("textCaptcha", captcha);
    UserController controller = new UserController();
    controller.setAppUserService(appUserServiceMock);
    controller.setFormUserValidator(formUserValidator);
    controller.setCaptchaService(captchaService);
    controller.setMailSender(mailSenderMock);
    BindingResult result = new BeanPropertyBindingResult(userForm, "userForm");
    assertEquals("redirect:/users/registered",
        controller.addAppUserFromForm(requestMock, session, userForm, result));
    verify(appUserServiceMock).doesEmailExist(userForm.getEmail());
    verify(appUserServiceMock).doesUsernameExist(userForm.getUsername());
    verify(bannedWordServiceMock).doesStringContainBannedWord(
        userForm.getUsername());
    verify(requestMock).getRequestURL();
    verify(requestMock).getServletPath();
  }

  @Test
  public void testAddAppUserFromForm_InvalidUser() throws Exception {
    TextCaptcha captcha = new TextCaptcha("What is Chris' name?",
        "6b34fe24ac2ff8103f6fce1f0da2ef57");
    TextCaptcha invalidUserCaptcha = new TextCaptcha(
        "What is eighteen thousand three hundred and fourteen as a number?",
        "eb98676e8ee16adce38796051a5cc7ff");
    UserForm userForm = new UserForm();
    userForm.setUsername("test");
    userForm.setPassword("testPassword");
    userForm.setConfirmPassword("testPassword");
    userForm.setEmail("test@test.com");
    userForm.setAcceptTerms(true);
    userForm.setCaptchaAnswer("Chris");
    AppUserService appUserServiceMock = mock(AppUserService.class);
    when(appUserServiceMock.doesEmailExist(userForm.getEmail())).thenReturn(false);
    CaptchaService captchaServiceMock = mock(TextCaptchaServiceImpl.class);
    when(captchaServiceMock.getTextCaptcha()).thenReturn(invalidUserCaptcha);
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    FormUserValidator formUserValidator = new FormUserValidator();
    formUserValidator.setAppUserService(appUserServiceMock);
    HttpSession session = new MockHttpSession();
    session.setAttribute("textCaptcha", captcha);
    UserController controller = new UserController();
    controller.setAppUserService(appUserServiceMock);
    controller.setFormUserValidator(formUserValidator);
    controller.setCaptchaService(captchaServiceMock);
    BindingResult result = new BeanPropertyBindingResult(userForm, "user");
    assertEquals("users/edit",
        controller.addAppUserFromForm(requestMock, session, userForm, result));
    assertFalse(userForm.getTextCaptchaQuestion().equals(captcha.getQuestion()));
    assertEquals(userForm.getTextCaptchaQuestion(),
        invalidUserCaptcha.getQuestion());
    assertNull(userForm.getCaptchaAnswer());
    assertFalse(captcha.equals(session
        .getAttribute("textCaptcha")));
    assertEquals(invalidUserCaptcha,
        session.getAttribute("textCaptcha"));
    verify(appUserServiceMock).doesEmailExist(userForm.getEmail());
    verify(captchaServiceMock).getTextCaptcha();
  }

  @Test
  public void testAddAppUserFromForm_IncorrectCaptchaAnswer() throws Exception {
    TextCaptcha captcha = new TextCaptcha("What is Chris' name?",
        "6b34fe24ac2ff8103f6fce1f0da2ef57");
    TextCaptcha incorrectAnswerCaptcha = new TextCaptcha(
        "What is eighteen thousand three hundred and fourteen as a number?",
        "eb98676e8ee16adce38796051a5cc7ff");
    UserForm userForm = new UserForm();
    userForm.setUsername("testUsername");
    userForm.setPassword("testPassword");
    userForm.setConfirmPassword("testPassword");
    userForm.setEmail("test@test.com");
    userForm.setAcceptTerms(true);
    userForm.setCaptchaAnswer("badAnswer");
    AppUserService appUserServiceMock = mock(AppUserService.class);
    when(appUserServiceMock.doesEmailExist(userForm.getEmail())).thenReturn(false);
    when(appUserServiceMock.doesUsernameExist(userForm.getUsername())).thenReturn(
        false);
    BannedWordService bannedWordServiceMock = mock(BannedWordService.class);
    when(bannedWordServiceMock.doesStringContainBannedWord(userForm.getUsername()))
    .thenReturn(false);
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    CaptchaService captchaServiceMock = mock(TextCaptchaServiceImpl.class);
    when(captchaServiceMock.getTextCaptcha()).thenReturn(incorrectAnswerCaptcha);
    FormUserValidator formUserValidator = new FormUserValidator();
    formUserValidator.setAppUserService(appUserServiceMock);
    formUserValidator.setBannedWordService(bannedWordServiceMock);
    HttpSession session = new MockHttpSession();
    session.setAttribute("textCaptcha", captcha);
    UserController controller = new UserController();
    controller.setAppUserService(appUserServiceMock);
    controller.setFormUserValidator(formUserValidator);
    controller.setCaptchaService(captchaServiceMock);
    BindingResult result = new BeanPropertyBindingResult(userForm, "user");
    assertEquals("users/edit",
        controller.addAppUserFromForm(requestMock, session, userForm, result));
    assertFalse(userForm.getTextCaptchaQuestion().equals(captcha.getQuestion()));
    assertEquals(userForm.getTextCaptchaQuestion(),
        incorrectAnswerCaptcha.getQuestion());
    assertNull(userForm.getCaptchaAnswer());
    assertFalse(captcha.equals(session
        .getAttribute("textCaptcha")));
    assertEquals(incorrectAnswerCaptcha,
        session.getAttribute("textCaptcha"));
    assertTrue(result.hasErrors());
    assertNotNull(result.getFieldError("captchaAnswer"));
    verify(appUserServiceMock).doesEmailExist(userForm.getEmail());
    verify(appUserServiceMock).doesUsernameExist(userForm.getUsername());
    verify(bannedWordServiceMock).doesStringContainBannedWord(
        userForm.getUsername());
    verify(captchaServiceMock).getTextCaptcha();
  }

  @Test
  public void testShowRegisteredUser() throws Exception {
    UserController controller = new UserController();
    assertEquals("users/registered", controller.showRegisteredUser());
  }

  @Test
  public void testGenerateEmailLink() throws Exception {
    String protocolDomanPortServlet = "http://domain:8080/Servlet";
    String oldServletPath = "/oldServletPathLevel1/oldServletPathLevel2";
    String newServletPath = "/oldServletPathLevel1/oldServletPathLevel2";
    UserController userController = new UserController();
    assertEquals(protocolDomanPortServlet + newServletPath, userController.getEmailLink(protocolDomanPortServlet + oldServletPath, oldServletPath, newServletPath));
  }

  @Test
  public void testShowConfirmedUser_HappyPath() throws Exception {
    Map<String, Object> model = new HashMap<String, Object>();
    String uniqueUrlParam = "582744a9-7f06-11e3-8afc-12313815ec2d";
    AppUserService appUserServiceMock = mock(AppUserService.class);
    when(appUserServiceMock.getUseridByUniqueUrlParam(uniqueUrlParam)).thenReturn(1);
    UserController userController = new UserController();
    userController.setAppUserService(appUserServiceMock);
    assertEquals("users/confirmed", userController.showConfirmedUser(uniqueUrlParam, model));
    assertTrue(model.containsKey("confirmed"));
    assertEquals(true, model.get("confirmed"));
    verify(appUserServiceMock).getUseridByUniqueUrlParam(uniqueUrlParam);
    verify(appUserServiceMock).updateAppUserAsActive(1);
    verify(appUserServiceMock).deleteRegistrationConfirmationByUniqueUrlParam(uniqueUrlParam);
  }

  @Test
  public void testShowConfirmedUser_UniqueUrlParamDoesNotExist() throws Exception {
    Map<String, Object> model = new HashMap<String, Object>();
    String uniqueUrlParam = "582744a9-7f06-11e3-8afc-12313815ec2d";
    AppUserService appUserServiceMock = mock(AppUserService.class);
    when(appUserServiceMock.getUseridByUniqueUrlParam(uniqueUrlParam)).thenReturn(null);
    UserController userController = new UserController();
    userController.setAppUserService(appUserServiceMock);
    assertEquals("users/confirmed", userController.showConfirmedUser(uniqueUrlParam, model));
    assertTrue(model.containsKey("confirmed"));
    assertEquals(false, model.get("confirmed"));
    verify(appUserServiceMock).getUseridByUniqueUrlParam(uniqueUrlParam);
  }
}
