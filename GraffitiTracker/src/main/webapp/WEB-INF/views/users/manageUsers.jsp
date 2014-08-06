<%@ taglib prefix="sec"
  uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<sec:authorize access="isAnonymous()">
  <div id="simple_content_text">
    <p>You must sign in to view this page.</p>
  </div>
</sec:authorize>
<sec:authorize access="isAuthenticated()">
  <div id="manageUser">
    <p>User Edit Here</p>
  </div>
  <div id="manageUsers">
    <table id="usersTable" class="display" cellspacing="0" width="100%">
        <thead>
            <tr>
                <th>Username</th>
                <th>Active</th>
                <th>Roles</th>
                <th>Registered</th>
                <th>Last Login</th>
                <th>Number Logins</th>
            </tr>
        </thead>
 
        <tfoot>
            <tr>
                <th>Username</th>
                <th>Active</th>
                <th>Roles</th>
                <th>Registered</th>
                <th>Last Login</th>
                <th>Number Logins</th>
            </tr>
        </tfoot>
    </table>
    <script type="text/javascript">
    $(document).ready(function() {
      $('#usersTable').dataTable( {
          "ajax": "<s:url value="/api/users" />",
          "columns": [
              { "data": "username" },
              { "data": "isActive"},
              { "data": "roles[<br>].role" },
              { "data": "registerTimestamp" },
              { "data": "previousLoginTimestamp" },
              { "data": "loginCount" }
          ]
      } );
  } );
    </script>
  </div>
</sec:authorize>