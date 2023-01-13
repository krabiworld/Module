package org.module.service.impl;

import net.dv8tion.jda.api.entities.Member;
import org.module.model.WarnModel;
import org.module.repository.WarnRepository;
import org.module.service.ModerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModerationServiceImpl implements ModerationService {
	private final WarnRepository warnRepository;

	@Autowired
	public ModerationServiceImpl(WarnRepository warnRepository) {
		this.warnRepository = warnRepository;
	}

	@Override
	@Cacheable("warn")
	public WarnModel getWarn(long id) {
		return warnRepository.findById(id);
	}

	@Override
	@Cacheable("warns")
	public List<WarnModel> getWarns(Member member) {
		return warnRepository.findAllByGuildAndUser(member.getGuild().getIdLong(), member.getIdLong());
	}

	@Override
	public long warn(Member member, String reason) {
		WarnModel warn = new WarnModel();
		warn.setGuild(member.getGuild().getIdLong());
		warn.setUser(member.getIdLong());
		warn.setReason(reason);
		warnRepository.saveAndFlush(warn);
		return warn.getId();
	}

	@Override
	public void removeWarn(WarnModel warnModel) {
		warnRepository.delete(warnModel);
	}
}
