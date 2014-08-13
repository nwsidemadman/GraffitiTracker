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
    <a href="<c:out value="${userId}" />"><c:out value="${usersSharingInets.get(userId)}" /></a>
  </c:forEach>
<script type="text/javascript">
  $(document).ready(function() {
    // capture a click on the link
    $('#usersSharingIp a').click(function (e) {
      e.preventDefault();
      var aUrlPaths = this.href.split("/");
      var userId = aUrlPaths[aUrlPaths.length - 1];
      $.ajax({ 
        type: "GET",
        dataType: "html",
        url: '<s:url value="/users?userId=' + userId + '" />',
        success: function(data){
          $('#manageUser').html(data);
        },
      });
    });
  });
</script>
</sec:authorize>