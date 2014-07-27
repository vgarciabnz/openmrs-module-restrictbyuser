/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.restrictbyrole;

import java.io.Serializable;
import org.openmrs.BaseOpenmrsObject;
import org.openmrs.BaseOpenmrsMetadata;
import org.openmrs.Role;

/**
 * It is a model class. It should extend either {@link BaseOpenmrsObject} or {@link BaseOpenmrsMetadata}.
 */
public class RoleRestriction extends BaseOpenmrsObject implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Role role;
	private String cohortUuid;
	
	public RoleRestriction() { }
	
	@Override
	public Integer getId() {
		return id;
	}
	
	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getCohortUuid() {
		return cohortUuid;
	}

	public void setCohortUuid(String cohortUuid) {
		this.cohortUuid = cohortUuid;
	}
	
	/**
	public PatientSearch getPatientSearch() {
		PatientSearchReportObject o = (PatientSearchReportObject) Context.getReportService().getReportObject(searchId);
		return o.getPatientSearch();
	}
	
	public PatientSearchReportObject getPatientSearchReportObject() {
		return (PatientSearchReportObject) Context.getReportService().getReportObject(searchId);
	}
	*/
	
}