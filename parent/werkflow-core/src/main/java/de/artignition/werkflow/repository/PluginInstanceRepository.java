package de.artignition.werkflow.repository;

import de.artignition.werkflow.domain.PluginInstance;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PluginInstanceRepository extends
		JpaRepository<PluginInstance, UUID>, JpaSpecificationExecutor<PluginInstance> {

}
