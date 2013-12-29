<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
  uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ page session="false"%>
<sec:authorize access="isAnonymous()">
  <div>
    <h2>Create a Graffiti Tracker Account</h2>

    <sf:form method="POST" modelAttribute="user" >
      <fieldset> 
        <p>
          <sf:label path="username">Username:</sf:label>
          <sf:input path="username" size="20" maxlength="20" />
          <small id="username_msg">No spaces, please. 6 characters or more</small>
          <sf:errors path="username" cssClass="error" />
        </p>
        <p>
          <sf:label path="password">Password:</sf:label>
          <sf:password path="password" size="30" showPassword="false" maxlength="64"/> 
          <small>6 characters or more</small><br/>
          <sf:errors path="password" cssClass="error" />
        </p>
        <p>
          <sf:label path="email">Email Address:</sf:label>
          <sf:input path="email" size="30" maxlength="100"/> 
          <sf:errors path="email" cssClass="error" />
        </p>
        <p>
          <input name="commit" type="submit" 
                    value="I accept. Create my account." />
        </p>
      </fieldset>
    </sf:form>
  </div>
</sec:authorize>

<sec:authorize access="isAuthenticated()">
  <div>
    <p>You are already a registered user.</p>
  </div>
</sec:authorize>
