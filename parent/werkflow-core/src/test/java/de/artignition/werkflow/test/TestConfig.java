package de.artignition.werkflow.test;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

@Configuration
public class TestConfig {

	
	@Bean
	ActiveMQConnectionFactory getMQConnectionFactory() {
		ActiveMQConnectionFactory fact = new ActiveMQConnectionFactory();
		fact.setBrokerURL("vm://localhost:6060");
		return fact;
	}
	
	
	@Bean
	JmsTemplate getJMSTemplate() {
		JmsTemplate retval = new JmsTemplate();
		retval.setConnectionFactory(getMQConnectionFactory());
		retval.setDefaultDestinationName("testdestination");
		return retval;
	}
}
