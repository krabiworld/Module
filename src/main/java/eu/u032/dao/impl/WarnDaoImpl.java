package eu.u032.dao.impl;

import eu.u032.dao.WarnDao;
import eu.u032.model.WarnModel;
import eu.u032.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class WarnDaoImpl implements WarnDao {
	final private static SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

	@Override
	public WarnModel findById(final long id) {
		final Session session = sessionFactory.openSession();
		final WarnModel warnModel = session.get(WarnModel.class, id);
		session.close();
		return warnModel;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<WarnModel> findAllByGuildAndUser(final long guild, final long user) {
		final Session session = sessionFactory.openSession();
		List<WarnModel> warnModels = (List<WarnModel>) session
			.createQuery("from WarnModel where guild_id = :guild and user_id = :user")
			.setParameter("guild", guild)
			.setParameter("user", user).list();
		session.close();
		return warnModels;
	}

	@Override
	public void save(final WarnModel warnModel) {
		final Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.save(warnModel);
		session.getTransaction().commit();
		session.close();
	}

	@Override
	public void update(final WarnModel warnModel) {
		final Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.update(warnModel);
		session.getTransaction().commit();
		session.close();
	}

	@Override
	public void delete(final WarnModel warnModel) {
		final Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.delete(warnModel);
		session.getTransaction().commit();
		session.close();
	}
}
