<%@ taglib prefix="sec"
  uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<sec:authorize access="!hasRole('ROLE_SUPERADMIN')">
  <p>Not authorized to view this pane</p>
</sec:authorize>
<sec:authorize access="hasRole('ROLE_SUPERADMIN')">
<table width="100%">
  <tr>
    <td width="50%">
      <table width="100%">
        <tr><td>Username:</td><td>${appUser.getUsername()}</td></tr>
        <tr>
          <td>Roles:<td>
          <td>
            <c:forEach var="role" items="${appUser.getRoles()}">
              <fmt:formatDate value="${role.getGrantedTimestamp()}" pattern="yyyy-MM-dd" var="grantedDate"/>
              <c:out value="${role.getRole().getDisplayString()} (${grantedDate})" /><br>
            </c:forEach>
          </td>
        </tr>
        <tr><td>Email:</td><td>${appUser.getEmail()}</td></tr>
        <tr><td>Active:</td><td>${appUser.getIsActive() ? "Yes" : "No"}</td></tr>
        <tr><td>Registered:</td><td><fmt:formatDate value="${appUser.getRegisterTimestamp()}" pattern="yyyy-MM-dd" /></td></tr>
        <tr><td>Last Login:</td><td><fmt:formatDate value="${appUser.getPreviousLoginTimestamp()}" pattern="yyyy-MM-dd" /></td></tr>
        <tr><td>Login Count:</td><td>${appUser.getLoginCount() }</td></tr>
      </table>
    </td>
    <td width="50%">
      <table id="userLoginsTable" class="display" cellspacing="0" width="100%">
        <thead>
          <tr>
            <th>IP</th>
            <th>Visits</th>
            <th>Last Visit</th>
          </tr>
        </thead>
        <tfoot>
          <tr>
            <th id="ip">IP</th>
            <th>Visits</th>
            <th>Last Visit</th>
          </tr>
        </tfoot>
      <table>
      </table>
    </td>
  </tr>
</table>
<script type="text/javascript">
  $(document).ready(function() {
    // create the datatable
    var userLoginsTableJObject = $('#userLoginsTable').dataTable( {
      "sDom": '<"H"lr>t<"F">',
      "scrollX": false,
      "scrollY": "400px",
      "scrollCollapse": true,
      "paging": false,
      "ajax": "<s:url value="/api/users/${appUser.getUserId()}/logins" />",
      "columns": [
        {"data": "inet"},
        {"data": "numberVisits"},
        {"data": "lastVisit",
          "mRender": function ( oObj ) {
            return dateFormat_yyyymmdd(oObj);
          }
        }
      ]
    });
  });
</script>
</sec:authorize>