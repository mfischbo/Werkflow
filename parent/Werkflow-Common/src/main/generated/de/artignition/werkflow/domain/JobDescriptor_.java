package de.artignition.werkflow.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.joda.time.LocalDateTime;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(JobDescriptor.class)
public abstract class JobDescriptor_ extends de.artignition.werkflow.domain.Domain_ {

	public static volatile SingularAttribute<JobDescriptor, LocalDateTime> dateModified;
	public static volatile ListAttribute<JobDescriptor, JobPlugin> plugins;
	public static volatile SingularAttribute<JobDescriptor, String> description;
	public static volatile SingularAttribute<JobDescriptor, String> name;
	public static volatile SingularAttribute<JobDescriptor, LocalDateTime> dateCreated;

}

