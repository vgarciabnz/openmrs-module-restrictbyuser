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

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class UserRestrictionValidator implements Validator {

	public boolean supports(Class c) {
		return c.equals(UserRestriction.class);
	}

	public void validate(Object o, Errors errors) {
		UserRestriction rr = (UserRestriction) o;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "user", "error.user");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "searchId", "error.search");
	}

}
