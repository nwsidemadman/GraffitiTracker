<%@ taglib prefix="sec"
  uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
  
<sec:authorize access="!hasRole('ROLE_SUPERADMIN')">
  <div id="simple_content_text">
    <p>Not authorized to view this page</p>
  </div>
</sec:authorize>
<sec:authorize access="hasRole('ROLE_SUPERADMIN')">
  <div id="simple_content_text">
    OH HAI!
  </div>
</sec:authorize>