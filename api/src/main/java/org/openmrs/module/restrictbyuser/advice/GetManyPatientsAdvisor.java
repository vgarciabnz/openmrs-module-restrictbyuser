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
package org.openmrs.module.restrictbyuser.advice;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.Cohort;
import org.openmrs.api.PatientService;
import org.openmrs.api.PatientSetService;
import org.openmrs.api.context.Context;
import org.openmrs.module.restrictbyuser.api.RestrictByUserService;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;

public class GetManyPatientsAdvisor extends StaticMethodMatcherPointcutAdvisor implements Advisor {

	private static final long serialVersionUID = 1L;
	private Log log = LogFactory.getLog(this.getClass());

	public boolean matches(Method method, Class targetClass) {
		return
			(PatientSetService.class.isAssignableFrom(targetClass) && method.getName().equals("getAllPatients")) ||
			(PatientService.class.isAssignableFrom(targetClass) && method.getName().equals("findPatients")) ||
			(PatientService.class.isAssignableFrom(targetClass) && method.getName().equals("getPatients"));
	}
	
	public Advice getAdvice() {
		return new GetManyPatientAdvice();
	}
	
	private class GetManyPatientAdvice implements MethodInterceptor {
		public Object invoke(MethodInvocation invocation) throws Throwable {
			RestrictByUserService service = (RestrictByUserService) Context.getService(RestrictByUserService.class);
			Cohort restrictedResult = service.getCurrentUserRestrictedPatientSet();
			if (restrictedResult == null)
				return invocation.proceed();
			if (invocation.getMethod().getName().equals("getAllPatients")) {
				return restrictedResult;
			} else { // "findPatients"
				List<Patient> originalResult = (List<Patient>) invocation.proceed();
				List<Patient> newResult = new ArrayList<Patient>();
				for (Patient p : originalResult)
					if (restrictedResult.contains(p.getPatientId()))
						newResult.add(p);
				return newResult;
			}
		}
	}

}
