<%@ taglib prefix="sec"
  uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<sec:authorize access="isAnonymous()">
  <div id="simple_content_text">
    <p>You must sign in to view this page.</p>
  </div>
</sec:authorize>
<sec:authorize access="isAuthenticated()">
  <div id="manageUser">
    <p>User Edit Here</p>
  </div>
  <div id="manageUsers">
    <p>List Users Here</p>
  </div>
</sec:authorize>