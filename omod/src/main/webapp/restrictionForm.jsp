<%@ include file="/WEB-INF/template/include.jsp" %>

<%@ include file="/WEB-INF/template/header.jsp" %>

<openmrs:require privilege="Manage User Restrictions" otherwise="/login.htm" redirect="/module/restrictbyuser/restrictionList.form" />

<h2><spring:message code="restrictbyuser.title"/></h2>

<form method="post">
	<table>
		<c:if test="${restriction.id != null}">
			<tr>
				<td><spring:message code="general.id"/></td>
				<td>${restriction.id}</td>
			</tr>
		</c:if>
		<tr>
			<td><spring:message code="restrictbyuser.user"/></td>
			<spring:bind path="restriction.user">
				<td>
					<select name="${status.expression}">
						<option value=""></option>
						<c:forEach items="${users}" var="objectUser" >
							<option value="${objectUser.id}" <c:if test="${objectUser.id == status.value}">selected</c:if>>
								${objectUser.username}
							</option>
						</c:forEach>
					</select>
				</td>
				<td>
					<c:if test="${status.errorMessage != ''}"><span class="error">${status.errorMessage}</span></c:if>
				</td>
			</spring:bind>
		</tr>
		<tr>
			<td><spring:message code="restrictbyuser.search"/></td>
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