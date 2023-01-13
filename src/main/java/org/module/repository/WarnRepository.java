package org.module.repository;

import org.module.model.WarnModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarnRepository extends JpaRepository<WarnModel, Long> {
	WarnModel findById(long id);

	List<WarnModel> findAllByGuildAndUser(long guild, long user);
}
