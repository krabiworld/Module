package eu.u032.models;

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
	@Column(name = "id")
	private long id;

	@Column(name = "user_id")
	private long user;

	@Column(name = "reason")
	private String reason;
}
