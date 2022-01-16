package eu.u032.dao;

import eu.u032.model.OwnerModel;

public interface OwnerDao {
	OwnerModel findById(final long id);

	void save(final OwnerModel ownerModel);

	void delete(final OwnerModel ownerModel);
}
