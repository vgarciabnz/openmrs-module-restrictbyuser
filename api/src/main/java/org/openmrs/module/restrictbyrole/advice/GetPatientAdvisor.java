package org.openmrs.module.restrictbyrole.advice;

import java.lang.reflect.Method;

import org.aopalliance.aop.Advice;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.restrictbyrole.RestrictByRoleAuthorizationException;
import org.openmrs.module.restrictbyrole.api.RestrictByRoleService;
import org.springframework.aop.Advisor;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;

public class GetPatientAdvisor extends StaticMethodMatcherPointcutAdvisor implements Advisor {

	private static final long serialVersionUID = 1L;
	private Log log = LogFactory.getLog(this.getClass());
	
	public boolean matches(Method method, Class targetClass) {
		String methodName = method.getName();
		return methodName.equals("getPatient") || methodName.equals("updatePatient");
	}
	
	public Advice getAdvice() {
		return new GetPatientAdvice();
	}
	
	private class GetPatientAdvice implements MethodBeforeAdvice {
		public void before(Method m, Object[] args, Object target) throws Throwable {
			RestrictByRoleService service = (RestrictByRoleService) Context.getService(RestrictByRoleService.class);
			Integer patientId = null;
			if (m.getName().equals("getPatient"))
				patientId = (Integer) args[0];
			else
				patientId = ((Patient) args[0]).getPatientId();
			if (patientId != null && !service.doesCurrentUserHavePermission(patientId))
				throw new RestrictByRoleAuthorizationException("No permission on " + patientId);
		}
	}

}
