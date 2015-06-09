package de.artignition.werkflow.engine;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;

import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.artignition.werkflow.domain.WorkItem;
import de.artignition.werkflow.plugin.Plugin;
import de.artignition.werkflow.plugin.PluginExitStatus;
import de.artignition.werkflow.plugin.annotation.Input;
import de.artignition.werkflow.plugin.annotation.Output;
import de.artignition.werkflow.plugin.annotation.PluginParameter;

public class PluginExecutionProxy implements Plugin {

	private Plugin					plugin;
	private Map<String, Object>		params;
	
	private Field[]					inputs;
	private Field[]					outputs;
	private Field[]					parameters;
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	public PluginExecutionProxy(Class<?> pluginClass, Map<String, Object> params) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		this.params = params;
		initializePlugin(pluginClass);
	}
	
	/**
	 * Initializes the plugin by creating an actual instance of it.
	 * The created instance will then be populated with the parameters specified from the PluginInstance
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings("unchecked")
	private void initializePlugin(Class<?> pluginClass) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		log.debug("Initializing plugin {}", pluginClass.getCanonicalName());
		
		// create a instance of the actual piece of code
		Class<?>[] ifaces = pluginClass.getInterfaces();
		boolean isPlugin = false;
		for (Class<?> iface : ifaces) {
			if (iface == Plugin.class) {
				isPlugin = true;
				break;
			}
		}
		
		if (!isPlugin)
			throw new RuntimeException("The provided class " + pluginClass.getCanonicalName() + " does not implement " + Plugin.class.getCanonicalName());

		// create constructor and instance
		Constructor<?> constructor = pluginClass.getConstructor();
		this.plugin = (Plugin) constructor.newInstance();
		log.debug("Successfully created executable plugin instance");
		
		// search inputs / outputs / parameters
		Set<Field> inputs = ReflectionUtils.getAllFields(this.plugin.getClass(), ReflectionUtils.withAnnotation(Input.class));
		this.inputs = inputs.toArray(new Field[inputs.size()]);
		
		Set<Field> outputs = ReflectionUtils.getAllFields(plugin.getClass(), ReflectionUtils.withAnnotation(Output.class));
		this.outputs = outputs.toArray(new Field[outputs.size()]);

		Set<Field> params = ReflectionUtils.getAllFields(plugin.getClass(), ReflectionUtils.withAnnotation(PluginParameter.class));
		this.parameters = params.toArray(new Field[params.size()]);		
		this.setParameters();
		log.debug("Plugin initialization complete");
	}
	
	private void setParameters() {
		for (Field f : parameters) {
			String key = f.getAnnotation(PluginParameter.class).name();
			if (params.containsKey(key)) {
				try {
					Object s = params.get(key);
					
					if (f.getType().isAssignableFrom(s.getClass())) {
						if (!f.isAccessible())
							f.setAccessible(true);
						f.set(this.plugin, s);
					} else {
						log.error("Unable to assign parameter " + f.getName() + " from object with type : " + s.getClass());
						throw new RuntimeException("Unable to assign parameter");
					}
				} catch (Exception ex) {
					throw new RuntimeException("Unable to set parameter coCollection<E>");
				}
			}
		}
	}

	
	public void setInput(WorkItem item, int port) throws IllegalArgumentException, IllegalAccessException {
		
		for (Field f : inputs) {
			if (f.getAnnotation(Input.class).number() == port) {
				
				Object o = item.getPayloadObject();
				if (f.getType().isAssignableFrom(o.getClass())) {
					if (!f.isAccessible())
						f.setAccessible(true);
					f.set(this.plugin, o);
				} else {
					log.error("Object of class {} is not assignable to input field with type {}", o.getClass(), f.getType());
					throw new RuntimeException("Input is not assignable");
				}
			}
		}
	}
	
	WorkItem getOutput(int port) throws IllegalArgumentException, IllegalAccessException {
		for (Field f : outputs) {
			if (f.getAnnotation(Output.class).number() == port) {
				if (!f.isAccessible())
					f.setAccessible(true);
				Object o = f.get(this.plugin);
				WorkItem retval = new WorkItem();
				retval.setPayloadObject(o);
				return retval;
			}
		}
		throw new RuntimeException("The plugin does not have a output with port number " + port);
	}

	public int[] getOutputPorts() {
		int[] retval = new int[this.outputs.length];
		for (int i=0; i < this.outputs.length; i++) 
			retval[i] = this.outputs[i].getAnnotation(Output.class).number();
		return retval;
	}
	
	public int[] getInputPorts() {
		int[] retval = new int[this.inputs.length];
		for (int i=0; i < this.inputs.length; i++)
			retval[i] = this.inputs[i].getAnnotation(Input.class).number();
		return retval;
	}
	
	public Plugin getWrappedPlugin() {
		return this.plugin;
	}
	
	@Override
	public PluginExitStatus execute() {
		return this.plugin.execute();
	}

	@Override
	public void beforeExecution() {
		this.plugin.beforeExecution();
	}

	@Override
	public void afterExecution() {
		this.plugin.afterExecution();
	}

	@Override
	public void beforeWorkItemProcessing() {
		this.plugin.beforeWorkItemProcessing();
	}

	@Override
	public void afterWorkItemProcessing() {
		this.plugin.afterWorkItemProcessing();
	}
}