package de.artignition.werkflow.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Connection.class)
public abstract class Connection_ extends de.artignition.werkflow.domain.Domain_ {

	public static volatile SingularAttribute<Connection, JobPlugin> source;
	public static volatile SingularAttribute<Connection, Integer> targetPort;
	public static volatile SingularAttribute<Connection, Integer> sourcePort;
	public static volatile SingularAttribute<Connection, JobPlugin> target;

}

