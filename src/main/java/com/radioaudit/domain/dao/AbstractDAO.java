package com.radioaudit.domain.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Provides a way to use annotations to load up DAOs.
 * 
 * @param <T>
 *            the type of entity.
 */
public abstract class AbstractDAO<T> {

	protected Class<T> persistentClazz;

	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Session getSession() {

		Session session = sessionFactory.getCurrentSession();
		if (!session.getTransaction().isActive()) {
			System.out.println("Sin transaccion.... ");
			session.beginTransaction();
		}
		return session;
	}

	@Transactional(readOnly = false)
	public T save(T t) {
		Session session = getSession();
		session.persist(t);
		session.flush();
		return t;
	}

	@Transactional(readOnly = false)
	public T update(T t) {
		Session session = getSession();
		session.update(t);
		session.flush();
		return t;
	}

	@Transactional(readOnly = false)
	public void delete(T t) {
		getSession().delete(t);
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<T> find() {
		Criteria criteria = getSession().createCriteria(this.persistentClazz);
		return (List<T>) criteria.list();

	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	protected T readBy(String propertyName, String value) {
		Criteria criteria = getSession().createCriteria(this.persistentClazz);
		criteria.add(Restrictions.eq(propertyName, value));
		return (T) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	protected T readByPropertyWith(String propertyName, String value,
			String withProperty) {
		Criteria criteria = getSession().createCriteria(this.persistentClazz);
		criteria.add(Restrictions.eq(propertyName, value));
		criteria.setFetchMode(withProperty, FetchMode.JOIN)
				.setResultTransformer(
						CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return (T) criteria.uniqueResult();
	}

}
