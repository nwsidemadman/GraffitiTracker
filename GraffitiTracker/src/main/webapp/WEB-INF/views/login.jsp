<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
  uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<sec:authorize access="isAnonymous()">
  <div>
    <h2>Sign in to Graffiti Tracker</h2>
    
    <c:if test="${not empty param.login_error}">
      <div class="error">
          Your login attempt was not successful, please try again.
      </div>
    </c:if>

    <s:url var="authUrl" value="/static/j_spring_security_check" />
    <!--<co id="co_securityCheckPath"/>-->
    <form method="post" class="signin" action="${authUrl}">

      <fieldset>
        <p>
          <label for="j_username">Username</label>
        </p>
        <p>
          <input id="username" name="j_username" type="text" />
        </p>
        <p>
          <label for="j_password">Password</label>
        </p>
        <p>
          <input id="password" name="j_password" type="password" />
        </p>
        <p>
          <input id="remember_me" name="_spring_security_remember_me"
            type="checkbox" /> <label for="remember_me" class="inline">Remember
            me</label>
        </p>
        <p>
          <input name="commit" type="submit" value="Sign In" />
        </p>
      </fieldset>
    </form>

    <script type="text/javascript">
          document.getElementById('username_or_email').focus();
        </script>
  </div>
</sec:authorize>

<sec:authorize access="isAuthenticated()">
  <div>
    <s:url var="unauthUrl" value="/static/j_spring_security_logout" />
    <p>
      <a href="${unauthUrl}">Logout</a>
    </p>
    <sec:authentication property="authorities" var="roles" />
    <p>Username: ${user.getUsername()} (ID: ${user.getUserId()})</p>
    <p>Is Active: ${user.getIsActive()}</p>
    <p>Register Date: <fmt:formatDate value="${user.getRegisterDate()}" pattern="yyyy-MM-d" /></p>
    <p>Last Login: <fmt:formatDate value="${user.getLastLogin()}" pattern="yyyy-MM-d" /></p>
    <p>Roles:</p>
    <c:forEach var="role" items="${user.getRoles()}">
      <p>
        <fmt:formatDate value="${role.getGrantedTimestamp()}" pattern="yyyy-MM-d" var="grantedDate"/>
        <c:out value="${role.getRole().getDisplayString()} (${grantedDate})" />
      </p>
    </c:forEach>
  </div>
</sec:authorize>