<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
  uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<sec:authorize access="isAnonymous()">
  <div>
    <c:if test="${not empty param.login_error}">
      <div class="error">
          Your login attempt was not successful, please try again.
      </div>
    </c:if>
    <s:url var="authUrl" value="/static/j_spring_security_check" />
    <!--<co id="co_securityCheckPath"/>-->
    <form method="post" class="signin" action="${authUrl}">
      <fieldset>
        <div>
          <label for="j_username">Username</label>
        </div>
        <div>
          <input id="username" name="j_username" type="text" />
        </div>
        <div>
          <label for="j_password">Password</label>
        </div>
        <div>
          <input id="password" name="j_password" type="password" />
       </div>
        <div>
          <input id="remember_me" name="_spring_security_remember_me"
            type="checkbox" />
          <label for="remember_me" class="inline">Remember
            me</label>
        </div>
        <div>
          <input name="commit" type="submit" value="Sign In" />
        </div>
      </fieldset>
    </form>
    <div><a href="<s:url value="/users/new"/>">Register</a></div>
    <div><a href="<s:url value="/users/forgotUsername?forgotUsername"/>">Forgot Username</a></div>
    <div><a href="<s:url value="/users/forgotPassword"/>">Forgot Password</a></div>
    <script type="text/javascript">
      document.getElementById('username').focus();
    </script>
  </div>
</sec:authorize>
<sec:authorize access="isAuthenticated()">
  <div>
    <s:url var="unauthUrl" value="/static/j_spring_security_logout" />
    <sec:authentication property="authorities" var="roles" />
    <div>Username: ${appUser.username} (ID: ${appUser.userId})</div>
    <div>Register Date: <fmt:formatDate value="${appUser.registerTimestamp}" pattern="yyyy-MM-dd" /></div>
    <div>Last Login: <fmt:formatDate value="${appUser.previousLoginTimestamp}" pattern="yyyy-MM-dd" /></div>
    <div>Roles:</div>
    <ul id="roles">
      <c:forEach var="role" items="${appUser.roles}">
        <li>
          <fmt:formatDate value="${role.grantedTimestamp}" pattern="yyyy-MM-dd" var="grantedDate"/>
          <c:out value="${role.role.displayString} (${grantedDate})" />
        </li>
      </c:forEach>
    </ul>
    <div>Number Of Logins: ${appUser.loginCount}</div>
    <FORM METHOD="LINK" ACTION="<s:url value="${unauthUrl}" />">
      <div><INPUT TYPE="submit" VALUE=" Logout "></div>
    </FORM>
  </div>
</sec:authorize>