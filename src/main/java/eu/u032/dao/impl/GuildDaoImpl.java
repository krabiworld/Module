package eu.u032.dao.impl;

import eu.u032.dao.GuildDao;
import eu.u032.model.GuildModel;
import eu.u032.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class GuildDaoImpl implements GuildDao {
	final private static SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

	@Override
	public GuildModel findById(final long id) {
		final Session session = sessionFactory.openSession();
		final GuildModel guildModel = session.get(GuildModel.class, id);
		session.close();
		return guildModel;
	}

	@Override
	public void save(final GuildModel guildModel) {
		final Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.save(guildModel);
		session.getTransaction().commit();
		session.close();
	}

	@Override
	public void update(final GuildModel guildModel) {
		final Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.update(guildModel);
		session.getTransaction().commit();
		session.close();
	}

	@Override
	public void delete(final GuildModel guildModel) {
		final Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.delete(guildModel);
		session.getTransaction().commit();
		session.close();
	}
}
