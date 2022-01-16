package eu.u032.service;

import eu.u032.dao.impl.WarnDaoImpl;
import eu.u032.model.WarnModel;

import java.util.List;

public class WarnService {
	private final WarnDaoImpl warnDao = new WarnDaoImpl();

	public WarnModel findById(final long id) {
		return warnDao.findById(id);
	}

	public List<WarnModel> findAllByGuildAndUser(final long guild, final long user) {
		return warnDao.findAllByGuildAndUser(guild, user);
	}

	public void save(final WarnModel warnModel) {
		warnDao.save(warnModel);
	}

	public void update(final WarnModel warnModel) {
		warnDao.update(warnModel);
	}

	public void delete(final WarnModel warnModel) {
		warnDao.delete(warnModel);
	}
}
