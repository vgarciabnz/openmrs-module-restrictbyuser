<%@ include file="/WEB-INF/template/include.jsp" %>

<%@ include file="/WEB-INF/template/header.jsp" %>

<openmrs:require privilege="Manage User Restrictions" otherwise="/login.htm" redirect="/module/restrictbyuser/restriction.list" />

<h2><spring:message code="restrictbyuser.title"/></h2>

<h3><spring:message code="restrictbyuser.current"/></h3>
<c:if test="${fn:length(restrictionList) == 0}">
	<spring:message code="general.none"/>
</c:if>
<c:if test="${fn:length(restrictionList) != 0}">
	<form method="post">
		<table>
			<tr>
				<th></th>
				<th></th>
				<th><spring:message code="general.id"/></th>
				<th><spring:message code="restrictbyuser.user"/></th>
				<th><spring:message code="restrictbyuser.search"/></th>
			</tr>
			<c:forEach var="restriction" items="${restrictionList}">
				<tr>
					<td><input type="checkbox" name="deleteId" value="${restriction.id}" /></td>
					<td><a href="restrictionForm.form?restrictionId=${restriction.id}">
						<img src="<c:url value='/images/edit.gif'/>" border="0"/></a>
					</td>
					<td>${restriction.id}</td>
					<td>${restriction.user.username}</td>
					<td>
						${restriction.serializedObject.name}
						<small>(${restriction.serializedObject.description})</small>
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