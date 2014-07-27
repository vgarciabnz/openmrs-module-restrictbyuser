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
package org.openmrs.module.restrictbyrole.api.db.hibernate;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openmrs.Role;
import org.openmrs.api.db.DAOException;
import org.openmrs.module.restrictbyrole.RoleRestriction;
import org.openmrs.module.restrictbyrole.api.db.RestrictByRoleDAO;

/**
 * It is a default implementation of  {@link RestrictByRoleDAO}.
 */
public class HibernateRestrictByRoleDAO implements RestrictByRoleDAO {
	protected final Log log = LogFactory.getLog(this.getClass());
	
private SessionFactory sessionFactory;
	
	public RoleRestriction getRoleRestriction(Integer id) throws DAOException {
		return (RoleRestriction) getSessionFactory().getCurrentSession().get(RoleRestriction.class, id);
	}

	public List<RoleRestriction> getRoleRestrictions() throws DAOException {
		return getSessionFactory().getCurrentSession().createQuery("from RoleRestriction").list();
	}
	
	public List<RoleRestriction> getRoleRestrictions(Role role) throws DAOException {
		Criteria crit = getSessionFactory().getCurrentSession().createCriteria(RoleRestriction.class);
		crit.add(Restrictions.eq("role", role));
		return crit.list();
	}
	
	public void createRoleRestriction(RoleRestriction roleRestriction) throws DAOException {
		getSessionFactory().getCurrentSession().save(roleRestriction);
	}

	public void deleteRoleRestriction(RoleRestriction roleRestriction) throws DAOException {
		getSessionFactory().getCurrentSession().delete(roleRestriction);
	}

	public void updateRoleRestriction(RoleRestriction roleRestriction) throws DAOException {
		getSessionFactory().getCurrentSession().update(roleRestriction);
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}