package de.artignition.werkflow.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Connection")
public class Connection extends Domain {

	
	private static final long serialVersionUID = 3924774651557633098L;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "SourcePlugin_Id", referencedColumnName = "id")
	private JobPlugin	source;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "TargetPlugin_Id", referencedColumnName = "id")
	private JobPlugin	target;
	
	private int			sourcePort;
	private int			targetPort;
	
	public Connection() {
		
	}

	public JobPlugin getSource() {
		return source;
	}

	public void setSource(JobPlugin source) {
		this.source = source;
	}

	public JobPlugin getTarget() {
		return target;
	}

	public void setTarget(JobPlugin target) {
		this.target = target;
	}

	public int getSourcePort() {
		return sourcePort;
	}

	public void setSourcePort(int sourcePort) {
		this.sourcePort = sourcePort;
	}

	public int getTargetPort() {
		return targetPort;
	}

	public void setTargetPort(int targetPort) {
		this.targetPort = targetPort;
	}
	
	
}
