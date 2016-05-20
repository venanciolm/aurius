package com.farmafene.aurius.jca;

import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.aurius.AuthInfo;
import com.farmafene.aurius.Registro;
import com.farmafene.aurius.core.ContextoCore;

@SuppressWarnings("serial")
public class AuriusMDBWrapper implements AuriusListener, MessageDrivenBean {

	private static final Logger logger = LoggerFactory
			.getLogger(AuriusMDBWrapper.class);
	private AuriusListenerImpl impl;
	private MessageDrivenContext messageDrivenContext;

	public AuriusMDBWrapper() {
		impl = new AuriusListenerImpl();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append("={");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.ejb.MessageDrivenBean#ejbRemove()
	 */
	@Override
	public void ejbRemove() throws EJBException {
		if (logger.isDebugEnabled()) {
			logger.debug("ejbRemove()");
		}

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.ejb.MessageDrivenBean#setMessageDrivenContext(javax.ejb.MessageDrivenContext)
	 */
	@Override
	public void setMessageDrivenContext(
			MessageDrivenContext messageDrivenContext) {
		this.messageDrivenContext = messageDrivenContext;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.jca.AuriusListener#invoke(com.farmafene.aurius.core.ContextoCore,
	 *      java.lang.String, java.lang.String, com.farmafene.aurius.Registro)
	 */
	@Override
	public Registro invoke(ContextoCore ctx, String idServicio, String version,
			Registro registro) {
		return impl.invoke(ctx, idServicio, version, registro);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.jca.AuriusListener#getRegistro(com.farmafene.aurius
	 *      .AuthInfo, java.lang.String)
	 */
	public Registro getRegistro(AuthInfo cookie, String idServicio) {
		return impl.getRegistro(cookie, idServicio);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.jca.AuriusListener#getRegistro(com.farmafene.aurius
	 *      .AuthInfo, java.lang.String, java.lang.String)
	 */
	@Override
	public Registro getRegistro(AuthInfo cookie, String idServicio,
			String version) {
		return impl.getRegistro(cookie, idServicio, version);
	}

	/**
	 * Getter for messageDrivenContext
	 * 
	 * @return the messageDrivenContext
	 */
	public MessageDrivenContext getMessageDrivenContext() {
		return messageDrivenContext;
	}
}
