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

import org.openmrs.api.APIAuthenticationException;

public class RestrictByUserAuthorizationException extends APIAuthenticationException {

	private static final long serialVersionUID = 1L;

	public RestrictByUserAuthorizationException() {
		super();
	}
	
	public RestrictByUserAuthorizationException(String message) {
		super(message);
	}
	
	public RestrictByUserAuthorizationException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public RestrictByUserAuthorizationException(Throwable cause) {
		super(cause);
	}
	
}
