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
import net.ccaper.GraffitiTracker.service.BannedWordService;
import net.ccaper.GraffitiTracker.service.CaptchaService;
import net.ccaper.GraffitiTracker.service.MailService;
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
    final String username = "testUser";

    class UserControllerMock extends UserController {
      @Override
      boolean isUserAnonymous() {
        return false;
      }

      @Override
      String getUsernameFromSecurity() {
        return username;
      }
    }

    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    AppUserService userServiceMock = mock(AppUserService.class);
    when(userServiceMock.getUser(username)).thenReturn(appUser);
    String inet = "127.0.0.1";
    TextCaptcha captcha = new TextCaptcha("Some question", "answer");
    CaptchaService captchaServiceMock = mock(CaptchaService.class);
    when(captchaServiceMock.getTextCaptcha()).thenReturn(captcha);
    BannedInetsService banndedInetsServiceMock = mock(BannedInetsService.class);
    when(banndedInetsServiceMock.isInetBanned(inet)).thenReturn(false);
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    when(requestMock.getRemoteAddr()).thenReturn(inet);
    UserController controllerMock = new UserControllerMock();
    controllerMock.setAppUserService(userServiceMock);
    controllerMock.setBannedInetsService(banndedInetsServiceMock);
    controllerMock.setCaptchaService(captchaServiceMock);
    Model model = new ExtendedModelMap();
    HttpSession session = new MockHttpSession();
    assertEquals("users/create",
        controllerMock.createUserProfile(model, session, requestMock));
    UserForm userForm = (UserForm) model.asMap().get("userForm");
    assertNull(userForm.getUsername());
    assertEquals(captcha.getQuestion(), userForm.getTextCaptchaQuestion());
    assertEquals(captcha, session.getAttribute("textCaptcha"));
    assertTrue(model.containsAttribute("appUser"));
    Map<String, Object> modelMap = model.asMap();
    assertEquals(username, ((AppUser) modelMap.get("appUser")).getUsername());
    verify(userServiceMock).getUser(username);
    verify(captchaServiceMock).getTextCaptcha();
    verify(banndedInetsServiceMock).isInetBanned(inet);
    verify(requestMock).getRemoteAddr();
    verify(userServiceMock).getUser(username);
  }

  @Test
  public void testCreateUserProfile_HappyPath_AnonymousUser() {
    class UserControllerMock extends UserController {
      @Override
      boolean isUserAnonymous() {
        return true;
      }
    }

    String inet = "127.0.0.1";
    TextCaptcha captcha = new TextCaptcha("Some question", "answer");
    CaptchaService captchaServiceMock = mock(CaptchaService.class);
    when(captchaServiceMock.getTextCaptcha()).thenReturn(captcha);
    BannedInetsService banndedInetsServiceMock = mock(BannedInetsService.class);
    when(banndedInetsServiceMock.isInetBanned(inet)).thenReturn(false);
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    when(requestMock.getRemoteAddr()).thenReturn(inet);
    UserController controllerMock = new UserControllerMock();
    controllerMock.setBannedInetsService(banndedInetsServiceMock);
    controllerMock.setCaptchaService(captchaServiceMock);
    Model model = new ExtendedModelMap();
    HttpSession session = new MockHttpSession();
    assertEquals("users/create",
        controllerMock.createUserProfile(model, session, requestMock));
    UserForm userForm = (UserForm) model.asMap().get("userForm");
    assertNull(userForm.getUsername());
    assertEquals(captcha.getQuestion(), userForm.getTextCaptchaQuestion());
    assertEquals(captcha, session.getAttribute("textCaptcha"));
    assertFalse(model.containsAttribute("appUser"));
    verify(captchaServiceMock).getTextCaptcha();
    verify(banndedInetsServiceMock).isInetBanned(inet);
    verify(requestMock).getRemoteAddr();
  }

  @Test
  public void testCreateUserProfile_InetBanned_NotAnonymousUser() {
    final String username = "testUser";

    class UserControllerMock extends UserController {
      @Override
      boolean isUserAnonymous() {
        return false;
      }

      @Override
      String getUsernameFromSecurity() {
        return username;
      }
    }

    String inet = "127.0.0.1";
    BannedInetsService banndedInetsServiceMock = mock(BannedInetsService.class);
    when(banndedInetsServiceMock.isInetBanned(inet)).thenReturn(true);
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    when(requestMock.getRemoteAddr()).thenReturn(inet);
    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    AppUserService userServiceMock = mock(AppUserService.class);
    when(userServiceMock.getUser(username)).thenReturn(appUser);
    UserController controllerMock = new UserControllerMock();
    controllerMock.setBannedInetsService(banndedInetsServiceMock);
    controllerMock.setAppUserService(userServiceMock);
    Model model = new ExtendedModelMap();
    HttpSession session = new MockHttpSession();
    assertEquals("users/banned",
        controllerMock.createUserProfile(model, session, requestMock));
    assertTrue(model.containsAttribute("appUser"));
    Map<String, Object> modelMap = model.asMap();
    assertEquals(username, ((AppUser) modelMap.get("appUser")).getUsername());
    verify(banndedInetsServiceMock).isInetBanned(inet);
    verify(requestMock).getRemoteAddr();
    verify(userServiceMock).getUser(username);
  }

  @Test
  public void testCreateUserProfile_InetBanned_AnonymousUser() {
    class UserControllerMock extends UserController {
      @Override
      boolean isUserAnonymous() {
        return true;
      }
    }

    String inet = "127.0.0.1";
    BannedInetsService banndedInetsServiceMock = mock(BannedInetsService.class);
    when(banndedInetsServiceMock.isInetBanned(inet)).thenReturn(true);
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    when(requestMock.getRemoteAddr()).thenReturn(inet);
    UserController controllerMock = new UserControllerMock();
    controllerMock.setBannedInetsService(banndedInetsServiceMock);
    Model model = new ExtendedModelMap();
    HttpSession session = new MockHttpSession();
    assertEquals("users/banned",
        controllerMock.createUserProfile(model, session, requestMock));
    assertFalse(model.containsAttribute("appUser"));
    verify(banndedInetsServiceMock).isInetBanned(inet);
    verify(requestMock).getRemoteAddr();
  }

  @Test
  public void testAddAppUserFromForm_HappyPath_NotAnonymousUser() throws Exception {
    final String username = "testUser";
    
    class UserControllerMock extends UserController {
      @Override
      String generateConfirmationEmailBodyWithVelocityEngine(UserForm userForm,
          HttpServletRequest request) {
        return "test";
      }
      
      @Override
      boolean isUserAnonymous() {
        return false;
      }

      @Override
      String getUsernameFromSecurity() {
        return username;
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
    userForm.setSecurityQuestion("testQuestion");
    userForm.setSecurityAnswer("testAnswer");
    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    AppUserService appUserServiceMock = mock(AppUserService.class);
    when(appUserServiceMock.doesEmailExist(userForm.getEmail())).thenReturn(
        false);
    when(appUserServiceMock.doesUsernameExist(userForm.getUsername()))
        .thenReturn(false);
    when(appUserServiceMock.getUser(username)).thenReturn(appUser);
    BannedWordService bannedWordServiceMock = mock(BannedWordService.class);
    when(
        bannedWordServiceMock.doesStringContainBannedWord(userForm
            .getUsername())).thenReturn(false);
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    MailService mailServiceMock = mock(MailService.class);
    CaptchaService captchaService = new TextCaptchaServiceImpl();
    FormUserValidator formUserValidator = new FormUserValidator();
    formUserValidator.setAppUserService(appUserServiceMock);
    formUserValidator.setBannedWordService(bannedWordServiceMock);
    HttpSession session = new MockHttpSession();
    session.setAttribute("textCaptcha", captcha);
    UserControllerMock controller = new UserControllerMock();
    controller.setAppUserService(appUserServiceMock);
    controller.setFormUserValidator(formUserValidator);
    controller.setCaptchaService(captchaService);
    controller.setMailService(mailServiceMock);
    BindingResult result = new BeanPropertyBindingResult(userForm, "userForm");
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("redirect:/users/registered", controller.addAppUserFromForm(
        requestMock, session, userForm, result, model));
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    verify(appUserServiceMock).getUser(username);
    verify(appUserServiceMock).doesEmailExist(userForm.getEmail());
    verify(appUserServiceMock).doesUsernameExist(userForm.getUsername());
    verify(bannedWordServiceMock).doesStringContainBannedWord(
        userForm.getUsername());
  }
  
  @Test
  public void testAddAppUserFromForm_HappyPath_AnonymousUser() throws Exception {
    class UserControllerMock extends UserController {
      @Override
      String generateConfirmationEmailBodyWithVelocityEngine(UserForm userForm,
          HttpServletRequest request) {
        return "test";
      }
      
      @Override
      boolean isUserAnonymous() {
        return true;
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
    when(appUserServiceMock.doesEmailExist(userForm.getEmail())).thenReturn(
        false);
    when(appUserServiceMock.doesUsernameExist(userForm.getUsername()))
        .thenReturn(false);
    BannedWordService bannedWordServiceMock = mock(BannedWordService.class);
    when(
        bannedWordServiceMock.doesStringContainBannedWord(userForm
            .getUsername())).thenReturn(false);
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    MailService mailServiceMock = mock(MailService.class);
    CaptchaService captchaService = new TextCaptchaServiceImpl();
    FormUserValidator formUserValidator = new FormUserValidator();
    formUserValidator.setAppUserService(appUserServiceMock);
    formUserValidator.setBannedWordService(bannedWordServiceMock);
    HttpSession session = new MockHttpSession();
    session.setAttribute("textCaptcha", captcha);
    UserControllerMock controller = new UserControllerMock();
    controller.setAppUserService(appUserServiceMock);
    controller.setFormUserValidator(formUserValidator);
    controller.setCaptchaService(captchaService);
    controller.setMailService(mailServiceMock);
    BindingResult result = new BeanPropertyBindingResult(userForm, "userForm");
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("redirect:/users/registered", controller.addAppUserFromForm(
        requestMock, session, userForm, result, model));
    assertFalse(model.containsKey("appUser"));
    verify(appUserServiceMock).doesEmailExist(userForm.getEmail());
    verify(appUserServiceMock).doesUsernameExist(userForm.getUsername());
    verify(bannedWordServiceMock).doesStringContainBannedWord(
        userForm.getUsername());
  }

  @Test
  public void testAddAppUserFromForm_InvalidUser_NotAnonymousUser()
      throws Exception {
    final String username = "testUser";

    class UserControllerMock extends UserController {
      @Override
      boolean isUserAnonymous() {
        return false;
      }

      @Override
      String getUsernameFromSecurity() {
        return username;
      }
    }

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
    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    AppUserService appUserServiceMock = mock(AppUserService.class);
    when(appUserServiceMock.doesEmailExist(userForm.getEmail())).thenReturn(
        false);
    when(appUserServiceMock.getUser(username)).thenReturn(appUser);
    CaptchaService captchaServiceMock = mock(TextCaptchaServiceImpl.class);
    when(captchaServiceMock.getTextCaptcha()).thenReturn(invalidUserCaptcha);
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    FormUserValidator formUserValidator = new FormUserValidator();
    formUserValidator.setAppUserService(appUserServiceMock);
    HttpSession session = new MockHttpSession();
    session.setAttribute("textCaptcha", captcha);
    UserController controllerMock = new UserControllerMock();
    controllerMock.setAppUserService(appUserServiceMock);
    controllerMock.setFormUserValidator(formUserValidator);
    controllerMock.setCaptchaService(captchaServiceMock);
    BindingResult result = new BeanPropertyBindingResult(userForm, "user");
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("users/create", controllerMock.addAppUserFromForm(requestMock,
        session, userForm, result, model));
    assertFalse(userForm.getTextCaptchaQuestion().equals(captcha.getQuestion()));
    assertEquals(userForm.getTextCaptchaQuestion(),
        invalidUserCaptcha.getQuestion());
    assertNull(userForm.getCaptchaAnswer());
    assertFalse(captcha.equals(session.getAttribute("textCaptcha")));
    assertEquals(invalidUserCaptcha, session.getAttribute("textCaptcha"));
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    verify(appUserServiceMock).doesEmailExist(userForm.getEmail());
    verify(captchaServiceMock).getTextCaptcha();
    verify(appUserServiceMock).getUser(username);
  }

  @Test
  public void testAddAppUserFromForm_InvalidUser_AnonymousUser()
      throws Exception {
    class UserControllerMock extends UserController {
      @Override
      boolean isUserAnonymous() {
        return true;
      }
    }

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
    when(appUserServiceMock.doesEmailExist(userForm.getEmail())).thenReturn(
        false);
    CaptchaService captchaServiceMock = mock(TextCaptchaServiceImpl.class);
    when(captchaServiceMock.getTextCaptcha()).thenReturn(invalidUserCaptcha);
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    FormUserValidator formUserValidator = new FormUserValidator();
    formUserValidator.setAppUserService(appUserServiceMock);
    HttpSession session = new MockHttpSession();
    session.setAttribute("textCaptcha", captcha);
    UserController controllerMock = new UserControllerMock();
    controllerMock.setAppUserService(appUserServiceMock);
    controllerMock.setFormUserValidator(formUserValidator);
    controllerMock.setCaptchaService(captchaServiceMock);
    BindingResult result = new BeanPropertyBindingResult(userForm, "user");
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("users/create", controllerMock.addAppUserFromForm(requestMock,
        session, userForm, result, model));
    assertFalse(userForm.getTextCaptchaQuestion().equals(captcha.getQuestion()));
    assertEquals(userForm.getTextCaptchaQuestion(),
        invalidUserCaptcha.getQuestion());
    assertNull(userForm.getCaptchaAnswer());
    assertFalse(captcha.equals(session.getAttribute("textCaptcha")));
    assertEquals(invalidUserCaptcha, session.getAttribute("textCaptcha"));
    assertFalse(model.containsKey("appUser"));
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
    userForm.setSecurityQuestion("testQuestion");
    userForm.setSecurityAnswer("testAnswer");
    AppUserService appUserServiceMock = mock(AppUserService.class);
    when(appUserServiceMock.doesEmailExist(userForm.getEmail())).thenReturn(
        false);
    when(appUserServiceMock.doesUsernameExist(userForm.getUsername()))
        .thenReturn(false);
    BannedWordService bannedWordServiceMock = mock(BannedWordService.class);
    when(
        bannedWordServiceMock.doesStringContainBannedWord(userForm
            .getUsername())).thenReturn(false);
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    CaptchaService captchaServiceMock = mock(TextCaptchaServiceImpl.class);
    when(captchaServiceMock.getTextCaptcha())
        .thenReturn(incorrectAnswerCaptcha);
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
    verify(appUserServiceMock).doesEmailExist(userForm.getEmail());
    verify(appUserServiceMock).doesUsernameExist(userForm.getUsername());
    verify(bannedWordServiceMock).doesStringContainBannedWord(
        userForm.getUsername());
    verify(captchaServiceMock).getTextCaptcha();
  }

  @Test
  public void testShowRegisteredUser_NotAnonymousUser() throws Exception {
    final String username = "testUser";

    class UserControllerMock extends UserController {
      @Override
      boolean isUserAnonymous() {
        return false;
      }

      @Override
      String getUsernameFromSecurity() {
        return username;
      }
    }

    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    AppUserService userServiceMock = mock(AppUserService.class);
    when(userServiceMock.getUser(username)).thenReturn(appUser);
    UserController controllerMock = new UserControllerMock();
    controllerMock.setAppUserService(userServiceMock);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("users/registered", controllerMock.showRegisteredUser(model));
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    verify(userServiceMock).getUser(username);
  }

  @Test
  public void testShowRegisteredUser_AnonymousUser() throws Exception {
    class UserControllerMock extends UserController {
      @Override
      boolean isUserAnonymous() {
        return true;
      }
    }

    UserController controllerMock = new UserControllerMock();
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("users/registered", controllerMock.showRegisteredUser(model));
    assertFalse(model.containsKey("appUser"));
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
    final String username = "testUser";

    class UserControllerMock extends UserController {
      @Override
      boolean isUserAnonymous() {
        return false;
      }

      @Override
      String getUsernameFromSecurity() {
        return username;
      }
    }

    Map<String, Object> model = new HashMap<String, Object>();
    String uniqueUrlParam = "582744a9-7f06-11e3-8afc-12313815ec2d";
    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    AppUserService appUserServiceMock = mock(AppUserService.class);
    when(
        appUserServiceMock
            .getUserIdByRegistrationConfirmationUniqueUrlParam(uniqueUrlParam))
        .thenReturn(1);
    when(appUserServiceMock.getUser(username)).thenReturn(appUser);
    UserController userControllerMock = new UserControllerMock();
    userControllerMock.setAppUserService(appUserServiceMock);
    assertEquals("users/confirmed",
        userControllerMock.showConfirmedUser(uniqueUrlParam, model));
    assertTrue(model.containsKey("confirmed"));
    assertEquals(true, model.get("confirmed"));
    verify(appUserServiceMock)
        .getUserIdByRegistrationConfirmationUniqueUrlParam(uniqueUrlParam);
    verify(appUserServiceMock).updateAppUserAsActive(1);
    verify(appUserServiceMock).deleteRegistrationConfirmationByUniqueUrlParam(
        uniqueUrlParam);
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    verify(appUserServiceMock).getUser(username);
  }

  @Test
  public void testShowConfirmedUser_HappyPath_AnonymousUser() throws Exception {
    class UserControllerMock extends UserController {
      @Override
      boolean isUserAnonymous() {
        return true;
      }
    }

    Map<String, Object> model = new HashMap<String, Object>();
    String uniqueUrlParam = "582744a9-7f06-11e3-8afc-12313815ec2d";
    AppUserService appUserServiceMock = mock(AppUserService.class);
    when(
        appUserServiceMock
            .getUserIdByRegistrationConfirmationUniqueUrlParam(uniqueUrlParam))
        .thenReturn(1);
    UserController userControllerMock = new UserControllerMock();
    userControllerMock.setAppUserService(appUserServiceMock);
    assertEquals("users/confirmed",
        userControllerMock.showConfirmedUser(uniqueUrlParam, model));
    assertTrue(model.containsKey("confirmed"));
    assertEquals(true, model.get("confirmed"));
    verify(appUserServiceMock)
        .getUserIdByRegistrationConfirmationUniqueUrlParam(uniqueUrlParam);
    verify(appUserServiceMock).updateAppUserAsActive(1);
    verify(appUserServiceMock).deleteRegistrationConfirmationByUniqueUrlParam(
        uniqueUrlParam);
    assertFalse(model.containsKey("appUser"));
  }

  @Test
  public void testShowConfirmedUser_UniqueUrlParamDoesNotExist_NotAnonymousUser()
      throws Exception {
    final String username = "testUser";

    class UserControllerMock extends UserController {
      @Override
      boolean isUserAnonymous() {
        return false;
      }

      @Override
      String getUsernameFromSecurity() {
        return username;
      }
    }

    Map<String, Object> model = new HashMap<String, Object>();
    String uniqueUrlParam = "582744a9-7f06-11e3-8afc-12313815ec2d";
    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    AppUserService appUserServiceMock = mock(AppUserService.class);
    when(
        appUserServiceMock
            .getUserIdByRegistrationConfirmationUniqueUrlParam(uniqueUrlParam))
        .thenReturn(null);
    when(appUserServiceMock.getUser(username)).thenReturn(appUser);
    UserController userControllerMock = new UserControllerMock();
    userControllerMock.setAppUserService(appUserServiceMock);
    assertEquals("users/confirmed",
        userControllerMock.showConfirmedUser(uniqueUrlParam, model));
    assertTrue(model.containsKey("confirmed"));
    assertEquals(false, model.get("confirmed"));
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    verify(appUserServiceMock).getUser(username);
    verify(appUserServiceMock)
        .getUserIdByRegistrationConfirmationUniqueUrlParam(uniqueUrlParam);
  }

  @Test
  public void testShowConfirmedUser_UniqueUrlParamDoesNotExist_AnonymousUser()
      throws Exception {
    class UserControllerMock extends UserController {
      @Override
      boolean isUserAnonymous() {
        return true;
      }
    }

    Map<String, Object> model = new HashMap<String, Object>();
    String uniqueUrlParam = "582744a9-7f06-11e3-8afc-12313815ec2d";
    AppUserService appUserServiceMock = mock(AppUserService.class);
    when(
        appUserServiceMock
            .getUserIdByRegistrationConfirmationUniqueUrlParam(uniqueUrlParam))
        .thenReturn(null);
    UserController userControllerMock = new UserControllerMock();
    userControllerMock.setAppUserService(appUserServiceMock);
    assertEquals("users/confirmed",
        userControllerMock.showConfirmedUser(uniqueUrlParam, model));
    assertTrue(model.containsKey("confirmed"));
    assertEquals(false, model.get("confirmed"));
    assertFalse(model.containsKey("appUser"));
    verify(appUserServiceMock)
        .getUserIdByRegistrationConfirmationUniqueUrlParam(uniqueUrlParam);
  }

  @Test
  public void testSendUsername_InvalidEmail_NotAnonymousUser() throws Exception {
    final String username = "testUser";

    class UserControllerMock extends UserController {
      @Override
      boolean isUserAnonymous() {
        return false;
      }

      @Override
      String getUsernameFromSecurity() {
        return username;
      }
    }

    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    AppUserService userServiceMock = mock(AppUserService.class);
    when(userServiceMock.getUser(username)).thenReturn(appUser);
    EmailForm emailForm = new EmailForm();
    emailForm.setEmail("test");
    BindingResult result = new BeanPropertyBindingResult(emailForm, "email");
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    FormEmailValidator formEmailValidator = new FormEmailValidator();
    UserController controllerMock = new UserControllerMock();
    controllerMock.setAppUserService(userServiceMock);
    controllerMock.setFormEmailValidator(formEmailValidator);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("users/forgotUsername",
        controllerMock.sendUsername(emailForm, result, requestMock, model));
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    verify(userServiceMock).getUser(username);
  }

  @Test
  public void testSendUsername_InvalidEmail_AnonymousUser() throws Exception {
    class UserControllerMock extends UserController {
      @Override
      boolean isUserAnonymous() {
        return true;
      }
    }

    EmailForm emailForm = new EmailForm();
    emailForm.setEmail("test");
    BindingResult result = new BeanPropertyBindingResult(emailForm, "email");
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    FormEmailValidator formEmailValidator = new FormEmailValidator();
    UserController controllerMock = new UserControllerMock();
    controllerMock.setFormEmailValidator(formEmailValidator);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("users/forgotUsername",
        controllerMock.sendUsername(emailForm, result, requestMock, model));
    assertFalse(model.containsKey("appUser"));
  }

  @Test
  public void testSendUsername_UserDoesNotExist_NotAnonymousUser() throws Exception {
    final String username = "testUser";

    class UserControllerMock extends UserController {
      @Override
      boolean isUserAnonymous() {
        return false;
      }

      @Override
      String getUsernameFromSecurity() {
        return username;
      }
    }

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
    when(appUserServiceMock.getUser(username)).thenReturn(appUser);
    UserController controllerMock = new UserControllerMock();
    controllerMock.setAppUserService(appUserServiceMock);
    controllerMock.setFormEmailValidator(formEmailValidator);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("redirect:/users/sentUsername",
        controllerMock.sendUsername(emailForm, result, requestMock, model));
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    verify(appUserServiceMock).getUser(username);
    verify(appUserServiceMock).getUsernameByEmail(email);
  }
  
  @Test
  public void testSendUsername_UserDoesNotExist_AnonymousUser() throws Exception {
    class UserControllerMock extends UserController {
      @Override
      boolean isUserAnonymous() {
        return true;
      }
    }

    EmailForm emailForm = new EmailForm();
    String email = "test@test.com";
    emailForm.setEmail(email);
    BindingResult result = new BeanPropertyBindingResult(emailForm, "email");
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    FormEmailValidator formEmailValidator = new FormEmailValidator();
    AppUserService appUserServiceMock = mock(AppUserService.class);
    when(appUserServiceMock.getUsernameByEmail(email)).thenReturn(null);
    UserController controllerMock = new UserControllerMock();
    controllerMock.setAppUserService(appUserServiceMock);
    controllerMock.setFormEmailValidator(formEmailValidator);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("redirect:/users/sentUsername",
        controllerMock.sendUsername(emailForm, result, requestMock, model));
    assertFalse(model.containsKey("appUser"));
    verify(appUserServiceMock).getUsernameByEmail(email);
  }

  @Test
  public void testSendUsername_HappyPath_NotAnonymousUser() throws Exception {
    final String username = "testUser";

    class UserControllerMock extends UserController {
      @Override
      String generateForgotUsernameEmailBodyWithVelocityEngine(String username,
          HttpServletRequest request) {
        return "test";
      }

      @Override
      boolean isUserAnonymous() {
        return false;
      }

      @Override
      String getUsernameFromSecurity() {
        return username;
      }
    }

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
    when(appUserServiceMock.getUser(username)).thenReturn(appUser);
    MailService mailServiceMock = mock(MailService.class);
    UserController controllerMock = new UserControllerMock();
    controllerMock.setAppUserService(appUserServiceMock);
    controllerMock.setFormEmailValidator(formEmailValidator);
    controllerMock.setMailService(mailServiceMock);
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
    verify(appUserServiceMock).getUser(username);
  }

  @Test
  public void testSendUsername_HappyPath_AnonymousUser() throws Exception {
    class UserControllerMock extends UserController {
      @Override
      String generateForgotUsernameEmailBodyWithVelocityEngine(String username,
          HttpServletRequest request) {
        return "test";
      }

      @Override
      boolean isUserAnonymous() {
        return true;
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
    when(appUserServiceMock.getUser(username)).thenReturn(appUser);
    MailService mailServiceMock = mock(MailService.class);
    UserController controllerMock = new UserControllerMock();
    controllerMock.setAppUserService(appUserServiceMock);
    controllerMock.setFormEmailValidator(formEmailValidator);
    controllerMock.setMailService(mailServiceMock);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("redirect:/users/sentUsername",
        controllerMock.sendUsername(emailForm, result, requestMock, model));
    verify(appUserServiceMock).getUsernameByEmail(email);
    List<String> recipients = new ArrayList<String>();
    recipients.add(email);
    verify(mailServiceMock).sendVelocityEmail(recipients, "Recover Username",
        "test");
    assertFalse(model.containsKey("appUser"));
  }

  @Test
  public void testSentUsername_NotAnonymousUser() {
    final String username = "testUser";

    class UserControllerMock extends UserController {
      @Override
      boolean isUserAnonymous() {
        return false;
      }

      @Override
      String getUsernameFromSecurity() {
        return username;
      }
    }

    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    AppUserService userServiceMock = mock(AppUserService.class);
    when(userServiceMock.getUser(username)).thenReturn(appUser);
    UserController controllerMock = new UserControllerMock();
    controllerMock.setAppUserService(userServiceMock);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("users/sentUsername", controllerMock.sentUsername(model));
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    verify(userServiceMock).getUser(username);
  }

  @Test
  public void testSentUsername_AnonymousUser() {
    class UserControllerMock extends UserController {
      @Override
      boolean isUserAnonymous() {
        return true;
      }
    }

    UserController controllerMock = new UserControllerMock();
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("users/sentUsername", controllerMock.sentUsername(model));
    assertFalse(model.containsKey("appUser"));
  }

  @Test
  public void testForgotUsername_NotAnonymousUser() {
    final String username = "testUser";

    class UserControllerMock extends UserController {
      @Override
      boolean isUserAnonymous() {
        return false;
      }

      @Override
      String getUsernameFromSecurity() {
        return username;
      }
    }

    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    AppUserService userServiceMock = mock(AppUserService.class);
    when(userServiceMock.getUser(username)).thenReturn(appUser);
    Model model = new ExtendedModelMap();
    UserController controllerMock = new UserControllerMock();
    controllerMock.setAppUserService(userServiceMock);
    assertEquals("users/forgotUsername", controllerMock.forgotUsername(model));
    assertTrue(model.containsAttribute("appUser"));
    Map<String, Object> modelMap = model.asMap();
    assertTrue(model.containsAttribute("emailForm"));
    assertEquals(username, ((AppUser) modelMap.get("appUser")).getUsername());
    verify(userServiceMock).getUser(username);
  }

  @Test
  public void testForgotUsername_AnonymousUser() {
    class UserControllerMock extends UserController {
      @Override
      boolean isUserAnonymous() {
        return true;
      }
    }

    Model model = new ExtendedModelMap();
    UserController controllerMock = new UserControllerMock();
    assertEquals("users/forgotUsername", controllerMock.forgotUsername(model));
    assertFalse(model.containsAttribute("appUser"));
    assertTrue(model.containsAttribute("emailForm"));
  }

  @Test
  public void testForgotPassword_NotAnonymousUser() throws Exception {
    final String username = "testUser";

    class UserControllerMock extends UserController {
      @Override
      boolean isUserAnonymous() {
        return false;
      }

      @Override
      String getUsernameFromSecurity() {
        return username;
      }
    }

    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    AppUserService userServiceMock = mock(AppUserService.class);
    when(userServiceMock.getUser(username)).thenReturn(appUser);
    Model model = new ExtendedModelMap();
    UserController controllerMock = new UserControllerMock();
    controllerMock.setAppUserService(userServiceMock);
    assertEquals("users/forgotPassword", controllerMock.forgotPassword(model));
    assertTrue(model.containsAttribute("usernameForm"));
    assertTrue(model.containsAttribute("appUser"));
    Map<String, Object> modelMap = model.asMap();
    assertEquals(username, ((AppUser) modelMap.get("appUser")).getUsername());
    verify(userServiceMock).getUser(username);
  }

  @Test
  public void testForgotPassword_AnonymousUser() throws Exception {
    class UserControllerMock extends UserController {
      @Override
      boolean isUserAnonymous() {
        return true;
      }
    }

    Model model = new ExtendedModelMap();
    UserController controllerMock = new UserControllerMock();
    assertEquals("users/forgotPassword", controllerMock.forgotPassword(model));
    assertTrue(model.containsAttribute("usernameForm"));
    assertFalse(model.containsAttribute("appUser"));
  }

  @Test
  public void testSendPasswordLink_invalidUsername_NotAnonymousUser()
      throws Exception {
    final String username = "testUser";

    class UserControllerMock extends UserController {
      @Override
      boolean isUserAnonymous() {
        return false;
      }

      @Override
      String getUsernameFromSecurity() {
        return username;
      }
    }

    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    AppUserService userServiceMock = mock(AppUserService.class);
    when(userServiceMock.getUser(username)).thenReturn(appUser);
    UsernameForm usernameForm = new UsernameForm();
    BindingResult result = new BeanPropertyBindingResult(usernameForm,
        "username");
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    UserController controllerMock = new UserControllerMock();
    controllerMock.setAppUserService(userServiceMock);
    FormUsernameValidator formUsernameValidator = new FormUsernameValidator();
    controllerMock.setFormUsernamelValidator(formUsernameValidator);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("users/forgotPassword", controllerMock.sendPasswordLink(
        usernameForm, result, requestMock, model));
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    verify(userServiceMock).getUser(username);
  }

  @Test
  public void testSendPasswordLink_invalidUsername_AnonymousUser()
      throws Exception {
    class UserControllerMock extends UserController {
      @Override
      boolean isUserAnonymous() {
        return true;
      }
    }

    UsernameForm usernameForm = new UsernameForm();
    BindingResult result = new BeanPropertyBindingResult(usernameForm,
        "username");
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    UserController controllerMock = new UserControllerMock();
    FormUsernameValidator formUsernameValidator = new FormUsernameValidator();
    controllerMock.setFormUsernamelValidator(formUsernameValidator);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("users/forgotPassword", controllerMock.sendPasswordLink(
        usernameForm, result, requestMock, model));
    assertFalse(model.containsKey("appUser"));
  }

  @Test
  public void testSendPasswordLink_usernameDoesNotExist_NotAnonymousUser()
      throws Exception {
    final String username = "testUser";

    class UserControllerMock extends UserController {
      @Override
      boolean isUserAnonymous() {
        return false;
      }

      @Override
      String getUsernameFromSecurity() {
        return username;
      }
    }

    UsernameForm usernameForm = new UsernameForm();
    usernameForm.setUsername("testUsername");
    BindingResult result = new BeanPropertyBindingResult(usernameForm,
        "username");
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    UserController controllerMock = new UserControllerMock();
    FormUsernameValidator formUsernameValidator = new FormUsernameValidator();
    controllerMock.setFormUsernamelValidator(formUsernameValidator);
    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    AppUserService appUserServiceMock = mock(AppUserService.class);
    when(appUserServiceMock.getEmailByUsername(usernameForm.getUsername()))
        .thenReturn(null);
    when(appUserServiceMock.getUser(username)).thenReturn(appUser);
    controllerMock.setAppUserService(appUserServiceMock);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("redirect:/users/forgotPassword/sentPassword",
        controllerMock.sendPasswordLink(usernameForm, result, requestMock,
            model));
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    verify(appUserServiceMock).getUser(username);
    verify(appUserServiceMock).getEmailByUsername(usernameForm.getUsername());
  }

  @Test
  public void testSendPasswordLink_usernameDoesNotExist_AnonymousUser()
      throws Exception {
    class UserControllerMock extends UserController {
      @Override
      boolean isUserAnonymous() {
        return true;
      }
    }

    UsernameForm usernameForm = new UsernameForm();
    usernameForm.setUsername("testUsername");
    BindingResult result = new BeanPropertyBindingResult(usernameForm,
        "username");
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    UserController controllerMock = new UserControllerMock();
    FormUsernameValidator formUsernameValidator = new FormUsernameValidator();
    controllerMock.setFormUsernamelValidator(formUsernameValidator);
    AppUserService appUserServiceMock = mock(AppUserService.class);
    when(appUserServiceMock.getEmailByUsername(usernameForm.getUsername()))
        .thenReturn(null);
    controllerMock.setAppUserService(appUserServiceMock);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("redirect:/users/forgotPassword/sentPassword",
        controllerMock.sendPasswordLink(usernameForm, result, requestMock,
            model));
    assertFalse(model.containsKey("appUser"));
    verify(appUserServiceMock).getEmailByUsername(usernameForm.getUsername());
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

      @Override
      boolean isUserAnonymous() {
        return false;
      }

      @Override
      String getUsernameFromSecurity() {
        return username;
      }
    }

    UsernameForm usernameForm = new UsernameForm();
    usernameForm.setUsername("testUsername");
    BindingResult result = new BeanPropertyBindingResult(usernameForm,
        "username");
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    UserController controllerMock = new UserControllerMock();
    FormUsernameValidator formUsernameValidator = new FormUsernameValidator();
    controllerMock.setFormUsernamelValidator(formUsernameValidator);
    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    AppUserService appUserServiceMock = mock(AppUserService.class);
    String userEmail = "test@test.com";
    when(appUserServiceMock.getEmailByUsername(usernameForm.getUsername()))
        .thenReturn(userEmail);
    when(appUserServiceMock.getUser(username)).thenReturn(appUser);
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
    verify(appUserServiceMock).getUser(username);
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

      @Override
      boolean isUserAnonymous() {
        return true;
      }
    }

    UsernameForm usernameForm = new UsernameForm();
    usernameForm.setUsername("testUsername");
    BindingResult result = new BeanPropertyBindingResult(usernameForm,
        "username");
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    UserController controllerMock = new UserControllerMock();
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
  }

  @Test
  public void testSentPassword_NotAnonymousUser() throws Exception {
    final String username = "testUser";

    class UserControllerMock extends UserController {
      @Override
      boolean isUserAnonymous() {
        return false;
      }

      @Override
      String getUsernameFromSecurity() {
        return username;
      }
    }

    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    AppUserService userServiceMock = mock(AppUserService.class);
    when(userServiceMock.getUser(username)).thenReturn(appUser);
    UserController controllerMock = new UserControllerMock();
    controllerMock.setAppUserService(userServiceMock);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("users/sentPassword", controllerMock.sentPassword(model));
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    verify(userServiceMock).getUser(username);
  }

  @Test
  public void testSentPassword_AnonymousUser() throws Exception {
    class UserControllerMock extends UserController {
      @Override
      boolean isUserAnonymous() {
        return true;
      }
    }

    UserController controllerMock = new UserControllerMock();
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("users/sentPassword", controllerMock.sentPassword(model));
    assertFalse(model.containsKey("appUser"));
  }

  @Test
  public void testRestPassword_UniqueUrlParamExists_NotAnonymousUser()
      throws Exception {
    final String username = "testUser";

    class UserControllerMock extends UserController {
      @Override
      boolean isUserAnonymous() {
        return false;
      }

      @Override
      String getUsernameFromSecurity() {
        return username;
      }
    }

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
    when(appUserServiceMock.getUser(username)).thenReturn(appUser);
    UserController controllerMock = new UserControllerMock();
    controllerMock.setAppUserService(appUserServiceMock);
    Map<String, Object> model = new HashMap<String, Object>();
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    String contextPath = "/test";
    when(requestMock.getContextPath()).thenReturn(contextPath);
    assertEquals("users/resetPassword",
        controllerMock.resetPassword(uniqueUrlParam, model, requestMock));
    assertTrue((Boolean) model.get("exists"));
    assertEquals(contextPath, model.get("contextPath"));
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    verify(appUserServiceMock).getUser(username);
    verify(appUserServiceMock)
        .getUserSecurityQuestionByResetPasswordUniqueUrlParam(uniqueUrlParam);
    verify(requestMock).getContextPath();
  }

  @Test
  public void testRestPassword_UniqueUrlParamExists_AnonymousUser()
      throws Exception {
    class UserControllerMock extends UserController {
      @Override
      boolean isUserAnonymous() {
        return true;
      }
    }

    UserSecurityQuestion userSecurityQuestion = new UserSecurityQuestion();
    userSecurityQuestion.setUserid(1);
    userSecurityQuestion.setSecurityQuestion("test question");
    String uniqueUrlParam = "testUniqueUrlParam";
    AppUserService appUserServiceMock = mock(AppUserService.class);
    when(
        appUserServiceMock
            .getUserSecurityQuestionByResetPasswordUniqueUrlParam(uniqueUrlParam))
        .thenReturn(userSecurityQuestion);
    UserController controllerMock = new UserControllerMock();
    controllerMock.setAppUserService(appUserServiceMock);
    Map<String, Object> model = new HashMap<String, Object>();
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    String contextPath = "/test";
    when(requestMock.getContextPath()).thenReturn(contextPath);
    assertEquals("users/resetPassword",
        controllerMock.resetPassword(uniqueUrlParam, model, requestMock));
    assertTrue((Boolean) model.get("exists"));
    assertEquals(contextPath, model.get("contextPath"));
    assertFalse(model.containsKey("appUser"));
    verify(appUserServiceMock)
        .getUserSecurityQuestionByResetPasswordUniqueUrlParam(uniqueUrlParam);
    verify(requestMock).getContextPath();
  }

  @Test
  public void testRestPassword_UniqueUrlParamNotExists_NotAnonymousUser()
      throws Exception {
    final String username = "testUser";

    class UserControllerMock extends UserController {
      @Override
      boolean isUserAnonymous() {
        return false;
      }

      @Override
      String getUsernameFromSecurity() {
        return username;
      }
    }

    String uniqueUrlParam = "test";
    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    AppUserService appUserServiceMock = mock(AppUserService.class);
    when(
        appUserServiceMock
            .getUserSecurityQuestionByResetPasswordUniqueUrlParam(uniqueUrlParam))
        .thenReturn(null);
    when(appUserServiceMock.getUser(username)).thenReturn(appUser);
    UserController controllerMock = new UserControllerMock();
    controllerMock.setAppUserService(appUserServiceMock);
    Map<String, Object> model = new HashMap<String, Object>();
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    assertEquals("users/resetPassword",
        controllerMock.resetPassword(uniqueUrlParam, model, requestMock));
    assertFalse((Boolean) model.get("exists"));
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    verify(appUserServiceMock).getUser(username);
    verify(appUserServiceMock)
        .getUserSecurityQuestionByResetPasswordUniqueUrlParam(uniqueUrlParam);
  }

  @Test
  public void testRestPassword_UniqueUrlParamNotExists_AnonymousUser()
      throws Exception {
    class UserControllerMock extends UserController {
      @Override
      boolean isUserAnonymous() {
        return true;
      }
    }

    String uniqueUrlParam = "test";
    AppUserService appUserServiceMock = mock(AppUserService.class);
    when(
        appUserServiceMock
            .getUserSecurityQuestionByResetPasswordUniqueUrlParam(uniqueUrlParam))
        .thenReturn(null);
    UserController controllerMock = new UserControllerMock();
    controllerMock.setAppUserService(appUserServiceMock);
    Map<String, Object> model = new HashMap<String, Object>();
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    assertEquals("users/resetPassword",
        controllerMock.resetPassword(uniqueUrlParam, model, requestMock));
    assertFalse((Boolean) model.get("exists"));
    assertFalse(model.containsKey("appUser"));
    verify(appUserServiceMock)
        .getUserSecurityQuestionByResetPasswordUniqueUrlParam(uniqueUrlParam);
  }

  @Test
  public void testUpdatePassword_InvalidPasswordSecurity_NotAnonymousUser() throws Exception {
    final String username = "testUser";

    class UserControllerMock extends UserController {
      @Override
      boolean isUserAnonymous() {
        return false;
      }

      @Override
      String getUsernameFromSecurity() {
        return username;
      }
    }

    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    AppUserService userServiceMock = mock(AppUserService.class);
    when(userServiceMock.getUser(username)).thenReturn(appUser);
    PasswordSecurityForm passwordSecurityForm = new PasswordSecurityForm();
    passwordSecurityForm.setUserid(1);
    passwordSecurityForm.setSecurityAnswer(StringUtils.EMPTY);
    passwordSecurityForm.setPassword(null);
    passwordSecurityForm.setConfirmPassword(null);
    FormPasswordSecurityValidator formPasswordSecurityValidator = new FormPasswordSecurityValidator();
    UserController controllerMock = new UserControllerMock();
    controllerMock.setAppUserService(userServiceMock);
    controllerMock.setFormPasswordSecuritylValidator(formPasswordSecurityValidator);
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    String contextPath = "/test";
    when(requestMock.getContextPath()).thenReturn(contextPath);
    BindingResult result = new BeanPropertyBindingResult(passwordSecurityForm,
        "paswordSecurityForm");
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("users/resetPassword", controllerMock.updatePassword(
        passwordSecurityForm, result, requestMock, model));
    assertTrue((Boolean) model.get("exists"));
    assertEquals(contextPath, model.get("contextPath"));
    assertEquals(passwordSecurityForm, model.get("passwordSecurityForm"));
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    verify(userServiceMock).getUser(username);
  }
  
  @Test
  public void testUpdatePassword_InvalidPasswordSecurity_AnonymousUser() throws Exception {
    class UserControllerMock extends UserController {
      @Override
      boolean isUserAnonymous() {
        return true;
      }
    }

    PasswordSecurityForm passwordSecurityForm = new PasswordSecurityForm();
    passwordSecurityForm.setUserid(1);
    passwordSecurityForm.setSecurityAnswer(StringUtils.EMPTY);
    passwordSecurityForm.setPassword(null);
    passwordSecurityForm.setConfirmPassword(null);
    FormPasswordSecurityValidator formPasswordSecurityValidator = new FormPasswordSecurityValidator();
    UserController controllerMock = new UserControllerMock();
    controllerMock.setFormPasswordSecuritylValidator(formPasswordSecurityValidator);
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    String contextPath = "/test";
    when(requestMock.getContextPath()).thenReturn(contextPath);
    BindingResult result = new BeanPropertyBindingResult(passwordSecurityForm,
        "paswordSecurityForm");
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("users/resetPassword", controllerMock.updatePassword(
        passwordSecurityForm, result, requestMock, model));
    assertTrue((Boolean) model.get("exists"));
    assertEquals(contextPath, model.get("contextPath"));
    assertEquals(passwordSecurityForm, model.get("passwordSecurityForm"));
    assertFalse(model.containsKey("appUser"));
  }

  @Test
  public void testUpdatePassword_HappyPath_NotAnonymousUser() throws Exception {
    final String username = "testUser";

    class UserControllerMock extends UserController {
      @Override
      boolean isUserAnonymous() {
        return false;
      }

      @Override
      String getUsernameFromSecurity() {
        return username;
      }
    }

    PasswordSecurityForm passwordSecurityForm = new PasswordSecurityForm();
    passwordSecurityForm.setUserid(1);
    passwordSecurityForm.setSecurityAnswer("testAnswer");
    String password = "testPassword";
    passwordSecurityForm.setPassword(password);
    passwordSecurityForm.setConfirmPassword(password);
    FormPasswordSecurityValidator formPasswordSecurityValidator = new FormPasswordSecurityValidator();
    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    AppUserService appUserServiceMock = mock(AppUserService.class);
    when(
        appUserServiceMock.getSecurityAnswerByUserid(passwordSecurityForm
            .getUserid())).thenReturn(passwordSecurityForm.getSecurityAnswer());
    when(
        appUserServiceMock.getUsernameByUserid(passwordSecurityForm.getUserid()))
        .thenReturn(username);
    when(appUserServiceMock.getUser(username)).thenReturn(appUser);
    formPasswordSecurityValidator.setAppUserService(appUserServiceMock);
    UserController controllerMock = new UserControllerMock();
    controllerMock.setAppUserService(appUserServiceMock);
    controllerMock.setFormPasswordSecuritylValidator(formPasswordSecurityValidator);
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    BindingResult result = new BeanPropertyBindingResult(passwordSecurityForm,
        "paswordSecurityForm");
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("redirect:/users/forgotPassword/passwordUpdated", controllerMock.updatePassword(
        passwordSecurityForm, result, requestMock, model));
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    verify(appUserServiceMock).getUser(username);
    verify(appUserServiceMock).getSecurityAnswerByUserid(
        passwordSecurityForm.getUserid());
    verify(appUserServiceMock).getUsernameByUserid(
        passwordSecurityForm.getUserid());
    verify(appUserServiceMock).updatePasswordByUserid(
        passwordSecurityForm.getUserid(),
        Encoder.encodeString(username, passwordSecurityForm.getPassword()));
  }
  
  @Test
  public void testUpdatePassword_HappyPath_AnonymousUser() throws Exception {
    class UserControllerMock extends UserController {
      @Override
      boolean isUserAnonymous() {
        return true;
      }
    }

    PasswordSecurityForm passwordSecurityForm = new PasswordSecurityForm();
    passwordSecurityForm.setUserid(1);
    passwordSecurityForm.setSecurityAnswer("testAnswer");
    String password = "testPassword";
    passwordSecurityForm.setPassword(password);
    passwordSecurityForm.setConfirmPassword(password);
    FormPasswordSecurityValidator formPasswordSecurityValidator = new FormPasswordSecurityValidator();
    AppUserService appUserServiceMock = mock(AppUserService.class);
    String username = "testUser";
    when(
        appUserServiceMock.getSecurityAnswerByUserid(passwordSecurityForm
            .getUserid())).thenReturn(passwordSecurityForm.getSecurityAnswer());
    when(
        appUserServiceMock.getUsernameByUserid(passwordSecurityForm.getUserid()))
        .thenReturn(username);
    formPasswordSecurityValidator.setAppUserService(appUserServiceMock);
    UserController controllerMock = new UserControllerMock();
    controllerMock.setAppUserService(appUserServiceMock);
    controllerMock.setFormPasswordSecuritylValidator(formPasswordSecurityValidator);
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    BindingResult result = new BeanPropertyBindingResult(passwordSecurityForm,
        "paswordSecurityForm");
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("redirect:/users/forgotPassword/passwordUpdated", controllerMock.updatePassword(
        passwordSecurityForm, result, requestMock, model));
    assertFalse(model.containsKey("appUser"));
    verify(appUserServiceMock).getSecurityAnswerByUserid(
        passwordSecurityForm.getUserid());
    verify(appUserServiceMock).getUsernameByUserid(
        passwordSecurityForm.getUserid());
    verify(appUserServiceMock).updatePasswordByUserid(
        passwordSecurityForm.getUserid(),
        Encoder.encodeString(username, passwordSecurityForm.getPassword()));
  }

  @Test
  public void testPasswordUpdated_NotAnonymousUser() throws Exception {
    final String username = "testUser";

    class UserControllerMock extends UserController {
      @Override
      boolean isUserAnonymous() {
        return false;
      }

      @Override
      String getUsernameFromSecurity() {
        return username;
      }
    }

    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    AppUserService userServiceMock = mock(AppUserService.class);
    when(userServiceMock.getUser(username)).thenReturn(appUser);
    UserController controllerMock = new UserControllerMock();
    controllerMock.setAppUserService(userServiceMock);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("users/passwordUpdated", controllerMock.passwordUpdated(model));
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    verify(userServiceMock).getUser(username);
  }

  @Test
  public void testPasswordUpdated_AnonymousUser() throws Exception {
    class UserControllerMock extends UserController {
      @Override
      boolean isUserAnonymous() {
        return true;
      }
    }

    UserController controllerMock = new UserControllerMock();
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("users/passwordUpdated", controllerMock.passwordUpdated(model));
    assertFalse(model.containsKey("appUser"));
  }

  @Test
  public void testTermsAndConditions() throws Exception {
    UserController controller = new UserController();
    assertEquals("users/termsAndConditions", controller.termsAndConditions());
  }
  
  @Test
  public void testManageAccount_NotAnonymousUser() throws Exception {
    final String username = "testUser";
    
    class UserControllerMock extends UserController {
      @Override
      boolean isUserAnonymous() {
        return false;
      }

      @Override
      String getUsernameFromSecurity() {
        return username;
      }
    }
    
    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    AppUserService userServiceMock = mock(AppUserService.class);
    when(userServiceMock.getUser(username)).thenReturn(appUser);
    UserController controllerMock = new UserControllerMock();
    controllerMock.setAppUserService(userServiceMock);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("users/manageAccount", controllerMock.manageAccount(model));
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    verify(userServiceMock).getUser(username);
  }
  
  @Test
  public void testManageAccount_anonymousUser() throws Exception {
    class UserControllerMock extends UserController {
      @Override
      boolean isUserAnonymous() {
        return true;
      }
    }
    
    UserController controllerMock = new UserControllerMock();
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("users/manageAccount", controllerMock.manageAccount(model));
    assertFalse(model.containsKey("appUser"));
  }
  
  @Test
  public void testManageAccountEdit_NotAnonymousUser() throws Exception {
    final String username = "testUser";
    
    class UserControllerMock extends UserController {
      @Override
      boolean isUserAnonymous() {
        return false;
      }

      @Override
      String getUsernameFromSecurity() {
        return username;
      }
    }
    
    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    AppUserService userServiceMock = mock(AppUserService.class);
    when(userServiceMock.getUser(username)).thenReturn(appUser);
    UserController controllerMock = new UserControllerMock();
    controllerMock.setAppUserService(userServiceMock);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("users/manageAccountEdit", controllerMock.manageAccountEdit(model));
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    verify(userServiceMock).getUser(username);
  }
  
  @Test
  public void testManageAccountEdit_anonymousUser() throws Exception {
    class UserControllerMock extends UserController {
      @Override
      boolean isUserAnonymous() {
        return true;
      }
    }
    
    UserController controllerMock = new UserControllerMock();
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("users/manageAccountEdit", controllerMock.manageAccountEdit(model));
    assertFalse(model.containsKey("appUser"));
  }
  
  @Test
  public void testManageAccountEditSubmit_invalidSubmission() throws Exception {
    final String username = "testUser";

    class UserControllerMock extends UserController {
      @Override
      boolean isUserAnonymous() {
        return false;
      }

      @Override
      String getUsernameFromSecurity() {
        return username;
      }
    }
    
    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    AppUserService userServiceMock = mock(AppUserService.class);
    when(userServiceMock.getUser(username)).thenReturn(appUser);
    ManageAccountForm manageAccountForm = new ManageAccountForm();
    manageAccountForm.setSecurityAnswer("12345678901234567890123456789012345678901");
    FormManageAccountEditValidator formManageAccountEditValidator = new FormManageAccountEditValidator();
    UserControllerMock userControllerMock = new UserControllerMock();
    userControllerMock.setAppUserService(userServiceMock);
    userControllerMock.setFormManageAccountEditValidator(formManageAccountEditValidator);
    BindingResult result = new BeanPropertyBindingResult(manageAccountForm,
        "manageAccountForm");
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("users/manageAccountEdit", userControllerMock.manageAccountEditSubmit(
        manageAccountForm, result, model, null));
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    verify(userServiceMock).getUser(username);
  }
  
  @Test
  public void testManageAccountEditSubmit_validSubmission_everythingEmpty() throws Exception {
    final String username = "testUser";

    class UserControllerMock extends UserController {
      @Override
      boolean isUserAnonymous() {
        return false;
      }

      @Override
      String getUsernameFromSecurity() {
        return username;
      }
    }
    
    AppUser appUser = new AppUser();
    appUser.setUsername(username);
    AppUserService userServiceMock = mock(AppUserService.class);
    when(userServiceMock.getUser(username)).thenReturn(appUser);
    ManageAccountForm manageAccountForm = new ManageAccountForm();
    FormManageAccountEditValidator formManageAccountEditValidator = new FormManageAccountEditValidator();
    UserControllerMock userControllerMock = new UserControllerMock();
    userControllerMock.setAppUserService(userServiceMock);
    userControllerMock.setFormManageAccountEditValidator(formManageAccountEditValidator);
    BindingResult result = new BeanPropertyBindingResult(manageAccountForm,
        "manageAccountForm");
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("redirect:/users/manageAccount", userControllerMock.manageAccountEditSubmit(
        manageAccountForm, result, model, null));
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    verify(userServiceMock).getUser(username);
  }
  
  @Test
  public void testManageAccountEditSubmit_validSubmission_everythingNotEmpty() throws Exception {
    final String username = "testUser";

    class UserControllerMock extends UserController {
      @Override
      String getUsernameFromSecurity() {
        return username;
      }
      
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
    when(userServiceMock.getUser(username)).thenReturn(appUser);
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
    UserControllerMock userControllerMock = new UserControllerMock();
    userControllerMock.setAppUserService(userServiceMock);
    userControllerMock.setFormManageAccountEditValidator(formManageAccountEditValidatorMock);
    userControllerMock.setMailService(mailServiceMock);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("redirect:/users/manageAccount", userControllerMock.manageAccountEditSubmit(
        manageAccountForm, result, model, null));
    assertTrue(model.containsKey("appUser"));
    assertEquals(username, ((AppUser) model.get("appUser")).getUsername());
    verify(userServiceMock).getUser(username);
    List<String> recipients = new ArrayList<String>();
    recipients.add(appUser.getEmail());
    recipients.add(newEmail);
    verify(mailServiceMock).sendVelocityEmail(recipients, "GraffitiTracker Email Address Change Notification",
        "test");
  }
}
