package de.artignition.werkflow.baseplugins;

import de.artignition.werkflow.plugin.ExitIdle;
import de.artignition.werkflow.plugin.PluginExitStatus;
import de.artignition.werkflow.plugin.annotation.Input;
import de.artignition.werkflow.plugin.annotation.Output;
import de.artignition.werkflow.plugin.annotation.Plugin;
import de.artignition.werkflow.plugin.annotation.PluginParameter;

@Plugin(name = "Upper Case Plugin", description = "Uppercases a string")
public class StringToUpperCasePlugin {

	@Input(number=0)
	private String	stringToBeUppercased;

	@Output(number=0)
	private String	output;
	
	@PluginParameter(name = "Username", description = "The username for the FTP Connection")
	private String		username;
	
	
	public PluginExitStatus execute() {
		this.output = this.stringToBeUppercased.toUpperCase();
		return new ExitIdle();
	}
	
}
