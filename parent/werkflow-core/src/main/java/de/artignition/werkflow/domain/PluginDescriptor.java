package de.artignition.werkflow.domain;

import java.io.Serializable;

public class PluginDescriptor implements Serializable {

	private static final long serialVersionUID = -9128917637550869015L;
	
	private String			name;
	private String			description;
	private String			classname;
	private String			version;
	
	private ConnectionDescriptor[]		inputs;
	private ConnectionDescriptor[]		outputs;
	
	private PluginParameter[]			params;
	
	
	public PluginDescriptor() {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public ConnectionDescriptor[] getInputs() {
		return inputs;
	}

	public void setInputs(ConnectionDescriptor[] inputs) {
		this.inputs = inputs;
	}

	public ConnectionDescriptor[] getOutputs() {
		return outputs;
	}

	public void setOutputs(ConnectionDescriptor[] outputs) {
		this.outputs = outputs;
	}

	public PluginParameter[] getParams() {
		return params;
	}

	public void setParams(PluginParameter[] params) {
		this.params = params;
	}
}
