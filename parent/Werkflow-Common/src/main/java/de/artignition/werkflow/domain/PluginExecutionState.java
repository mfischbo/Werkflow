package de.artignition.werkflow.domain;

public enum PluginExecutionState {

	/* set when the plugin is actually working on an item */
	PROCESSING,		
	
	/* 
	 * set when the plugin has finished a workitem and is willing
	 * to accept another item
	 */
	IDLE,
	
	/*
	 * set  by the executor, when no more work is in the pipe and the plugins last
	 *    exit status was idle or paused
	 */
	SUCCEEDED,
	
	/*
	 * Set when the plugin has failed to proccess a working item or in preparation
	 */
	FAILED,
	
	/*
	 * Set from the executor, when the job instance / engine has been paused
	 */
	PAUSED
}