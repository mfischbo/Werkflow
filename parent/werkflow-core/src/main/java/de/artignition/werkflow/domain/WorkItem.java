package de.artignition.werkflow.domain;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "WorkItem")
public class WorkItem extends Domain {

	private static final long serialVersionUID = 1130515417366535904L;

	@ManyToOne
	@JoinColumn(name = "ownerInstance_Id", referencedColumnName = "id")
	private PluginInstance owner;

	@Basic(fetch = FetchType.EAGER)
	private Integer			sourceOutput;
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private byte[]		payload;
	
	transient Object		payloadObject;
	
	public PluginInstance getOwner() {
		return owner;
	}
	public void setOwner(PluginInstance owner) {
		this.owner = owner;
	}
	public byte[] getPayload() {
		return payload;
	}
	public void setPayload(byte[] payload) {
		this.payload = payload;
	}
	public Integer getSourceOutput() {
		return sourceOutput;
	}
	public void setSourceOutput(Integer sourceOutput) {
		this.sourceOutput = sourceOutput;
	}

	@Transient
	public Object getObject() {
		return this.payloadObject;
	}
	
	@Transient
	public void setObject(Object o) {
		this.payloadObject = o;
		// NOTE: Do not serialize here!
		// Serialization into the payload byte[] is done outside this class
	}
}
