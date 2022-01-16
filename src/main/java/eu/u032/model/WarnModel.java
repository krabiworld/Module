package eu.u032.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "warns")
@Getter
@Setter
public class WarnModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "guild_id")
	private long guild;

	@Column(name = "user_id")
	private long user;

	private String reason;
}
