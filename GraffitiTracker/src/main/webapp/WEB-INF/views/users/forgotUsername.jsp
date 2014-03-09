<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
  uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ page session="false"%>
<sec:authorize access="isAnonymous()">
  <div>
    <h2>Forgot Username</h2>

    <sf:form method="POST" modelAttribute="emailForm" >
      <fieldset> 
        <p>
          <sf:errors path="email" cssClass="error" /><br/>
          <sf:label path="email">Email address used on account registration:</sf:label>
          <sf:input path="email" size="30" maxlength="100"/> 
          <sf:hidden path="recoverUsername" />
        </p>
        <p>
          <input name="commit" type="submit" 
                    value="Recover Username" />
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
