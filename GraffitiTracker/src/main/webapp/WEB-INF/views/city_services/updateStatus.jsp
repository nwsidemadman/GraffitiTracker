<%@ taglib prefix="sec"
  uri="http://www.springframework.org/security/tags"%>
  
<sec:authorize access="!hasRole('ROLE_SUPERADMIN')">
  <div id="simple_content_text">
    <p>Not authorized to view this page</p>
  </div>
</sec:authorize>
<sec:authorize access="hasRole('ROLE_SUPERADMIN')">
  <div id="simple_content_text">
    <p>${dataSizeServer} records were found on city services server.</p>
    <p>${dataSizeStored} records had images and were stored on system.</p>
  </div>
</sec:authorize>