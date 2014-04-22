package de.artignition.werkflow.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import de.artignition.werkflow.domain.JobDescriptor;

public interface JobDescriptorRepository extends
		JpaRepository<JobDescriptor, UUID> {

}
