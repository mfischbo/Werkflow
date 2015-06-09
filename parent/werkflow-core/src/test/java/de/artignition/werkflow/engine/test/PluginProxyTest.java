package de.artignition.werkflow.engine.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import de.artignition.werkflow.domain.WorkItem;
import de.artignition.werkflow.engine.PluginExecutionProxy;
import de.artignition.werkflow.test.MockPlugin;

public class PluginProxyTest {
	
	@Test
	public void initializesPluginCorrectly() throws Exception {
	
		Map<String, Object> params = new HashMap<>();
		params.put("username", "john.doe");
		params.put("password", "test");
		PluginExecutionProxy proxy = new PluginExecutionProxy(MockPlugin.class, params);
		MockPlugin x = (MockPlugin) proxy.getWrappedPlugin();
		assertNotNull(x.username);
		assertNotNull(x.password);
	}
	
	@Test
	public void assignsInputCorrectly() throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("username", "john.doe");
		params.put("password", "test");
		PluginExecutionProxy proxy = new PluginExecutionProxy(MockPlugin.class, params);

		ComplexInputGraph i1 = new ComplexInputGraph();
		ComplexInputGraph i2 = new ComplexInputGraph();
		
		WorkItem item1 = new WorkItem();
		item1.setPayloadObject(i1);
		proxy.setInput(item1, 1);
		
		WorkItem item2 = new WorkItem();
		item2.setPayloadObject(i2);
		proxy.setInput(item2, 2);

		MockPlugin x = (MockPlugin) proxy.getWrappedPlugin();
		assertNotNull(x.input1);
		assertNotNull(x.input2);
		assertEquals(x.input1, i1);
		assertEquals(x.input2, i2);
	}
	
	@Test
	public void fetchesOutputCorrectly() throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("username", "john.doe");
		params.put("password", "test");
		PluginExecutionProxy proxy = new PluginExecutionProxy(MockPlugin.class, params);

		ComplexInputGraph i1 = new ComplexInputGraph();
		ComplexInputGraph i2 = new ComplexInputGraph();
		
		WorkItem item1 = new WorkItem();
		item1.setPayloadObject(i1);
		proxy.setInput(item1, 1);
		
		WorkItem item2 = new WorkItem();
		item2.setPayloadObject(i2);
		proxy.setInput(item2, 2);
		
		proxy.execute();
		
		MockPlugin x = (MockPlugin) proxy.getWrappedPlugin();
		assertNotNull(x.output1);
		assertNotNull(x.output2);
	}
	
	@Test
	public void returnsCorrectPortNumbers() throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("Foo", "This is some");
		PluginExecutionProxy proxy = new PluginExecutionProxy(MockPlugin.class, params);
		
		assertTrue(proxy.getInputPorts()[0] == 1);
		assertTrue(proxy.getInputPorts()[1] == 2);
		assertTrue(proxy.getOutputPorts()[0] == 1);
		assertTrue(proxy.getOutputPorts()[1] == 2);
	}
}