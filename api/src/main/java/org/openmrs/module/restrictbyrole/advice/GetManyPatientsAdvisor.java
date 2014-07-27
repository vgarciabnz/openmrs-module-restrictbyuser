package org.openmrs.module.restrictbyrole.advice;

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
import org.openmrs.module.restrictbyrole.api.RestrictByRoleService;
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
			RestrictByRoleService service = (RestrictByRoleService) Context.getService(RestrictByRoleService.class);
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
