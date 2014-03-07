<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="t" uri="http://tiles.apache.org/tags-tiles"%>
<html>
<head>
<title>GraffitiTracker</title>
<link href="<s:url value="/resources" />/css/info.css"
  rel="stylesheet" type="text/css" />
</head>

<body>
  <t:insertAttribute name="content" />
  <!--<co id="co_tile_content" />-->
</body>
</html>