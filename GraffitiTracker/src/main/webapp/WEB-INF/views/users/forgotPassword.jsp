<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
  uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ page session="false"%>
<sec:authorize access="isAnonymous()">
  <div>
    <h2>Forgot Password</h2>

    <sf:form method="POST" modelAttribute="usernameForm" >
      <fieldset> 
        <p>
          <sf:errors path="username" cssClass="error" /><br/>
          <sf:label path="username">Username:</sf:label>
          <sf:input path="username" size="6" maxlength="20"/> 
          <sf:hidden path="recoverPassword" />
        </p>
        <p>
          <input name="commit" type="submit" 
                    value="Recover Password" />
        </p>
      </fieldset>
    </sf:form>
  </div>
</sec:authorize>

<sec:authorize access="isAuthenticated()">
  <div id="simple_content_text">
    <p>You are already a registered user.</p>
  </div>
</sec:authorize>
