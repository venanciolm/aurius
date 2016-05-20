/* Copyright (c) 2009-2014 farmafene.com
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
package com.farmafene.aurius.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.aurius.CampoRegistro;
import com.farmafene.aurius.DatoRegistro;
import com.farmafene.aurius.Registro;
import com.farmafene.aurius.TypeRegistro;
import com.farmafene.aurius.core.IDiccionarioFactory;
import com.farmafene.aurius.core.Servicio;
import com.farmafene.aurius.dao.hibernate.auth.RoleRol;
import com.farmafene.aurius.dao.hibernate.dic.DiccionarioDic;
import com.farmafene.aurius.dao.hibernate.dic.RegistroReg;
import com.farmafene.aurius.dao.hibernate.dic.ServicioDesc;
import com.farmafene.aurius.dao.hibernate.dic.ServicioSvr;
import com.farmafene.aurius.dao.hibernate.dic.ServicioSvrKey;
import com.farmafene.aurius.dao.hibernate.dic.TypeBlobVO;
import com.farmafene.aurius.dao.hibernate.dic.TypeDecimalVO;
import com.farmafene.aurius.dao.hibernate.dic.TypeRegistroVO;
import com.farmafene.aurius.dao.hibernate.dic.TypeStringVO;
import com.farmafene.aurius.dao.hibernate.dic.TypeTimeVO;
import com.farmafene.aurius.dao.util.FormatUtil;
import com.farmafene.aurius.impl.CampoRegistroImpl;
import com.farmafene.aurius.impl.DatoRegistroTypeBlob;
import com.farmafene.aurius.impl.DatoRegistroTypeDecimal;
import com.farmafene.aurius.impl.DatoRegistroTypeRegistro;
import com.farmafene.aurius.impl.DatoRegistroTypeString;
import com.farmafene.aurius.impl.DatoRegistroTypeTime;
import com.farmafene.aurius.impl.RegistroImpl;
import com.farmafene.aurius.util.ProxyFactory;

public class DiccionarioFactoryImpl implements IDiccionarioFactory {

	private static final Logger logger = LoggerFactory
			.getLogger(IDiccionarioFactory.class);
	private ProxyFactory<DatoRegistro<Serializable>> proxyDatoRegistro;

	/**
	 * Constructor por defecto
	 */
	public DiccionarioFactoryImpl() {
		logger.debug(this + "<init>");
		proxyDatoRegistro = new ProxyFactory<DatoRegistro<Serializable>>();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getName());
		sb.append("={");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IDiccionarioFactory#getRegistroNormalizado(java.lang.String)
	 */
	public String getRegistroNormalizado(String id) {
		String getRegistroNormalizado = null;
		if (null != id) {
			getRegistroNormalizado = FormatUtil.getIdDiccionarioNormalizado(id);
		}
		return getRegistroNormalizado;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IDiccionarioFactory#getServicioNormalizado(java.lang.String)
	 */
	public String getServicioNormalizado(String id) {
		return FormatUtil.getIdServicioNormalizado(id);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IDiccionarioFactory#getVersionNomalizada(java.lang.String)
	 */
	public String getVersionNomalizada(String version) {
		return FormatUtil.getNormalizedVersionOperacion(version);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IDiccionarioFactory#getDefinicionRegistro(java.lang.String)
	 */
	public TypeRegistro getDefinicionRegistro(String id)
			throws IllegalArgumentException {
		TypeRegistro salida = null;
		String grupo = FormatUtil.getAplicacionFromIdDiccionario(id);
		String i = FormatUtil.getOperacionFromIdDiccionario(id);
		BigDecimal idD = new BigDecimal(i);
		Session sess = DiccionarioSessionFactory.getSessionFactory()
				.getCurrentSession();
		Criteria crit = sess.createCriteria(RegistroReg.class);
		crit.add(Restrictions.eq("id.grupo", grupo));
		crit.add(Restrictions.eq("id.id", idD));
		crit.addOrder(Order.asc("id.orden"));
		@SuppressWarnings("unchecked")
		List<RegistroReg> l = (List<RegistroReg>) crit.list();
		salida = getTypeRegistroFromRegistros(id, l);
		return salida;
	}

	private TypeRegistro getTypeRegistroFromRegistros(String id,
			List<RegistroReg> lista) {
		DatoRegistroTypeRegistro reg = new DatoRegistroTypeRegistro(id);
		List<String> elementsNames = new LinkedList<String>();
		Map<String, CampoRegistro> fieldFromName = new HashMap<String, CampoRegistro>();
		reg.setFields(fieldFromName);
		for (RegistroReg r : lista) {
			CampoRegistroImpl c = new CampoRegistroImpl();
			c.setObligatorio(r.isObligatorio());
			c.setCardinalidadMaxima(r.getCardinalidadMaxima());
			c.setCardinalidadMinima(r.getCardinalidadMinima());
			@SuppressWarnings("unchecked")
			DatoRegistro<Serializable> datoRegistro = (DatoRegistro<Serializable>) getDatoRegistro(r
					.getElemento());
			DatoRegistro<Serializable> dd = proxyDatoRegistro
					.newInstance(datoRegistro);
			c.setDatoRegistro(dd);
			String nombre = dd.getNombre();
			if (fieldFromName.containsKey(nombre)) {
				int i = 1;
				while (fieldFromName.containsKey(nombre + "#" + i)) {
					i++;
				}
				nombre = nombre + "#" + i;
			}
			c.setNombre(nombre);
			elementsNames.add(c.getNombre());
			fieldFromName.put(c.getNombre(), c);
		}
		reg.setNombres(elementsNames.toArray(new String[0]));
		return reg;
	}

	private DatoRegistro<?> getDatoRegistro(DiccionarioDic elemento) {
		DatoRegistro<?> r = null;
		if (elemento instanceof TypeStringVO) {
			TypeStringVO e = (TypeStringVO) elemento;
			DatoRegistroTypeString d = new DatoRegistroTypeString(
					getIdDiccionario(e));
			d.setNombre(elemento.getNombre());
			d.setLongitudMaxima(e.getLongitudMaxima());
			d.setLongitudMinima(e.getLongitudMinima());
			d.setMascara(e.getMascara());
			r = d;
		} else if (elemento instanceof TypeDecimalVO) {
			TypeDecimalVO e = (TypeDecimalVO) elemento;
			DatoRegistroTypeDecimal d = new DatoRegistroTypeDecimal(
					getIdDiccionario(e));
			d.setNombre(elemento.getNombre());
			d.setEscala(e.getMaxDecimales());
			d.setPrecision(e.getMaxDigitos());
			d.setMascara(e.getMascara());
			d.setMaxExclusive((BigDecimal) d.valida(e.getMaxExclusive()));
			d.setMaxInclusive((BigDecimal) d.valida(e.getMaxInclusive()));
			d.setMinExclusive((BigDecimal) d.valida(e.getMinExclusive()));
			d.setMinInclusive((BigDecimal) d.valida(e.getMinInclusive()));
			r = d;
		} else if (elemento instanceof TypeTimeVO) {
			TypeTimeVO e = (TypeTimeVO) elemento;
			DatoRegistroTypeTime d = new DatoRegistroTypeTime(
					getIdDiccionario(e));
			d.setNombre(elemento.getNombre());
			d.setDateFormat(e.getDateFormat());
			d.setLongitud(e.getLongitud());
			d.setMascara(e.getMascara());
			d.setMaxExclusive((Date) d.valida(e.getMaxExclusive()));
			d.setMaxInclusive((Date) d.valida(e.getMaxInclusive()));
			d.setMinExclusive((Date) d.valida(e.getMinExclusive()));
			d.setMinInclusive((Date) d.valida(e.getMinInclusive()));
			r = d;
		} else if (elemento instanceof TypeBlobVO) {
			TypeBlobVO e = (TypeBlobVO) elemento;
			DatoRegistroTypeBlob d = new DatoRegistroTypeBlob(
					getIdDiccionario(e));
			d.setNombre(elemento.getNombre());
			d.setLongitudMaxima(e.getLongitudMaxima());
			d.setLongitudMinima(e.getLongitudMinima());
			r = d;
		} else if (elemento instanceof TypeRegistroVO) {
			TypeRegistroVO e = (TypeRegistroVO) elemento;
			DatoRegistroTypeRegistro d = new DatoRegistroTypeRegistro(
					getIdDiccionario(e));
			d.setNombre(elemento.getNombre());
			r = d;
		}
		return r;
	}

	private String getIdDiccionario(DiccionarioDic e) {
		return FormatUtil.getIdDiccionarioNormalizado(e.getId().getGrupo(), e
				.getId().getId().intValue());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IDiccionarioFactory#getServicio(java.lang.String)
	 */
	public Servicio getServicio(String id) throws IllegalArgumentException {

		if (logger.isDebugEnabled()) {
			logger.debug("getServicio(" + id + ")");
		}
		String grupo = FormatUtil.getAplicacionFromIdServicio(id);
		BigDecimal svr_id = new BigDecimal(
				FormatUtil.getOperacionFromIdServicio(id));

		Session sess = null;
		ServicioSvr svr = null;
		SessionFactory sf = DiccionarioSessionFactory.getSessionFactory();
		sess = sf.getCurrentSession();
		Query q = sess
				.createQuery("select s from ServicioSvr s,ServicioDesc d where "
						+ "s.id.grupo = d.id.grupo"
						+ " and s.id.id = d.id.id"
						+ " and s.id.version = d.version"
						+ " and d.id.grupo=:grupo and d.id.id=:id");
		q.setString("grupo", grupo);
		q.setBigDecimal("id", svr_id);
		if (logger.isDebugEnabled()) {
			logger.debug("getServicio(): Begin Query");
		}
		svr = (ServicioSvr) q.uniqueResult();
		if (svr != null) {
			svr.getDescripcion().getRoles();
			if (logger.isDebugEnabled()) {
				logger.debug("getServicio(): End Query");
			}
		}
		return getServicioFromServicioSvr(svr);

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IDiccionarioFactory#getServicio(java.lang.String,
	 *      java.lang.String)
	 */
	public Servicio getServicio(String id, String version)
			throws IllegalArgumentException {
		if (logger.isDebugEnabled()) {
			logger.debug("getServicio(" + id + ", " + version + ")");
		}
		String grupo = FormatUtil.getAplicacionFromIdServicio(id);
		BigDecimal svr_id = new BigDecimal(
				FormatUtil.getOperacionFromIdServicio(id));
		BigDecimal v = new BigDecimal(
				FormatUtil.getNormalizedVersionOperacion(version));

		Session sess = null;
		ServicioSvr svr = null;
		SessionFactory sf = DiccionarioSessionFactory.getSessionFactory();
		sess = sf.getCurrentSession();
		ServicioSvrKey key = new ServicioSvrKey();
		key.setGrupo(grupo);
		key.setId(svr_id);
		key.setVersion(v);
		svr = (ServicioSvr) sess.get(ServicioSvr.class, key);
		svr.getDescripcion().getRoles();
		return getServicioFromServicioSvr(svr);
	}

	/**
	 * @param svr
	 * @return
	 */
	private Servicio getServicioFromServicioSvr(ServicioSvr svr) {
		ServicioFarmafene sf = null;
		if (logger.isDebugEnabled()) {
			logger.debug("Transformando el servicio=" + svr);
		}
		if (null != svr) {
			sf = new ServicioFarmafene();
			sf.setDisponible(true);
			sf.setCommand(svr.getCommand());
			sf.setMotivoIndisponibilidad("N/A");
			sf.setId(FormatUtil.getIdServicioNormalizado(
					svr.getId().getGrupo(), svr.getId().getId().intValue()));
			sf.setIdAplicacion(FormatUtil.getAplicacionFromIdServicio(sf
					.getId()));
			sf.setVersion(FormatUtil.getNormalizedVersionOperacion(svr.getId()
					.getVersion().toString()));
			if (null != svr.getRegistroEntrada()) {
				sf.setIdRegistroEntrada(FormatUtil.getIdDiccionarioNormalizado(
						svr.getRegistroEntrada().getId().getGrupo(), svr
								.getRegistroEntrada().getId().getId()
								.intValue()));
			}
			if (null != svr.getRegistroSalida()) {
				sf.setIdRegistroSalida(FormatUtil
						.getIdDiccionarioNormalizado(svr.getRegistroSalida()
								.getId().getGrupo(), svr.getRegistroSalida()
								.getId().getId().intValue()));
			}
			ServicioDesc sdesc = svr.getDescripcion();
			sf.setPublic(sdesc.isPublic());
			sf.setInheritAuth(sdesc.isInheritAuth());
			Set<RoleRol> roles = sdesc.getRoles();
			if (null != roles && 0 < roles.size()) {
				Set<String> a = new HashSet<String>();
				sf.setRoles(a);
				for (RoleRol r : roles) {
					a.add(r.getName());
				}
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("getServicioFromServicioSvr(): " + sf);
		}
		return sf;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IDiccionarioFactory#getRegistro(java.lang.String,
	 *      java.util.Map)
	 */
	@Override
	public Registro getRegistro(String idRegistro,
			Map<String, TypeRegistro> definiciones) {
		return new RegistroImpl(idRegistro, definiciones);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.IConfigurableBean#getImplementationVentor()
	 */
	@Override
	public String getImplementationVentor() {
		return "Farmafene";
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.IConfigurableBean#getImplementationVersion()
	 */
	@Override
	public String getImplementationVersion() {
		return "1.0";
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.IConfigurableBean#getImplementationDescription()
	 */
	@Override
	public String getImplementationDescription() {
		return "Implementación de diccionario usando Hibernate";
	}
}
