package de.artignition.werkflow.domain;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.bson.types.ObjectId;

public class JobPlugin {

	private ObjectId			id;
	
	private String 				classname;
	private Map<String, Object>	parameters;
	private Integer 			x;
	private Integer 			y;
	private Set<Connection>		connections;
	
	public JobPlugin() {
		this.id = new ObjectId();
		this.parameters = new LinkedHashMap<>();
		this.connections = new LinkedHashSet<>();
	}
	
	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public Integer getX() {
		return x;
	}

	public void setX(Integer x) {
		this.x = x;
	}

	public Integer getY() {
		return y;
	}

	public void setY(Integer y) {
		this.y = y;
	}

	public Map<String, Object> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

	public Set<Connection> getConnections() {
		return connections;
	}

	public void setConnections(Set<Connection> connections) {
		this.connections = connections;
	}
}