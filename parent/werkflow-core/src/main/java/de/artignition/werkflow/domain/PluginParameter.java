package de.artignition.werkflow.domain;

import java.io.Serializable;

public class PluginParameter implements Serializable {

	private static final long serialVersionUID = 1868134690987225632L;
	private String fieldname;
	private String type;
	private String nicename;
	private String description;

	public PluginParameter() {
		
	}

	public String getFieldname() {
		return fieldname;
	}

	public void setFieldname(String fieldname) {
		this.fieldname = fieldname;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNicename() {
		return nicename;
	}

	public void setNicename(String nicename) {
		this.nicename = nicename;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
