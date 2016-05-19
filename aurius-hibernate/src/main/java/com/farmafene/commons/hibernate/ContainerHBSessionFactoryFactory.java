package com.farmafene.commons.hibernate;

import java.net.URL;
import java.util.List;

public class ContainerHBSessionFactoryFactory extends
		AbstractHBSessionFactoryFactory {

	private List<String> resources;

	class HBFinderSimple extends HBAnnotationFinder {

		/**
		 * {@inheritDoc}
		 * 
		 * @see com.farmafene.commons.hibernate.HBAnnotationFinder#find()
		 */
		@Override
		public void find() {
			for (String resource : resources) {
				getHolder().addIfIsValid(resource);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append("={");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.commons.hibernate.AbstractHBSessionFactoryFactory#getURLResources(java.lang.ClassLoader)
	 */
	@Override
	public URL[] getURLResources(ClassLoader cl) {
		return new URL[] {};
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.commons.hibernate.AbstractHBSessionFactoryFactory#getHBAnnotationFinder()
	 */
	@Override
	protected HBAnnotationFinder getHBAnnotationFinder() {
		return new HBFinderSimple();
	}

	/**
	 * @return the resources
	 */
	public List<String> getResources() {
		return resources;
	}

	/**
	 * @param resources
	 *            the resources to set
	 */
	public void setResources(List<String> resources) {
		this.resources = resources;
	}

}
