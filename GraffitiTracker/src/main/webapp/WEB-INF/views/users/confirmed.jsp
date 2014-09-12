<%@ taglib prefix="sec"
  uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>

<sec:authorize access="isAnonymous()">
  <div id="simple_content_text">
    <c:if test="${confirmed == true}">
      <p>Your registration is now complete.</p>
      <p>Please sign in!</p>
    </c:if>
    <c:if test="${confirmed == false}">
      <p>This registration link has expired, please register again.</p>
    </c:if>
  </div>
</sec:authorize>

<sec:authorize access="isAuthenticated()">
  <div id="simple_content_text">
    <p>You are already a registered user.</p>
  </div>
</sec:authorize>
