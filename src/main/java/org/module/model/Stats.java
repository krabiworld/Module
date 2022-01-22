package org.module.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "stats")
@Getter
@Setter
public class Stats {
	@Id
	private long id;

	@Column(name = "executed_commands")
	private long executedCommands;
}
