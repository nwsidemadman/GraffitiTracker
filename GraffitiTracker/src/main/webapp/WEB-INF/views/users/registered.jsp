<%@ taglib prefix="sec"
  uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>

<sec:authorize access="isAnonymous()">
  <div>
    <h2>Thanks you for registering!</h2>
    <p>You registered with the following information</p>
    <p>Username: ${param.username}</p>
    <p>Email: ${param.email}</p>
    <p>Please sign in!</p>
  </div>
</sec:authorize>

<sec:authorize access="isAuthenticated()">
  <div>
    <p>You are already a registered user.</p>
  </div>
</sec:authorize>
