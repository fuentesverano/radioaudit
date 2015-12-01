package com.radioaudit.domain.dao;

import java.util.List;

import javax.annotation.PostConstruct;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.radioaudit.domain.model.Radio;

@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
@Repository
public class RadioDAO extends AbstractDAO<Radio> {

	@PostConstruct
	private void setPersistentClazz() {
		this.persistentClazz = Radio.class;
	}

	public boolean isActive(String radioCode) {
		return this.getSession().createCriteria(Radio.class, "radio").add(Restrictions.eq("radio.code", radioCode))
				.add(Restrictions.eq("radio.active", true)).uniqueResult() != null;
	}

	@SuppressWarnings("unchecked")
	public List<Radio> findAll() {
		return this.getSession().createCriteria(Radio.class, "radio").add(Restrictions.ne("radio.url", ""))
				.addOrder(Order.asc("radio.id")).list();
	}

	public Radio loadByName(String selectedRadioName) {
		return this.readBy("name", selectedRadioName);
	}

	public Radio loadByCode(String radioCode) {
		return this.readBy("code", radioCode);
	}

	public Radio loadByCodeWithSuscribeCommercials(String radioCode) {

		return (Radio) this.getSession().createCriteria(Radio.class, "radio").add(Restrictions.eq("radio.code", radioCode))
				.createCriteria("suscribeJingles", "jingle", Criteria.LEFT_JOIN)
				.createCriteria("jingle.user", "user", Criteria.LEFT_JOIN).uniqueResult();
	}

	public Radio loadByCodeWithSuscribeCommercials(String radioCode, String username) {

		Radio radio = (Radio) this.getSession().createCriteria(Radio.class, "radio")
				.add(Restrictions.eq("radio.code", radioCode)).uniqueResult();

		if (radio.getSuscribeJingles().isEmpty()) {
			return radio;
		} else {
			radio = (Radio) this.getSession().createCriteria(Radio.class, "radio").add(Restrictions.eq("code", radioCode))
					.createAlias("radio.suscribeJingles", "jingle").createAlias("jingle.user", "user")
					.add(Restrictions.eq("user.username", username)).uniqueResult();
			return radio;
		}
	}

	@Transactional(readOnly = false)
	public void setNotConnect(String radioCode) {
		Session session = this.getSession();
		Radio radio = (Radio) session.createCriteria(Radio.class, "radio").add(Restrictions.eq("radio.code", radioCode))
				.uniqueResult();

		radio.setConnect(false);
		this.update(radio);
	}

	@SuppressWarnings("unchecked")
	public List<Radio> findActiveButNotConnect() {
		Session session = this.getSession();
		List<Radio> radios = session
				.createCriteria(Radio.class, "radio")
				.add(Restrictions.ne("radio.url", ""))
				.add(Restrictions.and(Restrictions.isNotEmpty("radio.suscribeJingles"),
						Restrictions.eq("radio.connect", false))).list();

		return radios;
	}

	@SuppressWarnings("unchecked")
	public List<Radio> findActiveRadios() {
		Session session = this.getSession();
		List<Radio> radios = session
				.createCriteria(Radio.class, "radio")
				.add(Restrictions.ne("radio.url", ""))
				.add(Restrictions.and(Restrictions.isNotEmpty("radio.suscribeJingles"),
						Restrictions.eq("radio.active", true))).list();

		return radios;
	}

	@SuppressWarnings("unchecked")
	public int countActiveRadios() {

		List<Radio> radios = this.getSession().createCriteria(Radio.class, "radio")
				.add(Restrictions.isNotEmpty("radio.suscribeJingles")).add(Restrictions.eq("radio.connect", true)).list();
		if (radios != null) {
			return radios.size();
		} else {
			return 0;
		}
	}

	public int countSuscribeCommercials(String radioCode) {
		Radio radio = (Radio) this.getSession().createCriteria(Radio.class, "radio")
				.add(Restrictions.eq("radio.code", radioCode)).uniqueResult();
		if (radio.getSuscribeJingles().isEmpty()) {
			return 0;
		} else {
			radio = (Radio) this.getSession().createCriteria(Radio.class, "radio").add(Restrictions.eq("code", radioCode))
					.createAlias("radio.suscribeJingles", "jingle").uniqueResult();
			return radio.getSuscribeJingles().size();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Radio> findByUser(String username) {
		return this.getSession().createCriteria(Radio.class, "radio").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.createAlias("radio.suscribeJingles", "jingle").createAlias("jingle.user", "user")
				.add(Restrictions.eq("user.username", username)).list();
	}

	@SuppressWarnings("unchecked")
	public List<Radio> findByNotUser(String username) {
		return this
				.getSession()
				.createCriteria(Radio.class, "radio")
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.createAlias("radio.suscribeJingles", "jingle", org.hibernate.sql.JoinType.LEFT_OUTER_JOIN)
				.createAlias("jingle.user", "user", org.hibernate.sql.JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.or(Restrictions.isEmpty("radio.suscribeJingles"),
						Restrictions.ne("user.username", username))).list();
	}
}
