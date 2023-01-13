package org.module.service;

import net.dv8tion.jda.api.entities.Member;
import org.module.model.WarnModel;

import java.util.List;

public interface ModerationService {
	WarnModel getWarn(long id);

	List<WarnModel> getWarns(Member member);

	long warn(Member member, String reason);

	void removeWarn(WarnModel warnModel);
}
