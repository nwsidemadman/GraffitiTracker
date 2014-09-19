<%@ taglib prefix="sec"
  uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
  
<sec:authorize access="isAnonymous()">
  <div id="simple_content_text">
    <p>Not authorized to view this page</p>
  </div>
</sec:authorize>
<sec:authorize access="isAuthenticated()">
  <div id="simple_content_text"></div>
  <div id="map-canvas"></div>
  <div id="graffitiData">
    <p><input id="newMapSearch" type="button" value="New Search" /></p>
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
  function initialize() {
    var chicago = new google.maps.LatLng(41.8781136, -87.6297982);
    var mapOptions = {
      zoom: 11,
      center: chicago
    }
    var map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);

    var marker;
    var infowindow = new google.maps.InfoWindow();
    
    <c:forEach var="item" items="${graffiti}">
      marker = new google.maps.Marker({
          position: new google.maps.LatLng(<c:out value="${item.latitude}" />, <c:out value="${item.longitude}" />),
          map: map,
          icon: 'https://raw.githubusercontent.com/stucka/cheap_markers/master/images/small_red.png',
          title: '<c:out value="${item.address }" />'
      });
      google.maps.event.addListener(marker, 'click', (function(marker) {
        return function() {
          var contentString = '<div id="mapInfoWindow">';
          contentString += '<p>Status: <c:out value="${item.status.displayString}" /><p>';
          contentString += '<p>Address: <c:out value="${item.address}" /><p>';
          contentString += '<p>Date: <fmt:formatDate value="${item.requestedDateTime}" pattern="yyyy-MM-dd" /></p>';
          contentString += '<p><a target="_blank" href="http://servicetracker.cityofchicago.org/requests/<c:out value="${item.serviceRequestId}" />">Service Request</a></p>';
          contentString += '<p><a target="_blank" href="<c:out value="${item.mediaUrl}" />"><img src="<c:out value="${item.mediaUrl}" />" width="75" height="75"></a></p>';
          contentString += '</div>';
          infowindow.setContent(contentString);
          infowindow.open(map, marker);
        }
      })(marker));
    </c:forEach>
  }
  google.maps.event.addDomListener(window, 'load', initialize);
  
  $(document).ready(function() {
    // define the search boxes to appear at bottom of datatable
    $('#graffitiTable tfoot #graffitiStatus').html('<select><option></option><option value="Open">Open</option><option value="Closed">Closed</option></select>');
    $('#graffitiTable tfoot #graffitiRequestedDate').html('<input type="text" placeholder="Search" />');
    $('#graffitiTable tfoot #graffitiAddress').html('<input type="text" placeholder="Search" />');
    
    // create the datatable
    var graffitiTableJObject = $('#graffitiTable').dataTable( {
      "sDom": '<"H"lr>t<"F"i>',
      "scrollX": false,
      "scrollY": "135px",
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
    
    // handle new search
    $('#graffitiData').on('click', '#newMapSearch', function () {
      window.location.replace('<s:url value="/maps/map2" />')
    });
  });
  </script>
</sec:authorize>