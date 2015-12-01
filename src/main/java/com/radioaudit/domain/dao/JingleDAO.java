package com.radioaudit.domain.dao;

import java.util.List;

import javax.annotation.PostConstruct;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.radioaudit.domain.model.Jingle;

@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
@Repository
public class JingleDAO extends AbstractDAO<Jingle> {

	@PostConstruct
	private void setPersistentClazz() {
		this.persistentClazz = Jingle.class;
	}

	public Jingle loadByName(String selectedRadioName) {
		return this.readBy("name", selectedRadioName);
	}

	@SuppressWarnings("unchecked")
	public List<Jingle> findByRadioCodeAndUsername(String radioCode,
			String username) {
		Session session = getSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(this.getClass());
		return criteria.createCriteria("radio")
				.add(Restrictions.eq("code", radioCode)).createCriteria("user")
				.add(Restrictions.eq("code", radioCode)).list();
	}

	@SuppressWarnings("unchecked")
	public List<Jingle> findByUsernameAndRadio(String radioCode,
			String username) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Jingle.class,
				"commercial");
		criteria.setFetchMode("commercial.user", FetchMode.JOIN);
		criteria.createAlias("commercial.user", "user");
		criteria.add(Restrictions.eq("user.username", username));
		criteria.setFetchMode("commercial.radios", FetchMode.JOIN);
		criteria.createAlias("commercial.radios", "radio");
		criteria.add(Restrictions.eq("radio.code", radioCode));
		return criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.list();
	}

	@SuppressWarnings("unchecked")
	public List<Jingle> findByUsername(String username) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Jingle.class,
				"commercial");
		criteria.createAlias("commercial.user", "user");
		criteria.add(Restrictions.eq("user.username", username));
		return criteria.list();
	}

	public boolean notExistCommercial(String fileName) {
		return this.readBy("name", fileName) == null;
	}
}
