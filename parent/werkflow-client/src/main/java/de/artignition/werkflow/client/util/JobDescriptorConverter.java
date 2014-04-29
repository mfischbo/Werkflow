/*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*  See the License for the specific language governing permissions and
*  limitations under the License.
*/

package de.artignition.werkflow.client.util;

import java.util.HashMap;
import java.util.Map;

import de.artignition.werkflow.domain.JobDescriptor;
import de.artignition.werkflow.domain.JobPlugin;

import eu.mihosoft.vrl.workflow.Connection;
import eu.mihosoft.vrl.workflow.Connections;
import eu.mihosoft.vrl.workflow.VFlow;
import eu.mihosoft.vrl.workflow.VNode;
import javafx.collections.ObservableMap;

import org.joda.time.LocalDateTime;

/**
 * This class handles conversion from a VFlow to a JobDescriptor and vice versa
 * @author M. Fischboeck
 *
 */
public class JobDescriptorConverter {

	
	public static JobDescriptor convert(JobDescriptor d, VFlow flow) {

		Map<VNode, JobPlugin> lookup = new HashMap<VNode, JobPlugin>();
		
		if (d == null) {
			d = new JobDescriptor();
			d.setDateCreated(LocalDateTime.now());
			d.setDateModified(d.getDateCreated());
		}

		// convert all nodes first
		for (VNode n : flow.getNodes()) {
			JobPlugin p = new JobPlugin();
			p.setX((int) n.getX());
			p.setY((int) n.getY());
			p.setJobDescriptor(d);
			d.getPlugins().add(p);
			
			lookup.put(n, p);
		}
		
		// find all connection pairs and create werkflow connections
		ObservableMap<String, Connections> connections = flow.getAllConnections();
		for (Connections c : connections.values()) {
			for (Connection tc : c.getConnections()) {
				de.artignition.werkflow.domain.Connection x = new de.artignition.werkflow.domain.Connection();
				
				JobPlugin source = lookup.get(tc.getSender().getNode());
				JobPlugin target = lookup.get(tc.getReceiver().getNode());
				
				x.setSource(source);
				x.setTarget(target);
				x.setSourcePort(0);
				x.setTargetPort(0);
				
				source.getOutboundConnections().add(x);
				target.getInboundConnections().add(x);
			}
		}
		
		return d;
	}
	
	
	public static VFlow convert(JobDescriptor descriptor) {
		// TODO: To be implemented
		return null;
	}

}
