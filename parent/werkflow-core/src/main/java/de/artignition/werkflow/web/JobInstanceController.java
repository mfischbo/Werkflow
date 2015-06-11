package de.artignition.werkflow.web;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.artignition.werkflow.domain.JobInstance;
import de.artignition.werkflow.service.JobDescriptorService;

@RestController
@RequestMapping(value = "/api/job-instances")
public class JobInstanceController {

	@Autowired
	private JobDescriptorService		service;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public Collection<JobInstance> getAllInstances() {
		return service.getAllJobInstances();
	}
}
