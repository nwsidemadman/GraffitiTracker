<%@ taglib prefix="sec"
  uri="http://www.springframework.org/security/tags"%>
  
<sec:authorize access="!hasRole('ROLE_SUPERADMIN')">
  <div id="simple_content_text">
    <p>Not authorized to view this page</p>
  </div>
</sec:authorize>
<sec:authorize access="hasRole('ROLE_SUPERADMIN')">
  <div id="simple_content_text">
    <p>System is getting data from City Services and storing to repo.</p>
    <p>Email notification will be sent when complete, including stats.</p>
  </div>
</sec:authorize>