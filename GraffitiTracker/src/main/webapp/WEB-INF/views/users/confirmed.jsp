<%@ taglib prefix="sec"
  uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>

<sec:authorize access="isAnonymous()">
  <div>
    <c:if test="${confirmed == true}">
      <h2>Your registration is now complete.</h2>
      <p>Please sign in!</p>
    </c:if>
    <c:if test="${confirmed == false}">
      <h2>This registration link has expired, please register again.</h2>
    </c:if>
  </div>
</sec:authorize>

<sec:authorize access="isAuthenticated()">
  <div>
    <p>You are already a registered user.</p>
  </div>
</sec:authorize>
