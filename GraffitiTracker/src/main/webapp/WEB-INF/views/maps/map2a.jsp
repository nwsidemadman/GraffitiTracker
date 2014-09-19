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
          <sf:select multiple="true" path="status" >
            <sf:option value="NONE" label="" />
            <sf:option value="open" label="Open"/>
            <sf:option value="closed" label="Closed"/>
          </sf:select>
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
  </script>
</sec:authorize>