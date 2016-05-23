/*
 * Copyright (c) 2009-2016 farmafene.com
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
package com.farmafene.aurius.webapp.test;

import java.sql.SQLException;

import javax.resource.spi.XATerminator;
import javax.resource.spi.work.WorkManager;
import javax.transaction.TransactionManager;
import javax.transaction.TransactionSynchronizationRegistry;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.farmafene.aurius.core.AuriusContainerSubject;
import com.farmafene.commons.j2ee.tools.jca.common.StringPrintStream;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:JNDI.xml",
		"classpath:SpringConfiguration.xml" })
public class InboundUTest implements InitializingBean {

	private static final Logger logger = LoggerFactory
			.getLogger(InboundUTest.class);

	@Autowired
	private ConfigurableApplicationContext ctx;
	private static ConfigurableApplicationContext CTX;

	@BeforeClass
	public static void beforeClass() throws SQLException {
		logger.info("Init!");
	}

	@AfterClass
	public static void afterClass() {
		logger.info("Destroy!");
		CTX.close();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		if (logger.isInfoEnabled()) {
			StringPrintStream ps = new StringPrintStream();
			ps.println();
			ps.println("/*--------------------------------------------+|");
			ps.println("|| Begin of Test                              ||");
			ps.print("+---------------------------------------------*/");
			logger.info("{}", ps);
		}
		Assert.assertNotNull(this.ctx);
		CTX = this.ctx;
	}

	@Test
	public void test() throws Exception {
		if (logger.isDebugEnabled()) {
			StringPrintStream ps = new StringPrintStream();
			ps.println();
			ps.println("/*--------------------------------------------+|");
			ps.println("|| Comenzamos a validar la BeanFactory        ||");
			ps.print("|+--------------------------------------------*/");
			logger.info("{}", ps);
		}
		logger.info("WorkManager:                        {}",
				this.ctx.getBean(WorkManager.class));
		logger.info("TransactionManager:                 {}",
				this.ctx.getBean(TransactionManager.class));
		logger.info("TransactionSynchronizationRegistry: {}",
				this.ctx.getBean(TransactionSynchronizationRegistry.class));
		logger.info("XATerminator:                       {}",
				this.ctx.getBean(XATerminator.class));
		Assert.assertNotNull(this.ctx.getBean(WorkManager.class));
		Assert.assertNotNull(this.ctx.getBean(TransactionManager.class));
		Assert.assertNotNull(this.ctx
				.getBean(TransactionSynchronizationRegistry.class));
		Assert.assertNotNull(this.ctx.getBean(XATerminator.class));
		if (logger.isDebugEnabled()) {
			StringPrintStream ps = new StringPrintStream();
			ps.println();
			ps.println("/*--------------------------------------------+|");
			ps.println("|| Starting.....                              ||");
			ps.print("|+--------------------------------------------*/");
			logger.info("{}", ps);
		}
		AuriusContainerSubject.start();
		if (logger.isDebugEnabled()) {
			StringPrintStream ps = new StringPrintStream();
			ps.println();
			ps.println("/*--------------------------------------------+|");
			ps.println("|| Started                                    ||");
			ps.print("|+--------------------------------------------*/");
			logger.info("{}", ps);
		}
		if (logger.isDebugEnabled()) {
			StringPrintStream ps = new StringPrintStream();
			ps.println();
			ps.println("/*--------------------------------------------+|");
			ps.println("|| Stoping ......                             ||");
			ps.print("|+--------------------------------------------*/");
			logger.info("{}", ps);
		}
		if (logger.isDebugEnabled()) {
			StringPrintStream ps = new StringPrintStream();
			ps.println();
			ps.println("/*--------------------------------------------+|");
			ps.println("|| Stopped                                    ||");
			ps.print("|+--------------------------------------------*/");
			logger.info("{}", ps);
		}
		AuriusContainerSubject.stop();
		if (logger.isDebugEnabled()) {
			StringPrintStream ps = new StringPrintStream();
			ps.println();
			ps.println("/*--------------------------------------------+|");
			ps.println("|| End....                                    ||");
			ps.print("|+--------------------------------------------*/");
			logger.info("{}", ps);
		}
	}
}
