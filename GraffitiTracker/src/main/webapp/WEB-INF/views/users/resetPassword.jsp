<%@ taglib prefix="sec"
  uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>

<sec:authorize access="isAnonymous()">
  <div>
    <c:if test="${exists == true}">
      <h2>Resetting password will go here.</h2>
    </c:if>
    <c:if test="${exists == false}">
      <h2>This reset password link has expired, please register again.</h2>
    </c:if>
  </div>
</sec:authorize>

<sec:authorize access="isAuthenticated()">
  <div>
    <p>You are already a registered user.</p>
  </div>
</sec:authorize>
