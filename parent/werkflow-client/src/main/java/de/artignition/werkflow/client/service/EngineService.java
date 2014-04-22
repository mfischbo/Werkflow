package de.artignition.werkflow.client.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import de.artignition.werkflow.command.EngineCommand;
import de.artignition.werkflow.command.EngineCommand.Action;

@Service
public class EngineService {

	@Autowired
	private JmsTemplate			jms;
	
	
	public String sendTriggerMessage() throws Exception {
		jms.send(new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				Queue replyTo = session.createQueue("AdminRequest");
				TextMessage m = session.createTextMessage();
				m.setText("Hello from JMS");
				m.setJMSCorrelationID(UUID.randomUUID().toString());
				m.setJMSReplyTo(replyTo);
				return m;
			}
		});
		
		jms.setReceiveTimeout(5000L);
		Message m = jms.receive("AdminRequest");
		if (m != null && m instanceof TextMessage) {
			return ((TextMessage) m).getText();
		}
		return "";
	}
	
	
	public List<String> getAvailableEngines() {
		List<String> retval = new ArrayList<String>(1);
		retval.add("Default Node (localhost:6060)");
		return retval;
	}
	
	
	public void sendStartEngineRequest(String engine) {
		jms.send(new MessageCreator() {

			@Override
			public Message createMessage(Session session) throws JMSException {
				Queue replyTo = session.createQueue("AdminRequest");
				
				EngineCommand cmd = new EngineCommand();
				cmd.setAction(Action.START);
				Message m = session.createObjectMessage(cmd);
				m.setJMSCorrelationID(UUID.randomUUID().toString());
				m.setJMSReplyTo(replyTo);
				return m;
			}
		});
		
		jms.setReceiveTimeout(5000L);
		Message m = jms.receive("AdminRequest");
		if (m != null) {
			System.out.println("Seems to work");
		}
	}
}
