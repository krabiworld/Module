/*
 * This file is part of Module.
 *
 * Module is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Module is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Module. If not, see <https://www.gnu.org/licenses/>.
 */

package org.module.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.module.dao.WarnDao;
import org.module.model.Warn;
import org.module.util.SessionFactoryUtil;

import java.util.List;

public class WarnDaoImpl implements WarnDao {
	private final static SessionFactory getSessionFactory = SessionFactoryUtil.getSessionFactory();

	@Override
	public Warn findById(long id) {
		Session session = getSessionFactory.openSession();
		Warn warn = session.get(Warn.class, id);
		session.close();
		return warn;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Warn> findAllByGuildAndUser(long guild, long user) {
		Session session = getSessionFactory.openSession();
		List<Warn> warns = (List<Warn>) session
			.createQuery("from Warn where guild = :guild and user = :user")
			.setParameter("guild", guild)
			.setParameter("user", user).list();
		session.close();
		return warns;
	}

	@Override
	public void save(Warn warn) {
		Session session = SessionFactoryUtil.getSessionFactory().openSession();
		session.beginTransaction();
		session.save(warn);
		session.getTransaction().commit();
		session.close();
	}

	@Override
	public void delete(Warn warn) {
		Session session = SessionFactoryUtil.getSessionFactory().openSession();
		session.beginTransaction();
		session.delete(warn);
		session.getTransaction().commit();
		session.close();
	}
}
