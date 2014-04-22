package de.artignition.werkflow.demon;

import de.artignition.werkflow.config.CoreConfig;
import de.artignition.werkflow.config.WerkflowEnvironment;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {

	private AnnotationConfigApplicationContext		ctx;

	public Application(String[] args) {
		
		// pull up the parent spring context
		WerkflowEnvironment env = new WerkflowEnvironment(args);
		ctx = new AnnotationConfigApplicationContext();
		ctx.setEnvironment(env.getEnvironment());
		ctx.register(CoreConfig.class);
		ctx.refresh();
	}

	public void execute() {
		
		while (true) {

			try {
				Thread.sleep(1000L);
				Thread.yield();
			} catch (Exception ex) {
				
			}
			

		}
	}
}