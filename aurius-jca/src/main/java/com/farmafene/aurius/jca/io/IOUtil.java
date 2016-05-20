package com.farmafene.aurius.jca.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.jms.BytesMessage;
import javax.jms.JMSException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IOUtil {
	private static final Logger logger = LoggerFactory.getLogger(IOUtil.class);

	private IOUtil() {

	}

	public static byte[] marshall(Serializable msg, Codec codec)
			throws IllegalArgumentException, IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("marshall('" + msg + "','" + codec + "')");
		}
		if (msg == null || codec == null) {
			throw new IllegalArgumentException("El mensaje debe existir");
		}
		byte[] marshall = null;
		switch (codec) {
		case Java:
			marshall = JavaMarshall(msg);
			break;
		case Unsuported:
		default:
			throw new IOException("Codificación no admitida");
		}
		return marshall;
	}

	public static Serializable unmarshall(byte[] msg, Codec codec)
			throws IllegalArgumentException, IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("unmarshall('" + msg + "','" + codec + "')");
		}
		Serializable unmarshall = null;
		if (msg == null || codec == null) {
			throw new IllegalArgumentException("El mensaje debe existir");
		}
		switch (codec) {
		case Java:
			unmarshall = JavaUnmarshall(msg);
			break;
		case Unsuported:
		default:
			throw new IOException("Codificaci�n no admitida");
		}
		return unmarshall;
	}

	private static byte[] JavaMarshall(Serializable msg) throws IOException {
		ByteArrayOutputStream baos = null;
		ObjectOutputStream oos = null;
		try {
			byte[] marshall = null;
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(msg);
			marshall = baos.toByteArray();
			oos.close();
			baos.close();
			return marshall;
		} catch (IOException e) {
			throw e;
		} finally {
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					logger.warn("Error al cerrar el Stream:", e);
				}
			}
			if (baos != null) {
				try {
					baos.close();
				} catch (IOException e) {
					logger.warn("Error al cerrar el Stream:", e);
				}
			}
		}
	}

	private static Serializable JavaUnmarshall(byte[] msg) throws IOException {
		InputStream bais = null;
		ObjectInputStream ois = null;
		try {
			bais = new ByteArrayInputStream(msg);
			ois = new ObjectInputStream(bais);
			return (Serializable) ois.readObject();
		} catch (IOException e) {
			throw e;
		} catch (ClassNotFoundException e) {
			throw new IOException("Error de comunicaciones", e);
		} finally {
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					logger.warn("Error al cerrar el Stream:", e);
				}
			}
			if (bais != null) {
				try {
					bais.close();
				} catch (IOException e) {
					logger.warn("Error al cerrar el Stream:", e);
				}
			}
		}
	}

	public static byte[] readBytes(BytesMessage msg) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] leido = null;
		int read = 0;
		try {
			byte[] buffer = new byte[1000];
			while ((read = msg.readBytes(buffer)) > 0) {
				baos.write(buffer, 0, read);
			}
			baos.flush();
			leido = baos.toByteArray();
		} catch (JMSException je) {
			throw new IOException(je);
		} finally {
			baos.close();
		}
		return leido;
	}

}
