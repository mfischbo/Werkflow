package de.artignition.werkflow.service;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import de.artignition.werkflow.domain.JobDescriptor;
import de.artignition.werkflow.engine.JobInstanceExecutor;
import de.artignition.werkflow.repo.JobDescriptorRepo;

@Service
public class JobDescriptorService {

	@Autowired
	private JobDescriptorRepo		jobRepo;
	
	public Page<JobDescriptor> getAllJobDescriptors(Pageable page) {
		return jobRepo.findAll(page);
	}
	
	public JobDescriptor getJobDescriptorById(ObjectId id) {
		return jobRepo.findOne(id);
	}
	
	public JobDescriptor createJobDescriptor(JobDescriptor jd) {
		return jobRepo.save(jd);
	}

	public void createJobInstance(JobDescriptor jd) {
		JobInstanceExecutor exec = new JobInstanceExecutor(jd);
		exec.startJob();
	}
	
	public JobDescriptor updateJobDescriptor(JobDescriptor jd) {
		return jobRepo.save(jd);
	}
	
	public void deleteJobDescriptor(JobDescriptor jd) {
		jobRepo.delete(jd);
	}
}