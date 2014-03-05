<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="t" uri="http://tiles.apache.org/tags-tiles"%>
<html>
<head>
<title>GraffitiTracker</title>
<link href="<s:url value="/resources" />/css/graffitiTracker.css"
  rel="stylesheet" type="text/css" />
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
    <div id="content">
      <t:insertAttribute name="content" />
      <!--<co id="co_tile_content" />-->
    </div>
  </div>
  <div id="footer">
    <t:insertAttribute name="footer" />
    <!--<co id="co_tile_footer" />-->
  </div>
</body>
</html>