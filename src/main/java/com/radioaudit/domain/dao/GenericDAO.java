package com.radioaudit.domain.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

/**
 * Provides a way to use annotations to load up DAOs.
 * 
 * @param <T>
 *            the type of entity.
 */
@Repository
public abstract class GenericDAO<T> extends HibernateDaoSupport {

	@Autowired
	public GenericDAO(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	protected void init() {
		// Do Nothing
	}

	public void save(T t) {
		// TODO Auto-generated method stub
		getHibernateTemplate().save(t);
	}

	public void update(T t) {
		// TODO Auto-generated method stub
		getHibernateTemplate().update(t);
	}

	public void delete(T t) {
		// TODO Auto-generated method stub
		getHibernateTemplate().delete(t);
	}

}
