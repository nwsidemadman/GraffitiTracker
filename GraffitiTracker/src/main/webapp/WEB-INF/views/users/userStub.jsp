<%@ taglib prefix="sec"
  uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%@ page import="net.ccaper.GraffitiTracker.enums.RoleEnum" %>

<sec:authorize access="!hasRole('ROLE_SUPERADMIN')">
  <p>Not authorized to view this pane</p>
</sec:authorize>
<sec:authorize access="hasRole('ROLE_SUPERADMIN')">
<table width="100%">
  <tr valign="top">
    <td width="50%">
      <form id="adminEditUser">
        <div>Username: ${appUser.username}</div>
        <div>Roles:
          <select id="manageUsersSelectRoles" name="roles" multiple size="3">
            <c:set var="roles" value="${appUser.rolesAsTimestampToRoleEnumMap}" />
            <c:forEach var="entry" items="<%=RoleEnum.values()%>">
              <c:set var="roleSet" value="${roles.containsKey(entry)}" />
              <option value="${entry}" <c:if test="${roleSet}">selected</c:if> >
                ${entry.displayString}
                <c:if test="${roleSet}">
                  <fmt:formatDate value="${roles.get(entry)}" pattern="yyyy-MM-dd" var="grantedDate"/>
                  <c:out value="(${grantedDate})" />
                </c:if>
              </option>
            </c:forEach>
          </select>
        </div>
        <div>Email: <input type="email" id="manageUsersEmail" name="email" value="${appUser.email}"></div>
        <div>Active:
          <input id="manageUsersIsActive" type="radio" name="isActive" value="true" <c:if test="${appUser.isActive}">checked</c:if>>Yes
          <input id="manageUsersIsActive" type="radio" name="isActive" value="false" <c:if test="${appUser.isActive == false}">checked</c:if>>No
        </div>
        <div>Registered: <fmt:formatDate value="${appUser.registerTimestamp}" pattern="yyyy-MM-dd" /></div>
        <div>Last Login: <fmt:formatDate value="${appUser.previousLoginTimestamp}" pattern="yyyy-MM-dd" /></div>
        <div>Login Count: ${appUser.loginCount }</p>
        <div><input id="manageUsersSubmit" type="submit" value="Edit"><p>
      </form>
    </td>
    <td width="50%">
      <table width="100%">
        <tr>
          <td>
            <table id="userLoginsTable" class="display" cellspacing="0" width="100%">
              <thead>
                <tr>
                  <th align="left">IP</th>
                  <th align="left">Visits</th>
                  <th align="left">Last Visit</th>
                  <th align="left">Banned</th>
                </tr>
              </thead>
            </table>
          </td>
        </tr>
        <tr>
          <td id="usersSharingIp">
            Click an IP to see users sharing IP
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<script type="text/javascript">
  $(document).ready(function() {
    // handle editing user
    $('#adminEditUser').submit(function (e) {
      e.preventDefault();
      if (!$('#manageUsersSelectRoles').val()) {
        alert("You must select at least one role");
      } else {
        var editedUser = new Object();
        editedUser.email = $('#manageUsersEmail').val();
        editedUser.isActive = $('#manageUsersIsActive:checked').val();
        editedUser.roles = $('#manageUsersSelectRoles').val();
        $.ajax({ 
          type: "PUT",
          url: '<s:url value="/api/users/${appUser.userId}" />',
          data: JSON.stringify(editedUser),
          contentType: "application/json; charset=utf-8",
          dataType: "json",
          success: function(data){
            $.ajax({ 
              type: "GET",
              dataType: "html",
              url: '<s:url value="/users?userId=' + ${appUser.userId} + '" />',
              success: function(data){
                $('#manageUser').html(data);
              }
            });
          }
        });
      }
    });
    
    // create the datatable
    var userLoginsTableJObject = $('#userLoginsTable').dataTable( {
      "sDom": '<"H"lr>t<"F">',
      "scrollX": false,
      "scrollY": "80px",
      "scrollCollapse": true,
      "paging": false,
      "ajax": "<s:url value="/api/users/${appUser.userId}/logins" />",
      "columns": [
        {"data": "inet"},
        {"data": "numberVisits"},
        {"data": "lastVisit",
          "mRender": function ( oObj ) {
            return dateFormat_yyyymmdd(oObj);
          }
        },
        {"data": "isInetBanned",
          "mRender": function ( oObj ) {
            if (oObj) {
              return "Yes";
            }
            return "No";
          }
        }
      ]
    });
    
    // capture a click on ip column of the datatable
    $('#userLoginsTable tbody').on('click', 'td:first-child', function () {
      var aData = userLoginsTableJObject.fnGetData(this);
      $.ajax({ 
        type: "GET",
        dataType: "html",
        url: '<s:url value="/users/usersSharingInets?inet=' + aData + '" />',
        success: function(data){
          $('#usersSharingIp').html(data);
        }
      });
    });
    
    // capture a click on banned column of the datatable
    $('#userLoginsTable tbody').on('click', 'td:last-child', function () {
      var bannedCol = this;
      var ip = $(this).closest('tr').find('td:first').text();
      if (confirm('Do you want to ban IP \'' + ip + '\'?')) {
        var bannedInet = new Object();
        bannedInet.inetMinIncl = ip;
        bannedInet.inetMaxIncl = ip;
        bannedInet.isActive = true;
        bannedInet.numberRegistrationAttempts = 0;
        bannedInet.notes = "${appUser.username} (${appUser.userId})";
        $.ajax({ 
          type: "POST",
          url: '<s:url value="/api/banned_inets" />',
          data: JSON.stringify(bannedInet),
          contentType: "application/json; charset=utf-8",
          dataType: "json",
          success: function(data){
            $(bannedCol).html('Yes');
          }
        });
      }
    });
  });
</script>
</sec:authorize>