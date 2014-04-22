package de.artignition.werkflow.domain;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

@MappedSuperclass
public class Domain implements Serializable {
	
	private static final long serialVersionUID = 3152833204793526867L;
	
	@Id
	@GenericGenerator(name="uuid", strategy="uuid2")
	@GeneratedValue(generator="uuid")
	protected UUID id;
	
	public UUID getId() {
		return this.id;
	}
	
	public void setId(UUID id) {
		this.id = id;
	}
}
