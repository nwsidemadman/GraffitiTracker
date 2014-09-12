<%@ taglib prefix="sec"
  uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
  
<sec:authorize access="!hasRole('ROLE_SUPERADMIN')">
  <div id="simple_content_text">
    <p>Not authorized to view this page</p>
  </div>
</sec:authorize>
<sec:authorize access="hasRole('ROLE_SUPERADMIN')">
  <div id="editBannedIp">
    <p align="center" style="height: 170px; line-height: 170px;">Click a row to select a banned IP from the table below</p>
  </div>
  <div id="bannedIps">
    <div id="createNewBannedInet">
      <input id="createNewBannedInetButton" type="button" value="Create New Banned IP" />
    </div>
    <div>
      <table id="bannedInetsTable" class="display" cellspacing="0" width="100%">
          <thead>
              <tr align="left">
                  <th>Min IP</th>
                  <th>Max IP</th>
                  <th>Active</th>
                  <th>Attempts</th>
                  <th>Created</th>
                  <th>Notes</th>
              </tr>
          </thead>
          <tfoot>
              <tr align="left">
                  <th id="minIp">Min IP</th>
                  <th id="maxIp">Max IP</th>
                  <th id="active">Active</th>
                  <th id="attempts">Attempts</th>
                  <th id="created">Created</th>
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
        $('#bannedInetsTable tfoot #attempts').html('<input type="text" placeholder="Search" />');
        $('#bannedInetsTable tfoot #created').html('<input type="text" placeholder="Search" />');
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
                { "data" : "createdTimestamp",
                  "mRender": function ( oObj ) {
                    if (oObj == null) {
                      return "N/A";
                    }
                    return dateFormat_yyyymmdd(oObj);
                  }
                },
                { "data": "notes"}
            ]
        });
        
        // capture a click on the datatable
        $('#bannedInetsTable tbody').on('click', 'tr', function () {
          var aData = bannedInetsTableJObject.fnGetData(this);
          delete aData.createdTimestamp;
          $.ajax({ 
            type: "GET",
            dataType: "html",
            url: '<s:url value="/banned_inets/editCreateBannedInet?' + $.param(aData) + '" />',
            success: function(data){
              $('#editBannedIp').html(data);
            }
          });
        });
        
        // capture a click on create banned ip button
        $('#createNewBannedInet').on('click', 'input', function () {
          $.ajax({ 
            type: "GET",
            dataType: "html",
            url: '<s:url value="/banned_inets/editCreateBannedInet" />',
            success: function(data){
              $('#editBannedIp').html(data);
            }
          });
        });
      });
      </script>
    </div>
  </div>
</sec:authorize>