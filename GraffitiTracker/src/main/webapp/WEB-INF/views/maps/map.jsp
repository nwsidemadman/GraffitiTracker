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
  <div id="simple_content_text">
    <div id="map-canvas"></div>
  </div>
  <style>
    html, body, #map-canvas {
      height: 250px;
      margin: 0px;
      padding: 0px
    }
  </style>
  <script src="https://maps.googleapis.com/maps/api/js?v=3.exp"></script>
  <script>
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
  </script>
</sec:authorize>