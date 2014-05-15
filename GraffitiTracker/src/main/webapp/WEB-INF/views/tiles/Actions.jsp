<%@ taglib prefix="sec"
  uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<sec:authorize access="isAnonymous()">
</sec:authorize>
<sec:authorize access="isAuthenticated()">
  <div id="inner_actions">
    <p>
      Upload Graffiti
      &middot;
      <a href="<s:url value="/users/manageAccount" />">Manage Account</a>
    </p>
  </div>
</sec:authorize>