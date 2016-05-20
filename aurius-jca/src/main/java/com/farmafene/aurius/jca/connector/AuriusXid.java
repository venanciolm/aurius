package com.farmafene.aurius.jca.connector;

import java.io.Serializable;
import java.util.Arrays;

import javax.transaction.xa.Xid;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@SuppressWarnings("serial")
@XStreamAlias("AuriusXid")
public class AuriusXid implements Xid, Serializable {

	@XStreamAlias("formatId")
	@XStreamAsAttribute
	private int formatId;
	@XStreamAlias("globalId")
	private byte[] globalId;
	@XStreamAlias("branchId")
	private byte[] branchId;
	private transient int hash;

	/**
	 * Constructor del wrapper para transmisi�n del Xid local de para ser
	 * serializable sin necesidad de dependencias externas.
	 * 
	 * @param other
	 *            Xid a transmitir
	 */
	public AuriusXid(Xid other) {
		if (other == null) {
			throw new IllegalArgumentException("El Xid no puede ser 'null'.");
		}
		this.formatId = other.getFormatId();
		this.globalId = other.getGlobalTransactionId();
		this.branchId = other.getBranchQualifier();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(getClass().getSimpleName()).append("={");
		s.append("globalId=");
		for (int i = 0; i < globalId.length; i++) {
			s.append(Integer.toHexString(globalId[i]));
		}
		s.append(", length=").append(globalId.length);
		s.append(", branchId=");
		for (int i = 0; i < branchId.length; i++) {
			s.append(Integer.toHexString(branchId[i]));
		}
		s.append(", length=");
		s.append(branchId.length);
		s.append("}");
		return s.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj instanceof AuriusXid == false) {
			return false;
		}
		AuriusXid other = (AuriusXid) obj;
		return formatId == other.formatId
				&& Arrays.equals(globalId, other.globalId)
				&& Arrays.equals(branchId, other.branchId);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		if (hash == 0) {
			hash = hash(hash(0, globalId), branchId);
		}
		return hash;
	}

	private int hash(int hash, byte[] id) {
		for (int i = 0; i < id.length; i++) {
			hash = (hash * 37) + id[i];
		}
		return hash;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.transaction.xa.Xid#getFormatId()
	 */
	@Override
	public int getFormatId() {

		return formatId;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.transaction.xa.Xid#getGlobalTransactionId()
	 */
	@Override
	public byte[] getGlobalTransactionId() {
		return (byte[]) globalId.clone();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.transaction.xa.Xid#getBranchQualifier()
	 */
	@Override
	public byte[] getBranchQualifier() {
		return (byte[]) branchId.clone();
	}
}
