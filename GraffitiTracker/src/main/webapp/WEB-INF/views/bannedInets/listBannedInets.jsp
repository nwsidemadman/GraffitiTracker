<%@ taglib prefix="sec"
  uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
  
<sec:authorize access="!hasRole('ROLE_SUPERADMIN')">
  <div id="simple_content_text">
    <p>Not authorized to view this page</p>
  </div>
</sec:authorize>
<sec:authorize access="hasRole('ROLE_SUPERADMIN')">
  <div id="listBannedInets">
    <table id="bannedInetsTable" class="display" cellspacing="0" width="100%">
        <thead>
            <tr align="left">
                <th>Min IP</th>
                <th>Max IP</th>
                <th>Active</th>
                <th>Registration Attempts</th>
                <th>Notes</th>
            </tr>
        </thead>
        <tfoot>
            <tr align="left">
                <th id="minIp">Min IP</th>
                <th id="maxIp">Max IP</th>
                <th id="Active">Active</th>
                <th id="regAttempts">Registration Attemps</th>
                <th id="notes">Notes</th>
            </tr>
        </tfoot>
    </table>
    
    <script type="text/javascript">
    $(document).ready(function() {
      // define the search boxes to appear at bottom of datatable
      $('#bannedInetsTable tfoot #minIp').html('<input type="text" placeholder="Search" />');
      $('#bannedInetsTable tfoot #maxIp').html('<input type="text" placeholder="Search" />');
      $('#bannedInetsTable tfoot #active').html('<select><option></option><option value="Yes">Yes</option><option value="No">No</option></select>');
      $('#bannedInetsTable tfoot #regAttempts').html('<input type="text" placeholder="Search" />');
      $('#bannedInetsTable tfoot #notes').html('<input type="text" placeholder="Search" />');
      
      // create the datatable
      var bannedInetsTableJObject = $('#bannedInetsTable').dataTable( {
          "sDom": '<"H"lr>t<"F"i>',
          "scrollX": false,
          "scrollY": "238px",
          "scrollCollapse": true,
          "paging": false,
          "ajax": "<s:url value="/api/banned_inets" />",
          "columns": [
              { "data": "inetMinIncl" },
              { "data": "inetMaxIncl" },
              { "data": "isActive",
                "mRender": function ( oObj ) {
                  if (oObj == true) {
                    return "Yes";
                  } else {
                    return "No";
                  }
                }
              },
              { "data": "numberRegistrationAttempts"},
              { "data": "notes"}
          ]
      });
      
    });
    </script>
  </div>
</sec:authorize>