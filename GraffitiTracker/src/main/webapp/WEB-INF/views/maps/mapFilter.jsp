<%@ taglib prefix="sec"
  uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
  
<sec:authorize access="isAnonymous()">
  <div id="simple_content_text">
    <p>Not authorized to view this page</p>
  </div>
</sec:authorize>
<sec:authorize access="isAuthenticated()">
  <div id="simple_content_text"></div>
  <div id="map-canvas">
    <sf:form id="filterMap" method="POST" modelAttribute="mapForm" >
      <fieldset>
        <div>
          <sf:label path="status">Status: </sf:label>
          <sf:select multiple="true" path="status" size="3">
            <sf:option value="NONE" label="" />
            <sf:option value="'open'" label="Open"/>
            <sf:option value="'closed'" label="Closed"/>
          </sf:select>
        </div>
        <div id="date-range" class="selectbox">
          <p><sf:label path="startDate">Date Range:</sf:label> <span>Click to select range, default today</span> <i class="fa fa-calendar"></i></p>
          <sf:hidden path="startDate" id="startDate"/>
          <sf:hidden path="endDate" id="endDate"/>
        </div>
        <div>
          <input id="filterMapSubmit" name="commit" type="submit" value="Submit" />
        </div>
      </fieldset>
    </sf:form>
  </div>
  <div id="graffitiData">
    <table id="graffitiTable" class="display" cellspacing="0" width="100%">
      <thead>
        <tr>
          <th>Status</th>
          <th>Date</th>
          <th>Address</th>
          <th>Request</th>
          <th>Image</th>
        </tr>
      </thead>
      <tfoot>
        <tr>
          <th id="graffitiStatus">Status</th>
          <th id="graffitiRequestedDate">Date</th>
          <th id="graffitiAddress">Address</th>
        </tr>
      </tfoot>
      <tbody>
        <c:forEach var="item" items="${graffiti}">
          <tr>
            <td><c:out value="${item.status.displayString}" /></td>
            <td><fmt:formatDate value="${item.requestedDateTime}" pattern="yyyy-MM-dd" /></td>
            <td><c:out value="${item.address}" /></td>
            <td><a target="_blank" href="http://servicetracker.cityofchicago.org/requests/<c:out value="${item.serviceRequestId}" />">Request</a></td>
            <td><a target="_blank" href="<c:out value="${item.mediaUrl}" />">Image</a></td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
  </div>
  <script type="text/javascript">
  $(document).ready(function() {
    // define the search boxes to appear at bottom of datatable
    $('#graffitiTable tfoot #graffitiStatus').html('<select><option></option><option value="Open">Open</option><option value="Closed">Closed</option></select>');
    $('#graffitiTable tfoot #graffitiRequestedDate').html('<input type="text" placeholder="Search" />');
    $('#graffitiTable tfoot #graffitiAddress').html('<input type="text" placeholder="Search" />');
    
    // create the datatable
    var graffitiTableJObject = $('#graffitiTable').dataTable( {
      "sDom": '<"H"lr>t<"F"i>',
      "scrollX": false,
      "scrollY": "160px",
      "scrollCollapse": true,
      "paging": false,
      "aoColumnDefs": [
        { 'bSortable': false, 'aTargets': [ 3, 4 ] }
      ]
    });
    
    // Apply the search
    var graffitiTableDTObject = graffitiTableJObject.api();
    $( 'select', graffitiTableDTObject.column( 0 ).footer() ).on( 'change', function () {
      graffitiTableDTObject
            .column( 0 )
            .search( this.value )
            .draw();
    } );
    $( 'input', graffitiTableDTObject.column( 1 ).footer() ).on( 'keyup change', function () {
      graffitiTableDTObject
            .column( 1 )
            .search( this.value )
            .draw();
    } );
  });
  
  var today = new Date();
  $("#startDate").val(today.getTime());
  $("#endDate").val(today.getTime());
  
  //activate date range input
  $('#date-range').daterangepicker({
    ranges: {
      'Today': [new Date(), new Date()],
      'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
      'Last 7 Days': [moment().subtract(6, 'days'), new Date()],
      'Last 30 Days': [moment().subtract(29, 'days'), new Date()],
      'Month To Date': [moment().startOf('month'), new Date()],
      'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')],
      'Year To Date': [moment().startOf('year'), new Date()],
      'Last Year': [moment().startOf('year').subtract(1, 'year'), moment().endOf('year').subtract(1, 'year')]
    },
    opens: 'right',
    format: 'YYYY-MM-DD',
    startDate: today,
    endDate: today,
    maxDate: today,
  },
    
  function(start, end, label) {
    $('#date-range span').html(start.format('YYYY-MM-DD') + ' - ' + end.format('YYYY-MM-DD'));
    $("#startDate").val(start.getTime());
    $("#endDate").val(end.getTime());
  });
  </script>
</sec:authorize>