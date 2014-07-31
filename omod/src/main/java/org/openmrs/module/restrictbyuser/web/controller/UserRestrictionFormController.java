package org.openmrs.module.restrictbyuser.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.api.db.SerializedObject;
import org.openmrs.module.restrictbyuser.UserRestriction;
import org.openmrs.module.restrictbyuser.UserRestrictionValidator;
import org.openmrs.module.restrictbyuser.SerializedObjectEditor;
import org.openmrs.module.restrictbyuser.api.RestrictByUserService;
import org.openmrs.propertyeditor.RoleEditor;
import org.openmrs.propertyeditor.UserEditor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/module/restrictbyuser/restrictionForm")
public class UserRestrictionFormController {

	protected final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private UserRestrictionValidator userRestUserrictionValidator;
	
	@RequestMapping(method = RequestMethod.GET)
	public void initForm(){
		
	}
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(java.lang.Integer.class,
                new CustomNumberEditor(java.lang.Integer.class, true));
        binder.registerCustomEditor(org.openmrs.User.class, new UserEditor());
        binder.registerCustomEditor(org.openmrs.api.db.SerializedObject.class, new SerializedObjectEditor());
	}
	
	@ModelAttribute("restriction")
	protected UserRestriction formBackingObject(HttpServletRequest request) throws ServletException {
		if (!Context.isAuthenticated())
			return new UserRestriction();
		
		UserRestriction restriction = null;
		String idStr = request.getParameter("restrictionId");
		if (idStr != null) {
			RestrictByUserService service = (RestrictByUserService) Context.getService(RestrictByUserService.class);
			restriction = service.getUserRestriction(Integer.valueOf(idStr));
		}
		if (restriction == null)
			restriction = new UserRestriction();
		
		return restriction;
    }
	
	@ModelAttribute("serializedObjects")
    protected List<SerializedObject> initSerializedObjects() {
		if (!Context.isAuthenticated())
			return new ArrayList<SerializedObject>();
		
		RestrictByUserService service = (RestrictByUserService) Context.getService(RestrictByUserService.class);
		List<SerializedObject> serializedObjects = service.getAllSerializedObjects();
		
		return serializedObjects;
	}
	
	@ModelAttribute("users")
    protected List<User> initUsers() {
		if (!Context.isAuthenticated())
			return new ArrayList<User>();
		
		return Context.getUserService().getAllUsers();
	}
	
	/**
	@RequestMapping(method = RequestMethod.POST)
	protected String processSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		if (Context.isAuthenticated()) {
			RoleRestriction rr = (RoleRestriction) command;
			RestrictByRoleService service = (RestrictByRoleService) Context.getService(RestrictByRoleService.class);
			if (rr.getId() == null) {
				log.info("Creating new RoleRestriction");
				service.createRoleRestriction(rr);
			} else {
				log.info("Updating RoleRestriction");
				service.updateRoleRestriction(rr);
			}
		}
		return "/module/restrictbyrole/restrictionList.form";
	}*/
	
	@RequestMapping(method = RequestMethod.POST)
	protected String processSubmit(@ModelAttribute("restriction")UserRestriction rr) {
		if (Context.isAuthenticated()) {
			//RoleRestriction rr = (RoleRestriction) command;
			RestrictByUserService service = (RestrictByUserService) Context.getService(RestrictByUserService.class);
			if (rr.getId() == null) {
				log.info("Creating new RoleRestriction");
				service.createUserRestriction(rr);
			} else {
				log.info("Updating RoleRestriction");
				service.updateUserRestriction(rr);
			}
		}
		return "redirect:restrictionList.form";
	}
	
}
