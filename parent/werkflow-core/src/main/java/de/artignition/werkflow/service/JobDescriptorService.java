package de.artignition.werkflow.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import de.artignition.werkflow.domain.JobDescriptor;
import de.artignition.werkflow.domain.JobInstance;
import de.artignition.werkflow.engine.JobInstanceExecutor;
import de.artignition.werkflow.repo.JobDescriptorRepo;

@Service
public class JobDescriptorService {

	@Autowired
	private JobDescriptorRepo		jobRepo;
	
	private Map<ObjectId, JobInstance>		jobInstanceRepo;

	@PostConstruct
	private void init() {
		this.jobInstanceRepo = new HashMap<ObjectId, JobInstance>();
	}
	
	public Page<JobDescriptor> getAllJobDescriptors(Pageable page) {
		return jobRepo.findAll(page);
	}
	
	public JobDescriptor getJobDescriptorById(ObjectId id) {
		return jobRepo.findOne(id);
	}
	
	public JobDescriptor createJobDescriptor(JobDescriptor jd) {
		return jobRepo.save(jd);
	}

	public JobInstance createJobInstance(JobDescriptor jd) {
		JobInstanceExecutor exec = new JobInstanceExecutor(jd);
		JobInstance i = exec.getWrappedInstance();
		jobInstanceRepo.put(i.getId(), i);
		exec.startJob();
		return i;
	}
	
	public Collection<JobInstance> getAllJobInstances() {
		return jobInstanceRepo.values();
	}
	
	public JobDescriptor updateJobDescriptor(JobDescriptor jd) {
		return jobRepo.save(jd);
	}
	
	public void deleteJobDescriptor(JobDescriptor jd) {
		jobRepo.delete(jd);
	}
}