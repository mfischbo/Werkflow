package de.artignition.werkflow.config;

import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.core.env.StandardEnvironment;

public class WerkflowEnvironment {

	private StandardEnvironment		env;
	
	public WerkflowEnvironment(String[] args) {

		env = new StandardEnvironment();
		MutablePropertySources sources = env.getPropertySources();
		SimpleCommandLinePropertySource clips = new SimpleCommandLinePropertySource(args);
		sources.addLast(clips);
	}
	
	public StandardEnvironment getEnvironment() {
		return this.env;
	}
}
