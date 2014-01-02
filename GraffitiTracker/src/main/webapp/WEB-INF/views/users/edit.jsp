<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
  uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ page session="false"%>
<sec:authorize access="isAnonymous()">
  <div>
    <h2>Create a Graffiti Tracker Account</h2>

    <sf:form method="POST" modelAttribute="user" >
      <fieldset> 
        <p>
          <sf:errors path="username" cssClass="error" /><br/>
          <sf:label path="username">Username:</sf:label>
          <sf:input path="username" size="20" maxlength="20" />
          <small id="username_msg">No spaces, please. 6 characters or more</small>
        </p>
        <p>
          <sf:errors path="password" cssClass="error" /><br/>
          <sf:label path="password">Password:</sf:label>
          <sf:password path="password" size="30" showPassword="false" maxlength="64"/> 
          <small>6 characters or more</small><br/>
        </p>
        <p>
          <sf:errors path="confirmPassword" cssClass="error" /><br/>
          <sf:label path="confirmPassword">Confirm Password:</sf:label>
          <sf:password path="confirmPassword" size="30" showPassword="false" maxlength="64"/> 
        </p>
        <p>
          <sf:errors path="email" cssClass="error" /><br/>
          <sf:label path="email">Email Address:</sf:label>
          <sf:input path="email" size="30" maxlength="100"/> 
        </p>
        <p>
          <sf:errors path="userCaptchaAnswer" cssClass="error" /><br/>
          <sf:label path="userCaptchaAnswer"><c:out value="${user.getTextCaptcha().getQuestion()}" /></sf:label>
          <sf:input path="userCaptchaAnswer" size="10" maxlength="20"/>
        </p>
        <p>
          <sf:errors path="acceptTerms" cssClass="error" /><br/>
          <sf:label path="acceptTerms">Accept Terms & Conditions:</sf:label>
          <sf:checkbox path="acceptTerms"/>
        </p>
        <sf:hidden path="textCaptcha" />
        <p>
          <input name="commit" type="submit" 
                    value="Create account" />
        </p>
      </fieldset>
    </sf:form>
  </div>
</sec:authorize>

<sec:authorize access="isAuthenticated()">
  <div>
    <p>You are already a registered user.</p>
  </div>
</sec:authorize>
