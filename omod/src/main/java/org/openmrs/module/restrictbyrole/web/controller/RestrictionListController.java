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
package org.openmrs.module.restrictbyrole.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Role;
import org.openmrs.api.context.Context;
import org.openmrs.module.restrictbyrole.RoleRestriction;
import org.openmrs.module.restrictbyrole.api.RestrictByRoleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * The main controller.
 */
@Controller
@RequestMapping("/module/restrictbyrole/restrictionList")
public class  RestrictionListController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	@RequestMapping(method = RequestMethod.GET)
	public void initForm(){
		
	}
	
	@ModelAttribute("restrictionList")
	public List<RoleRestriction> populateRestrictionList(){
		
		if (!Context.isAuthenticated())
			return new ArrayList<RoleRestriction>();
		
		RestrictByRoleService service = (RestrictByRoleService) Context.getService(RestrictByRoleService.class);
		List<RoleRestriction> restrictions = service.getRoleRestrictions();
		
		return restrictions;
	}
	
	
	@RequestMapping(method = RequestMethod.POST)
	protected String processSubmit(HttpServletRequest request) {
		String[] toDelete = request.getParameterValues("deleteId");
		RestrictByRoleService service = (RestrictByRoleService) Context.getService(RestrictByRoleService.class);
		for (String s : toDelete) {
			Integer id = Integer.valueOf(s);
			service.deleteRoleRestriction(service.getRoleRestriction(id));
		}
		return "redirect:restrictionList.form";
	}
}
