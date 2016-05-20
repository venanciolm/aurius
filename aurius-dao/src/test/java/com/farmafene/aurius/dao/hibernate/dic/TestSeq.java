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
package com.farmafene.aurius.dao.hibernate.dic;

import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;

import com.farmafene.aurius.dao.hibernate.auth.GrupoGrp;
import com.farmafene.aurius.dao.hibernate.auth.RoleRol;
import com.farmafene.aurius.dao.hibernate.auth.UserUsr;
import com.farmafene.aurius.dao.test.Test;
import com.farmafene.aurius.dao.test.TestUUIDHex;

public class TestSeq {

	public static void main(String... args) {
		Properties props = new Properties();
		props.put("org.hibernate.dialect.MySQLDialect",
				"org.hibernate.dialect.MySQL5InnoDBDialect");
		props.put("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
		props.put("hibernate.connection.url", "jdbc:mysql://localhost/dic");
		props.put("hibernate.connection.username", "system");
		props.put("hibernate.connection.password", "manager");
		props.put("hibernate.current_session_context_class", "thread"/* "org.hibernate.context.ThreadLocalSessionContext" */);
		props.put("hibernate.show_sql", "true");
		AnnotationConfiguration conf = new AnnotationConfiguration();
		conf.addProperties(props);
		conf.addAnnotatedClass(DiccionarioDesc.class);
		conf.addAnnotatedClass(UserUsr.class);
		conf.addAnnotatedClass(RoleRol.class);
		conf.addAnnotatedClass(GrupoGrp.class);
		conf.addAnnotatedClass(Test.class);
		conf.addAnnotatedClass(TestUUIDHex.class);
		SessionFactory sf = conf.buildSessionFactory();
		Session sess = sf.getCurrentSession();
		DiccionarioDesc o = new DiccionarioDesc();
		o.setDescripcion("pruebas x");
		Transaction tx = sess.beginTransaction();
//		UserUsr user = (UserUsr) sess.get(UserUsr.class, new BigDecimal("1"));
//		Set<RoleRol> roles = user.getRoles();
//		if (roles != null) {
//			System.out.println("--> Hay roles");
//			for (RoleRol r : roles) {
//				System.out.println(r);
//			}
//		}
//		Set<GrupoGrp> grupos = user.getGrupos();
//		if (grupos != null) {
//			System.out.println("--> Hay grupos");
//			for (GrupoGrp r : grupos) {
//				System.out.println(r);
//				System.out.println(r.getGrupos());
//
//			}
//		}
		TestUUIDHex test = new TestUUIDHex();
		sess.save(test);
		tx.commit();
	}
}
