package org.module.repository;

import org.module.model.StatsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatsRepository extends JpaRepository<StatsModel, Long> {
	StatsModel findById(long id);
}
