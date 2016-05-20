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
package com.farmafene.aurius.jca;

import java.rmi.RemoteException;

import javax.resource.ResourceException;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

import bitronix.tm.TransactionManagerServices;

import com.farmafene.aurius.Registro;
import com.farmafene.commons.cas.AuthInfoString;
import com.farmafene.commons.ioc.BeanFactory;

public class Sample {

	public static void main(String... args) throws SecurityException,
			IllegalStateException, RollbackException, HeuristicMixedException,
			HeuristicRollbackException, SystemException, NotSupportedException,
			IllegalArgumentException, RemoteException, ResourceException {

		boolean transaccional = true;
		boolean error = false;

		Object bean = null;
		bean = BeanFactory.getBean(Object.class,"mama");
		System.out.println("mama: " + bean);
		bean = BeanFactory.getBean(TransactionManager.class,"BitronixTransactionManager");
		// LoginUtil l = new LoginUtil();
		TransactionManager txm = null;
		try {
			System.out.println("Bean: " + bean);
			AuriusConnectionFactory acf = null;
			acf = (AuriusConnectionFactory) BeanFactory
					.getBean(AuriusConnectionFactory.class);
			System.out.println("acf: " + acf);
			if (acf != null) {
				txm = TransactionManagerServices.getTransactionManager();
				System.out.println("Ha encontrado la factoría");
				AuthInfoString info = new AuthInfoString();
				info.setValue("vlopez");
				Registro registro = null;
				// AuriusConnection con1 = acf.getConnection();
				// registro = con1.process(info, "AURIUS999");
				// registro.put("EchoMsg", "Este es un mensaje");
				// System.out.println(" ** Registro: " + registro);
				// System.out.println(" -* Registro: "
				// + new RegistroNoModificable(registro));
				// System.out.println(" -- "
				// + registro.getClass().getCanonicalName());
				// System.out.println(con1.process(info, "AURIUS999",
				// registro));
				// System.out.println("Conexion1: " + con1);
				// con1.close();
				System.out.println("Ha encontrado la factoría");
				if (transaccional) {
					txm.begin();
				}
				AuriusConnection con2 = acf.getConnection();
				System.out.println("Conexion2: " + con2);
				registro = con2.process(info, "AURIUS999");
				registro.put("EchoMsg", "Este es un mensaje");
				System.out.println(con2.process(info, "AURIUS999", registro));
				con2.close();
				System.out.println("Conexion2: " + con2);
				AuriusConnection con = acf.getConnection();
				System.out.println("Conexion: " + con);
				registro = con.process(info, "AURIUS999");
				registro.put("EchoMsg", "Este es un mensaje");
				System.out.println(con.process(info, "AURIUS999", registro));
				con.close();
				if (error) {
					throw new RuntimeException();
				}
				if (transaccional) {
					txm.commit();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (transaccional) {
				txm.rollback();
			}
		} finally {
			BeanFactory.getIBeanFactory().destroy();
		}
	}
}
