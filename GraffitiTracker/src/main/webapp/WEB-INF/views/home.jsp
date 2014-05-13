<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
  uri="http://www.springframework.org/security/tags"%>
<%@ page session="false"%>
<sec:authorize access="isAnonymous()">
  <div id="simple_content_text">
    <h2>Welcome to GraffitiTracker</h2>
    <p>Options for non logged in users will go here</p>
  </div>
</sec:authorize>
<sec:authorize access="isAuthenticated()">
  <div id="simple_content_text">
    <h2>Welcome to GraffitiTracker</h2>
    <p>Options for logged in users will go here</p>
  </div>
</sec:authorize>