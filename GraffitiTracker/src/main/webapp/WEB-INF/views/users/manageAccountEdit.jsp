<%@ taglib prefix="sec"
  uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ page import="net.ccaper.GraffitiTracker.objects.SecurityQuestions" %>
<%@ page session="false"%>
<sec:authorize access="isAnonymous()">
  <div id="simple_content_text">
    <p>You must sign in to view this page.</p>
  </div>
</sec:authorize>
<sec:authorize access="isAuthenticated()">
  <div id="simple_content_text">
    <h2>Manage Account Edit</h2>
    <p>Only edit fields that should change.</p>
    <sf:form method = "POST" modelAttribute="manageAccountForm" >
      <fieldset> 
          <p>
            <sf:errors path="email" cssClass="error" /><br/>
            <sf:label path="email">Email Address:</sf:label>
            <sf:input path="email" size="30" maxlength="100" />
          </p>
          <p>
            <sf:errors path="securityQuestion" cssClass="error" /><br/>
            <sf:label path="securityQuestion">Security Question:</sf:label>
          </p>
          <p>
            <sf:select path="securityQuestion" items="<%= SecurityQuestions.QUESTIONS %>" />
          </p>
          <p>
            <sf:errors path="securityAnswer" cssClass="error" /><br/>
            <sf:label path="securityAnswer">Security Answer:</sf:label>
            <sf:input path="securityAnswer" size="30" maxlength="40"/> 
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
            <input name="commit" type="submit" 
                      value="Edit Account" />
          </p>
      </fieldset>
    </sf:form>
  </div>
</sec:authorize>