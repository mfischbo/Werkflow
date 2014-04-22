package de.artignition.werkflow.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import de.artignition.werkflow.domain.JobPlugin;

public interface JobPluginRepository extends JpaRepository<JobPlugin, UUID> {

}
