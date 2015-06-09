package de.artignition.werkflow.engine;

import de.artignition.werkflow.domain.JobPlugin;
import de.artignition.werkflow.domain.PluginInstance;

public class PluginTuple {

	private JobPlugin		plugin;
	private PluginInstance	instance;
	
	public PluginTuple(JobPlugin plugin, PluginInstance instance) {
		this.plugin = plugin;
		this.instance = instance;
	}
	
	public JobPlugin getPlugin() {
		return this.plugin;
	}
	
	public PluginInstance getInstance() {
		return this.instance;
	}
}