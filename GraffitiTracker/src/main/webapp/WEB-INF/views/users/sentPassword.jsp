<%@ taglib prefix="sec"
  uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>

<sec:authorize access="isAnonymous()">
  <div id="simple_content_text">
    <p>An email was sent to your registered email address containing instructions on how to reset your password.</p>
  </div>
</sec:authorize>

<sec:authorize access="isAuthenticated()">
  <div id="simple_content_text">
    <p>You are already a registered user.</p>
  </div>
</sec:authorize>
