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

package org.module.service;

import net.dv8tion.jda.api.entities.Member;
import org.module.model.WarnModel;

import java.util.List;

public interface ModerationService {
	boolean isModerator(Member member);

	WarnModel getWarn(long id);

	List<WarnModel> getWarns(Member member);

	long warn(Member member, String reason);

	void removeWarn(WarnModel warnModel);
}
