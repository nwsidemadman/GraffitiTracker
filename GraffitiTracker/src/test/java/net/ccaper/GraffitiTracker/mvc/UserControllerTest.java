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
import net.ccaper.GraffitiTracker.mvc.validators.FormPasswordSecurityValidator;
import net.ccaper.GraffitiTracker.mvc.validators.FormUserValidator;
import net.ccaper.GraffitiTracker.mvc.validators.FormUsernameValidator;
import net.ccaper.GraffitiTracker.objects.AppUser;
import net.ccaper.GraffitiTracker.objects.EmailForm;
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
  public void testCreateUserProfile_HappyPath() {
    String inet = "127.0.0.1";
    TextCaptcha captcha = new TextCaptcha("Some quesiton", "answer");
    CaptchaService captchaServiceMock = mock(CaptchaService.class);
    when(captchaServiceMock.getTextCaptcha()).thenReturn(captcha);
    BannedInetsService banndedInetsServiceMock = mock(BannedInetsService.class);
    when(banndedInetsServiceMock.isInetBanned(inet)).thenReturn(false);
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    when(requestMock.getRemoteAddr()).thenReturn(inet);
    UserController controller = new UserController();
    controller.setBannedInetsService(banndedInetsServiceMock);
    controller.setCaptchaService(captchaServiceMock);
    Model model = new ExtendedModelMap();
    HttpSession session = new MockHttpSession();
    assertEquals("users/create", controller.createUserProfile(model, session, requestMock));
    UserForm userForm = (UserForm) model.asMap().get("userForm");
    assertNull(userForm.getUsername());
    assertEquals(captcha.getQuestion(), userForm.getTextCaptchaQuestion());
    assertEquals(captcha, session.getAttribute("textCaptcha"));
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
    assertEquals("users/banned", controllerMock.createUserProfile(model, session, requestMock));
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
    assertEquals("users/banned", controllerMock.createUserProfile(model, session, requestMock));
    assertFalse(model.containsAttribute("appUser"));
    verify(banndedInetsServiceMock).isInetBanned(inet);
    verify(requestMock).getRemoteAddr();
  }

  @Test
  public void testAddAppUserFromForm_HappyPath() throws Exception {
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
    assertEquals("redirect:/users/registered",
        controller.addAppUserFromForm(requestMock, session, userForm, result, model));
    verify(appUserServiceMock).doesEmailExist(userForm.getEmail());
    verify(appUserServiceMock).doesUsernameExist(userForm.getUsername());
    verify(bannedWordServiceMock).doesStringContainBannedWord(
        userForm.getUsername());
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
    when(appUserServiceMock.doesEmailExist(userForm.getEmail())).thenReturn(
        false);
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
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("users/create",
        controller.addAppUserFromForm(requestMock, session, userForm, result, model));
    assertFalse(userForm.getTextCaptchaQuestion().equals(captcha.getQuestion()));
    assertEquals(userForm.getTextCaptchaQuestion(),
        invalidUserCaptcha.getQuestion());
    assertNull(userForm.getCaptchaAnswer());
    assertFalse(captcha.equals(session.getAttribute("textCaptcha")));
    assertEquals(invalidUserCaptcha, session.getAttribute("textCaptcha"));
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
    assertEquals("users/create",
        controller.addAppUserFromForm(requestMock, session, userForm, result, model));
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
  public void testShowRegisteredUser() throws Exception {
    UserController controller = new UserController();
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("users/registered", controller.showRegisteredUser(model));
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
  public void testShowConfirmedUser_HappyPath() throws Exception {
    Map<String, Object> model = new HashMap<String, Object>();
    String uniqueUrlParam = "582744a9-7f06-11e3-8afc-12313815ec2d";
    AppUserService appUserServiceMock = mock(AppUserService.class);
    when(
        appUserServiceMock
        .getUserIdByRegistrationConfirmationUniqueUrlParam(uniqueUrlParam))
        .thenReturn(1);
    UserController userController = new UserController();
    userController.setAppUserService(appUserServiceMock);
    assertEquals("users/confirmed",
        userController.showConfirmedUser(uniqueUrlParam, model));
    assertTrue(model.containsKey("confirmed"));
    assertEquals(true, model.get("confirmed"));
    verify(appUserServiceMock)
    .getUserIdByRegistrationConfirmationUniqueUrlParam(uniqueUrlParam);
    verify(appUserServiceMock).updateAppUserAsActive(1);
    verify(appUserServiceMock).deleteRegistrationConfirmationByUniqueUrlParam(
        uniqueUrlParam);
  }

  @Test
  public void testShowConfirmedUser_UniqueUrlParamDoesNotExist()
      throws Exception {
    Map<String, Object> model = new HashMap<String, Object>();
    String uniqueUrlParam = "582744a9-7f06-11e3-8afc-12313815ec2d";
    AppUserService appUserServiceMock = mock(AppUserService.class);
    when(
        appUserServiceMock
        .getUserIdByRegistrationConfirmationUniqueUrlParam(uniqueUrlParam))
        .thenReturn(null);
    UserController userController = new UserController();
    userController.setAppUserService(appUserServiceMock);
    assertEquals("users/confirmed",
        userController.showConfirmedUser(uniqueUrlParam, model));
    assertTrue(model.containsKey("confirmed"));
    assertEquals(false, model.get("confirmed"));
    verify(appUserServiceMock)
    .getUserIdByRegistrationConfirmationUniqueUrlParam(uniqueUrlParam);
  }

  @Test
  public void testSendUsername_InvalidEmail() throws Exception {
    EmailForm emailForm = new EmailForm();
    emailForm.setEmail("test");
    BindingResult result = new BeanPropertyBindingResult(emailForm, "email");
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    FormEmailValidator formEmailValidator = new FormEmailValidator();
    UserController controller = new UserController();
    controller.setFormEmailValidator(formEmailValidator);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("users/forgotUsername",
        controller.sendUsername(emailForm, result, requestMock, model));
  }

  @Test
  public void testSendUsername_UserDoesNotExist() throws Exception {
    EmailForm emailForm = new EmailForm();
    String email = "test@test.com";
    emailForm.setEmail(email);
    BindingResult result = new BeanPropertyBindingResult(emailForm, "email");
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    FormEmailValidator formEmailValidator = new FormEmailValidator();
    AppUserService appUserServiceMock = mock(AppUserService.class);
    when(appUserServiceMock.getUsernameByEmail(email)).thenReturn(null);
    UserController controller = new UserController();
    controller.setAppUserService(appUserServiceMock);
    controller.setFormEmailValidator(formEmailValidator);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("redirect:/users/sentUsername",
        controller.sendUsername(emailForm, result, requestMock, model));
    verify(appUserServiceMock).getUsernameByEmail(email);
  }

  @Test
  public void testSendUsername_HappyPath() throws Exception {
    class UserControllerMock extends UserController {
      @Override
      String generateForgotUsernameEmailBodyWithVelocityEngine(String username,
          HttpServletRequest request) {
        return "test";
      }
    }

    EmailForm emailForm = new EmailForm();
    String email = "test@test.com";
    String username = "test";
    emailForm.setEmail(email);
    BindingResult result = new BeanPropertyBindingResult(emailForm, "email");
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    FormEmailValidator formEmailValidator = new FormEmailValidator();
    AppUserService appUserServiceMock = mock(AppUserService.class);
    when(appUserServiceMock.getUsernameByEmail(email)).thenReturn(username);
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
  }

  @Test
  public void testSentUsername() {
    UserController controller = new UserController();
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("users/sentUsername", controller.sentUsername(model));
  }

  @Test
  public void testForgotUsername() {
    Model model = new ExtendedModelMap();
    UserController controller = new UserController();
    assertEquals("users/forgotUsername", controller.forgotUsername(model));
    assertTrue(model.containsAttribute("emailForm"));
  }

  @Test
  public void testForgotPassword() {
    Model model = new ExtendedModelMap();
    UserController controller = new UserController();
    assertEquals("users/forgotPassword", controller.forgotPassword(model));
    assertTrue(model.containsAttribute("usernameForm"));
  }

  @Test
  public void testSendPasswordLink_invalidUsername() throws Exception {
    UsernameForm usernameForm = new UsernameForm();
    BindingResult result = new BeanPropertyBindingResult(usernameForm,
        "username");
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    UserController controller = new UserController();
    FormUsernameValidator formUsernameValidator = new FormUsernameValidator();
    controller.setFormUsernamelValidator(formUsernameValidator);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("users/forgotPassword",
        controller.sendPasswordLink(usernameForm, result, requestMock, model));
  }

  @Test
  public void testSendPasswordLink_usernameDoesNotExist() throws Exception {
    UsernameForm usernameForm = new UsernameForm();
    usernameForm.setUsername("testUsername");
    BindingResult result = new BeanPropertyBindingResult(usernameForm,
        "username");
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    UserController controller = new UserController();
    FormUsernameValidator formUsernameValidator = new FormUsernameValidator();
    controller.setFormUsernamelValidator(formUsernameValidator);
    AppUserService appUserServiceMock = mock(AppUserService.class);
    when(appUserServiceMock.getEmailByUsername(usernameForm.getUsername()))
    .thenReturn(null);
    controller.setAppUserService(appUserServiceMock);
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("redirect:/users/sentPassword",
        controller.sendPasswordLink(usernameForm, result, requestMock, model));
    verify(appUserServiceMock).getEmailByUsername(usernameForm.getUsername());
  }

  @Test
  public void testSendPasswordLink_HappyPath() throws Exception {
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
    assertEquals("redirect:/users/sentPassword",
        controllerMock.sendPasswordLink(usernameForm, result, requestMock, model));
    verify(appUserServiceMock).getEmailByUsername(usernameForm.getUsername());
    verify(appUserServiceMock).addResetPassword(usernameForm.getUsername());
    List<String> recipients = new ArrayList<String>(1);
    recipients.add(userEmail);
    verify(mailServiceMock).sendVelocityEmail(recipients, "Recover Password",
        emailBody);
  }

  @Test
  public void testSentPassword() throws Exception {
    UserController controller = new UserController();
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("users/sentPassword", controller.sentPassword(model));
  }

  @Test
  public void testRestPassword_UniqueUrlParamExists() throws Exception {
    UserSecurityQuestion userSecurityQuestion = new UserSecurityQuestion();
    userSecurityQuestion.setUserid(1);
    userSecurityQuestion.setSecurityQuestion("test question");
    String uniqueUrlParam = "testUniqueUrlParam";
    AppUserService appUserServiceMock = mock(AppUserService.class);
    when(
        appUserServiceMock
        .getUserSecurityQuestionByResetPasswordUniqueUrlParam(uniqueUrlParam))
        .thenReturn(userSecurityQuestion);
    UserController controller = new UserController();
    controller.setAppUserService(appUserServiceMock);
    Map<String, Object> model = new HashMap<String, Object>();
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    String contextPath = "/test";
    when(requestMock.getContextPath()).thenReturn(contextPath);
    assertEquals("users/resetPassword",
        controller.resetPassword(uniqueUrlParam, model, requestMock));
    assertTrue((Boolean) model.get("exists"));
    assertEquals(contextPath, model.get("contextPath"));
    verify(appUserServiceMock)
    .getUserSecurityQuestionByResetPasswordUniqueUrlParam(uniqueUrlParam);
    verify(requestMock).getContextPath();
  }

  @Test
  public void testRestPassword_UniqueUrlParamNotExists() throws Exception {
    String uniqueUrlParam = "test";
    AppUserService appUserServiceMock = mock(AppUserService.class);
    when(
        appUserServiceMock
        .getUserSecurityQuestionByResetPasswordUniqueUrlParam(uniqueUrlParam))
        .thenReturn(null);
    UserController controller = new UserController();
    controller.setAppUserService(appUserServiceMock);
    Map<String, Object> model = new HashMap<String, Object>();
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    assertEquals("users/resetPassword",
        controller.resetPassword(uniqueUrlParam, model, requestMock));
    assertFalse((Boolean) model.get("exists"));
    verify(appUserServiceMock)
    .getUserSecurityQuestionByResetPasswordUniqueUrlParam(uniqueUrlParam);
  }

  @Test
  public void testUpdatePassword_InvalidPasswordSecurity() throws Exception {
    PasswordSecurityForm passwordSecurityForm = new PasswordSecurityForm();
    passwordSecurityForm.setUserId(1);
    passwordSecurityForm.setSecurityAnswer(StringUtils.EMPTY);
    passwordSecurityForm.setPassword(null);
    passwordSecurityForm.setConfirmPassword(null);
    FormPasswordSecurityValidator formPasswordSecurityValidator = new FormPasswordSecurityValidator();
    UserController controller = new UserController();
    controller.setFormPasswordSecuritylValidator(formPasswordSecurityValidator);
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    BindingResult result = new BeanPropertyBindingResult(passwordSecurityForm,
        "paswordSecurityForm");
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("users/resetPassword", controller.updatePassword(
        passwordSecurityForm, result, requestMock, model));
    assertTrue((Boolean) model.get("exists"));
    assertEquals(passwordSecurityForm, model.get("passwordSecurityForm"));
  }

  @Test
  public void testUpdatePassword_HappyPath() throws Exception {
    PasswordSecurityForm passwordSecurityForm = new PasswordSecurityForm();
    passwordSecurityForm.setUserId(1);
    passwordSecurityForm.setSecurityAnswer("testAnswer");
    String password = "testPassword";
    passwordSecurityForm.setPassword(password);
    passwordSecurityForm.setConfirmPassword(password);
    FormPasswordSecurityValidator formPasswordSecurityValidator = new FormPasswordSecurityValidator();
    AppUserService appUserServiceMock = mock(AppUserService.class);
    String username = "testUsername";
    when(
        appUserServiceMock.getSecurityAnswerByUserId(passwordSecurityForm
            .getUserId())).thenReturn(passwordSecurityForm.getSecurityAnswer());
    when(
        appUserServiceMock.getUsernameByUserId(passwordSecurityForm.getUserId()))
        .thenReturn(username);
    formPasswordSecurityValidator.setAppUserService(appUserServiceMock);
    UserController controller = new UserController();
    controller.setAppUserService(appUserServiceMock);
    controller.setFormPasswordSecuritylValidator(formPasswordSecurityValidator);
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    BindingResult result = new BeanPropertyBindingResult(passwordSecurityForm,
        "paswordSecurityForm");
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("redirect:/users/passwordUpdated", controller.updatePassword(
        passwordSecurityForm, result, requestMock, model));
    verify(appUserServiceMock).getSecurityAnswerByUserId(
        passwordSecurityForm.getUserId());
    verify(appUserServiceMock).getUsernameByUserId(
        passwordSecurityForm.getUserId());
    verify(appUserServiceMock).updatePasswordByUserid(
        passwordSecurityForm.getUserId(),
        Encoder.encodeString(username, passwordSecurityForm.getPassword()));
  }

  @Test
  public void testPasswordUpdated() throws Exception {
    UserController controller = new UserController();
    Map<String, Object> model = new HashMap<String, Object>();
    assertEquals("users/passwordUpdated", controller.passwordUpdated(model));
  }
  
  @Test
  public void testTermsAndConditions() throws Exception {
    UserController controller = new UserController();
    assertEquals("users/termsAndConditions", controller.termsAndConditions());
  }
}
