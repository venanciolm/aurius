package com.farmafene.aurius.mngt;

public interface IBasicStat {

	public abstract void start();

	public abstract void stop();

	public abstract void stop(Throwable th);

	public abstract long getDuracion();

	public abstract long getInitTime();

	public abstract long getMiliSeconds();

	/**
	 * @return the error
	 */
	public abstract String getError();

	/**
	 * @return the stop
	 */
	public abstract boolean isStop();

	/**
	 * @return the local
	 */
	public abstract boolean isLocal();

}