package eu.u032.dao;

import eu.u032.model.WarnModel;

import java.util.List;

public interface WarnDao {
	WarnModel findById(final long id);

	List<WarnModel> findAllByGuildAndUser(final long guild, final long user);

	void save(final WarnModel warnModel);

	void update(final WarnModel warnModel);

	void delete(final WarnModel warnModel);
}
