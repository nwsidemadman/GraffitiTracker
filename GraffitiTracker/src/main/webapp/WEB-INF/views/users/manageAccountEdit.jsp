<%@ taglib prefix="sec"
  uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<sec:authorize access="isAnonymous()">
  <div id="simple_content_text">
    <p>You must sign in to view this page.</p>
  </div>
</sec:authorize>
<sec:authorize access="isAuthenticated()">
  <div id="simple_content_text">
    <h2>Manage Account Edit</h2>
    <p>Email: ${appUser.getEmail()}</p>
    <p>Security Question: ${appUser.getSecurityQuestion()}</p>
    <p>Security Answer: ${appUser.getSecurityAnswer()}</p>
    <p>Password: *****</p>
  </div>
</sec:authorize>