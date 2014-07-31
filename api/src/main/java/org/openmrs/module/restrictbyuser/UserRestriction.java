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
package org.openmrs.module.restrictbyuser;

import java.io.Serializable;
import org.openmrs.BaseOpenmrsObject;
import org.openmrs.User;
import org.openmrs.api.db.SerializedObject;

/**
 * The class represents a restriction associated to a role.
 */
public class UserRestriction extends BaseOpenmrsObject implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private User user;
	private SerializedObject serializedObject;
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public SerializedObject getSerializedObject() {
		return serializedObject;
	}

	public void setSerializedObject(SerializedObject serializedObject) {
		this.serializedObject = serializedObject;
	}

	public UserRestriction() { }
	
	@Override
	public Integer getId() {
		return id;
	}
	
	@Override
	public void setId(Integer id) {
		this.id = id;
	}
	
}