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
package org.openmrs.module.restrictbyrole.api.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openmrs.module.reporting.cohort.EvaluatedCohort;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.service.CohortDefinitionService;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.Cohort;
import org.openmrs.Patient;
import org.openmrs.Role;
import org.openmrs.api.context.Context;
import org.openmrs.api.db.SerializedObject;
import org.openmrs.api.db.SerializedObjectDAO;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.query.person.PersonQueryResult;
import org.openmrs.module.reporting.query.person.definition.PersonQuery;
import org.openmrs.module.reporting.query.person.service.PersonQueryService;
import org.openmrs.module.reporting.serializer.ReportingSerializer;
import org.openmrs.module.restrictbyrole.RoleRestriction;
import org.openmrs.module.restrictbyrole.api.RestrictByRoleService;
import org.openmrs.module.restrictbyrole.api.db.RestrictByRoleDAO;
import org.openmrs.module.serialization.xstream.XStreamSerializer;
import org.openmrs.serialization.SerializationException;

/**
 * It is a default implementation of {@link RestrictByRoleService}.
 */
public class RestrictByRoleServiceImpl extends BaseOpenmrsService implements RestrictByRoleService {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private RestrictByRoleDAO dao;
	private SerializedObjectDAO sodao;
	
	public SerializedObjectDAO getSodao() {
		return sodao;
	}

	public void setSodao(SerializedObjectDAO sodao) {
		this.sodao = sodao;
	}

	public void createRoleRestriction(RoleRestriction roleRestriction) {
		getDao().createRoleRestriction(roleRestriction);
	}

	public void deleteRoleRestriction(RoleRestriction roleRestriction) {
		getDao().deleteRoleRestriction(roleRestriction);
	}

	public RoleRestriction getRoleRestriction(Integer id) {
		return getDao().getRoleRestriction(id);
	}

	public List<RoleRestriction> getRoleRestrictions() {
		return getDao().getRoleRestrictions();
	}
	
	public List<RoleRestriction> getRoleRestrictions(Role role) {
		return getDao().getRoleRestrictions(role);
	}

	public void updateRoleRestriction(RoleRestriction roleRestriction) {
		getDao().updateRoleRestriction(roleRestriction);
	}
	
	public boolean doesCurrentUserHavePermission(Patient patient) {
		return doesCurrentUserHavePermission(patient.getPatientId());
	}

	public boolean doesCurrentUserHavePermission(Integer patientId) {
		Cohort ps = getCurrentUserRestrictedPatientSet();
		if (ps == null)
			return true;
		else
			return ps.contains(patientId);
	}
		
	public Set<RoleRestriction> getCurrentUserRestrictions() {
		if (!Context.isAuthenticated())
			return null;
		Set<Role> roles = Context.getAuthenticatedUser().getAllRoles();
		Set<RoleRestriction> ret = new HashSet<RoleRestriction>();
		for (Role role : roles)
			ret.addAll(getRoleRestrictions(role));
		log.debug("current user has " + ret.size() + " restrictions");
		return ret;
	}
	
	public List<SerializedObject> getAllSerializedObjects(){
		return sodao.getAllSerializedObjects(CohortDefinition.class, false);
	}

	public Cohort getCurrentUserRestrictedPatientSet() {
		Set<RoleRestriction> restrictions = getCurrentUserRestrictions();
		if (restrictions == null)
			return null;
		Cohort ret = null;
		
		for (RoleRestriction restriction : restrictions) {
					
			try {

				SerializedObject so = restriction.getSerializedObject();
				CohortDefinition c = Context.getSerializationService().deserialize(so.getSerializedData(), CohortDefinition.class, ReportingSerializer.class);

				CohortDefinitionService pq = Context.getService(CohortDefinitionService.class);
				EvaluatedCohort result = pq.evaluate(c, null);
				
				ret = ret == null ? result : Cohort.intersect(ret, result);
				
			} catch (SerializationException e) {
				e.printStackTrace();
			} catch (EvaluationException e) {
				e.printStackTrace();
			}
			
		}
				
		return ret;
	}

	public RestrictByRoleDAO getDao() {
		return dao;
	}

	public void setDao(RestrictByRoleDAO dao) {
		this.dao = dao;
	}

	@Override
	public SerializedObject getSerializedObject(Integer id) {
		return sodao.getSerializedObject(id);
	}

	@Override
	public SerializedObject getSerializedObjectByUuid(String uuid) {
		return sodao.getSerializedObjectByUuid(uuid);
	}
}