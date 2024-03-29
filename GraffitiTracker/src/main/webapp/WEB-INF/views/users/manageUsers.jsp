<%@ taglib prefix="sec"
  uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<sec:authorize access="!hasRole('ROLE_SUPERADMIN')">
  <div id="simple_content_text">
    <p>Not authorized to view this page</p>
  </div>
</sec:authorize>
<sec:authorize access="hasRole('ROLE_SUPERADMIN')">
  <div id="manageUser">
    <p align="center" style="height: 170px; line-height: 170px;">Click a row to select a user from the table below</p>
  </div>
  <div id="manageUsers">
    <table id="usersTable" class="display" cellspacing="0" width="100%">
      <thead>
        <tr align="left">
          <th>Username</th>
          <th>Active</th>
          <th>Roles</th>
          <th>Registered</th>
          <th>Last Login</th>
          <th>Logins</th>
        </tr>
      </thead>
      <tfoot>
        <tr align="left">
          <th id="username">Username</th>
          <th id="active">Active</th>
          <th id="roles">Roles</th>
          <th id="registered">Registered</th>
          <th id="lastLogin">Last Login</th>
          <th id="logins">Logins</th>
        </tr>
      </tfoot>
    </table>
    <script type="text/javascript">
    $(document).ready(function() {
      // define the search boxes to appear at bottom of datatable
      $('#usersTable tfoot #username').html('<input type="text" placeholder="Search" />');
      $('#usersTable tfoot #active').html('<select><option></option><option value="Yes">Yes</option><option value="No">No</option></select>');
      $('#usersTable tfoot #roles').html('<select><option></option><option value="Super Admin">Super Admin</option><option value="Admin">Admin</option><option value="Basic">Basic</option><option value="Supscription">Subscription</option><option value="Licensed">Licensed</option><option value="Trial">Trial</option></select>');
      $('#usersTable tfoot #registered').html('<input type="text" placeholder="Search" />');
      $('#usersTable tfoot #lastLogin').html('<input type="text" placeholder="Search" />');
      $('#usersTable tfoot #logins').html('<input type="text" placeholder="Search" />');
      
      // create the datatable
      var usersTableJObject = $('#usersTable').dataTable( {
          "sDom": '<"H"lr>t<"F"i>',
          "scrollX": false,
          "scrollY": "238px",
          "scrollCollapse": true,
          "paging": false,
          "ajax": "<s:url value="/api/users" />",
          "columns": [
              { "data": "username" },
              { "data": "isActive",
                "mRender": function ( oObj ) {
                  if (oObj == true) {
                    return "Yes";
                  } else {
                    return "No";
                  }
                }
              },
              { "data": "roles[<br>].role",
                "mRender": function( oObj ) {
                  value = oObj.toLowerCase();
                  values = value.split("<br>");
                  for (var i = 0; i < values.length; i++) {
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
              { "data": "currentLoginTimestamp",
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
      
      // capture a click on the datatable
      $('#usersTable tbody').on('click', 'tr', function () {
        var aData = usersTableJObject.fnGetData(this);
        $.ajax({ 
          type: "GET",
          dataType: "html",
          url: '<s:url value="/users?userId=' + aData.userId + '" />',
          success: function(data){
            $('#manageUser').html(data);
          }
        });
      });

      // Apply the search
      var usersTableDTObject = usersTableJObject.api();
      $( 'input', usersTableDTObject.column( 0 ).footer() ).on( 'keyup change', function () {
        usersTableDTObject
              .column( 0 )
              .search( this.value )
              .draw();
      } );
      $( 'select', usersTableDTObject.column( 1 ).footer() ).on( 'change', function () {
        usersTableDTObject
              .column( 1 )
              .search( this.value )
              .draw();
      } );
      $( 'select', usersTableDTObject.column( 2 ).footer() ).on( 'change', function () {
        usersTableDTObject
              .column( 2 )
              .search( this.value )
              .draw();
      } );
      $( 'input', usersTableDTObject.column( 3 ).footer() ).on( 'keyup change', function () {
        usersTableDTObject
              .column( 3 )
              .search( this.value )
              .draw();
      } );
      $( 'input', usersTableDTObject.column( 4 ).footer() ).on( 'keyup change', function () {
        usersTableDTObject
              .column( 4 )
              .search( this.value )
              .draw();
      } );
      $( 'input', usersTableDTObject.column( 5 ).footer() ).on( 'keyup change', function () {
        usersTableDTObject
              .column( 5 )
              .search( this.value )
              .draw();
      } );
    });
    </script>
  </div>
</sec:authorize>