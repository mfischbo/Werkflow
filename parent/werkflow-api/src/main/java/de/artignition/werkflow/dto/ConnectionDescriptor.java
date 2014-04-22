package de.artignition.werkflow.dto;

import java.io.Serializable;

public class ConnectionDescriptor implements Serializable {
	
	private static final long serialVersionUID = -5865979704164194865L;
	private	int			number;
	private String		type;
	public ConnectionDescriptor() {
		
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
