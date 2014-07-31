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
package org.openmrs.module.restrictbyuser.api.impl;

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
import org.openmrs.User;
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
import org.openmrs.module.restrictbyuser.UserRestriction;
import org.openmrs.module.restrictbyuser.api.RestrictByUserService;
import org.openmrs.module.restrictbyuser.api.db.RestrictByUserDAO;
import org.openmrs.module.serialization.xstream.XStreamSerializer;
import org.openmrs.serialization.SerializationException;

/**
 * It is a default implementation of {@link RestrictByUserService}.
 */
public class RestrictByUserServiceImpl extends BaseOpenmrsService implements RestrictByUserService {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private RestrictByUserDAO dao;
	private SerializedObjectDAO sodao;
	
	public RestrictByUserDAO getDao() {
		return dao;
	}

	public void setDao(RestrictByUserDAO dao) {
		this.dao = dao;
	}	
	
	public SerializedObjectDAO getSodao() {
		return sodao;
	}

	public void setSodao(SerializedObjectDAO sodao) {
		this.sodao = sodao;
	}

	/**
	 * @see org.openmrs.module.restrictbyuser.api.RestrictByUserService#createUserRestriction(UserRestriction)
	 */
	public void createUserRestriction(UserRestriction roleRestriction) {
		getDao().createUserRestriction(roleRestriction);
	}

	/**
	 * @see org.openmrs.module.restrictbyuser.api.RestrictByUserService#deleteUserRestriction(UserRestriction)
	 */
	public void deleteUserRestriction(UserRestriction roleRestriction) {
		getDao().deleteUserRestriction(roleRestriction);
	}

	/**
	 * @see org.openmrs.module.restrictbyuser.api.RestrictByUserService#getUserRestriction(Integer)
	 */
	public UserRestriction getUserRestriction(Integer id) {
		return getDao().getUserRestriction(id);
	}

	/**
	 * @see org.openmrs.module.restrictbyuser.api.RestrictByUserService#getUserRestrictions()
	 */
	public List<UserRestriction> getUserRestrictions() {
		return getDao().getUserRestrictions();
	}
	
	/**
	 * @see org.openmrs.module.restrictbyuser.api.RestrictByUserService#getUserRestrictions(Role)
	 */
	public List<UserRestriction> getUserRestrictions(User user) {
		return getDao().getUserRestrictions(user);
	}

	/**
	 * @see org.openmrs.module.restrictbyuser.api.RestrictByUserService#updateUserRestriction(UserRestriction)
	 */
	public void updateUserRestriction(UserRestriction roleRestriction) {
		getDao().updateUserRestriction(roleRestriction);
	}
	
	/**
	 * @see org.openmrs.module.restrictbyuser.api.RestrictByUserService#doesCurrentUserHavePermission(Patient)
	 */
	public boolean doesCurrentUserHavePermission(Patient patient) {
		return doesCurrentUserHavePermission(patient.getPatientId());
	}

	/**
	 * @see org.openmrs.module.restrictbyuser.api.RestrictByUserService#doesCurrentUserHavePermission(Integer)
	 */
	public boolean doesCurrentUserHavePermission(Integer patientId) {
		Cohort ps = getCurrentUserRestrictedPatientSet();
		if (ps == null)
			return true;
		else
			return ps.contains(patientId);
	}
	
	/**
	 * @see org.openmrs.module.restrictbyuser.api.RestrictByUserService#getCurrentUserRestrictions()
	 */
	public Set<UserRestriction> getCurrentUserRestrictions() {
		if (!Context.isAuthenticated())
			return null;
		User user = Context.getAuthenticatedUser();
		Set<UserRestriction> ret = new HashSet<UserRestriction>();
		//for (Role role : roles)
			//ret.addAll(getUserRestrictions(role));
		ret.addAll(getUserRestrictions(user));
		log.debug("current user has " + ret.size() + " restrictions");
		return ret;
	}
	
	/**
	 * @see org.openmrs.module.restrictbyuser.api.RestrictByUserService#getAllSerializedObjects()
	 */
	public List<SerializedObject> getAllSerializedObjects(){
		return sodao.getAllSerializedObjects(CohortDefinition.class, false);
	}

	/**
	 * @see org.openmrs.module.restrictbyuser.api.RestrictByUserService#getCurrentUserRestrictedPatientSet()
	 */
	public Cohort getCurrentUserRestrictedPatientSet() {
		Set<UserRestriction> restrictions = getCurrentUserRestrictions();
		if (restrictions == null)
			return null;
		Cohort ret = null;
		
		for (UserRestriction restriction : restrictions) {
					
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

	/**
	 * @see org.openmrs.module.restrictbyuser.api.RestrictByUserService#getSerializedObject(Integer)
	 */
	public SerializedObject getSerializedObject(Integer id) {
		return sodao.getSerializedObject(id);
	}

	/**
	 * @see org.openmrs.module.restrictbyuser.api.RestrictByUserService#getSerializedObjectByUuid(String)
	 */
	public SerializedObject getSerializedObjectByUuid(String uuid) {
		return sodao.getSerializedObjectByUuid(uuid);
	}
}