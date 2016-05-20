package com.farmafene.aurius.jca;

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
import java.rmi.RemoteException;

import javax.resource.ResourceException;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import bitronix.tm.TransactionManagerServices;

import com.farmafene.commons.ioc.BeanFactory;

public class Sample2 {

	public static void main(String... args) throws SecurityException,
			IllegalStateException, RollbackException, HeuristicMixedException,
			HeuristicRollbackException, SystemException, NotSupportedException,
			IllegalArgumentException, RemoteException, ResourceException {

		Object bean = null;
		bean = BeanFactory.getBean(TransactionManager.class,
				"BitronixTransactionManager");
		try {
			System.out.println("Bean: " + bean);
			AuriusConnectionFactory acf = null;
			acf = (AuriusConnectionFactory) BeanFactory
					.getBean(AuriusConnectionFactory.class);
			System.out.println("acf: " + acf);
			if (acf != null) {
				System.out.println("Ha encontrado la factoría");
				UserTransaction tx = (UserTransaction) TransactionManagerServices
						.getTransactionManager();
				System.out.println("La tx está activa: " + tx);
				tx.begin();
				tx.setTransactionTimeout(400);
				System.out.println("La tx está activa: " + tx);
				AuriusConnection con1 = acf.getConnection();
				con1.process(null, "Hola");
				System.out.println("Conexion1: " + con1);
				AuriusConnection con2 = acf.getConnection();
				System.out.println("Conexion2: " + con2);
				con1.close();
				System.out.println("Conexion1: " + con1);
				con2.process(null, "Hola");
				con2.close();
				System.out.println("Conexion2: " + con2);
				tx.commit();
			}
		} finally {
			BeanFactory.getIBeanFactory().destroy();
		}

	}
}
