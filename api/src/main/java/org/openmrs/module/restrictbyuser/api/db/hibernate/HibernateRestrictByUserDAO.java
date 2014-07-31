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
package org.openmrs.module.restrictbyuser.api.db.hibernate;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openmrs.Role;
import org.openmrs.User;
import org.openmrs.api.db.DAOException;
import org.openmrs.module.restrictbyuser.UserRestriction;
import org.openmrs.module.restrictbyuser.api.db.RestrictByUserDAO;

/**
 * It is a default implementation of  {@link RestrictByUserDAO}.
 */
public class HibernateRestrictByUserDAO implements RestrictByUserDAO {
	protected final Log log = LogFactory.getLog(this.getClass());
	
private SessionFactory sessionFactory;
	
	public UserRestriction getUserRestriction(Integer id) throws DAOException {
		return (UserRestriction) getSessionFactory().getCurrentSession().get(UserRestriction.class, id);
	}

	public List<UserRestriction> getUserRestrictions() throws DAOException {
		return getSessionFactory().getCurrentSession().createQuery("from UserRestriction").list();
	}
	
	public List<UserRestriction> getUserRestrictions(User user) throws DAOException {
		Criteria crit = getSessionFactory().getCurrentSession().createCriteria(UserRestriction.class);
		crit.add(Restrictions.eq("user", user));
		return crit.list();
	}
	
	public void createUserRestriction(UserRestriction userRestriction) throws DAOException {
		getSessionFactory().getCurrentSession().save(userRestriction);
	}

	public void deleteUserRestriction(UserRestriction userRestriction) throws DAOException {
		getSessionFactory().getCurrentSession().delete(userRestriction);
	}

	public void updateUserRestriction(UserRestriction userRestriction) throws DAOException {
		getSessionFactory().getCurrentSession().update(userRestriction);
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}