package org.module.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "stats")
@Getter
@Setter
public class StatsModel {
	@Id
	private long id;

	@Column(name = "executed_commands")
	private long executedCommands;
}
