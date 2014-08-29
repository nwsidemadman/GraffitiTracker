package net.ccaper.GraffitiTracker.mvc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.ccaper.GraffitiTracker.mvc.validators.FormEmailValidator;
import net.ccaper.GraffitiTracker.mvc.validators.FormManageAccountEditValidator;
import net.ccaper.GraffitiTracker.mvc.validators.FormPasswordSecurityValidator;
import net.ccaper.GraffitiTracker.mvc.validators.FormUserValidator;
import net.ccaper.GraffitiTracker.mvc.validators.FormUsernameValidator;
import net.ccaper.GraffitiTracker.objects.AppUser;
import net.ccaper.GraffitiTracker.objects.EmailForm;
import net.ccaper.GraffitiTracker.objects.ManageAccountForm;
import net.ccaper.GraffitiTracker.objects.PasswordSecurityForm;
import net.ccaper.GraffitiTracker.objects.TextCaptcha;
import net.ccaper.GraffitiTracker.objects.UserForm;
import net.ccaper.GraffitiTracker.objects.UserSecurityQuestion;
import net.ccaper.GraffitiTracker.objects.UsernameForm;
import net.ccaper.GraffitiTracker.service.AppUserService;
import net.ccaper.GraffitiTracker.service.BannedInetsService;
import net.ccaper.GraffitiTracker.service.CaptchaService;
import net.ccaper.GraffitiTracker.service.LoginAddressService;
import net.ccaper.GraffitiTracker.service.MailService;
import net.ccaper.GraffitiTracker.service.UserSecurityService;
import net.ccaper.GraffitiTracker.serviceImpl.TextCaptchaServiceImpl;
import net.ccaper.GraffitiTracker.utils.Encoder;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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
  public void testCreateUserProfile_HappyPath_NotAnonymousUser() {
    String username = "testUser";
    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    AppUserService userServiceMock = mock(AppUserService.class);
    when(userServiceMock.getUserByUsername(username)).thenReturn(appUser);
    String inet = "127.0.0.1";
    TextCaptcha captcha = new TextCaptcha("Some question", "answer");
    CaptchaService captchaServiceMock = mock(CaptchaService.class);
    when(captchaServiceMock.getTextCaptcha()).thenReturn(captcha);
    BannedInetsService banndedInetsServiceMock = mock(BannedInetsService.class);
    when(banndedInetsServiceMock.isInetBanned(inet)).thenReturn(false);
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    when(requestMock.getRemoteAddr()).thenReturn(inet);
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(false);
    when(userSecurityService.getUsernameFromSecurity()).thenReturn(username);
    UserController classUnderTest = new UserController();
    classUnderTest.setAppUserService(userServiceMock);
    classUnderTest.setBannedInetsService(banndedInetsServiceMock);
    classUnderTest.setCaptchaService(captchaServiceMock);
    classUnderTest.setUserSecurityService(userSecurityService);
    Model model = new ExtendedModelMap();
    HttpSession session = new MockHttpSession();
    assertEquals("users/create",
        classUnderTest.createUserProfile(model, session, requestMock));
    UserForm userForm = (UserForm) model.asMap().get("userForm");
    assertNull(userForm.getUsername());
    assertEquals(captcha.getQuestion(), userForm.getTextCaptchaQuestion());
    assertEquals(captcha, session.getAttribute("textCaptcha"));
    assertTrue(model.containsAttribute("appUser"));
    Map<String, Object> modelMap = model.asMap();
    assertEquals(username, ((AppUser) modelMap.get("appUser")).getUsername());
    verify(userServiceMock).getUserByUsername(username);
    verify(captchaServiceMock).getTextCaptcha();
    verify(banndedInetsServiceMock).isInetBanned(inet);
    verify(requestMock).getRemoteAddr();
    verify(userServiceMock).getUserByUsername(username);
    verify(userSecurityService).isUserAnonymous();
    verify(userSecurityService).getUsernameFromSecurity();
  }

  @Test
  public void testCreateUserProfile_HappyPath_AnonymousUser() {
    String inet = "127.0.0.1";
    TextCaptcha captcha = new TextCaptcha("Some question", "answer");
    CaptchaService captchaServiceMock = mock(CaptchaService.class);
    when(captchaServiceMock.getTextCaptcha()).thenReturn(captcha);
    BannedInetsService banndedInetsServiceMock = mock(BannedInetsService.class);
    when(banndedInetsServiceMock.isInetBanned(inet)).thenReturn(false);
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    when(requestMock.getRemoteAddr()).thenReturn(inet);
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(true);
    UserController classUnderTest = new UserController();
    classUnderTest.setBannedInetsService(banndedInetsServiceMock);
    classUnderTest.setCaptchaService(captchaServiceMock);
    classUnderTest.setUserSecurityService(userSecurityService);
    Model model = new ExtendedModelMap();
    HttpSession session = new MockHttpSession();
    assertEquals("users/create",
        classUnderTest.createUserProfile(model, session, requestMock));
    UserForm userForm = (UserForm) model.asMap().get("userForm");
    assertNull(userForm.getUsername());
    assertEquals(captcha.getQuestion(), userForm.getTextCaptchaQuestion());
    assertEquals(captcha, session.getAttribute("textCaptcha"));
    assertFalse(model.containsAttribute("appUser"));
    verify(captchaServiceMock).getTextCaptcha();
    verify(banndedInetsServiceMock).isInetBanned(inet);
    verify(requestMock).getRemoteAddr();
    verify(userSecurityService).isUserAnonymous();
  }

  @Test
  public void testCreateUserProfile_InetBanned_NotAnonymousUser() {
    String username = "testUser";
    String inet = "127.0.0.1";
    BannedInetsService banndedInetsServiceMock = mock(BannedInetsService.class);
    when(banndedInetsServiceMock.isInetBanned(inet)).thenReturn(true);
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    when(requestMock.getRemoteAddr()).thenReturn(inet);
    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    AppUserService userServiceMock = mock(AppUserService.class);
    when(userServiceMock.getUserByUsername(username)).thenReturn(appUser);
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(false);
    when(userSecurityService.getUsernameFromSecurity()).thenReturn(username);
    UserController classUnderTest = new UserController();
    classUnderTest.setBannedInetsService(banndedInetsServiceMock);
    classUnderTest.setAppUserService(userServiceMock);
    classUnderTest.setUserSecurityService(userSecurityService);
    Model model = new ExtendedModelMap();
    HttpSession session = new MockHttpSession();
    assertEquals("users/banned",
        classUnderTest.createUserProfile(model, session, requestMock));
    assertTrue(model.containsAttribute("appUser"));
    Map<String, Object> modelMap = model.asMap();
    assertEquals(username, ((AppUser) modelMap.get("appUser")).getUsername());
    verify(banndedInetsServiceMock).isInetBanned(inet);
    verify(requestMock).getRemoteAddr();
    verify(userServiceMock).getUserByUsername(username);
    verify(userSecurityService).isUserAnonymous();
    verify(userSecurityService).getUsernameFromSecurity();
  }

  @Test
  public void testCreateUserProfile_InetBanned_AnonymousUser() {
    String inet = "127.0.0.1";
    BannedInetsService banndedInetsServiceMock = mock(BannedInetsService.class);
    when(banndedInetsServiceMock.isInetBanned(inet)).thenReturn(true);
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    when(requestMock.getRemoteAddr()).thenReturn(inet);
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(true);
    UserController classUnderTest = new UserController();
    classUnderTest.setBannedInetsService(banndedInetsServiceMock);
    classUnderTest.setUserSecurityService(userSecurityService);
    Model model = new ExtendedModelMap();
    HttpSession session = new MockHttpSession();
    assertEquals("users/banned",
        classUnderTest.createUserProfile(model, session, requestMock));
    assertFalse(model.containsAttribute("appUser"));
    verify(banndedInetsServiceMock).isInetBanned(inet);
    verify(requestMock).getRemoteAddr();
    verify(userSecurityService).isUserAnonymous();
  }

  @Test
  public void testAddAppUserFromForm_HappyPath_NotAnonymousUser() throws Exception {
    class UserControllerMock extends UserController {
      @Override
      String generateConfirmationEmailBodyWithVelocityEngine(UserForm userForm,
          HttpServletRequest request) {
        return "test";
      }
    }

    String username = "testUser";
    TextCaptcha captcha = new TextCaptcha("What is Chris' name?",
        "6b34fe24ac2ff8103f6fce1f0da2ef57");
    UserForm userForm = new UserForm();
    userForm.setUsername("testUsername");
    userForm.setPassword("testPassword");
    userForm.setConfirmPassword("testPassword");
    userForm.setEmail("test@test.com");
    userForm.setAcceptTerms(true);
    userForm.setCaptchaAnswer("Chris");
    userForm.setSecurityQuestion("testQuestion");
    userForm.setSecurityAnswer("testAnswer");
    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    AppUserService appUserServiceMock = mock(AppUserService.class);
    when(appUserServiceMock.getUserByUsername(username)).thenReturn(appUser);
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    MailService mailServiceMock = mock(MailService.class);
    CaptchaService captchaService = new TextCaptchaServiceImpl();
    FormUserValidator formUserValidatorMock = mock(FormUserValidator.class);
    HttpSession session = new MockHttpSession();
    session.setAttribute("textCaptcha", captcha);
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(false);
    when(userSecurityService.getUsernameFromSecurity()).thenReturn(username);
    UserControllerMock controller = new UserControllerMock();
    controller.setAppUserService(appUserServiceMock);
    controller.setFormUserValidator(formUserValidatorMock);
    controller.setCaptchaService(captchaService);
    controller.setMailService(mailServiceMock);
    controller.setUserSecurityService(userSecurityService);
    BindingResult result = new BeanPropertyBindingResult(userForm, "userForm");
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("redirect:/users/registered", controller.addAppUserFromForm(
        requestMock, session, userForm, result, model));
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    verify(appUserServiceMock).getUserByUsername(username);
    verify(userSecurityService).isUserAnonymous();
    verify(userSecurityService).getUsernameFromSecurity();
  }
  
  @Test
  public void testAddAppUserFromForm_HappyPath_AnonymousUser() throws Exception {
    class UserControllerMock extends UserController {
      @Override
      String generateConfirmationEmailBodyWithVelocityEngine(UserForm userForm,
          HttpServletRequest request) {
        return "test";
      }
    }

    TextCaptcha captcha = new TextCaptcha("What is Chris' name?",
        "6b34fe24ac2ff8103f6fce1f0da2ef57");
    UserForm userForm = new UserForm();
    userForm.setUsername("testUsername");
    userForm.setPassword("testPassword");
    userForm.setConfirmPassword("testPassword");
    userForm.setEmail("test@test.com");
    userForm.setAcceptTerms(true);
    userForm.setCaptchaAnswer("Chris");
    userForm.setSecurityAnswer("testAnswer");
    userForm.setSecurityQuestion("testQuestion");
    AppUserService appUserServiceMock = mock(AppUserService.class);
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    MailService mailServiceMock = mock(MailService.class);
    CaptchaService captchaService = new TextCaptchaServiceImpl();
    FormUserValidator formUserValidatorMock = mock(FormUserValidator.class);
    HttpSession session = new MockHttpSession();
    session.setAttribute("textCaptcha", captcha);
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(true);
    UserControllerMock controller = new UserControllerMock();
    controller.setAppUserService(appUserServiceMock);
    controller.setFormUserValidator(formUserValidatorMock);
    controller.setCaptchaService(captchaService);
    controller.setMailService(mailServiceMock);
    controller.setUserSecurityService(userSecurityService);
    BindingResult result = new BeanPropertyBindingResult(userForm, "userForm");
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("redirect:/users/registered", controller.addAppUserFromForm(
        requestMock, session, userForm, result, model));
    assertFalse(model.containsKey("appUser"));
    verify(userSecurityService).isUserAnonymous();
  }

  @Test
  public void testAddAppUserFromForm_InvalidUser_NotAnonymousUser()
      throws Exception {
    String username = "testUser";
    TextCaptcha captcha = new TextCaptcha("What is Chris' name?",
        "6b34fe24ac2ff8103f6fce1f0da2ef57");
    TextCaptcha invalidUserCaptcha = new TextCaptcha(
        "What is eighteen thousand three hundred and fourteen as a number?",
        "eb98676e8ee16adce38796051a5cc7ff");
    UserForm userForm = new UserForm();
    userForm.setUsername("test");
    userForm.setPassword("testPassword");
    userForm.setConfirmPassword("testPassword");
    userForm.setEmail("badEmail");
    userForm.setAcceptTerms(true);
    userForm.setCaptchaAnswer("Chris");
    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    AppUserService appUserServiceMock = mock(AppUserService.class);
    when(appUserServiceMock.getUserByUsername(username)).thenReturn(appUser);
    CaptchaService captchaServiceMock = mock(TextCaptchaServiceImpl.class);
    when(captchaServiceMock.getTextCaptcha()).thenReturn(invalidUserCaptcha);
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    FormUserValidator formUserValidator = new FormUserValidator();
    HttpSession session = new MockHttpSession();
    session.setAttribute("textCaptcha", captcha);
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(false);
    when(userSecurityService.getUsernameFromSecurity()).thenReturn(username);
    UserController classUnderTest = new UserController();
    classUnderTest.setAppUserService(appUserServiceMock);
    classUnderTest.setFormUserValidator(formUserValidator);
    classUnderTest.setCaptchaService(captchaServiceMock);
    classUnderTest.setUserSecurityService(userSecurityService);
    BindingResult result = new BeanPropertyBindingResult(userForm, "user");
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("users/create", classUnderTest.addAppUserFromForm(requestMock,
        session, userForm, result, model));
    assertFalse(userForm.getTextCaptchaQuestion().equals(captcha.getQuestion()));
    assertEquals(userForm.getTextCaptchaQuestion(),
        invalidUserCaptcha.getQuestion());
    assertNull(userForm.getCaptchaAnswer());
    assertFalse(captcha.equals(session.getAttribute("textCaptcha")));
    assertEquals(invalidUserCaptcha, session.getAttribute("textCaptcha"));
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    verify(captchaServiceMock).getTextCaptcha();
    verify(appUserServiceMock).getUserByUsername(username);
    verify(userSecurityService).isUserAnonymous();
    verify(userSecurityService).getUsernameFromSecurity();
  }

  @Test
  public void testAddAppUserFromForm_InvalidUser_AnonymousUser()
      throws Exception {
    TextCaptcha captcha = new TextCaptcha("What is Chris' name?",
        "6b34fe24ac2ff8103f6fce1f0da2ef57");
    TextCaptcha invalidUserCaptcha = new TextCaptcha(
        "What is eighteen thousand three hundred and fourteen as a number?",
        "eb98676e8ee16adce38796051a5cc7ff");
    UserForm userForm = new UserForm();
    userForm.setUsername("test");
    userForm.setPassword("testPassword");
    userForm.setConfirmPassword("testPassword");
    userForm.setEmail("badEmail");
    userForm.setAcceptTerms(true);
    userForm.setCaptchaAnswer("Chris");
    AppUserService appUserServiceMock = mock(AppUserService.class);
    CaptchaService captchaServiceMock = mock(TextCaptchaServiceImpl.class);
    when(captchaServiceMock.getTextCaptcha()).thenReturn(invalidUserCaptcha);
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    FormUserValidator formUserValidator = new FormUserValidator();
    HttpSession session = new MockHttpSession();
    session.setAttribute("textCaptcha", captcha);
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(true);
    UserController classUnderTest = new UserController();
    classUnderTest.setAppUserService(appUserServiceMock);
    classUnderTest.setFormUserValidator(formUserValidator);
    classUnderTest.setCaptchaService(captchaServiceMock);
    classUnderTest.setUserSecurityService(userSecurityService);
    BindingResult result = new BeanPropertyBindingResult(userForm, "user");
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("users/create", classUnderTest.addAppUserFromForm(requestMock,
        session, userForm, result, model));
    assertFalse(userForm.getTextCaptchaQuestion().equals(captcha.getQuestion()));
    assertEquals(userForm.getTextCaptchaQuestion(),
        invalidUserCaptcha.getQuestion());
    assertNull(userForm.getCaptchaAnswer());
    assertFalse(captcha.equals(session.getAttribute("textCaptcha")));
    assertEquals(invalidUserCaptcha, session.getAttribute("textCaptcha"));
    assertFalse(model.containsKey("appUser"));
    verify(captchaServiceMock).getTextCaptcha();
    verify(userSecurityService).isUserAnonymous();
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
    userForm.setSecurityQuestion("testQuestion");
    userForm.setSecurityAnswer("testAnswer");
    AppUserService appUserServiceMock = mock(AppUserService.class);
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    CaptchaService captchaServiceMock = mock(TextCaptchaServiceImpl.class);
    when(captchaServiceMock.getTextCaptcha())
        .thenReturn(incorrectAnswerCaptcha);
    FormUserValidator formUserValidatorMock = mock(FormUserValidator.class);
    HttpSession session = new MockHttpSession();
    session.setAttribute("textCaptcha", captcha);
    UserController controller = new UserController();
    controller.setAppUserService(appUserServiceMock);
    controller.setFormUserValidator(formUserValidatorMock);
    controller.setCaptchaService(captchaServiceMock);
    BindingResult result = new BeanPropertyBindingResult(userForm, "user");
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("users/create", controller.addAppUserFromForm(requestMock,
        session, userForm, result, model));
    assertFalse(userForm.getTextCaptchaQuestion().equals(captcha.getQuestion()));
    assertEquals(userForm.getTextCaptchaQuestion(),
        incorrectAnswerCaptcha.getQuestion());
    assertNull(userForm.getCaptchaAnswer());
    assertFalse(captcha.equals(session.getAttribute("textCaptcha")));
    assertEquals(incorrectAnswerCaptcha, session.getAttribute("textCaptcha"));
    assertTrue(result.hasErrors());
    assertNotNull(result.getFieldError("captchaAnswer"));
    verify(captchaServiceMock).getTextCaptcha();
  }

  @Test
  public void testShowRegisteredUser_NotAnonymousUser() throws Exception {
    String username = "testUser";
    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    AppUserService userServiceMock = mock(AppUserService.class);
    when(userServiceMock.getUserByUsername(username)).thenReturn(appUser);
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(false);
    when(userSecurityService.getUsernameFromSecurity()).thenReturn(username);
    UserController classUnderTest = new UserController();
    classUnderTest.setAppUserService(userServiceMock);
    classUnderTest.setUserSecurityService(userSecurityService);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("users/registered", classUnderTest.showRegisteredUser(model));
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    verify(userServiceMock).getUserByUsername(username);
    verify(userSecurityService).isUserAnonymous();
    verify(userSecurityService).getUsernameFromSecurity();
  }

  @Test
  public void testShowRegisteredUser_AnonymousUser() throws Exception {
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(true);
    UserController classUnderTest = new UserController();
    classUnderTest.setUserSecurityService(userSecurityService);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("users/registered", classUnderTest.showRegisteredUser(model));
    assertFalse(model.containsKey("appUser"));
    verify(userSecurityService).isUserAnonymous();
  }

  @Test
  public void testGetEmailLink() throws Exception {
    String protocolDomainPortServlet = "http://domain:8080/Servlet";
    String oldServletPath = "/oldServletPathLevel1/oldServletPathLevel2";
    String uniqueUrlParam = "test";
    UserController userController = new UserController();
    assertEquals(protocolDomainPortServlet
        + UserController.CONFIRMED_EMAIL_LINK_SERVLET_PATH_WITH_PARAM
        + uniqueUrlParam, userController.getEmailLink(protocolDomainPortServlet
        + oldServletPath, oldServletPath,
        UserController.CONFIRMED_EMAIL_LINK_SERVLET_PATH_WITH_PARAM,
        uniqueUrlParam));
  }

  @Test
  public void testGetHomeLink() throws Exception {
    String protocolDomainPortServlet = "http://domain:8080/Servlet";
    String oldServletPath = "/oldServletPathLevel1/oldServletPathLevel2";
    UserController userController = new UserController();
    assertEquals(protocolDomainPortServlet + UserController.HOME_LINK,
        userController.getHomeLink(protocolDomainPortServlet + oldServletPath,
            oldServletPath));
  }

  @Test
  public void testShowConfirmedUser_HappyPath_NotAnonymousUser()
      throws Exception {
    String username = "testUser";
    Map<String, Object> model = new HashMap<String, Object>();
    String uniqueUrlParam = "582744a9-7f06-11e3-8afc-12313815ec2d";
    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    AppUserService appUserServiceMock = mock(AppUserService.class);
    when(
        appUserServiceMock
            .getUserIdByRegistrationConfirmationUniqueUrlParam(uniqueUrlParam))
        .thenReturn(1);
    when(appUserServiceMock.getUserByUsername(username)).thenReturn(appUser);
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(false);
    when(userSecurityService.getUsernameFromSecurity()).thenReturn(username);
    UserController classUnderTest = new UserController();
    classUnderTest.setAppUserService(appUserServiceMock);
    classUnderTest.setUserSecurityService(userSecurityService);
    assertEquals("users/confirmed",
        classUnderTest.showConfirmedUser(uniqueUrlParam, model));
    assertTrue(model.containsKey("confirmed"));
    assertEquals(true, model.get("confirmed"));
    verify(appUserServiceMock)
        .getUserIdByRegistrationConfirmationUniqueUrlParam(uniqueUrlParam);
    verify(appUserServiceMock).updateAppUserAsActive(1);
    verify(appUserServiceMock).deleteRegistrationConfirmationByUniqueUrlParam(
        uniqueUrlParam);
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    verify(appUserServiceMock).getUserByUsername(username);
    verify(userSecurityService).isUserAnonymous();
    verify(userSecurityService).getUsernameFromSecurity();
  }

  @Test
  public void testShowConfirmedUser_HappyPath_AnonymousUser() throws Exception {
    Map<String, Object> model = new HashMap<String, Object>();
    String uniqueUrlParam = "582744a9-7f06-11e3-8afc-12313815ec2d";
    AppUserService appUserServiceMock = mock(AppUserService.class);
    when(
        appUserServiceMock
            .getUserIdByRegistrationConfirmationUniqueUrlParam(uniqueUrlParam))
        .thenReturn(1);
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(true);
    UserController classUnderTest = new UserController();
    classUnderTest.setAppUserService(appUserServiceMock);
    classUnderTest.setUserSecurityService(userSecurityService);
    assertEquals("users/confirmed",
        classUnderTest.showConfirmedUser(uniqueUrlParam, model));
    assertTrue(model.containsKey("confirmed"));
    assertEquals(true, model.get("confirmed"));
    verify(appUserServiceMock)
        .getUserIdByRegistrationConfirmationUniqueUrlParam(uniqueUrlParam);
    verify(appUserServiceMock).updateAppUserAsActive(1);
    verify(appUserServiceMock).deleteRegistrationConfirmationByUniqueUrlParam(
        uniqueUrlParam);
    assertFalse(model.containsKey("appUser"));
    verify(userSecurityService).isUserAnonymous();
  }

  @Test
  public void testShowConfirmedUser_UniqueUrlParamDoesNotExist_NotAnonymousUser()
      throws Exception {
    String username = "testUser";
    Map<String, Object> model = new HashMap<String, Object>();
    String uniqueUrlParam = "582744a9-7f06-11e3-8afc-12313815ec2d";
    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    AppUserService appUserServiceMock = mock(AppUserService.class);
    when(
        appUserServiceMock
            .getUserIdByRegistrationConfirmationUniqueUrlParam(uniqueUrlParam))
        .thenReturn(null);
    when(appUserServiceMock.getUserByUsername(username)).thenReturn(appUser);
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(false);
    when(userSecurityService.getUsernameFromSecurity()).thenReturn(username);
    UserController classUnderTest = new UserController();
    classUnderTest.setAppUserService(appUserServiceMock);
    classUnderTest.setUserSecurityService(userSecurityService);
    assertEquals("users/confirmed",
        classUnderTest.showConfirmedUser(uniqueUrlParam, model));
    assertTrue(model.containsKey("confirmed"));
    assertEquals(false, model.get("confirmed"));
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    verify(appUserServiceMock).getUserByUsername(username);
    verify(appUserServiceMock)
        .getUserIdByRegistrationConfirmationUniqueUrlParam(uniqueUrlParam);
    verify(userSecurityService).isUserAnonymous();
    verify(userSecurityService).getUsernameFromSecurity();
  }

  @Test
  public void testShowConfirmedUser_UniqueUrlParamDoesNotExist_AnonymousUser()
      throws Exception {
    Map<String, Object> model = new HashMap<String, Object>();
    String uniqueUrlParam = "582744a9-7f06-11e3-8afc-12313815ec2d";
    AppUserService appUserServiceMock = mock(AppUserService.class);
    when(
        appUserServiceMock
            .getUserIdByRegistrationConfirmationUniqueUrlParam(uniqueUrlParam))
        .thenReturn(null);
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(true);
    UserController classUnderTest = new UserController();
    classUnderTest.setAppUserService(appUserServiceMock);
    classUnderTest.setUserSecurityService(userSecurityService);
    assertEquals("users/confirmed",
        classUnderTest.showConfirmedUser(uniqueUrlParam, model));
    assertTrue(model.containsKey("confirmed"));
    assertEquals(false, model.get("confirmed"));
    assertFalse(model.containsKey("appUser"));
    verify(appUserServiceMock)
        .getUserIdByRegistrationConfirmationUniqueUrlParam(uniqueUrlParam);
    verify(userSecurityService).isUserAnonymous();
  }

  @Test
  public void testSendUsername_InvalidEmail_NotAnonymousUser() throws Exception {
    String username = "testUser";
    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    AppUserService userServiceMock = mock(AppUserService.class);
    when(userServiceMock.getUserByUsername(username)).thenReturn(appUser);
    EmailForm emailForm = new EmailForm();
    emailForm.setEmail("test");
    BindingResult result = new BeanPropertyBindingResult(emailForm, "email");
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    FormEmailValidator formEmailValidator = new FormEmailValidator();
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(false);
    when(userSecurityService.getUsernameFromSecurity()).thenReturn(username);
    UserController classUnderTest = new UserController();
    classUnderTest.setAppUserService(userServiceMock);
    classUnderTest.setFormEmailValidator(formEmailValidator);
    classUnderTest.setUserSecurityService(userSecurityService);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("users/forgotUsername",
        classUnderTest.sendUsername(emailForm, result, requestMock, model));
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    verify(userServiceMock).getUserByUsername(username);
    verify(userSecurityService).isUserAnonymous();
    verify(userSecurityService).getUsernameFromSecurity();
  }

  @Test
  public void testSendUsername_InvalidEmail_AnonymousUser() throws Exception {
    EmailForm emailForm = new EmailForm();
    emailForm.setEmail("test");
    BindingResult result = new BeanPropertyBindingResult(emailForm, "email");
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    FormEmailValidator formEmailValidator = new FormEmailValidator();
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(true);
    UserController classUnderTest = new UserController();
    classUnderTest.setFormEmailValidator(formEmailValidator);
    classUnderTest.setUserSecurityService(userSecurityService);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("users/forgotUsername",
        classUnderTest.sendUsername(emailForm, result, requestMock, model));
    assertFalse(model.containsKey("appUser"));
    verify(userSecurityService).isUserAnonymous();
  }

  @Test
  public void testSendUsername_UserDoesNotExist_NotAnonymousUser() throws Exception {
     String username = "testUser";
    EmailForm emailForm = new EmailForm();
    String email = "test@test.com";
    emailForm.setEmail(email);
    BindingResult result = new BeanPropertyBindingResult(emailForm, "email");
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    FormEmailValidator formEmailValidator = new FormEmailValidator();
    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    AppUserService appUserServiceMock = mock(AppUserService.class);
    when(appUserServiceMock.getUsernameByEmail(email)).thenReturn(null);
    when(appUserServiceMock.getUserByUsername(username)).thenReturn(appUser);
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(false);
    when(userSecurityService.getUsernameFromSecurity()).thenReturn(username);
    UserController classUnderTest = new UserController();
    classUnderTest.setAppUserService(appUserServiceMock);
    classUnderTest.setFormEmailValidator(formEmailValidator);
    classUnderTest.setUserSecurityService(userSecurityService);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("redirect:/users/sentUsername",
        classUnderTest.sendUsername(emailForm, result, requestMock, model));
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    verify(appUserServiceMock).getUserByUsername(username);
    verify(appUserServiceMock).getUsernameByEmail(email);
    verify(userSecurityService).isUserAnonymous();
    verify(userSecurityService).getUsernameFromSecurity();
  }
  
  @Test
  public void testSendUsername_UserDoesNotExist_AnonymousUser() throws Exception {
    EmailForm emailForm = new EmailForm();
    String email = "test@test.com";
    emailForm.setEmail(email);
    BindingResult result = new BeanPropertyBindingResult(emailForm, "email");
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    FormEmailValidator formEmailValidator = new FormEmailValidator();
    AppUserService appUserServiceMock = mock(AppUserService.class);
    when(appUserServiceMock.getUsernameByEmail(email)).thenReturn(null);
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(true);
    UserController classUnderTest = new UserController();
    classUnderTest.setAppUserService(appUserServiceMock);
    classUnderTest.setFormEmailValidator(formEmailValidator);
    classUnderTest.setUserSecurityService(userSecurityService);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("redirect:/users/sentUsername",
        classUnderTest.sendUsername(emailForm, result, requestMock, model));
    assertFalse(model.containsKey("appUser"));
    verify(appUserServiceMock).getUsernameByEmail(email);
    verify(userSecurityService).isUserAnonymous();
  }

  @Test
  public void testSendUsername_HappyPath_NotAnonymousUser() throws Exception {
    class UserControllerMock extends UserController {
      @Override
      String generateForgotUsernameEmailBodyWithVelocityEngine(String username,
          HttpServletRequest request) {
        return "test";
      }
    }

    String username = "testUser";
    EmailForm emailForm = new EmailForm();
    String email = "test@test.com";
    emailForm.setEmail(email);
    BindingResult result = new BeanPropertyBindingResult(emailForm, "email");
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    FormEmailValidator formEmailValidator = new FormEmailValidator();
    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    AppUserService appUserServiceMock = mock(AppUserService.class);
    when(appUserServiceMock.getUsernameByEmail(email)).thenReturn(username);
    when(appUserServiceMock.getUserByUsername(username)).thenReturn(appUser);
    MailService mailServiceMock = mock(MailService.class);
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(false);
    when(userSecurityService.getUsernameFromSecurity()).thenReturn(username);
    UserController controllerMock = new UserControllerMock();
    controllerMock.setAppUserService(appUserServiceMock);
    controllerMock.setFormEmailValidator(formEmailValidator);
    controllerMock.setMailService(mailServiceMock);
    controllerMock.setUserSecurityService(userSecurityService);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("redirect:/users/sentUsername",
        controllerMock.sendUsername(emailForm, result, requestMock, model));
    verify(appUserServiceMock).getUsernameByEmail(email);
    List<String> recipients = new ArrayList<String>();
    recipients.add(email);
    verify(mailServiceMock).sendVelocityEmail(recipients, "Recover Username",
        "test");
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    verify(appUserServiceMock).getUserByUsername(username);
    verify(userSecurityService).isUserAnonymous();
    verify(userSecurityService).getUsernameFromSecurity();
  }

  @Test
  public void testSendUsername_HappyPath_AnonymousUser() throws Exception {
    class UserControllerMock extends UserController {
      @Override
      String generateForgotUsernameEmailBodyWithVelocityEngine(String username,
          HttpServletRequest request) {
        return "test";
      }
    }

    EmailForm emailForm = new EmailForm();
    String email = "test@test.com";
    String username = "testUser";
    emailForm.setEmail(email);
    BindingResult result = new BeanPropertyBindingResult(emailForm, "email");
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    FormEmailValidator formEmailValidator = new FormEmailValidator();
    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    AppUserService appUserServiceMock = mock(AppUserService.class);
    when(appUserServiceMock.getUsernameByEmail(email)).thenReturn(username);
    when(appUserServiceMock.getUserByUsername(username)).thenReturn(appUser);
    MailService mailServiceMock = mock(MailService.class);
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(true);
    UserController controllerMock = new UserControllerMock();
    controllerMock.setAppUserService(appUserServiceMock);
    controllerMock.setFormEmailValidator(formEmailValidator);
    controllerMock.setMailService(mailServiceMock);
    controllerMock.setUserSecurityService(userSecurityService);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("redirect:/users/sentUsername",
        controllerMock.sendUsername(emailForm, result, requestMock, model));
    verify(appUserServiceMock).getUsernameByEmail(email);
    List<String> recipients = new ArrayList<String>();
    recipients.add(email);
    verify(mailServiceMock).sendVelocityEmail(recipients, "Recover Username",
        "test");
    assertFalse(model.containsKey("appUser"));
    verify(userSecurityService).isUserAnonymous();
  }

  @Test
  public void testSentUsername_NotAnonymousUser() {
    String username = "testUser";
    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    AppUserService userServiceMock = mock(AppUserService.class);
    when(userServiceMock.getUserByUsername(username)).thenReturn(appUser);
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(false);
    when(userSecurityService.getUsernameFromSecurity()).thenReturn(username);
    UserController classUnderTest = new UserController();
    classUnderTest.setAppUserService(userServiceMock);
    classUnderTest.setUserSecurityService(userSecurityService);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("users/sentUsername", classUnderTest.sentUsername(model));
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    verify(userServiceMock).getUserByUsername(username);
    verify(userSecurityService).isUserAnonymous();
    verify(userSecurityService).getUsernameFromSecurity();
  }

  @Test
  public void testSentUsername_AnonymousUser() {
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(true);
    UserController classUnderTest = new UserController();
    classUnderTest.setUserSecurityService(userSecurityService);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("users/sentUsername", classUnderTest.sentUsername(model));
    assertFalse(model.containsKey("appUser"));
    verify(userSecurityService).isUserAnonymous();
  }

  @Test
  public void testForgotUsername_NotAnonymousUser() {
    String username = "testUser";
    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    AppUserService userServiceMock = mock(AppUserService.class);
    when(userServiceMock.getUserByUsername(username)).thenReturn(appUser);
    Model model = new ExtendedModelMap();
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(false);
    when(userSecurityService.getUsernameFromSecurity()).thenReturn(username);
    UserController classUnderTest = new UserController();
    classUnderTest.setAppUserService(userServiceMock);
    classUnderTest.setUserSecurityService(userSecurityService);
    assertEquals("users/forgotUsername", classUnderTest.forgotUsername(model));
    assertTrue(model.containsAttribute("appUser"));
    Map<String, Object> modelMap = model.asMap();
    assertTrue(model.containsAttribute("emailForm"));
    assertEquals(username, ((AppUser) modelMap.get("appUser")).getUsername());
    verify(userServiceMock).getUserByUsername(username);
    verify(userSecurityService).isUserAnonymous();
    verify(userSecurityService).getUsernameFromSecurity();
  }

  @Test
  public void testForgotUsername_AnonymousUser() {
    Model model = new ExtendedModelMap();
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(true);
    UserController classUnderTest = new UserController();
    classUnderTest.setUserSecurityService(userSecurityService);
    assertEquals("users/forgotUsername", classUnderTest.forgotUsername(model));
    assertFalse(model.containsAttribute("appUser"));
    assertTrue(model.containsAttribute("emailForm"));
    verify(userSecurityService).isUserAnonymous();
  }

  @Test
  public void testForgotPassword_NotAnonymousUser() throws Exception {
    String username = "testUser";
    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    AppUserService userServiceMock = mock(AppUserService.class);
    when(userServiceMock.getUserByUsername(username)).thenReturn(appUser);
    Model model = new ExtendedModelMap();
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(false);
    when(userSecurityService.getUsernameFromSecurity()).thenReturn(username);
    UserController classUnderTest = new UserController();
    classUnderTest.setAppUserService(userServiceMock);
    classUnderTest.setUserSecurityService(userSecurityService);
    assertEquals("users/forgotPassword", classUnderTest.forgotPassword(model));
    assertTrue(model.containsAttribute("usernameForm"));
    assertTrue(model.containsAttribute("appUser"));
    Map<String, Object> modelMap = model.asMap();
    assertEquals(username, ((AppUser) modelMap.get("appUser")).getUsername());
    verify(userServiceMock).getUserByUsername(username);
    verify(userSecurityService).isUserAnonymous();
    verify(userSecurityService).getUsernameFromSecurity();
  }

  @Test
  public void testForgotPassword_AnonymousUser() throws Exception {
    Model model = new ExtendedModelMap();
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(true);
    UserController classUnderTest = new UserController();
    classUnderTest.setUserSecurityService(userSecurityService);
    assertEquals("users/forgotPassword", classUnderTest.forgotPassword(model));
    assertTrue(model.containsAttribute("usernameForm"));
    assertFalse(model.containsAttribute("appUser"));
    verify(userSecurityService).isUserAnonymous();
  }

  @Test
  public void testSendPasswordLink_invalidUsername_NotAnonymousUser()
      throws Exception {
    String username = "testUser";
    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    AppUserService userServiceMock = mock(AppUserService.class);
    when(userServiceMock.getUserByUsername(username)).thenReturn(appUser);
    UsernameForm usernameForm = new UsernameForm();
    BindingResult result = new BeanPropertyBindingResult(usernameForm,
        "username");
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(false);
    when(userSecurityService.getUsernameFromSecurity()).thenReturn(username);
    UserController classUnderTest = new UserController();
    classUnderTest.setAppUserService(userServiceMock);
    FormUsernameValidator formUsernameValidator = new FormUsernameValidator();
    classUnderTest.setFormUsernamelValidator(formUsernameValidator);
    classUnderTest.setUserSecurityService(userSecurityService);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("users/forgotPassword", classUnderTest.sendPasswordLink(
        usernameForm, result, requestMock, model));
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    verify(userServiceMock).getUserByUsername(username);
    verify(userSecurityService).isUserAnonymous();
    verify(userSecurityService).getUsernameFromSecurity();
  }

  @Test
  public void testSendPasswordLink_invalidUsername_AnonymousUser()
      throws Exception {
    UsernameForm usernameForm = new UsernameForm();
    BindingResult result = new BeanPropertyBindingResult(usernameForm,
        "username");
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(true);
    UserController classUnderTest = new UserController();
    FormUsernameValidator formUsernameValidator = new FormUsernameValidator();
    classUnderTest.setFormUsernamelValidator(formUsernameValidator);
    classUnderTest.setUserSecurityService(userSecurityService);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("users/forgotPassword", classUnderTest.sendPasswordLink(
        usernameForm, result, requestMock, model));
    assertFalse(model.containsKey("appUser"));
    verify(userSecurityService).isUserAnonymous();
  }

  @Test
  public void testSendPasswordLink_usernameDoesNotExist_NotAnonymousUser()
      throws Exception {
    String username = "testUser";
    UsernameForm usernameForm = new UsernameForm();
    usernameForm.setUsername("testUsername");
    BindingResult result = new BeanPropertyBindingResult(usernameForm,
        "username");
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(false);
    when(userSecurityService.getUsernameFromSecurity()).thenReturn(username);
    UserController classUnderTest = new UserController();
    classUnderTest.setUserSecurityService(userSecurityService);
    FormUsernameValidator formUsernameValidator = new FormUsernameValidator();
    classUnderTest.setFormUsernamelValidator(formUsernameValidator);
    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    AppUserService appUserServiceMock = mock(AppUserService.class);
    when(appUserServiceMock.getEmailByUsername(usernameForm.getUsername()))
        .thenReturn(null);
    when(appUserServiceMock.getUserByUsername(username)).thenReturn(appUser);
    classUnderTest.setAppUserService(appUserServiceMock);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("redirect:/users/forgotPassword/sentPassword",
        classUnderTest.sendPasswordLink(usernameForm, result, requestMock,
            model));
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    verify(appUserServiceMock).getUserByUsername(username);
    verify(appUserServiceMock).getEmailByUsername(usernameForm.getUsername());
    verify(userSecurityService).isUserAnonymous();
    verify(userSecurityService).getUsernameFromSecurity();
  }

  @Test
  public void testSendPasswordLink_usernameDoesNotExist_AnonymousUser()
      throws Exception {
    UsernameForm usernameForm = new UsernameForm();
    usernameForm.setUsername("testUsername");
    BindingResult result = new BeanPropertyBindingResult(usernameForm,
        "username");
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(true);
    UserController classUnderTest = new UserController();
    classUnderTest.setUserSecurityService(userSecurityService);
    FormUsernameValidator formUsernameValidator = new FormUsernameValidator();
    classUnderTest.setFormUsernamelValidator(formUsernameValidator);
    AppUserService appUserServiceMock = mock(AppUserService.class);
    when(appUserServiceMock.getEmailByUsername(usernameForm.getUsername()))
        .thenReturn(null);
    classUnderTest.setAppUserService(appUserServiceMock);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("redirect:/users/forgotPassword/sentPassword",
        classUnderTest.sendPasswordLink(usernameForm, result, requestMock,
            model));
    assertFalse(model.containsKey("appUser"));
    verify(appUserServiceMock).getEmailByUsername(usernameForm.getUsername());
    verify(userSecurityService).isUserAnonymous();
  }

  @Test
  public void testSendPasswordLink_HappyPath_NotAnonymousUser()
      throws Exception {
    final String emailBody = "test";
    final String username = "testUser";

    class UserControllerMock extends UserController {
      @Override
      String generateForgotPasswordEmailBodyWithVelocityEngine(String username,
          HttpServletRequest request) {
        return emailBody;
      }
    }

    UsernameForm usernameForm = new UsernameForm();
    usernameForm.setUsername("testUsername");
    BindingResult result = new BeanPropertyBindingResult(usernameForm,
        "username");
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(false);
    when(userSecurityService.getUsernameFromSecurity()).thenReturn(username);
    UserController controllerMock = new UserControllerMock();
    controllerMock.setUserSecurityService(userSecurityService);
    FormUsernameValidator formUsernameValidator = new FormUsernameValidator();
    controllerMock.setFormUsernamelValidator(formUsernameValidator);
    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    AppUserService appUserServiceMock = mock(AppUserService.class);
    String userEmail = "test@test.com";
    when(appUserServiceMock.getEmailByUsername(usernameForm.getUsername()))
        .thenReturn(userEmail);
    when(appUserServiceMock.getUserByUsername(username)).thenReturn(appUser);
    controllerMock.setAppUserService(appUserServiceMock);
    MailService mailServiceMock = mock(MailService.class);
    controllerMock.setMailService(mailServiceMock);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("redirect:/users/forgotPassword/sentPassword",
        controllerMock.sendPasswordLink(usernameForm, result, requestMock,
            model));
    verify(appUserServiceMock).getEmailByUsername(usernameForm.getUsername());
    verify(appUserServiceMock).addResetPassword(usernameForm.getUsername());
    List<String> recipients = new ArrayList<String>(1);
    recipients.add(userEmail);
    verify(mailServiceMock).sendVelocityEmail(recipients, "Recover Password",
        emailBody);
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    verify(appUserServiceMock).getUserByUsername(username);
    verify(userSecurityService).isUserAnonymous();
    verify(userSecurityService).getUsernameFromSecurity();
  }

  @Test
  public void testSendPasswordLink_HappyPath_AnonymousUser() throws Exception {
    final String emailBody = "test";

    class UserControllerMock extends UserController {
      @Override
      String generateForgotPasswordEmailBodyWithVelocityEngine(String username,
          HttpServletRequest request) {
        return emailBody;
      }
    }

    UsernameForm usernameForm = new UsernameForm();
    usernameForm.setUsername("testUsername");
    BindingResult result = new BeanPropertyBindingResult(usernameForm,
        "username");
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(true);
    UserController controllerMock = new UserControllerMock();
    controllerMock.setUserSecurityService(userSecurityService);
    FormUsernameValidator formUsernameValidator = new FormUsernameValidator();
    controllerMock.setFormUsernamelValidator(formUsernameValidator);
    AppUserService appUserServiceMock = mock(AppUserService.class);
    String userEmail = "test@test.com";
    when(appUserServiceMock.getEmailByUsername(usernameForm.getUsername()))
        .thenReturn(userEmail);
    controllerMock.setAppUserService(appUserServiceMock);
    MailService mailServiceMock = mock(MailService.class);
    controllerMock.setMailService(mailServiceMock);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("redirect:/users/forgotPassword/sentPassword",
        controllerMock.sendPasswordLink(usernameForm, result, requestMock,
            model));
    verify(appUserServiceMock).getEmailByUsername(usernameForm.getUsername());
    verify(appUserServiceMock).addResetPassword(usernameForm.getUsername());
    List<String> recipients = new ArrayList<String>(1);
    recipients.add(userEmail);
    verify(mailServiceMock).sendVelocityEmail(recipients, "Recover Password",
        emailBody);
    assertFalse(model.containsKey("appUser"));
    verify(userSecurityService).isUserAnonymous();
  }

  @Test
  public void testSentPassword_NotAnonymousUser() throws Exception {
    String username = "testUser";
    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    AppUserService userServiceMock = mock(AppUserService.class);
    when(userServiceMock.getUserByUsername(username)).thenReturn(appUser);
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(false);
    when(userSecurityService.getUsernameFromSecurity()).thenReturn(username);
    UserController classUnderTest = new UserController();
    classUnderTest.setUserSecurityService(userSecurityService);
    classUnderTest.setAppUserService(userServiceMock);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("users/sentPassword", classUnderTest.sentPassword(model));
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    verify(userServiceMock).getUserByUsername(username);
    verify(userSecurityService).isUserAnonymous();
    verify(userSecurityService).getUsernameFromSecurity();
  }

  @Test
  public void testSentPassword_AnonymousUser() throws Exception {
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(true);
    UserController classUnderTest = new UserController();
    classUnderTest.setUserSecurityService(userSecurityService);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("users/sentPassword", classUnderTest.sentPassword(model));
    assertFalse(model.containsKey("appUser"));
    verify(userSecurityService).isUserAnonymous();
  }

  @Test
  public void testRestPassword_UniqueUrlParamExists_NotAnonymousUser()
      throws Exception {
    String username = "testUser";
    UserSecurityQuestion userSecurityQuestion = new UserSecurityQuestion();
    userSecurityQuestion.setUserid(1);
    userSecurityQuestion.setSecurityQuestion("test question");
    String uniqueUrlParam = "testUniqueUrlParam";
    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    AppUserService appUserServiceMock = mock(AppUserService.class);
    when(
        appUserServiceMock
            .getUserSecurityQuestionByResetPasswordUniqueUrlParam(uniqueUrlParam))
        .thenReturn(userSecurityQuestion);
    when(appUserServiceMock.getUserByUsername(username)).thenReturn(appUser);
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(false);
    when(userSecurityService.getUsernameFromSecurity()).thenReturn(username);
    UserController classUnderTest = new UserController();
    classUnderTest.setUserSecurityService(userSecurityService);
    classUnderTest.setAppUserService(appUserServiceMock);
    Map<String, Object> model = new HashMap<String, Object>();
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    String contextPath = "/test";
    when(requestMock.getContextPath()).thenReturn(contextPath);
    assertEquals("users/resetPassword",
        classUnderTest.resetPassword(uniqueUrlParam, model, requestMock));
    assertTrue((Boolean) model.get("exists"));
    assertEquals(contextPath, model.get("contextPath"));
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    verify(appUserServiceMock).getUserByUsername(username);
    verify(appUserServiceMock)
        .getUserSecurityQuestionByResetPasswordUniqueUrlParam(uniqueUrlParam);
    verify(requestMock).getContextPath();
    verify(userSecurityService).isUserAnonymous();
    verify(userSecurityService).getUsernameFromSecurity();
  }

  @Test
  public void testRestPassword_UniqueUrlParamExists_AnonymousUser()
      throws Exception {
    UserSecurityQuestion userSecurityQuestion = new UserSecurityQuestion();
    userSecurityQuestion.setUserid(1);
    userSecurityQuestion.setSecurityQuestion("test question");
    String uniqueUrlParam = "testUniqueUrlParam";
    AppUserService appUserServiceMock = mock(AppUserService.class);
    when(
        appUserServiceMock
            .getUserSecurityQuestionByResetPasswordUniqueUrlParam(uniqueUrlParam))
        .thenReturn(userSecurityQuestion);
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(true);
    UserController classUnderTest = new UserController();
    classUnderTest.setUserSecurityService(userSecurityService);
    classUnderTest.setAppUserService(appUserServiceMock);
    Map<String, Object> model = new HashMap<String, Object>();
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    String contextPath = "/test";
    when(requestMock.getContextPath()).thenReturn(contextPath);
    assertEquals("users/resetPassword",
        classUnderTest.resetPassword(uniqueUrlParam, model, requestMock));
    assertTrue((Boolean) model.get("exists"));
    assertEquals(contextPath, model.get("contextPath"));
    assertFalse(model.containsKey("appUser"));
    verify(appUserServiceMock)
        .getUserSecurityQuestionByResetPasswordUniqueUrlParam(uniqueUrlParam);
    verify(requestMock).getContextPath();
    verify(userSecurityService).isUserAnonymous();
  }

  @Test
  public void testRestPassword_UniqueUrlParamNotExists_NotAnonymousUser()
      throws Exception {
    String username = "testUser";
    String uniqueUrlParam = "test";
    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    AppUserService appUserServiceMock = mock(AppUserService.class);
    when(
        appUserServiceMock
            .getUserSecurityQuestionByResetPasswordUniqueUrlParam(uniqueUrlParam))
        .thenReturn(null);
    when(appUserServiceMock.getUserByUsername(username)).thenReturn(appUser);
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(false);
    when(userSecurityService.getUsernameFromSecurity()).thenReturn(username);
    UserController classUnderTest = new UserController();
    classUnderTest.setUserSecurityService(userSecurityService);
    classUnderTest.setAppUserService(appUserServiceMock);
    Map<String, Object> model = new HashMap<String, Object>();
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    assertEquals("users/resetPassword",
        classUnderTest.resetPassword(uniqueUrlParam, model, requestMock));
    assertFalse((Boolean) model.get("exists"));
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    verify(appUserServiceMock).getUserByUsername(username);
    verify(appUserServiceMock)
        .getUserSecurityQuestionByResetPasswordUniqueUrlParam(uniqueUrlParam);
    verify(userSecurityService).isUserAnonymous();
    verify(userSecurityService).getUsernameFromSecurity();
  }

  @Test
  public void testRestPassword_UniqueUrlParamNotExists_AnonymousUser()
      throws Exception {
    String uniqueUrlParam = "test";
    AppUserService appUserServiceMock = mock(AppUserService.class);
    when(
        appUserServiceMock
            .getUserSecurityQuestionByResetPasswordUniqueUrlParam(uniqueUrlParam))
        .thenReturn(null);
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(true);
    UserController classUnderTest = new UserController();
    classUnderTest.setUserSecurityService(userSecurityService);
    classUnderTest.setAppUserService(appUserServiceMock);
    Map<String, Object> model = new HashMap<String, Object>();
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    assertEquals("users/resetPassword",
        classUnderTest.resetPassword(uniqueUrlParam, model, requestMock));
    assertFalse((Boolean) model.get("exists"));
    assertFalse(model.containsKey("appUser"));
    verify(appUserServiceMock)
        .getUserSecurityQuestionByResetPasswordUniqueUrlParam(uniqueUrlParam);
    verify(userSecurityService).isUserAnonymous();
  }

  @Test
  public void testUpdatePassword_InvalidPasswordSecurity_NotAnonymousUser() throws Exception {
    String username = "testUser";
    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    AppUserService userServiceMock = mock(AppUserService.class);
    when(userServiceMock.getUserByUsername(username)).thenReturn(appUser);
    PasswordSecurityForm passwordSecurityForm = new PasswordSecurityForm();
    passwordSecurityForm.setUserid(1);
    passwordSecurityForm.setSecurityAnswer(StringUtils.EMPTY);
    passwordSecurityForm.setPassword(null);
    passwordSecurityForm.setConfirmPassword(null);
    FormPasswordSecurityValidator formPasswordSecurityValidator = new FormPasswordSecurityValidator();
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(false);
    when(userSecurityService.getUsernameFromSecurity()).thenReturn(username);
    UserController classUnderTest = new UserController();
    classUnderTest.setUserSecurityService(userSecurityService);
    classUnderTest.setAppUserService(userServiceMock);
    classUnderTest.setFormPasswordSecuritylValidator(formPasswordSecurityValidator);
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    String contextPath = "/test";
    when(requestMock.getContextPath()).thenReturn(contextPath);
    BindingResult result = new BeanPropertyBindingResult(passwordSecurityForm,
        "paswordSecurityForm");
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("users/resetPassword", classUnderTest.updatePassword(
        passwordSecurityForm, result, requestMock, model));
    assertTrue((Boolean) model.get("exists"));
    assertEquals(contextPath, model.get("contextPath"));
    assertEquals(passwordSecurityForm, model.get("passwordSecurityForm"));
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    verify(userServiceMock).getUserByUsername(username);
    verify(userSecurityService).isUserAnonymous();
    verify(userSecurityService).getUsernameFromSecurity();
  }
  
  @Test
  public void testUpdatePassword_InvalidPasswordSecurity_AnonymousUser() throws Exception {
    PasswordSecurityForm passwordSecurityForm = new PasswordSecurityForm();
    passwordSecurityForm.setUserid(1);
    passwordSecurityForm.setSecurityAnswer(StringUtils.EMPTY);
    passwordSecurityForm.setPassword(null);
    passwordSecurityForm.setConfirmPassword(null);
    FormPasswordSecurityValidator formPasswordSecurityValidator = new FormPasswordSecurityValidator();
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(true);
    UserController classUnderTest = new UserController();
    classUnderTest.setUserSecurityService(userSecurityService);
    classUnderTest.setFormPasswordSecuritylValidator(formPasswordSecurityValidator);
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    String contextPath = "/test";
    when(requestMock.getContextPath()).thenReturn(contextPath);
    BindingResult result = new BeanPropertyBindingResult(passwordSecurityForm,
        "paswordSecurityForm");
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("users/resetPassword", classUnderTest.updatePassword(
        passwordSecurityForm, result, requestMock, model));
    assertTrue((Boolean) model.get("exists"));
    assertEquals(contextPath, model.get("contextPath"));
    assertEquals(passwordSecurityForm, model.get("passwordSecurityForm"));
    assertFalse(model.containsKey("appUser"));
    verify(userSecurityService).isUserAnonymous();
  }

  @Test
  public void testUpdatePassword_HappyPath_NotAnonymousUser() throws Exception {
    String username = "testUser";
    PasswordSecurityForm passwordSecurityForm = new PasswordSecurityForm();
    passwordSecurityForm.setUserid(1);
    passwordSecurityForm.setSecurityAnswer("testAnswer");
    String password = "testPassword";
    passwordSecurityForm.setPassword(password);
    passwordSecurityForm.setConfirmPassword(password);
    FormPasswordSecurityValidator formPasswordSecurityValidatorMock = mock(FormPasswordSecurityValidator.class);
    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    AppUserService appUserServiceMock = mock(AppUserService.class);
    when(
        appUserServiceMock.getUsernameByUserid(passwordSecurityForm.getUserid()))
        .thenReturn(username);
    when(appUserServiceMock.getUserByUsername(username)).thenReturn(appUser);
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(false);
    when(userSecurityService.getUsernameFromSecurity()).thenReturn(username);
    UserController classUnderTest = new UserController();
    classUnderTest.setUserSecurityService(userSecurityService);
    classUnderTest.setAppUserService(appUserServiceMock);
    classUnderTest.setFormPasswordSecuritylValidator(formPasswordSecurityValidatorMock);
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    BindingResult result = new BeanPropertyBindingResult(passwordSecurityForm,
        "paswordSecurityForm");
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("redirect:/users/forgotPassword/passwordUpdated", classUnderTest.updatePassword(
        passwordSecurityForm, result, requestMock, model));
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    verify(appUserServiceMock).getUserByUsername(username);
    verify(appUserServiceMock).getUsernameByUserid(
        passwordSecurityForm.getUserid());
    verify(appUserServiceMock).updatePasswordByUserid(
        passwordSecurityForm.getUserid(),
        Encoder.encodeString(username, passwordSecurityForm.getPassword()));
    verify(userSecurityService).isUserAnonymous();
    verify(userSecurityService).getUsernameFromSecurity();
  }
  
  @Test
  public void testUpdatePassword_HappyPath_AnonymousUser() throws Exception {
    PasswordSecurityForm passwordSecurityForm = new PasswordSecurityForm();
    passwordSecurityForm.setUserid(1);
    passwordSecurityForm.setSecurityAnswer("testAnswer");
    String password = "testPassword";
    passwordSecurityForm.setPassword(password);
    passwordSecurityForm.setConfirmPassword(password);
    FormPasswordSecurityValidator formPasswordSecurityValidatorMock = mock(FormPasswordSecurityValidator.class);
    AppUserService appUserServiceMock = mock(AppUserService.class);
    String username = "testUser";
    when(
        appUserServiceMock.getUsernameByUserid(passwordSecurityForm.getUserid()))
        .thenReturn(username);
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(true);
    UserController classUnderTest = new UserController();
    classUnderTest.setUserSecurityService(userSecurityService);
    classUnderTest.setAppUserService(appUserServiceMock);
    classUnderTest.setFormPasswordSecuritylValidator(formPasswordSecurityValidatorMock);
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    BindingResult result = new BeanPropertyBindingResult(passwordSecurityForm,
        "paswordSecurityForm");
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("redirect:/users/forgotPassword/passwordUpdated", classUnderTest.updatePassword(
        passwordSecurityForm, result, requestMock, model));
    assertFalse(model.containsKey("appUser"));
    verify(appUserServiceMock).getUsernameByUserid(
        passwordSecurityForm.getUserid());
    verify(appUserServiceMock).updatePasswordByUserid(
        passwordSecurityForm.getUserid(),
        Encoder.encodeString(username, passwordSecurityForm.getPassword()));
    verify(userSecurityService).isUserAnonymous();
  }

  @Test
  public void testPasswordUpdated_NotAnonymousUser() throws Exception {
    String username = "testUser";
    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    AppUserService userServiceMock = mock(AppUserService.class);
    when(userServiceMock.getUserByUsername(username)).thenReturn(appUser);
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(false);
    when(userSecurityService.getUsernameFromSecurity()).thenReturn(username);
    UserController classUnderTest = new UserController();
    classUnderTest.setUserSecurityService(userSecurityService);
    classUnderTest.setAppUserService(userServiceMock);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("users/passwordUpdated", classUnderTest.passwordUpdated(model));
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    verify(userServiceMock).getUserByUsername(username);
    verify(userSecurityService).isUserAnonymous();
    verify(userSecurityService).getUsernameFromSecurity();
  }

  @Test
  public void testPasswordUpdated_AnonymousUser() throws Exception {
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(true);
    UserController classUnderTest = new UserController();
    classUnderTest.setUserSecurityService(userSecurityService);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("users/passwordUpdated", classUnderTest.passwordUpdated(model));
    assertFalse(model.containsKey("appUser"));
    verify(userSecurityService).isUserAnonymous();
  }

  @Test
  public void testTermsAndConditions() throws Exception {
    UserController controller = new UserController();
    assertEquals("users/termsAndConditions", controller.termsAndConditions());
  }
  
  @Test
  public void testManageAccount_NotAnonymousUser() throws Exception {
    String username = "testUser";
    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    AppUserService userServiceMock = mock(AppUserService.class);
    when(userServiceMock.getUserByUsername(username)).thenReturn(appUser);
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(false);
    when(userSecurityService.getUsernameFromSecurity()).thenReturn(username);
    UserController classUnderTest = new UserController();
    classUnderTest.setUserSecurityService(userSecurityService);
    classUnderTest.setAppUserService(userServiceMock);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("users/manageAccount", classUnderTest.manageAccount(model));
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    verify(userServiceMock).getUserByUsername(username);
    verify(userSecurityService).isUserAnonymous();
    verify(userSecurityService).getUsernameFromSecurity();
  }
  
  @Test
  public void testManageAccount_anonymousUser() throws Exception {
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(true);
    UserController classUnderTest = new UserController();
    classUnderTest.setUserSecurityService(userSecurityService);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("users/manageAccount", classUnderTest.manageAccount(model));
    assertFalse(model.containsKey("appUser"));
    verify(userSecurityService).isUserAnonymous();
  }
  
  @Test
  public void testManageAccountEdit_NotAnonymousUser() throws Exception {
    String username = "testUser";
    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    AppUserService userServiceMock = mock(AppUserService.class);
    when(userServiceMock.getUserByUsername(username)).thenReturn(appUser);
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(false);
    when(userSecurityService.getUsernameFromSecurity()).thenReturn(username);
    UserController classUnderTest = new UserController();
    classUnderTest.setUserSecurityService(userSecurityService);
    classUnderTest.setAppUserService(userServiceMock);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("users/manageAccountEdit", classUnderTest.manageAccountEdit(model));
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    verify(userServiceMock).getUserByUsername(username);
    verify(userSecurityService).isUserAnonymous();
    verify(userSecurityService).getUsernameFromSecurity();
  }
  
  @Test
  public void testManageAccountEdit_anonymousUser() throws Exception {
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(true);
    UserController classUnderTest = new UserController();
    classUnderTest.setUserSecurityService(userSecurityService);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("users/manageAccountEdit", classUnderTest.manageAccountEdit(model));
    assertFalse(model.containsKey("appUser"));
    verify(userSecurityService).isUserAnonymous();
  }
  
  @Test
  public void testManageAccountEditSubmit_invalidSubmission() throws Exception {
    String username = "testUser";
    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    AppUserService userServiceMock = mock(AppUserService.class);
    when(userServiceMock.getUserByUsername(username)).thenReturn(appUser);
    ManageAccountForm manageAccountForm = new ManageAccountForm();
    manageAccountForm.setSecurityAnswer("12345678901234567890123456789012345678901");
    FormManageAccountEditValidator formManageAccountEditValidator = new FormManageAccountEditValidator();
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.getUsernameFromSecurity()).thenReturn(username);
    UserController classUnderTest = new UserController();
    classUnderTest.setUserSecurityService(userSecurityService);
    classUnderTest.setAppUserService(userServiceMock);
    classUnderTest.setFormManageAccountEditValidator(formManageAccountEditValidator);
    BindingResult result = new BeanPropertyBindingResult(manageAccountForm,
        "manageAccountForm");
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("users/manageAccountEdit", classUnderTest.manageAccountEditSubmit(
        manageAccountForm, result, model, null));
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    verify(userServiceMock).getUserByUsername(username);
    verify(userSecurityService).getUsernameFromSecurity();
  }
  
  @Test
  public void testManageAccountEditSubmit_validSubmission_everythingEmpty() throws Exception {
    String username = "testUser";
    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    AppUserService userServiceMock = mock(AppUserService.class);
    when(userServiceMock.getUserByUsername(username)).thenReturn(appUser);
    ManageAccountForm manageAccountForm = new ManageAccountForm();
    FormManageAccountEditValidator formManageAccountEditValidator = new FormManageAccountEditValidator();
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.getUsernameFromSecurity()).thenReturn(username);
    UserController classUnderTest = new UserController();
    classUnderTest.setUserSecurityService(userSecurityService);
    classUnderTest.setAppUserService(userServiceMock);
    classUnderTest.setFormManageAccountEditValidator(formManageAccountEditValidator);
    BindingResult result = new BeanPropertyBindingResult(manageAccountForm,
        "manageAccountForm");
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("redirect:/users/manageAccount", classUnderTest.manageAccountEditSubmit(
        manageAccountForm, result, model, null));
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    verify(userServiceMock).getUserByUsername(username);
    verify(userSecurityService).getUsernameFromSecurity();
  }
  
  @Test
  public void testManageAccountEditSubmit_validSubmission_everythingNotEmpty() throws Exception {
    final String username = "testUser";

    class UserControllerMock extends UserController {
      @Override
      String generateEmailAddressChangeEmailBodyWithVelocityEngine(String oldEmail,
          String newEmail, HttpServletRequest request) {
        return "test";
      }
    }
    
    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    String oldEmail = "testOld@test.com";
    appUser.setEmail(oldEmail);
    AppUserService userServiceMock = mock(AppUserService.class);
    when(userServiceMock.getUserByUsername(username)).thenReturn(appUser);
    ManageAccountForm manageAccountForm = new ManageAccountForm();
    String newEmail = "testNew@test.com";
    manageAccountForm.setEmail(newEmail);
    manageAccountForm.setSecurityQuestion("testSecurityQuestion");
    manageAccountForm.setSecurityAnswer("testSecurityAnswer");
    manageAccountForm.setPassword("testPassword");
    BindingResult result = new BeanPropertyBindingResult(manageAccountForm,
        "manageAccountForm");
    FormManageAccountEditValidator formManageAccountEditValidatorMock = mock(FormManageAccountEditValidator.class);
    MailService mailServiceMock = mock(MailService.class);
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.getUsernameFromSecurity()).thenReturn(username);
    UserControllerMock userControllerMock = new UserControllerMock();
    userControllerMock.setUserSecurityService(userSecurityService);
    userControllerMock.setAppUserService(userServiceMock);
    userControllerMock.setFormManageAccountEditValidator(formManageAccountEditValidatorMock);
    userControllerMock.setMailService(mailServiceMock);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("redirect:/users/manageAccount", userControllerMock.manageAccountEditSubmit(
        manageAccountForm, result, model, null));
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    verify(userServiceMock).getUserByUsername(username);
    List<String> recipients = new ArrayList<String>();
    recipients.add(appUser.getEmail());
    recipients.add(newEmail);
    verify(mailServiceMock).sendVelocityEmail(recipients, "GraffitiTracker Email Address Change Notification",
        "test");
    verify(userSecurityService).getUsernameFromSecurity();
  }
  
  @Test
  public void testManageUsers_anonymousUser() throws Exception {
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(true);
    UserController classUnderTest = new UserController();
    classUnderTest.setUserSecurityService(userSecurityService);
    Map<String, Object> model = new HashMap<String, Object>(0);
    assertEquals("users/manageUsers", classUnderTest.manageUsers(model));
    assertTrue(model.isEmpty());
    verify(userSecurityService).isUserAnonymous();
  }
  
  @Test
  public void testManageUsers_nonAnonymousUser() throws Exception {
    String username = "testUser";
    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    AppUserService userServiceMock = mock(AppUserService.class);
    when(userServiceMock.getUserByUsername(username)).thenReturn(appUser);
    UserSecurityService userSecurityService = mock(UserSecurityService.class);
    when(userSecurityService.isUserAnonymous()).thenReturn(false);
    when(userSecurityService.getUsernameFromSecurity()).thenReturn(username);
    UserController classUnderTest = new UserController();
    classUnderTest.setUserSecurityService(userSecurityService);
    Map<String, Object> model = new HashMap<String, Object>(0);
    classUnderTest.setAppUserService(userServiceMock);
    assertEquals("users/manageUsers", classUnderTest.manageUsers(model));
    assertEquals(appUser, model.get("appUser"));
    verify(userServiceMock).getUserByUsername(username);
    verify(userSecurityService).isUserAnonymous();
    verify(userSecurityService).getUsernameFromSecurity();
  }
  
  @Test
  public void testGetUser() throws Exception {
    int id = 5;
    AppUser appUser = new AppUser();
    appUser.setUserId(id);
    AppUserService userServiceMock = mock(AppUserService.class);
    when(userServiceMock.getUserById(id)).thenReturn(appUser);
    UserController classUnderTest = new UserController();
    Map<String, Object> model = new HashMap<String, Object>(0);
    classUnderTest.setAppUserService(userServiceMock);
    assertEquals("users/user", classUnderTest.getUser(id, model));
    assertEquals(appUser, model.get("appUser"));
    verify(userServiceMock).getUserById(id);
  }
  
  @Test
  public void testGetSharedInets() throws Exception {
    String inet = "127.0.0.1";
    LoginAddressService loginAddressServiceMock = mock(LoginAddressService.class);
    SortedMap<String, Integer> idsToUsernames = new TreeMap<String, Integer>();
    idsToUsernames.put("test1", 5);
    idsToUsernames.put("test2", 6);
    when(loginAddressServiceMock.getUsersSharingInet(inet)).thenReturn(idsToUsernames);
    UserController classUnderTest = new UserController();
    Map<String, Object> model = new HashMap<String, Object>(0);
    classUnderTest.setLoginAddressService(loginAddressServiceMock);
    assertEquals("users/usersSharingInets", classUnderTest.getSharedInets(inet, model));
    assertEquals(idsToUsernames, model.get("usersSharingInets"));
    assertEquals(StringUtils.join(idsToUsernames.keySet(), " ").length(), model.get("displayUsernamesStringLength"));
    verify(loginAddressServiceMock).getUsersSharingInet(inet);
  }
}
