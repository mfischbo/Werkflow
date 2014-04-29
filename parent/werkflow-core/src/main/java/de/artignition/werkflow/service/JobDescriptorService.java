package de.artignition.werkflow.service;

import java.util.List;
import java.util.UUID;

import de.artignition.werkflow.domain.JobDescriptor;
import de.artignition.werkflow.repository.JobDescriptorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobDescriptorService {

	@Autowired
	private JobDescriptorRepository		jobDRepo;

	
	public List<JobDescriptor> getAllJobDescriptors() {
		return jobDRepo.findAll();
	}
	
	public JobDescriptor getJobDescriptorById(UUID id) {
		return jobDRepo.findOne(id);
	}
	
	public JobDescriptor createJobDescriptor(JobDescriptor jd) {
		return jobDRepo.saveAndFlush(jd);
	}
	
}
