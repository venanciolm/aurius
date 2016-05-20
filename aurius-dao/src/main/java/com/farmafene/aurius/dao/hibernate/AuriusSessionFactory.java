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
package com.farmafene.aurius.dao.hibernate;

import com.farmafene.commons.hibernate.HibernateFactory;
import com.farmafene.commons.hibernate.HibernateSessionFactory;

@HibernateFactory(jndiName = "farmafene.com/aurius/jdbc/XA/common", name = AuriusSessionFactory.AURIUS_DB)
public class AuriusSessionFactory extends HibernateSessionFactory {
	public static final String AURIUS_DB = "AURIUS";
	
//	   |- com.farmafene.aurius.dao.hibernate.OperativasOp
//	   |- com.farmafene.aurius.dao.hibernate.OperativasDescOpDesc
//	   |- com.farmafene.aurius.dao.hibernate.DiccionarioDic
//	   |- com.farmafene.aurius.dao.hibernate.DiccionarioDescDdesc
//	   |- com.farmafene.aurius.dao.hibernate.ServicioDesc
//	   |- com.farmafene.aurius.dao.hibernate.ServicioDescDesc
//	   |- com.farmafene.aurius.dao.hibernate.ServicioSvr
//	   |- com.farmafene.aurius.dao.hibernate.RegistroReg
//	   |- com.farmafene.aurius.dao.hibernate.TypeBlobVO
//	   |- com.farmafene.aurius.dao.hibernate.TypeDecimalVO
//	   |- com.farmafene.aurius.dao.hibernate.TypeTimeVO
//	   |- com.farmafene.aurius.dao.hibernate.TypeStringVO
//	   |- com.farmafene.aurius.dao.hibernate.TypeRegistroVO
}
