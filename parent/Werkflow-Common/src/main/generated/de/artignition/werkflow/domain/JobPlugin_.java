package de.artignition.werkflow.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(JobPlugin.class)
public abstract class JobPlugin_ extends de.artignition.werkflow.domain.Domain_ {

	public static volatile SetAttribute<JobPlugin, Connection> inboundConnections;
	public static volatile SingularAttribute<JobPlugin, JobDescriptor> jobDescriptor;
	public static volatile SingularAttribute<JobPlugin, String> classname;
	public static volatile SingularAttribute<JobPlugin, Integer> y;
	public static volatile SetAttribute<JobPlugin, Connection> outboundConnections;
	public static volatile SingularAttribute<JobPlugin, Integer> x;

}

