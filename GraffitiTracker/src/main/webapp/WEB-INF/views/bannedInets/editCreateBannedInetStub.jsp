<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec"
  uri="http://www.springframework.org/security/tags"%>

<sec:authorize access="!hasRole('ROLE_SUPERADMIN')">
  <div id="simple_content_text">
    <p>Not authorized to view this page</p>
  </div>
</sec:authorize>
<sec:authorize access="hasRole('ROLE_SUPERADMIN')">
  <c:set var="actionValue" value="${contextPath}/banned_inets/editCreateBannedInet" />
  <div>
    <div>Banned IP</div>
    <sf:form method="POST" modelAttribute="editedBannedInet" action="${actionValue}" >
      <fieldset>
        <div>
          <sf:errors path="inetMinIncl" cssClass="error" /><br/>
          <sf:label path="inetMinIncl">Min IP: </sf:label>
          <sf:input path="inetMinIncl" size="15" maxlength="15" />
        </div>
        <div>
          <sf:errors path="inetMaxIncl" cssClass="error" /><br/>
          <sf:label path="inetMaxIncl">Max IP: </sf:label>
          <sf:input path="inetMaxIncl" size="15" maxlength="15" />
        </div>
        <div>
          <sf:label path="isActive">Active: </sf:label>
          <sf:radiobutton path="isActive" value="true" /> Yes
          <sf:radiobutton path="isActive" value="false" /> No
        </div>
        <div>
          <sf:label path="notes">Notes: </sf:label>
          <sf:input path="notes" size="15" maxlength="15" />
        </div>
        <div>
          <input name="commit" type="submit" 
                    value="Create Banned IP" />
        </div>
      </fieldset>
    </sf:form>
  </div>
</sec:authorize>