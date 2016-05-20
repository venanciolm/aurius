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
package com.farmafene.aurius.dao.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.aurius.server.Configuracion;

/**
 * Utilidad de validación de datos del DAO
 * 
 * @author vlopez
 * @version 1.0.0
 * @since 1.0.0
 */
public class FormatUtil {

	private static final Logger logger = LoggerFactory
			.getLogger(FormatUtil.class);
	private static final int LENGTH_APP = getAppLength();
	private static final int LENGTH_OPERACION = getOperacionLength();
	private static final int LENGTH_DICCIONARIO = getDiccionarioLength();
	private static final int LENGTH_VERSION = getVersionLength();

	private static final String MASK_APP = defaulfMask(LENGTH_APP, ' ');
	private static final String MASK_OPERACION = defaulfMask(LENGTH_OPERACION,
			'0');
	private static final String MASK_DICCIONARIO = defaulfMask(
			LENGTH_DICCIONARIO, '0');
	private static final String MASK_VERSION = defaulfMask(LENGTH_VERSION, '0');

	/**
	 * Constructor privado
	 * 
	 * @since 1.0.0
	 */
	private FormatUtil() {
	}

	/**
	 * Obtiene la longitud de la parte del ID correspondiente a la aplicación
	 * 
	 * @return longitud de la parte de aplicación
	 * @since 1.0.0
	 */
	private static int getAppLength() {
		return getIntProperty("aurius.length.app", "6");
	}

	/**
	 * Obtiene la longitud de la parte del ID correspondiente a la ordenación
	 * (numérico) del ID de diccionario
	 * 
	 * @return longitud de la parte numérica del ID de diccionario
	 * @since 1.0.0
	 */
	private static int getDiccionarioLength() {
		return getIntProperty("aurius.length.diccionario", "5");
	}

	/**
	 * Obtiene la longiutd de la parte del ID correspondiente al número de
	 * operación en una aplicación
	 * 
	 * @return longitud de la parte numérica de un servicio
	 * @since 1.0.0
	 */
	private static int getOperacionLength() {
		return getIntProperty("aurius.length.servicio", "3");
	}

	/**
	 * Obtiene la longitud total de un campo de versión de un determinado
	 * servicio
	 * 
	 * @return longitud de la versión
	 * @since 1.0.0
	 */
	private static int getVersionLength() {
		return getIntProperty("aurius.length.version", "2");
	}

	private static int getIntProperty(String key, String value)
			throws ExceptionInInitializerError {
		int salida = -1;
		try {
			String property = Configuracion.getProperty(key);
			if (null == property || "".equals(property.trim())) {
				property = value;
			}
			salida = Integer.parseInt(property);
		} catch (Throwable e) {
			logger.error("Error en el parámetro de configuración >" + key
					+ "< tomando el valor por defecto >" + value + "<: "
					+ e.getMessage());
		}
		if (salida == -1) {
			throw new ExceptionInInitializerError(
					new UnsupportedOperationException(
							"Error en la incicialización del parámetro de configuración >"
									+ key + "< y valor por defecto >" + value
									+ "<"));
		}
		return salida;
	}

	/**
	 * Obtiene una máscara (valor por defecto) para un determinado valor
	 * 
	 * @param length
	 *            longitud de la máscara
	 * @param charValue
	 *            valor por defecto
	 * @return la m�scara del valor.
	 * @since 1.0.0
	 */
	private static String defaulfMask(int length, char charValue) {
		char[] aux = new char[length];
		Arrays.fill(aux, charValue);
		return new String(aux);

	}

	/**
	 * Obtiene el máximo de un determinado decimal (precision,escala)
	 * 
	 * @param precision
	 *            la precición
	 * @param scale
	 *            la escala
	 * @return numérico que indica el máximo
	 * @throws IllegalArgumentException
	 *             incorreción en los parámetros de entrada
	 * @since 1.0.0
	 */
	public static BigDecimal getMaxDecimal(int precision, int scale)
			throws IllegalArgumentException {
		if (precision < 0) {
			throw new IllegalArgumentException("FormatUtil.getMaxDecimal('"
					+ precision + "','" + scale + "') con precición inválida");
		}
		if (scale < 0) {
			throw new IllegalArgumentException("FormatUtil.getMaxDecimal('"
					+ precision + "','" + scale + "') con escala inválida");
		}
		if (precision <= scale) {
			throw new IllegalArgumentException("FormatUtil.getMaxDecimal('"
					+ precision + "','" + scale
					+ "') la escala debe ser menor que la precision");
		}
		char[] aux = new char[precision];
		Arrays.fill(aux, '9');
		BigInteger bi = new BigInteger(new String(aux));
		return new BigDecimal(bi, scale);
	}

	/**
	 * Obtiene una ID de servicio normalizada
	 * 
	 * @param id
	 *            id sin normalizar
	 * @return id normalizado
	 * @throws IllegalArgumentException
	 *             El id a normalizar es inválido
	 * @since 1.0.0
	 */
	public static final String getIdServicioNormalizado(String id)
			throws IllegalArgumentException {
		if (id == null || id.length() > LENGTH_APP + LENGTH_OPERACION) {
			throw new IllegalArgumentException(
					"FormatUtil.getIdServicioNormalizado('" + id
							+ "'): El identificador en inválido");
		}
		int beginOp = id.length() - LENGTH_OPERACION;
		if (beginOp < 0) {
			beginOp = 0;
		}
		StringBuilder opId = new StringBuilder(MASK_OPERACION);
		opId.append(id.substring(beginOp));
		String strOpId = opId.substring(opId.length() - LENGTH_OPERACION);
		try {
			Integer.parseInt(strOpId);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(
					"FormatUtil.getIdServicioNormalizado('" + id
							+ "'): El identificador en inválido");
		}
		StringBuilder appId = new StringBuilder(id.substring(0, beginOp));
		appId.append(MASK_APP);
		return appId.substring(0, LENGTH_APP).toUpperCase() + strOpId;
	}

	/**
	 * Obtiene una ID de diccionario normalizado
	 * 
	 * @param id
	 *            id sin normalizar
	 * @return id normalizado
	 * @throws IllegalArgumentException
	 *             El id a normalizar es inv�lido
	 * @since 1.0.0
	 */
	public static final String getIdDiccionarioNormalizado(String id) {
		if (id == null || id.length() > LENGTH_APP + LENGTH_DICCIONARIO) {
			throw new IllegalArgumentException(
					"FormatUtil.getIdDiccionarioNormalizado('" + id
							+ "'): El identificador en inválido");
		}
		int beginOp = id.length() - LENGTH_DICCIONARIO;
		if (beginOp < 0) {
			beginOp = 0;
		}
		StringBuilder opId = new StringBuilder(MASK_DICCIONARIO);
		opId.append(id.substring(beginOp));
		String strOpId = opId.substring(opId.length() - LENGTH_DICCIONARIO);
		try {
			Integer.parseInt(strOpId);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(
					"FormatUtil.getIdDiccionarioNormalizado('" + id
							+ "'): El identificador en inválido");
		}
		StringBuilder appId = new StringBuilder(id.substring(0, beginOp));
		appId.append(MASK_APP);
		return appId.substring(0, LENGTH_APP).toUpperCase() + strOpId;
	}

	/**
	 * Obtiene un Id de servicio normalizado en función de:
	 * 
	 * @param app
	 *            aplicacion
	 * @param code
	 *            codigo de la operacion
	 * @return id normalizado
	 * @since 1.0.0
	 */
	public static final String getIdServicioNormalizado(String app, int code) {

		String strApp = app == null ? "" : app + MASK_APP;
		String strCode = MASK_OPERACION + code;
		return strApp.substring(0, LENGTH_APP)
				+ strCode.substring(strCode.length() - LENGTH_OPERACION);
	}

	/**
	 * Obtiene un id de diccionario normalizado
	 * 
	 * @param app
	 *            aplicaci�n
	 * @param code
	 *            identificador del dato en la aplicación
	 * @return id de diccionario normalizado
	 * @since 1.0.0
	 */
	public static final String getIdDiccionarioNormalizado(String app, int code) {
		String strApp = app == null ? "" : app + MASK_APP;
		String strCode = MASK_DICCIONARIO + code;
		return strApp.substring(0, LENGTH_APP)
				+ strCode.substring(strCode.length() - LENGTH_DICCIONARIO);
	}

	/**
	 * Obtiene la Aplicación en función de un ID de diccionario
	 * 
	 * @param idDiccionario
	 * @return aplicacion
	 * @throws IllegalArgumentException
	 *             si el Id es inválido
	 * @since 1.00
	 */
	public static final String getAplicacionFromIdDiccionario(
			String idDiccionario) throws IllegalArgumentException {
		String idNormalizado = getIdDiccionarioNormalizado(idDiccionario);
		return idNormalizado.substring(0, LENGTH_APP);
	}

	public static final String getOperacionFromIdDiccionario(
			String idDiccionario) throws IllegalArgumentException {
		String idNormalizado = getIdDiccionarioNormalizado(idDiccionario);
		return idNormalizado.substring(LENGTH_APP);
	}

	public static final String getAplicacionFromIdServicio(String idServicio) {
		String idNormalizado = getIdServicioNormalizado(idServicio);
		return idNormalizado.substring(0, LENGTH_APP);
	}

	public static final String getOperacionFromIdServicio(String idServicio) {
		String idNormalizado = getIdServicioNormalizado(idServicio);
		return idNormalizado.substring(LENGTH_APP);
	}

	public static final String getNormalizedVersionOperacion(String version) {
		String versionNormalizada = version;
		if (versionNormalizada != null) {
			if (versionNormalizada.length() > LENGTH_VERSION) {
				throw new IllegalArgumentException(
						"FormatUtil.getNormalizedVersionOperacion('" + version
								+ "'): El identificador en inválido");
			} else {
				versionNormalizada = (MASK_VERSION + versionNormalizada);
				versionNormalizada = versionNormalizada
						.substring(LENGTH_VERSION);
			}
		}
		return versionNormalizada;
	}
}
