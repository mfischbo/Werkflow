package de.artignition.werkflow.repo;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import de.artignition.werkflow.domain.JobDescriptor;

public interface JobDescriptorRepo extends
		MongoRepository<JobDescriptor, ObjectId> {

}