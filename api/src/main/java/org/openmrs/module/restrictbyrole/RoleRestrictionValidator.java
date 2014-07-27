package org.openmrs.module.restrictbyrole;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class RoleRestrictionValidator implements Validator {

	public boolean supports(Class c) {
		return c.equals(RoleRestriction.class);
	}

	public void validate(Object o, Errors errors) {
		RoleRestriction rr = (RoleRestriction) o;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "role", "error.role");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "searchId", "error.search");
	}

}
