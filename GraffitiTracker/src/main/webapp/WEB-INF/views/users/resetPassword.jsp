<%@ taglib prefix="sec"
  uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ page session="false"%>

<sec:authorize access="isAnonymous()">
  <c:set var="actionValue" value="${contextPath}/users/forgotPassword/resetPassword" />
  <div id="resetPassword">
    <c:if test="${exists == true}">
      <h2>Reset Password</h2>
      <sf:form method="POST" modelAttribute="passwordSecurityForm" action="${actionValue}">
        <fieldset>
          <sf:hidden path="userid" />
          <sf:hidden path="securityQuestion" />
          <div>
            <sf:errors path="securityAnswer" cssClass="error" /><br/>
            <sf:label path="securityAnswer"><c:out value="${passwordSecurityForm.securityQuestion}" /></sf:label>
            <sf:input path="securityAnswer" size="40" maxlength="40" />
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
            <input name="commit" type="submit" value="Reset Password" />
          </div>
        </fieldset>
      </sf:form>
    </c:if>
    <c:if test="${exists == false}">
      <p>This reset password link has expired, please click Forgot Password again.</p>
    </c:if>
  </div>
</sec:authorize>
<sec:authorize access="isAuthenticated()">
  <div id="simple_content_text">
    <p>You are already a registered user.</p>
  </div>
</sec:authorize>
