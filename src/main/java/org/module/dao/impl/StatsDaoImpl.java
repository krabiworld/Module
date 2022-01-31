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
import org.module.dao.StatsDao;
import org.module.model.Stats;
import org.module.util.SessionFactoryUtil;

public class StatsDaoImpl implements StatsDao {
	private final static SessionFactory getSessionFactory = SessionFactoryUtil.getSessionFactory();

	@Override
	public Stats findById(long id) {
		Session session = getSessionFactory.openSession();
		Stats stats = session.get(Stats.class, id);
		session.close();
		return stats;
	}

	@Override
	public void update(Stats stats) {
		Session session = getSessionFactory.openSession();
		session.beginTransaction();
		session.update(stats);
		session.getTransaction().commit();
		session.close();
	}
}
