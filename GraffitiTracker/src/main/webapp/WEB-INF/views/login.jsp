<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
  uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<sec:authorize access="isAnonymous()">
  <div>
    <h2>Sign in to Graffiti Tracker</h2>

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
    <p>
      Welcome, <sec:authentication property="principal.username" />.
    </p>
    <p>Roles:</p>
    <sec:authentication property="authorities" var="roles" />
    <c:forEach var="role" items="${roles}">
      <p>
        <c:out value="${role}" />
      </p>
    </c:forEach>
  </div>
</sec:authorize>