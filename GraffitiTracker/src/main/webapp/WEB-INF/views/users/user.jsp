<%@ taglib prefix="sec"
  uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<sec:authorize access="!hasRole('ROLE_SUPERADMIN')">
  <p>Not authorized to view this pane</p>
</sec:authorize>
<sec:authorize access="hasRole('ROLE_SUPERADMIN')">
<table width="100%">
  <tr>
    <td width="50%">
      <table width="100%">
        <tr><td>Username:</td><td>${appUser.getUsername()}</td></tr>
        <tr>
          <td>Roles:<td>
          <td>
            <c:forEach var="role" items="${appUser.getRoles()}">
              <fmt:formatDate value="${role.getGrantedTimestamp()}" pattern="yyyy-MM-dd hh:mm:ss" var="grantedDate"/>
              <c:out value="${role.getRole().getDisplayString()} (${grantedDate})" /><br>
            </c:forEach>
          </td>
        </tr>
        <tr><td>Email:</td><td>${appUser.getEmail()}</td></tr>
        <tr><td>Active:</td><td>${appUser.getIsActive() ? "Yes" : "No"}</td></tr>
        <tr><td>Registered:</td><td><fmt:formatDate value="${appUser.getRegisterTimestamp()}" pattern="yyyy-MM-dd hh:mm:ss" /></td></tr>
        <tr><td>Last Login:</td><td><fmt:formatDate value="${appUser.getPreviousLoginTimestamp()}" pattern="yyyy-MM-dd hh:mm:ss" /></td></tr>
        <tr><td>Login Count:</td><td>${appUser.getLoginCount() }</td></tr>
      </table>
    </td>
    <td width="50%">
      <table width="100%">
        
      </table>
    </td>
  </tr>
</table>
</sec:authorize>