<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
  uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ page import="net.ccaper.graffitiTracker.objects.SecurityQuestions" %>
<%@ page session="false"%>
<sec:authorize access="isAnonymous()">
  <div id="createAccount">
    <h2>Create Account</h2>
    <sf:form method="POST" modelAttribute="userForm" >
      <fieldset> 
        <div>
          <sf:errors path="username" cssClass="error" /><br/>
          <sf:label path="username">Username:</sf:label>
          <sf:input path="username" size="20" maxlength="20" />
          <small>No spaces, please. 6 characters or more</small>
        </div>
        <div>
          <sf:errors path="password" cssClass="error" /><br/>
          <sf:label path="password">Password:</sf:label>
          <sf:password path="password" size="30" showPassword="false" maxlength="64"/> 
          <small>6 characters or more</small><br/>
        </div>
        <div>
          <sf:errors path="confirmPassword" cssClass="error" /><br/>
          <sf:label path="confirmPassword">Confirm Password:</sf:label>
          <sf:password path="confirmPassword" size="30" showPassword="false" maxlength="64"/> 
        </div>
        <div>
          <sf:errors path="email" cssClass="error" /><br/>
          <sf:label path="email">Email Address:</sf:label>
          <sf:input path="email" size="30" maxlength="100"/> 
        </div>
        <div>
          <sf:errors path="securityQuestion" cssClass="error" /><br/>
          <sf:label path="securityQuestion">Security Question:</sf:label>
        </div>
        <div>
          <sf:select path="securityQuestion" items="<%= SecurityQuestions.QUESTIONS %>" />
        </div>
        <div>
          <sf:errors path="securityAnswer" cssClass="error" /><br/>
          <sf:label path="securityAnswer">Security Answer:</sf:label>
          <sf:input path="securityAnswer" size="30" maxlength="40"/> 
        </div>
        <div>
          <sf:errors path="acceptTerms" cssClass="error" /><br/>
          <sf:label path="acceptTerms"><a href="#" onclick="window.open('termsAndConditions', 'newwindow', 'width=275, height=135'); return false;">Accept Terms &amp; Conditions</a>:</sf:label>
          <sf:checkbox path="acceptTerms"/>
        </div>
        <div id="captcha_question">
          <sf:errors path="captchaAnswer" cssClass="error" /><br/>
          <sf:label path="captchaAnswer"><c:out value="${userForm.textCaptchaQuestion}" /></sf:label>
        </div>
        <div>
          <sf:input path="captchaAnswer" size="10" maxlength="20"/>
        </div>
        <div>
          <input name="commit" type="submit" 
                    value="Create account" />
        </div>
      </fieldset>
    </sf:form>
  </div>
</sec:authorize>

<sec:authorize access="isAuthenticated()">
  <div id="simple_content_text">
    <p>You are already a registered user.</p>
  </div>
</sec:authorize>
