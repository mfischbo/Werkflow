package de.artignition.werkflow.repository;

import de.artignition.werkflow.domain.JobInstance;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface JobInstanceRepository extends JpaRepository<JobInstance, UUID>, JpaSpecificationExecutor<JobInstance> {

}
