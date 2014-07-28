package org.openmrs.module.restrictbyrole;

import java.beans.PropertyEditorSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.api.db.SerializedObject;
import org.openmrs.module.restrictbyrole.api.RestrictByRoleService;
import org.springframework.util.StringUtils;

public class SerializedObjectEditor extends PropertyEditorSupport {
	
	private Log log = LogFactory.getLog(this.getClass());
	
	public SerializedObjectEditor(){	
	}
	
	public void setAsText(String text) throws IllegalArgumentException {
		if (StringUtils.hasText(text)) {
			RestrictByRoleService service = Context.getService(RestrictByRoleService.class);
			try {
				System.out.println("Intenta por id");
				SerializedObject object = service.getSerializedObject(Integer.valueOf(text)); 
				setValue(object);
			}
			catch (Exception ex) {
				System.out.println("Prueba con uuid");
				SerializedObject object = service.getSerializedObjectByUuid(text);
				setValue(object);
				if (object == null) {
					log.error("Error setting text: " + text, ex);
					throw new IllegalArgumentException("SerializedObject not found: " + ex.getMessage());
				}
			}
		} else {
			setValue(null);
		}
	}
	
	@Override
	public String getAsText() {
		SerializedObject so = (SerializedObject) getValue();
		if (so == null) {
			return "";
		} else {
			System.out.println("Devuelve "+so.getUuid());
			return so.getUuid();
		}
	}

}
