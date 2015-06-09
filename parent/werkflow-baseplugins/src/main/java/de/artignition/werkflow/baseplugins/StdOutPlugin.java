package de.artignition.werkflow.baseplugins;

import de.artignition.werkflow.plugin.ExitIdle;
import de.artignition.werkflow.plugin.Plugin;
import de.artignition.werkflow.plugin.PluginExitStatus;
import de.artignition.werkflow.plugin.annotation.Input;
import de.artignition.werkflow.plugin.annotation.Output;
import de.artignition.werkflow.plugin.annotation.PluginInfo;

@PluginInfo(name = "Standard Output Plugin", description = "Writes stuff to stdout")
public class StdOutPlugin implements Plugin {

	@Input(number=0)
	private String string;
	
	@Output(number=0)
	private String output;
	
	public PluginExitStatus execute() {
		System.out.println(string);
		return new ExitIdle();
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
