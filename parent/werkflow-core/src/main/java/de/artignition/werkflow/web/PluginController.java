package de.artignition.werkflow.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.artignition.werkflow.dto.PluginDescriptor;
import de.artignition.werkflow.service.PluginDiscoveryService;

@RestController
@RequestMapping(value = "/plugins")
public class PluginController {

	@Autowired
	private PluginDiscoveryService		service;
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<PluginDescriptor> getAllPlugins() {
		return service.getAllPlugins();
	}
}
