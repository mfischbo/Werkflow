package de.artignition.werkflow.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(WorkItem.class)
public abstract class WorkItem_ extends de.artignition.werkflow.domain.Domain_ {

	public static volatile SingularAttribute<WorkItem, PluginInstance> owner;
	public static volatile SingularAttribute<WorkItem, byte[]> payload;
	public static volatile SingularAttribute<WorkItem, Integer> sourceOutput;

}

