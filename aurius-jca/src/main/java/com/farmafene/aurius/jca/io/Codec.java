package com.farmafene.aurius.jca.io;

public enum Codec {
	Unsuported, Java;

	public static Codec getByCode(int code) {
		Codec salida = Unsuported;
		try {
			salida = values()[code];
		} catch (IndexOutOfBoundsException e) {
			// do nothing
		}
		return salida;
	}
}
