<%@ taglib prefix="sec"
  uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>

<sec:authorize access="isAnonymous()">
  <div id="simple_content_text">
    <h2>Thank you for registering!</h2>
    <p>You can not sign in until your registration is confirmed.</p>
    <p>You will receive an email shortly that contains a registration confirmation link.</p>
    <p>You MUST click on the link within 48 hours of registration to complete your registration.</p>
  </div>
</sec:authorize>

<sec:authorize access="isAuthenticated()">
  <div id="simple_content_text">
    <p>You are already a registered user.</p>
  </div>
</sec:authorize>
