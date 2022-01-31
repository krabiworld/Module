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

package org.module.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.module.Module;
import org.module.model.GuildConfig;
import org.module.model.Owner;
import org.module.model.Stats;
import org.module.model.Warn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionFactoryUtil {
	private static final Logger logger = LoggerFactory.getLogger(Module.class);
	private static SessionFactory sessionFactory;

	public static SessionFactory getSessionFactory() {
		if (sessionFactory == null) {
			try {
				Configuration configure = new Configuration();
				configure.addAnnotatedClass(GuildConfig.class);
				configure.addAnnotatedClass(Owner.class);
				configure.addAnnotatedClass(Stats.class);
				configure.addAnnotatedClass(Warn.class);
				StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configure.getProperties());
				sessionFactory = configure.buildSessionFactory(builder.build());
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		return sessionFactory;
	}
}
