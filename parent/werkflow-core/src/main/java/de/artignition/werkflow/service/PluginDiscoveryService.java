package de.artignition.werkflow.service;

import de.artignition.werkflow.dto.PluginDescriptor;
import de.artignition.werkflow.util.AnnotationPluginScanner;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PluginDiscoveryService {

	@Autowired
	private	AnnotationPluginScanner		scanner;

	public List<PluginDescriptor>		getAllPlugins() {
		return scanner.getPlugins();
	}
}
