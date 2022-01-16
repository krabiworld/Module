package eu.u032.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "owners")
@Getter
@Setter
public class OwnerModel {
	@Id
	private long id;
}
