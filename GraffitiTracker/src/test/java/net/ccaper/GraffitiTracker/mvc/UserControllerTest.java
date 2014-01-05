package net.ccaper.GraffitiTracker.mvc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpSession;

import net.ccaper.GraffitiTracker.objects.TextCaptcha;
import net.ccaper.GraffitiTracker.objects.User;
import net.ccaper.GraffitiTracker.service.CaptchaService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

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
}
