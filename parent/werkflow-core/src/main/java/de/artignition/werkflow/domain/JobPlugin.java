package de.artignition.werkflow.domain;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "JobPlugin")
public class JobPlugin extends Domain {

	private static final long serialVersionUID = -8875610320923453937L;

	@Basic(fetch = FetchType.EAGER)
	private String classname;

	@ManyToOne(optional = false)
	@JoinColumn(name = "JobDescriptor_Id", referencedColumnName = "id")
	private JobDescriptor jobDescriptor;
	
	@ElementCollection
	private Map<String, byte[]>	parameters;

	@Basic(fetch = FetchType.EAGER)
	private Integer x;
	
	@Basic(fetch = FetchType.EAGER)
	private Integer y;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "source")
	private Set<Connection>		outboundConnections;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "target")
	private Set<Connection>		inboundConnections;
	
	public JobPlugin() {
		this.parameters = new LinkedHashMap<String, byte[]>();
		this.inboundConnections = new HashSet<Connection>();
		this.outboundConnections= new HashSet<Connection>();
	}
	
	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public JobDescriptor getJobDescriptor() {
		return jobDescriptor;
	}

	public void setJobDescriptor(JobDescriptor jobDescriptor) {
		this.jobDescriptor = jobDescriptor;
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

	public Map<String, byte[]> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, byte[]> parameters) {
		this.parameters = parameters;
	}

	public Set<Connection> getOutboundConnections() {
		return outboundConnections;
	}

	public void setOutboundConnections(Set<Connection> outboundConnections) {
		this.outboundConnections = outboundConnections;
	}

	public Set<Connection> getInboundConnections() {
		return inboundConnections;
	}

	public void setInboundConnections(Set<Connection> inboundConnections) {
		this.inboundConnections = inboundConnections;
	}
}
