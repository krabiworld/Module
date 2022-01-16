package eu.u032.service;

import eu.u032.dao.impl.OwnerDaoImpl;
import eu.u032.model.OwnerModel;

public class OwnerService {
	private final OwnerDaoImpl ownerDao = new OwnerDaoImpl();

	public OwnerModel findById(final long id) {
		return ownerDao.findById(id);
	}

	public void save(final OwnerModel ownerModel) {
		ownerDao.save(ownerModel);
	}

	public void delete(final OwnerModel ownerModel) {
		ownerDao.delete(ownerModel);
	}
}
