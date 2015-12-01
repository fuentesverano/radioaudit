package com.radioaudit.domain.dao;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.radioaudit.domain.model.Coincidence;

@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
@Repository
public class CoincidenceDAO extends AbstractDAO<Coincidence> {

	@PostConstruct
	private void setPersistentClazz() {
		this.persistentClazz = Coincidence.class;
	}

	@SuppressWarnings("unchecked")
	public List<Coincidence> findByRadioAndUsername(String radioCode, String username) {

		Criteria criteria = this.getSession().createCriteria(Coincidence.class, "coincidence");
		criteria.createCriteria("coincidence.radio", "radio").add(Restrictions.eq("radio.code", radioCode));
		criteria.createCriteria("jingle.user", "user").add(Restrictions.eq("user.username", username));
		criteria.createCriteria("coincidence.jingle", "jingle", Criteria.LEFT_JOIN);
		criteria.addOrder(Order.desc("timestamp"));

		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<Coincidence> findByRadioAndUsername(String radioCode, String username, Date fromDate, Date toDate) {

		Criteria criteria = this.getSession().createCriteria(Coincidence.class, "coincidence")
				.add(Restrictions.le("coincidence.timestamp", toDate))
				.add(Restrictions.ge("coincidence.timestamp", fromDate));
		criteria.createCriteria("coincidence.radio", "radio").add(Restrictions.eq("radio.code", radioCode));
		criteria.createCriteria("jingle.user", "user").add(Restrictions.eq("user.username", username));
		criteria.createCriteria("coincidence.jingle", "jingle", Criteria.LEFT_JOIN);
		criteria.addOrder(Order.desc("timestamp"));

		return criteria.list();
	}
}
