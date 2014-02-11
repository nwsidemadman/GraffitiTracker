<%@ taglib prefix="sec"
  uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ page session="false"%>

<sec:authorize access="isAnonymous()">
  <div>
    <c:if test="${exists == true}">
      <h2>Reset Password</h2>
      <sf:form method="POST" modelAttribute="passwordSecurityForm" action="/GraffitiTracker/users">
        <fieldset>
          <sf:hidden path="resetPassword" />
          <sf:hidden path="userId" />
          <sf:hidden path="securityQuestion" />
          <p>
            <sf:errors path="securityAnswer" cssClass="error" /><br/>
            <sf:label path="securityAnswer"><c:out value="${passwordSecurityForm.getSecurityQuestion()}" /></sf:label>
            <sf:input path="securityAnswer" size="40" maxlength="40" />
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
            <input name="commit" type="submit" value="Reset Password" />
          </p>
        </fieldset>
      </sf:form>
    </c:if>
    <c:if test="${exists == false}">
      <p>This reset password link has expired, please register
        again.</p>
    </c:if>
  </div>
</sec:authorize>

<sec:authorize access="isAuthenticated()">
  <div>
    <p>You are already a registered user.</p>
  </div>
</sec:authorize>
