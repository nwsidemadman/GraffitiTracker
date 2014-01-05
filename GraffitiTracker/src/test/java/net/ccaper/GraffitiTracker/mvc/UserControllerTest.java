package net.ccaper.GraffitiTracker.mvc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpSession;

import net.ccaper.GraffitiTracker.mvc.validators.UserValidator;
import net.ccaper.GraffitiTracker.objects.TextCaptcha;
import net.ccaper.GraffitiTracker.objects.User;
import net.ccaper.GraffitiTracker.service.BannedWordService;
import net.ccaper.GraffitiTracker.service.CaptchaService;
import net.ccaper.GraffitiTracker.service.UserService;
import net.ccaper.GraffitiTracker.serviceImpl.TextCaptchaServiceImpl;

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
  public void testCreateUserProfile() {
    TextCaptcha captcha = new TextCaptcha("Some quesiton", "answer");
    CaptchaService captchaServiceMock = mock(CaptchaService.class);
    when(captchaServiceMock.getTextCaptcha()).thenReturn(captcha);
    UserController controller = new UserController();
    controller.setCaptchaService(captchaServiceMock);
    Model model = new ExtendedModelMap();
    HttpSession session = new MockHttpSession();
    assertEquals("users/edit", controller.createUserProfile(model, session));
    User user = (User) model.asMap().get("user");
    assertNull(user.getUsername());
    assertEquals(captcha.getQuestion(), user.getTextCaptchaQuestion());
    assertEquals(captcha, (TextCaptcha) session.getAttribute("textCaptcha"));
    verify(captchaServiceMock).getTextCaptcha();
  }

  @Test
  public void testAddUserFromForm_HappyPath() throws Exception {
    TextCaptcha captcha = new TextCaptcha("What is Chris' name?",
        "6b34fe24ac2ff8103f6fce1f0da2ef57");
    User user = new User();
    user.setUsername("testUsername");
    user.setPassword("testPassword");
    user.setConfirmPassword("testPassword");
    user.setEmail("test@test.com");
    user.setAcceptTerms(true);
    user.setCaptchaAnswer("Chris");
    UserService userServiceMock = mock(UserService.class);
    when(userServiceMock.doesEmailExist(user.getEmail())).thenReturn(false);
    when(userServiceMock.doesUsernameExist(user.getUsername())).thenReturn(
        false);
    BannedWordService bannedWordServiceMock = mock(BannedWordService.class);
    when(bannedWordServiceMock.doesStringContainBannedWord(user.getUsername()))
        .thenReturn(false);
    CaptchaService captchaService = new TextCaptchaServiceImpl();
    UserValidator userValidator = new UserValidator();
    userValidator.setUserService(userServiceMock);
    userValidator.setBannedWordService(bannedWordServiceMock);
    HttpSession session = new MockHttpSession();
    session.setAttribute("textCaptcha", captcha);
    UserController controller = new UserController();
    controller.setUserService(userServiceMock);
    controller.setUserValidator(userValidator);
    controller.setCaptchaService(captchaService);
    BindingResult result = new BeanPropertyBindingResult(user, "user");
    assertEquals("redirect:/home",
        controller.addUserFromForm(session, user, result));
    verify(userServiceMock).doesEmailExist(user.getEmail());
    verify(userServiceMock).doesUsernameExist(user.getUsername());
    verify(bannedWordServiceMock).doesStringContainBannedWord(
        user.getUsername());
  }

  @Test
  public void testAddUserFromForm_InvalidUser() throws Exception {
    TextCaptcha captcha = new TextCaptcha("What is Chris' name?",
        "6b34fe24ac2ff8103f6fce1f0da2ef57");
    TextCaptcha invalidUserCaptcha = new TextCaptcha(
        "What is eighteen thousand three hundred and fourteen as a number?",
        "eb98676e8ee16adce38796051a5cc7ff");
    User user = new User();
    user.setUsername("test");
    user.setPassword("testPassword");
    user.setConfirmPassword("testPassword");
    user.setEmail("test@test.com");
    user.setAcceptTerms(true);
    user.setCaptchaAnswer("Chris");
    UserService userServiceMock = mock(UserService.class);
    when(userServiceMock.doesEmailExist(user.getEmail())).thenReturn(false);
    CaptchaService captchaServiceMock = mock(TextCaptchaServiceImpl.class);
    when(captchaServiceMock.getTextCaptcha()).thenReturn(invalidUserCaptcha);
    UserValidator userValidator = new UserValidator();
    userValidator.setUserService(userServiceMock);
    HttpSession session = new MockHttpSession();
    session.setAttribute("textCaptcha", captcha);
    UserController controller = new UserController();
    controller.setUserService(userServiceMock);
    controller.setUserValidator(userValidator);
    controller.setCaptchaService(captchaServiceMock);
    BindingResult result = new BeanPropertyBindingResult(user, "user");
    assertEquals("users/edit",
        controller.addUserFromForm(session, user, result));
    assertFalse(user.getTextCaptchaQuestion().equals(captcha.getQuestion()));
    assertEquals(user.getTextCaptchaQuestion(),
        invalidUserCaptcha.getQuestion());
    assertNull(user.getCaptchaAnswer());
    assertFalse(captcha.equals((TextCaptcha) session
        .getAttribute("textCaptcha")));
    assertEquals(invalidUserCaptcha,
        (TextCaptcha) session.getAttribute("textCaptcha"));
    verify(userServiceMock).doesEmailExist(user.getEmail());
    verify(captchaServiceMock).getTextCaptcha();
  }
  
  @Test
  public void testAddUserFromForm_IncorrectCaptchaAnswer() throws Exception {
    TextCaptcha captcha = new TextCaptcha("What is Chris' name?",
        "6b34fe24ac2ff8103f6fce1f0da2ef57");
    TextCaptcha incorrectAnswerCaptcha = new TextCaptcha(
        "What is eighteen thousand three hundred and fourteen as a number?",
        "eb98676e8ee16adce38796051a5cc7ff");
    User user = new User();
    user.setUsername("testUsername");
    user.setPassword("testPassword");
    user.setConfirmPassword("testPassword");
    user.setEmail("test@test.com");
    user.setAcceptTerms(true);
    user.setCaptchaAnswer("badAnswer");
    UserService userServiceMock = mock(UserService.class);
    when(userServiceMock.doesEmailExist(user.getEmail())).thenReturn(false);
    when(userServiceMock.doesUsernameExist(user.getUsername())).thenReturn(
        false);
    BannedWordService bannedWordServiceMock = mock(BannedWordService.class);
    when(bannedWordServiceMock.doesStringContainBannedWord(user.getUsername()))
        .thenReturn(false);
    CaptchaService captchaServiceMock = mock(TextCaptchaServiceImpl.class);
    when(captchaServiceMock.getTextCaptcha()).thenReturn(incorrectAnswerCaptcha);
    UserValidator userValidator = new UserValidator();
    userValidator.setUserService(userServiceMock);
    userValidator.setBannedWordService(bannedWordServiceMock);
    HttpSession session = new MockHttpSession();
    session.setAttribute("textCaptcha", captcha);
    UserController controller = new UserController();
    controller.setUserService(userServiceMock);
    controller.setUserValidator(userValidator);
    controller.setCaptchaService(captchaServiceMock);
    BindingResult result = new BeanPropertyBindingResult(user, "user");
    assertEquals("users/edit",
        controller.addUserFromForm(session, user, result));
    assertFalse(user.getTextCaptchaQuestion().equals(captcha.getQuestion()));
    assertEquals(user.getTextCaptchaQuestion(),
        incorrectAnswerCaptcha.getQuestion());
    assertNull(user.getCaptchaAnswer());
    assertFalse(captcha.equals((TextCaptcha) session
        .getAttribute("textCaptcha")));
    assertEquals(incorrectAnswerCaptcha,
        (TextCaptcha) session.getAttribute("textCaptcha"));
    assertTrue(result.hasErrors());
    assertNotNull(result.getFieldError("captchaAnswer"));
    verify(userServiceMock).doesEmailExist(user.getEmail());
    verify(userServiceMock).doesUsernameExist(user.getUsername());
    verify(bannedWordServiceMock).doesStringContainBannedWord(
        user.getUsername());
    verify(captchaServiceMock).getTextCaptcha();
  }
}
