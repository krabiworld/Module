package org.module.structure;

import lombok.Getter;

@Getter
public enum Category {
	INFORMATION("Information"),
	MODERATION("Moderation"),
	SETTINGS("Settings"),
	UTILITIES("Utilities");

	private final String name;

	Category(String name) {
		this.name = name;
	}
}
