package de.artignition.werkflow.config;

import de.artignition.werkflow.io.MessageReceiver;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@ComponentScan(basePackages = "de.artignition.werkflow")
@EnableJpaRepositories(entityManagerFactoryRef = "werkflowEM")
@EnableTransactionManagement
public class CoreConfig {

	@Autowired
	private Environment env;
	
	@Bean
	public DataSource getWerkflowDS() {
		
		HikariConfig c = new HikariConfig();
		c.setMaximumPoolSize(100);
		c.setDataSourceClassName("org.h2.jdbcx.JdbcDataSource");
		c.setDataSourceProperties(new Properties());
		c.addDataSourceProperty("url", "jdbc:h2:./werkflowdb");
		c.addDataSourceProperty("user", "sa");
		c.addDataSourceProperty("password", "sa");
		HikariDataSource ds = new HikariDataSource(c);
		return ds;
		
	}
	
	@Bean
	public JpaVendorAdapter getHibernateJpaVendorAdapter() {
		HibernateJpaVendorAdapter va = new HibernateJpaVendorAdapter();
		va.setGenerateDdl(true);
		va.setShowSql(false);
		va.setDatabasePlatform("org.hibernate.dialect.H2Dialect");
		return va;
	}
	
	@Bean
	public PlatformTransactionManager transactionManager() throws Exception {
		JpaTransactionManager txMan = new JpaTransactionManager();
		txMan.setEntityManagerFactory(getWerkflowEM());
		txMan.setNestedTransactionAllowed(true);
		return txMan;
	}
	
	@Bean
	public HibernateExceptionTranslator getExceptionTranslator() {
		return new HibernateExceptionTranslator();
	}
	
	@Bean(name = "werkflowEM")
	public EntityManagerFactory getWerkflowEM() {
		LocalContainerEntityManagerFactoryBean lcemfb = new LocalContainerEntityManagerFactoryBean();
		lcemfb.setPersistenceProviderClass(HibernatePersistenceProvider.class);
		lcemfb.setPackagesToScan("de.artignition.werkflow.domain");
		
		lcemfb.setJpaVendorAdapter(getHibernateJpaVendorAdapter());
		lcemfb.setDataSource(getWerkflowDS());
		lcemfb.afterPropertiesSet();
		
		return lcemfb.getObject();
	}
	
	@Bean
	public MessageReceiver getMessageReceiver() {
		return new MessageReceiver();
	}
	
	
	@Bean
	MessageListenerAdapter getAdapter() {
		MessageListenerAdapter adapter = new MessageListenerAdapter(getMessageReceiver());
		adapter.setDefaultListenerMethod("handleMessage");
		return adapter;
	}

	@Bean
	ActiveMQConnectionFactory getActiveMQConnectionFactory() {
		String jmsUrl = env.getProperty("jmsUrl");
		if (jmsUrl == null)
			jmsUrl = "localhost:6060";
		
		ActiveMQConnectionFactory fact = new ActiveMQConnectionFactory();
		fact.setBrokerURL("vm:broker:tcp://" + jmsUrl);
		return fact;
	}
	
    @Bean
    DefaultMessageListenerContainer container() {
        DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
        container.setMessageListener(getAdapter());
        container.setConnectionFactory(getActiveMQConnectionFactory());
        container.setDestinationName("testdestination");
        container.setPubSubDomain(false);
        return container;
    }
}