package de.artignition.werkflow.messaging;

import java.util.UUID;

import de.artignition.werkflow.command.EntityCommand;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.Session;

import org.springframework.jms.core.MessageCreator;

public class EntityCommandMessageCreator implements MessageCreator {
	
	private EntityCommand cmd;
	
	public EntityCommandMessageCreator(EntityCommand e) {
		this.cmd = e;
	}
	
	@Override
	public Message createMessage(Session session) throws JMSException {
	
		assert(this.cmd != null);
		
		Queue replyTo = session.createQueue("AdminRequest");
		Message m = session.createObjectMessage(this.cmd);
		m.setJMSCorrelationID(UUID.randomUUID().toString());
		m.setJMSReplyTo(replyTo);
		return m;
		
	}
}
