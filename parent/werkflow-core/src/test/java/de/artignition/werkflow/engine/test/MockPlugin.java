package de.artignition.werkflow.engine.test;

import de.artignition.werkflow.plugin.ExitIdle;
import de.artignition.werkflow.plugin.Plugin;
import de.artignition.werkflow.plugin.PluginExitStatus;
import de.artignition.werkflow.plugin.annotation.Input;
import de.artignition.werkflow.plugin.annotation.Output;
import de.artignition.werkflow.plugin.annotation.PluginParameter;

public class MockPlugin implements Plugin {

	@Input(number = 1)
	private ComplexInputGraph		input1;
	
	@Input(number = 2)
	private ComplexInputGraph		input2;
	
	@Output(number = 1)
	private ComplexOutputGraph		output1;
	
	@Output(number = 2)
	private ComplexOutputGraph		output2;

	@PluginParameter
	private String					username;
	
	@PluginParameter
	private String					password;
	
	@Override
	public PluginExitStatus execute() {

		ComplexOutputGraph o1 = new ComplexOutputGraph();
		o1.setI1(input1);
		o1.setI2(input2);
		this.output1 = o1;
		
		ComplexOutputGraph o2 = new ComplexOutputGraph();
		o2.setI1(input1);
		o2.setI2(input2);
		this.output2 = o2;
		
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
