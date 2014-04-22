package de.artignition.werkflow.domain;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.joda.time.LocalDateTime;

@Entity
@Table(name = "JobInstance")
public class JobInstance extends Domain {

	private static final long serialVersionUID = -1311817993697760186L;

	@Basic(fetch = FetchType.EAGER)
	private LocalDateTime dateCreated;

	@Basic(fetch = FetchType.EAGER)
	private LocalDateTime dateStarted;
	
	@Basic(fetch = FetchType.EAGER)
	private LocalDateTime dateFinished;
	
	@OneToMany(mappedBy = "jobInstance", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<PluginInstance>	pluginInstances;
	
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "JobDescriptor_id", referencedColumnName = "id")
	private JobDescriptor descriptor;
	
	public LocalDateTime getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(LocalDateTime dateCreated) {
		this.dateCreated = dateCreated;
	}

	public LocalDateTime getDateFinished() {
		return dateFinished;
	}

	public void setDateFinished(LocalDateTime dateFinished) {
		this.dateFinished = dateFinished;
	}

	public List<PluginInstance> getPluginInstances() {
		return pluginInstances;
	}

	public void setPluginInstances(List<PluginInstance> pluginInstances) {
		this.pluginInstances = pluginInstances;
	}

	public JobDescriptor getDescriptor() {
		return descriptor;
	}

	public void setDescriptor(JobDescriptor descriptor) {
		this.descriptor = descriptor;
	}

	public LocalDateTime getDateStarted() {
		return dateStarted;
	}

	public void setDateStarted(LocalDateTime dateStarted) {
		this.dateStarted = dateStarted;
	}
}
