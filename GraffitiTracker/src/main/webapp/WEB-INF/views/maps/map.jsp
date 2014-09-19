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
  <div id="map-canvas"></div>
  <style>
    html, body, #map-canvas {
      height: 100%;
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

    <c:forEach var="item" items="${graffiti}">
    var marker = new google.maps.Marker({
        position: new google.maps.LatLng(<c:out value="${item.latitude }" />, <c:out value="${item.longitude }" />),
        map: map,
        title: '<c:out value="${item.address }" />'
    });
    </c:forEach>
  }
  google.maps.event.addDomListener(window, 'load', initialize);
  </script>
</sec:authorize>