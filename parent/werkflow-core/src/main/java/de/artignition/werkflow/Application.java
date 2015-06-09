package de.artignition.werkflow;

import org.bson.types.ObjectId;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;

@SpringBootApplication
@EnableAutoConfiguration
@Configuration
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class);
	}
	
	@Bean
	public Module getObjectIdSerializer() {
		SimpleModule sm = new SimpleModule();
		sm.addSerializer(ObjectId.class, new ObjectIdSerializer());
		return sm;
	}
	
	@Bean
	public Module getJacksonJodaModule() {
		return new JodaModule();
	}
	
}