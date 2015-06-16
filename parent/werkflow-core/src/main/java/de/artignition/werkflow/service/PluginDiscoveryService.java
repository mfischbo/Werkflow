package de.artignition.werkflow.service;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.artignition.werkflow.domain.PluginDescriptor;
import de.artignition.werkflow.util.AnnotationPluginScanner;

@Service
public class PluginDiscoveryService {

	@Autowired
	private	AnnotationPluginScanner		scanner;

	private List<PluginDescriptor>		_cache;
	
	public List<PluginDescriptor>		getAllPlugins() {
		if (_cache == null) {
			this._cache = new LinkedList<>();
			this._cache.addAll(scanner.getPlugins());
		}
		return _cache;
	}
}
