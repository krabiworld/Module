package org.module.repository;

import org.module.model.GuildModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuildRepository extends JpaRepository<GuildModel, Long> {
	GuildModel findById(long id);
}
