package de.artignition.werkflow.engine;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import de.artignition.werkflow.domain.Connection;
import de.artignition.werkflow.domain.PluginInstance;
import de.artignition.werkflow.domain.WorkItem;
import de.artignition.werkflow.plugin.Plugin;
import de.artignition.werkflow.plugin.PluginExitStatus;
import de.artignition.werkflow.plugin.annotation.Input;
import de.artignition.werkflow.plugin.annotation.Output;
import de.artignition.werkflow.plugin.annotation.PluginParameter;

import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles communication of inputs/outputs/parameters with the plugin.
 * This class delegates all public method calls to the actual plugin instance.
 * 
 * @author M. Fischboeck 
 *
 */
class PluginExecutionProxy implements Plugin {

	private PluginInstance			instance;
	private Plugin					plugin;

	private Field[]					inputs;
	private Field[]					outputs;
	private Field[]					parameters;
	
	private ObjectOutputStream		oOut;
	private ByteArrayOutputStream   bOut;
	
	private Logger log	= LoggerFactory.getLogger(getClass());

	/**
	 * Default constructor
	 * @param instance The underlying plugin instance to be proxied
	 * @throws IOException 
	 */
	PluginExecutionProxy(PluginInstance instance) throws IOException {
		this.instance = instance;
		this.bOut = new ByteArrayOutputStream();
		this.oOut = new ObjectOutputStream(bOut);
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
	void initializePlugin() throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		log.debug("Initializing plugin " + this.instance.getJobPlugin().getClassname());
		
		// create a instance of the actual piece of code
		Class<?> c = Class.forName(instance.getJobPlugin().getClassname());
		Class<?>[] ifaces = c.getInterfaces();
		boolean isPlugin = false;
		for (Class<?> iface : ifaces) {
			if (iface == Plugin.class) {
				isPlugin = true;
				break;
			}
		}
		
		if (!isPlugin)
			throw new RuntimeException("The provided class : " + c.getCanonicalName() + " does not implement " + Plugin.class.getCanonicalName());

		// create constructor and instance
		Constructor<?> constructor = c.getConstructor();
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
			String key = f.getName();
			if (instance.getJobPlugin().getParameters().containsKey(key)) {
				//byte[] o = instance.getJobPlugin().getParameters().get(key);
				try {
					Serializable s = instance.getJobPlugin().getParameters().get(key);
					
				/*	
					ObjectInputStream oIns = new ObjectInputStream(new ByteArrayInputStream(o));
					Object object = oIns.readObject();
					oIns.close();
				*/	
					if (f.getType().isAssignableFrom(s.getClass())) {
						if (!f.isAccessible())
							f.setAccessible(true);
						f.set(this.plugin, s);
					} else {
						log.error("Unable to assign parameter " + f.getName() + " from object with type : " + s.getClass());
						throw new RuntimeException("Unable to assign parameter");
					}
				} catch (Exception ex) {
					throw new RuntimeException("Unable to set parameter correctly");
				}
			}
		}
	}
	

	/**
	 * Sets the input fields of the underlying plugin with the objects from the provided work items.
	 * @param items The items holding the actual objects.
	 */
	void setInputs(WorkItem[] items) {
		log.debug("Setting " + items.length + " work items to input fields");
		
		for (WorkItem i : items) {
			Connection c = getConnectionBySourcePort(i.getSourceOutput());
			int targetPort = c.getTargetPort();
			
			for (Field f : inputs) {
				if (f.getAnnotation(Input.class).number() == targetPort) {
					
					// deserialize object and set it
					try {
						ByteArrayInputStream bins = new ByteArrayInputStream(i.getPayload());
						ObjectInputStream oins = new ObjectInputStream(bins);
						
						Object o = oins.readObject();
						oins.close();
						bins.close();
						
						if (f.getType().isAssignableFrom(o.getClass())) {
							if (!f.isAccessible())
								f.setAccessible(true);
							f.set(this.plugin, o);
						} else {
							log.error("Object of class : " + o.getClass() + " is not assignable to field with type : " + f.getType());
							throw new RuntimeException("Input is not assignable");
						}
						break;
					} catch (Exception ex) {
						log.error("Unable to deserialize payload for workitem : " + i.getId());
						throw new RuntimeException("Unable to deserialize object");
					}
				}
			}
		}
		log.debug("Input data successfully set on plugin : " + this.instance.getJobPlugin().getClassname());
	}
	

	/**
	 * Returns WorkItems after the plugin has executed by pulling the objects from the output fields
	 * @return The resulting WorkItems
	 */
	WorkItem[] getOutput() {
		log.debug("Producing WorkItems from outputs of plugin : " + this.plugin.getClass());
		
		WorkItem[] retval = new WorkItem[outputs.length];
		for (int i=0; i < outputs.length; i++) {
			
			Field f = outputs[i];
			WorkItem item = new WorkItem();
			item.setOwner(this.instance);
			item.setSourceOutput(f.getAnnotation(Output.class).number());
			
			Object o = null;
			try {
				log.debug("Serializing output from field : " + f.getName() + " on plugin : " + this.plugin.getClass());
				if (!f.isAccessible())
					f.setAccessible(true);
				o = f.get(this.plugin);
				
				// set the plugins output to null
				f.set(this.plugin, null);
				
			} catch (Exception ex) {
				log.error("Failed to get Object for output : " + f.getName() + " on plugin " + this.plugin.getClass());
				throw new RuntimeException("Unable to get Object. Cause: " + ex.getMessage());
			}
			item.setObject(o);
			if (o == null) {
				item.setPayload(new byte[0]);
			} else {
				try {
					this.oOut.writeObject(o);
					this.oOut.flush();
					item.setPayload(this.bOut.toByteArray());
					this.bOut.flush();
			
					// reset for next cycle
					this.bOut.reset();
					this.oOut.reset();
				} catch (Exception ex) {
					log.error("Failed to serialize output from field : " + f.getName() + " on plugin : " + this.plugin.getClass());
					throw new RuntimeException("Failed serialization. Cause: " + ex.getMessage());
				}
			}
			retval[i] = item;
		}
		return retval;
	}
	
	private Connection getConnectionBySourcePort(Integer sourcePort) {
		Set<Connection> inbounds = this.instance.getJobPlugin().getInboundConnections();
		for (Connection c : inbounds) 
			if (c.getSourcePort() == sourcePort)
				return c;
		
		throw new RuntimeException("Unable to find connection for source port: " + sourcePort);
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
