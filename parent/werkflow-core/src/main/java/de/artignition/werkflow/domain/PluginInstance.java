package de.artignition.werkflow.domain;

import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "PluginInstance")
public class PluginInstance extends Domain {

	private static final long serialVersionUID = 934356864931467033L;

	@Basic(fetch = FetchType.EAGER)
	private Integer stepCount;
	
	@Basic(fetch = FetchType.EAGER)
	@Enumerated(EnumType.STRING)
	private PluginExecutionState		state;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "JobInstance_id", referencedColumnName = "id")
	private JobInstance			jobInstance;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "JobPlugin_id", referencedColumnName = "id")
	private JobPlugin			jobPlugin;
	
	@ElementCollection
	private Map<String, byte[]>	parameters;

	public Integer getStepCount() {
		return stepCount;
	}

	public void setStepCount(Integer stepCount) {
		this.stepCount = stepCount;
	}

	public PluginExecutionState getState() {
		return state;
	}

	public void setState(PluginExecutionState state) {
		this.state = state;
	}

	public JobInstance getJobInstance() {
		return jobInstance;
	}

	public void setJobInstance(JobInstance jobInstance) {
		this.jobInstance = jobInstance;
	}

	public JobPlugin getJobPlugin() {
		return jobPlugin;
	}

	public void setJobPlugin(JobPlugin jobPlugin) {
		this.jobPlugin = jobPlugin;
	}

	public Map<String, byte[]> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, byte[]> parameters) {
		this.parameters = parameters;
	}
}
