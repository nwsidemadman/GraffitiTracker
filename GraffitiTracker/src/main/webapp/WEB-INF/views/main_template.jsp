<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="t" uri="http://tiles.apache.org/tags-tiles"%>
<html>
<head>
<title>GraffitiTracker</title>

<link href="//cdn.datatables.net/1.10.2/css/jquery.dataTables.min.css"
  rel="stylesheet" type="text/css" />
<link href="<s:url value="/resources" />/css/graffitiTracker.css"
  rel="stylesheet" type="text/css" />
  
<script src="//code.jquery.com/jquery-2.1.1.min.js"></script>
<script src="//cdn.datatables.net/1.10.2/js/jquery.dataTables.min.js"></script>
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