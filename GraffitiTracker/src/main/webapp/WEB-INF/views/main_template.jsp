<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="t" uri="http://tiles.apache.org/tags-tiles"%>
<html>
<head>
<title>GraffitiTracker</title>

<link rel="stylesheet"
  href="https://code.jquery.com/ui/1.11.1/themes/smoothness/jquery-ui.css" />
<link
  href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css"
  rel="stylesheet" type="text/css">
<link rel="stylesheet"
  href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap-theme.min.css">
<link href="//cdn.datatables.net/1.10.2/css/jquery.dataTables.min.css"
  rel="stylesheet" type="text/css" />
  
<link href="<s:url value="/resources" />/css/daterangepicker-bs3.css"
  rel="stylesheet" type="text/css" />
  
<link href="<s:url value="/resources" />/css/graffitiTracker.css"
  rel="stylesheet" type="text/css" />

<script src="//code.jquery.com/jquery-2.1.1.min.js"></script>
<script src="//code.jquery.com/ui/1.11.1/jquery-ui.min.js"></script>
<script
  src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
<script src="//cdn.datatables.net/1.10.2/js/jquery.dataTables.min.js"></script>
<script
  src="//ajax.aspnetcdn.com/ajax/jquery.validate/1.13.0/jquery.validate.min.js"></script>
<script
  src="//ajax.aspnetcdn.com/ajax/jquery.validate/1.13.0/additional-methods.min.js"></script>
<script src="https://maps.googleapis.com/maps/api/js?v=3.exp"></script>

<script src="<s:url value="/resources" />/js/moment.min.js"></script>
<script src="<s:url value="/resources" />/js/daterangepicker.js"></script>

<script src="<s:url value="/resources" />/js/graffitiTracker.js"></script>
</head>

<body>
  <div id="container">
    <div id="header">
      <t:insertAttribute name="header" />
      <!--<co id="co_tile_header" />-->
    </div>
    <div id="side">
      <t:insertAttribute name="side" />
      <!--<co id="co_tile_side" />-->
    </div>
    <div id="actions">
      <t:insertAttribute name="actions" />
    </div>
    <div id="content">
      <t:insertAttribute name="content" />
      <!--<co id="co_tile_content" />-->
    </div>
  </div>
  <div id="footer">
    <t:insertAttribute name="footer" />
    <!--<co id="co_tile_footer" />-->
  </div>
  <script type="text/javascript"
    src="https://code.jquery.com/jquery-2.1.1.min.js"></script>
  <script type="text/javascript"
    src="https://cdn.datatables.net/1.10.2/js/jquery.dataTables.min.js"></script>
</body>
</html>