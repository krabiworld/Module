package eu.u032.dao.impl;

import eu.u032.dao.OwnerDao;
import eu.u032.model.OwnerModel;
import eu.u032.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class OwnerDaoImpl implements OwnerDao {
	final private static SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

	@Override
	public OwnerModel findById(final long id) {
		final Session session = sessionFactory.openSession();
		final OwnerModel ownerModel = session.get(OwnerModel.class, id);
		session.close();
		return ownerModel;
	}

	@Override
	public void save(final OwnerModel ownerModel) {
		final Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.save(ownerModel);
		session.getTransaction().commit();
		session.close();
	}

	@Override
	public void delete(final OwnerModel ownerModel) {
		final Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.delete(ownerModel);
		session.getTransaction().commit();
		session.close();
	}
}
