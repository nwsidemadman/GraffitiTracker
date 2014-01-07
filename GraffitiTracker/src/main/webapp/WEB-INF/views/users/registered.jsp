<%@ taglib prefix="sec"
  uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>

<sec:authorize access="isAnonymous()">
  <div>
    <h2>User Registered</h2>
    <p>Username: userForm.getUsername()</p>
    <p>Email: userForm.getEmail()</p>
    <p>Please sign in!</p>P
  </div>
</sec:authorize>

<sec:authorize access="isAuthenticated()">
  <div>
    <p>You are already a registered user.</p>
  </div>
</sec:authorize>
