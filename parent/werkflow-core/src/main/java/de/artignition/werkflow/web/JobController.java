package de.artignition.werkflow.web;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.artignition.werkflow.domain.JobDescriptor;
import de.artignition.werkflow.domain.JobInstance;
import de.artignition.werkflow.service.JobDescriptorService;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

	@Autowired
	private JobDescriptorService			service;
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public Page<JobDescriptor> getAllJobs(@PageableDefault Pageable page) {
		return service.getAllJobDescriptors(page);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public JobDescriptor getJobById(@PathVariable("id") ObjectId id) {
		return service.getJobDescriptorById(id);
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	public JobDescriptor createJob(@RequestBody JobDescriptor job) {
		return service.createJobDescriptor(job);
	}
	
	@RequestMapping(value = "", method = RequestMethod.PATCH)
	public JobDescriptor updateJob(@RequestBody JobDescriptor job) {
		return service.updateJobDescriptor(job);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void deleteJob(@PathVariable("id") ObjectId id) {
		JobDescriptor jd = service.getJobDescriptorById(id);
		service.deleteJobDescriptor(jd);
	}
	
	@RequestMapping(value = "/{id}/instance", method = RequestMethod.POST) 
	public JobInstance createJobInstance(@PathVariable("id") ObjectId id) {
		JobDescriptor jd = service.getJobDescriptorById(id);
		return service.createJobInstance(jd);
	}
}