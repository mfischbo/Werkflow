package de.artignition.werkflow.domain;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.joda.time.LocalDateTime;

@Entity
@Table(name = "JobDescriptor")
public class JobDescriptor extends Domain {

	private static final long serialVersionUID = 5457994484391874746L;

	@Basic(fetch = FetchType.EAGER)
	private String name;
	
	@Basic(fetch = FetchType.EAGER)
	private String description;
	
	@Basic(fetch = FetchType.EAGER)
	private LocalDateTime dateCreated;
	
	@Basic(fetch = FetchType.EAGER)
	private LocalDateTime dateModified;
	
	@OneToMany(mappedBy = "jobDescriptor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<JobPlugin>	plugins;
	
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
	public LocalDateTime getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(LocalDateTime dateCreated) {
		this.dateCreated = dateCreated;
	}
	public LocalDateTime getDateModified() {
		return dateModified;
	}
	public void setDateModified(LocalDateTime dateModified) {
		this.dateModified = dateModified;
	}
	public List<JobPlugin> getPlugins() {
		return plugins;
	}
	public void setPlugins(List<JobPlugin> plugins) {
		this.plugins = plugins;
	}
}
