package de.artignition.werkflow.repository;


import de.artignition.werkflow.domain.WorkItem;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface WorkItemRepository extends
		JpaRepository<WorkItem, UUID>, JpaSpecificationExecutor<WorkItem> {

}
