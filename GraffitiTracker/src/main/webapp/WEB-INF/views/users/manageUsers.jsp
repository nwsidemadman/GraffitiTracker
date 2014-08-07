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
    <p align="center" style="height: 170px; line-height: 170px;">Select user to edit from list below</p>
  </div>
  <div id="manageUsers">
    <table id="usersTable" class="display" cellspacing="0" width="100%">
        <thead>
            <tr>
                <th>Username</th>
                <th>Status</th>
                <th>Roles</th>
                <th>Registered</th>
                <th>Last Login</th>
                <th>Number Logins</th>
            </tr>
        </thead>
    </table>
    <script type="text/javascript">
    $(document).ready(function() {
      $('#usersTable').dataTable( {
          "scrollX": false,
          "scrollY": "400px",
          "scrollCollapse": true,
          "paging": false,
          "ajax": "<s:url value="/api/users" />",
          "columns": [
              { "data": "username" },
              { "data": "isActive",
                "mRender": function ( oObj ) {
                  if (oObj == true) {
                    return "Enabled";
                  } else {
                    return "Disabled";
                  }
                }
              },
              { "data": "roles[<br>].role",
                "mRender": function( oObj ) {
                  value = oObj.toLowerCase();
                  values = value.split("<br>");
                  for (i = 0; i < values.length; i++) {
                    if (values[i] == 'superadmin') {
                      values[i] = 'Super Admin';
                    } else {
                      values[i] = values[i].charAt(0).toUpperCase() + values[i].slice(1);
                    }
                  }
                  return values.join("<br>");
                }
              },
              { "data": "registerTimestamp",
                "mRender": function ( oObj ) {
                  return dateFormat_yyyymmdd(oObj);
                }
              },
              { "data": "previousLoginTimestamp",
                "mRender": function ( oObj ) {
                  if (oObj == null) {
                    return "N/A";
                  }
                  return dateFormat_yyyymmdd(oObj);
                }
              },
              { "data": "loginCount" }
          ]
      });
    });
    </script>
  </div>
</sec:authorize>