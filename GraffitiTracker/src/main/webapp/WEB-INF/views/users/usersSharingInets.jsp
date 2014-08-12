<%@ taglib prefix="sec"
  uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<sec:authorize access="!hasRole('ROLE_SUPERADMIN')">
  <p>Not authorized to view this pane</p>
</sec:authorize>
<sec:authorize access="hasRole('ROLE_SUPERADMIN')">
  <c:forEach var="userId" items="${usersSharingInets.keySet()}">
    <c:out value="${usersSharingInets.get(userId)}(${userId}) " />
  </c:forEach>
</script>
</sec:authorize>