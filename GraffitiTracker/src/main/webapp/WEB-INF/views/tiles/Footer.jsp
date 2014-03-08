<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<div id="info">
  <p>
    <a href="<s:url value="/contact" />" >Contact</a>
    &middot;
    <a href="<s:url value="/about" />" >About</a>
    &middot;
    <a href="<s:url value="/legal" />" >Legal</a>
    &middot;
    <a href="<s:url value="/privacy" />" >Privacy</a>
  </p>
</div>
<div id="copyright">
  <p>
    <script type="text/javascript">
      var d = new Date();
      document.write("&copy; " + d.getFullYear());
    </script>
    <span id="company_name">Whalecopter Productions</span>
  </p>
</div>