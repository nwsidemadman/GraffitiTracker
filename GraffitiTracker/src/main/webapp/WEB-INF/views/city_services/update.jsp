<%@ taglib prefix="sec"
  uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
  
<sec:authorize access="!hasRole('ROLE_SUPERADMIN')">
  <div id="simple_content_text">
    <p>Not authorized to view this page</p>
  </div>
</sec:authorize>
<sec:authorize access="hasRole('ROLE_SUPERADMIN')">
  <div id="simple_content_text">
    <p>Update City Service Data</p>
    <sf:form id="updateCityService" method="POST" modelAttribute="cityServiceUpdateForm" >
      <fieldset>
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
  <script type="text/javascript">
  var today = new Date();
  $("#startDate").val(moment().startOf('day').valueOf());
  $("#endDate").val(moment().endOf('day').valueOf());
  
  //activate date range input
  $('#date-range').daterangepicker({
    ranges: {
      'Today': [moment().startOf('day'), moment().endOf('day')],
      'Last 2 Days': [moment().subtract(2, 'days'), moment().endOf('day')],
      'Last 7 Days': [moment().subtract(6, 'days'), moment().endOf('day')],
      'Last 30 Days': [moment().subtract(29, 'days'), moment().endOf('day')],
      'Year To Date': [moment().startOf('year'), moment().endOf('day')],
      'Everything': [0, 0]
    },
    opens: 'right',
    format: 'YYYY-MM-DD',
    startDate: moment().startOf('day'),
    endDate: moment().endOf('day'),
    maxDate: today,
  },
    
  function(start, end, label) {
    $('#date-range span').html(start.format('YYYY-MM-DD') + ' - ' + end.format('YYYY-MM-DD'));
    $("#startDate").val(start.valueOf());
    $("#endDate").val(end.valueOf());
  });
  </script>
</sec:authorize>