package de.artignition.werkflow.domain;

import java.io.Serializable;
import javax.annotation.Generated;
import javax.persistence.metamodel.MapAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PluginInstance.class)
public abstract class PluginInstance_ extends de.artignition.werkflow.domain.Domain_ {

	public static volatile SingularAttribute<PluginInstance, JobPlugin> jobPlugin;
	public static volatile SingularAttribute<PluginInstance, Integer> stepCount;
	public static volatile SingularAttribute<PluginInstance, JobInstance> jobInstance;
	public static volatile SingularAttribute<PluginInstance, PluginExecutionState> state;
	public static volatile MapAttribute<PluginInstance, String, Serializable> parameters;

}

