<%@ include file="/WEB-INF/template/include.jsp" %>

<%@ include file="/WEB-INF/template/header.jsp" %>

<openmrs:require privilege="Manage Role Restrictions" otherwise="/login.htm" redirect="/module/restrictbyrole/restrictionList.form" />

<h2><spring:message code="restrictbyrole.title"/></h2>

<form method="post">
	<table>
		<c:if test="${restriction.id != null}">
			<tr>
				<td><spring:message code="general.id"/></td>
				<td>${restriction.id}</td>
			</tr>
		</c:if>
		<tr>
			<td><spring:message code="restrictbyrole.role"/></td>
			<spring:bind path="restriction.role">
				<td>
					<select name="${status.expression}">
						<option value=""></option>
						<openmrs:forEachRecord name="role">
							<option value="${record.role}" <c:if test="${record.role == status.value}">selected</c:if>>
								${record.role}
							</option>
						</openmrs:forEachRecord>
					</select>
				</td>
				<td>
					<c:if test="${status.errorMessage != ''}"><span class="error">${status.errorMessage}</span></c:if>
				</td>
			</spring:bind>
		</tr>
		<tr>
			<td><spring:message code="restrictbyrole.search"/></td>
			<spring:bind path="restriction.serializedObject">
				<td>
					<select name="${status.expression}">
						<option value=""></option>
						<c:forEach items="${serializedObjects}" var="object" >
							<option value="${object.uuid}" <c:if test="${object.uuid == status.value}">selected</c:if>>
								${object.name}
							</option>
						</c:forEach>
					</select>
				</td>
				<td>
					<c:if test="${status.errorMessage != ''}"><span class="error">${status.errorMessage}</span></c:if>
				</td>
			</spring:bind>
		</tr>
	</table>
	<br/>
	<input type="submit" value="<spring:message code="general.save" />" />
	&nbsp;&nbsp;&nbsp;&nbsp;
	<input type="button" value="<spring:message code="general.cancel" />" onClick="window.location = 'restrictionList.form';"/>
</form>

<br/><br/>

<%@ include file="/WEB-INF/template/footer.jsp" %>