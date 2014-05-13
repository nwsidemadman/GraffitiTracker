<%@ taglib prefix="sec"
  uri="http://www.springframework.org/security/tags"%>
<sec:authorize access="isAnonymous()">
</sec:authorize>
<sec:authorize access="isAuthenticated()">
  <p>
    Upload Graffiti
    &middot;
    Manage Account
  </p>
</sec:authorize>