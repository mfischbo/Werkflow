package de.artignition.werkflow.baseplugins;

import de.artignition.werkflow.plugin.ExitSuccess;
import de.artignition.werkflow.plugin.Plugin;
import de.artignition.werkflow.plugin.PluginExitStatus;
import de.artignition.werkflow.plugin.annotation.Input;
import de.artignition.werkflow.plugin.annotation.Output;
import de.artignition.werkflow.plugin.annotation.PluginInfo;
import de.artignition.werkflow.plugin.annotation.PluginParameter;

@PluginInfo(name = "Upper Case Plugin", description = "Uppercases a string")
public class StringToUpperCasePlugin implements Plugin {

	@Input(number=0)
	private String	stringToBeUppercased;

	@Output(number=0)
	private String	output;
	
	@PluginParameter(name = "Username", description = "The username for the FTP Connection")
	private String		username;
	
	
	public PluginExitStatus execute() {
		//this.output = this.stringToBeUppercased.toUpperCase();

		this.output = "HELL YEAH!";
		//return new ExitIdle();
		return new ExitSuccess();
	}


	@Override
	public void beforeExecution() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void afterExecution() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void beforeWorkItemProcessing() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void afterWorkItemProcessing() {
		// TODO Auto-generated method stub
		
	}
	
}
