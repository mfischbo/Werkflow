package de.artignition.werkflow.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import de.artignition.werkflow.domain.ConnectionDescriptor;
import de.artignition.werkflow.domain.PluginDescriptor;
import de.artignition.werkflow.domain.PluginParameter;
import de.artignition.werkflow.plugin.annotation.Input;
import de.artignition.werkflow.plugin.annotation.Output;
import de.artignition.werkflow.plugin.annotation.PluginInfo;

@Component
public class AnnotationPluginScanner {

	private List<PluginDescriptor> _plugins;
	
	private Logger	_log = LoggerFactory.getLogger(getClass());

	public AnnotationPluginScanner() {
		_plugins = new LinkedList<>();
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setUrls(ClasspathHelper.forClassLoader());
	
		_log.debug("Seeking on classpath for plugins annotated with Plugin.class");
		Reflections r = cb.build();
		Set<Class<?>> pluginClasses = r.getTypesAnnotatedWith(PluginInfo.class);
	
		_log.debug("Found " + pluginClasses.size() + " classes");
		for (Class<?> c : pluginClasses) {
			PluginDescriptor pd = new PluginDescriptor();
			pd.setClassname(c.getCanonicalName());
			for (Annotation a : c.getDeclaredAnnotations()) {
				if (a instanceof PluginInfo) {
					PluginInfo p = (PluginInfo) a;
					pd.setName(p.name());
					pd.setDescription(p.description());
				}
			}
			pd.setInputs(getConnectionDescriptors(c, Input.class));
			pd.setOutputs(getConnectionDescriptors(c, Output.class));
			pd.setParams(getPluginParameters(c));
			_plugins.add(pd);
		}
	}
	
	public List<PluginDescriptor> getPlugins() {
		return _plugins;
	}
	
	public PluginDescriptor getByClassname(String classname) {
		for (PluginDescriptor p : _plugins) {
			if (p.getClassname().equals(classname))
				return p;
		}
		return null;
	}
	
	
	@SuppressWarnings("unchecked")
	private static ConnectionDescriptor[] getConnectionDescriptors(Class<?> c, Class<? extends Annotation> a) {
		
		List<ConnectionDescriptor> retval = new LinkedList<ConnectionDescriptor>();
		Set<Field> fields = ReflectionUtils.getFields(c, ReflectionUtils.withAnnotation(a));
		for (Field f : fields) {
			Class<?> fc = f.getType();
			// TODO: This trivial implementation does not support generic types, arrays or primitives. Fix it!
			
			ConnectionDescriptor d = new ConnectionDescriptor();
			d.setType(fc.getCanonicalName());
			
			if (a == Input.class)
				d.setNumber(f.getAnnotation(Input.class).number());
			if (a == Output.class)
				d.setNumber(f.getAnnotation(Output.class).number());
			retval.add(d);	
		}
		return retval.toArray(new ConnectionDescriptor[retval.size()]);
	}
	
	
	@SuppressWarnings("unchecked")
	private static PluginParameter[] getPluginParameters(Class<?> c) {
		
		Set<Field> fields = ReflectionUtils.getFields(c, 
				ReflectionUtils.withAnnotation(de.artignition.werkflow.plugin.annotation.PluginParameter.class));
		List<PluginParameter> retval = new ArrayList<PluginParameter>(fields.size());
		
		for (Field f : fields) {
			PluginParameter pp = new PluginParameter();
			pp.setType(f.getType().getCanonicalName());
			pp.setFieldname(f.getName());
			pp.setNicename(f.getAnnotation(de.artignition.werkflow.plugin.annotation.PluginParameter.class).name());
			pp.setDescription(f.getAnnotation(de.artignition.werkflow.plugin.annotation.PluginParameter.class).description());
			retval.add(pp);
		}
		return retval.toArray(new PluginParameter[retval.size()]);
	}
}
