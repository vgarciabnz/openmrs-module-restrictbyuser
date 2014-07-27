<%@ include file="/WEB-INF/template/include.jsp" %>

<%@ include file="/WEB-INF/template/header.jsp" %>

<openmrs:require privilege="Manage Role Restrictions" otherwise="/login.htm" redirect="/module/restrictbyrole/restriction.list" />

<h2><spring:message code="restrictbyrole.title"/></h2>

<h3><spring:message code="restrictbyrole.current"/></h3>
<c:if test="${fn:length(restrictionList) == 0}">
	<spring:message code="general.none"/>
</c:if>
<c:if test="${fn:length(restrictionList) != 0}">
	<form method="post">
		<table>
			<tr>
				<th></th>
				<th><spring:message code="general.id"/></th>
				<th><spring:message code="restrictbyrole.role"/></th>
				<th><spring:message code="restrictbyrole.search"/></th>
			</tr>
			<c:forEach var="restriction" items="${restrictionList}">
				<tr>
					<td><input type="checkbox" name="deleteId" value="${restriction.id}" /></td>
					<td>${restriction.id}</td>
					<td>${restriction.role.role}</td>
					<td>
						${restriction.cohortUuid}
						<small>(${restriction.id})</small>
					</td>
				</tr>
			</c:forEach>
		</table>
		<input type="submit" value="<spring:message code="general.delete" />"/>
	</form>
</c:if>

<br/>
<br/>
<a href="restrictionForm.form"><spring:message code="general.add"/></a>

<%@ include file="/WEB-INF/template/footer.jsp" %>