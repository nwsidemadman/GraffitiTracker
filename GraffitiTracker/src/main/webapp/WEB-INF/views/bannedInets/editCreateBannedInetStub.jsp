<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec"
  uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>

<sec:authorize access="!hasRole('ROLE_SUPERADMIN')">
  <div id="simple_content_text">
    <p>Not authorized to view this page</p>
  </div>
</sec:authorize>
<sec:authorize access="hasRole('ROLE_SUPERADMIN')">
  <c:set var="actionValue" value="${contextPath}/banned_inets/editCreateBannedInet" />
  <div id="editCreateDeleteBannedInet">
    <div>Banned IP</div>
      <sf:form id="editCreateBannedInet" method="POST" modelAttribute="editedBannedInet" action="${actionValue}" >
        <fieldset>
          <div>
            <sf:errors path="inetMinIncl" cssClass="error" /><br/>
            <sf:label path="inetMinIncl">Min IP: </sf:label>
            <sf:input id="inetMinIncl" path="inetMinIncl" size="15" maxlength="15" />
          </div>
          <div>
            <sf:errors path="inetMaxIncl" cssClass="error" /><br/>
            <sf:label path="inetMaxIncl">Max IP: </sf:label>
            <sf:input id="inetMaxIncl" path="inetMaxIncl" size="15" maxlength="15" />
          </div>
          <div>
            <sf:label path="isActive">Active: </sf:label>
            <sf:radiobutton id="isActive" path="isActive" value="true" /> Yes
            <sf:radiobutton id="isActive" path="isActive" value="false" /> No
          </div>
          <div>
            <sf:label path="notes">Notes:</sf:label>
            <sf:input id="notes" path="notes" size="30" />
          </div>
          <div>
            <input id="bannedInetsSubmit" name="commit" type="submit" 
            value="${isNew == true ? "Create" : "Edit" }" />
            <c:if test="${isNew == false}">
              <input id="bannedInetDelete" name="delete" type="button" value="Delete" />
            </c:if>
          </div>
        </fieldset>
      </sf:form>
    </div>
    <script type="text/javascript">
    $(document).ready(function() {
      // handle editing banned inet
      $('#editCreateBannedInet').submit(function (e) {
        e.preventDefault();
        var editedBannedInet = new Object();
        editedBannedInet.inetMinIncl = $('#inetMinIncl').val();
        editedBannedInet.inetMaxIncl = $('#inetMaxIncl').val();
        editedBannedInet.isActive = $('#isActive:checked').val();
        editedBannedInet.notes = $('#notes').val();
        var origBannedInet = new Object();
        origBannedInet.inetMinIncl = "${editedBannedInet.inetMinIncl == null ? null : editedBannedInet.inetMinIncl}";
        origBannedInet.inetMaxIncl = "${editedBannedInet.inetMaxIncl == null ? null : editedBannedInet.inetMaxIncl}";
        origBannedInet.isActive = ${editedBannedInet.isActive};
        origBannedInet.notes = "${editedBannedInet.notes == null ? null : editedBannedInet.notes}";
        var originalEditedBannedInet = new Object();
        originalEditedBannedInet.originalBannedInet = origBannedInet;
        originalEditedBannedInet.editedBannedInet = editedBannedInet; 
        $.ajax({ 
          type: "POST",
          url: '<s:url value="/api/banned_inets" />',
          data: JSON.stringify(originalEditedBannedInet),
          contentType: "application/json; charset=utf-8",
          dataType: "json",
          success: function(data){
            window.location.reload(true);
          }
        });
      });
      
      // handle deleting banned inet
      $('#editCreateBannedInet').on('click', '#bannedInetDelete', function () {
        inetMinIncl = "${editedBannedInet.inetMinIncl == null ? null : editedBannedInet.inetMinIncl}";
        inetMaxIncl = "${editedBannedInet.inetMaxIncl == null ? null : editedBannedInet.inetMaxIncl}";
        $.ajax({ 
          type: "DELETE",
          url: '<s:url value="/api/banned_inets/' + inetMinIncl + '/' + inetMaxIncl + '/" />',
          contentType: "application/json; charset=utf-8",
          dataType: "json",
          success: function(){
            window.location.reload(true);
          }
        });
      });
    });
    </script>
  </div>
</sec:authorize>