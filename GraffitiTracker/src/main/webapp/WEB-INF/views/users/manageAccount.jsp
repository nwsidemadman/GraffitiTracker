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
    <h2>Manage Account</h2>
    <p>Email: ${appUser.email}</p>
    <p>Security Question: ${appUser.securityQuestion}</p>
    <p>Security Answer: ${appUser.securityAnswer}</p>
    <p>Password: *****</p>
    <FORM METHOD="LINK" ACTION="<s:url value="/users/manageAccountEdit" />">
      <INPUT TYPE="submit" VALUE=" Edit ">
    </FORM>
  </div>
</sec:authorize>