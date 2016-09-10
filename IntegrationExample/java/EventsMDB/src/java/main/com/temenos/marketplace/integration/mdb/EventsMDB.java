package com.temenos.marketplace.integration.mdb;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.MessageDrivenContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * <p>Message Driven Bean responsible for handling OFS/OFSML messages.</p>
 */
public class EventsMDB implements MessageListener {
    
    private final static Logger logger = Logger.getLogger(EventsMDB.class.getName());

    @Resource
    private MessageDrivenContext mdctx;  


    /** 
     * Create an EJB instance upon first invocation
     * 
     * @throws EJBException
     */
    @PostConstruct
    public void ejbConstruct() throws EJBException {
        // Add some initialisation logic here if required
        
    }
    
    @PreDestroy
    public void ejbDestroy() throws EJBException {
    	// Cleanup here
    }

    /** 
     * Will be invoked by Application Server upon receiving an activation specification event
     * 
     */
    public void onMessage(Message message) {
    	// Check if we have the valid Object to deal with
    	if (message instanceof TextMessage) {
    	} else {
    		// Trigger JMS Re-Delivery, after re-delivery count exausted message will be moved to DLQ for further analysis
    		throw new RuntimeException("Invalid message type, expecting TextMessage");
    	}
    	
    	try {
    		String eventData = ((TextMessage) message).getText();
    		processEventData(eventData);
    	} catch (JMSException jme) {
    		logger.log(Level.SEVERE, "Error occured while processing event data", jme);
    		// Trigger JMS Re-Delivery, after re-delivery count exausted message will be moved to DLQ for further analysis
    		throw new RuntimeException("Error occured while processing event data", jme);
    	}
    }
    
    /**
     * Process the event data according to your need
     * @param eventData
     */
    private void processEventData(String eventData) {
    	// Just Echo for sample
    	logger.info("Integration Framework Event(s) Receieved [" + eventData + "]");
        try {
        	// Send data whereever you want
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
