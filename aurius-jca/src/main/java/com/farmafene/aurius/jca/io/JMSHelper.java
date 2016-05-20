/*
 * Copyright (c) 2009-2011 farmafene.com
 * All rights reserved.
 * 
 * Permission is hereby granted, free  of charge, to any person obtaining
 * a  copy  of this  software  and  associated  documentation files  (the
 * "Software"), to  deal in  the Software without  restriction, including
 * without limitation  the rights to  use, copy, modify,  merge, publish,
 * distribute,  sublicense, and/or sell  copies of  the Software,  and to
 * permit persons to whom the Software  is furnished to do so, subject to
 * the following conditions:
 * 
 * The  above  copyright  notice  and  this permission  notice  shall  be
 * included in all copies or substantial portions of the Software.
 * 
 * THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
 * EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
 * MERCHANTABILITY,    FITNESS    FOR    A   PARTICULAR    PURPOSE    AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE,  ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.farmafene.aurius.jca.io;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;

import com.farmafene.aurius.AuriusRuntimeException;
import com.farmafene.aurius.mngt.MarshallStatus;

public class JMSHelper {

	private JMSHelper() {
	}

	public static void response(Codec codec, Session sess,
			Destination destinationQueue, Destination localQueue,
			long timeToLive, long timeOut, AuriusMessage outAuriusMessage) {
		MessageProducer prod = null;
		MarshallStatus status = new MarshallStatus();
		Throwable thStatus = null;
		try {
			status.start();
			prod = sess.createProducer(destinationQueue);
			if (timeToLive >= 0) {
				prod.setTimeToLive(timeToLive);
			}
			BytesMessage msg = sess.createBytesMessage();
			msg.setIntProperty("Codec", codec.ordinal());
			msg.writeBytes(IOUtil.marshall(outAuriusMessage, codec));
			if (localQueue != null) {
				msg.setJMSReplyTo(localQueue);
			}
			prod.send(msg);
			close(null, null, prod, null);
		} catch (Throwable e) {
			thStatus = e;
			throw new AuriusRuntimeException(e);
		} finally {
			status.stop(thStatus);
		}
	}

	public static void close(Connection jmsConnection, Session jmsSession,
			MessageProducer jmsMessageProducer,
			MessageConsumer jmsMessageConsumer) {
		if (jmsMessageConsumer != null) {
			try {
				if (jmsMessageConsumer != null)
					jmsMessageConsumer.close();
			} catch (JMSException e) {
				// do noting
			}
		}
		if (jmsMessageProducer != null) {
			try {
				if (jmsMessageProducer != null)
					jmsMessageProducer.close();
			} catch (JMSException e) {
				// do noting
			}
		}
		if (jmsSession != null) {
			try {
				if (jmsSession != null)
					jmsSession.close();
			} catch (JMSException e) {
				// do noting
			}
		}
		if (jmsConnection != null) {
			try {
				jmsConnection.stop();
			} catch (JMSException e) {
				// do noting
			}
			try {
				jmsConnection.close();
			} catch (JMSException e) {
				// do noting
			}
		}
	}
}
