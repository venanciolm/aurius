package com.farmafene.aurius.core;

public interface IContextoManager {

	public void setContexto(ContextoCore ctx);

	public ContextoCore createContext();

	public void setIdServicio(ContextoCore ctx, String id);

	public void setIdServicioActual(ContextoCore ctx, String version);

	public void setVersionServicio(ContextoCore ctx, String version);

	public void setVersionServicioActual(ContextoCore ctx, String version);

	public ContextoCore getContexto();
}
