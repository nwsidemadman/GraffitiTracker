<%@ taglib prefix="sec"
  uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<sec:authorize access="isAnonymous()">
</sec:authorize>
<sec:authorize access="isAuthenticated()">
  <div id="inner_actions">
    <p>
      <a href="<s:url value="/maps/map" />">Map</a>
      &middot;
      <a href="<s:url value="/users/manageAccount" />">Manage Account</a>
      <sec:authorize access="hasRole('ROLE_SUPERADMIN')">
        &middot;
        <a href="<s:url value="/users/manageUsers" />">Manage Users</a>
      </sec:authorize>
      <sec:authorize access="hasRole('ROLE_SUPERADMIN')">
        &middot;
        <a href="<s:url value="/banned_inets" />">Banned IPs</a>
      </sec:authorize>
    </p>
  </div>
</sec:authorize>