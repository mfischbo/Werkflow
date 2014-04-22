package de.artignition.werkflow.client.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;


@Configuration
@ComponentScan(basePackages = {"de.artignition.werkflow.client.controller", "de.artignition.werkflow.client.service"})
public class ApplicationConfig {

	
	@Bean
	ActiveMQConnectionFactory getActiveMQConnectionFactory() {
		String jmsUrl = null;		// TODO: Set via configuration 
		if (jmsUrl == null)
			jmsUrl = "localhost:6060";
		
		ActiveMQConnectionFactory fact = new ActiveMQConnectionFactory();
		fact.setBrokerURL("tcp://" + jmsUrl);
		return fact;
	}

	@Bean
	JmsTemplate getJMSMessageTemplate() {
		JmsTemplate retval = new JmsTemplate();
		retval.setConnectionFactory(getActiveMQConnectionFactory());
		retval.setDefaultDestinationName("testdestination");
		return retval;
	}
}
