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

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.aurius.AuthInfo;
import com.farmafene.aurius.core.IAuriusUserFactory;
import com.farmafene.aurius.dao.hibernate.auth.GrupoGrp;
import com.farmafene.aurius.dao.hibernate.auth.RoleRol;
import com.farmafene.aurius.dao.hibernate.auth.UserUsr;
import com.farmafene.aurius.server.IAuriusUser;
import com.farmafene.commons.cas.AuthInfoString;

public class FarmafeneAuriusUserFactory implements IAuriusUserFactory {

	private static final Logger logger = LoggerFactory
			.getLogger(FarmafeneAuriusUserFactory.class);
	private static final RoleRol ROL_ADMIN;
	private static final GrupoGrp GRP_ADMIN;
	private static final String ID_ADMIN = "00000000000000000000000000000000";

	static {
		ROL_ADMIN = new RoleRol();
		ROL_ADMIN.setId(ID_ADMIN);
		GRP_ADMIN = new GrupoGrp();
		GRP_ADMIN.setId(ID_ADMIN);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IAuriusUserFactory#getIAuriusUser(java.lang
	 *      .String)
	 */
	@Override
	public IAuriusUser getIAuriusUser(AuthInfo cookie) {
		String login = null;
		AuriusPrincipal user = null;
		if (cookie.isWrapperFor(AuthInfoString.class)) {
			login = cookie.unwrap(AuthInfoString.class).getValue();
		}
		if (login == null) {
			throw new IllegalArgumentException("Debe establecerse un usuario");
		}
		Session sess = DiccionarioSessionFactory.getSessionFactory()
				.getCurrentSession();
		UserUsr dao = (UserUsr) sess.createCriteria(UserUsr.class)
				.add(Restrictions.eq("login", login)).uniqueResult();
		if (null != dao) {
			user = new AuriusPrincipal();
			user.setUserName(dao.getLogin());
			user.setName(dao.getApellido1() + " " + dao.getApellido2() + ", "
					+ dao.getNombre());
			user.setLocale(createLocale(dao));
			Set<String> roles = null;
			if (ID_ADMIN.equals(dao.getId())) {
				roles = getAllRoles(sess);
			} else {
				roles = getRoles(sess, dao);
			}
			user.getRoles().addAll(roles);
			if (logger.isDebugEnabled()) {
				if (null != user) {
					logger.debug("Roles: " + user.getRoles());
				}
			}
		}
		return user;
	}

	private Set<String> getAllRoles(Session sess) {
		@SuppressWarnings("unchecked")
		List<RoleRol> roles = sess.createCriteria(RoleRol.class).list();
		return transform(roles);
	}

	private Set<String> getRoles(Session sess, UserUsr dao) {
		Set<RoleRol> roles = new HashSet<RoleRol>();
		Set<String> getRoles = null;
		Set<RoleRol> rUser = dao.getRoles();
		if (null != rUser) {
			roles.addAll(rUser);
		}
		if (roles.contains(ROL_ADMIN)) {
			getRoles = getAllRoles(sess);
		} else {
			getRoles = getRolesFromGroupsIterative(sess, roles, dao.getGrupos());
		}
		return getRoles;
	}

	private Set<String> getRolesFromGroupsIterative(Session sess,
			Set<RoleRol> roles, Set<GrupoGrp> grupos) {
		Set<String> getRolesFromGroupsIterative = null;
		int count = 0;
		Set<GrupoGrp> gruposs = null;
		if (null == grupos) {
			gruposs = new LinkedHashSet<GrupoGrp>();
		} else {
			gruposs = new LinkedHashSet<GrupoGrp>(grupos);
		}
		for (GrupoGrp g : gruposs) {
			grupos.addAll(g.getGrupos());
			count = grupos.size();
		}
		if (0 == count) {
			getRolesFromGroupsIterative = new LinkedHashSet<String>();
		} else if (gruposs.contains(GRP_ADMIN)) {
			getRolesFromGroupsIterative = getAllRoles(sess);
		} else {
			if (count == gruposs.size()) {
				getRolesFromGroupsIterative = getRolesFromGroups(sess, roles,
						gruposs);
			} else {
				getRolesFromGroupsIterative = getRolesFromGroupsIterative(sess,
						roles, gruposs);
			}
		}
		return getRolesFromGroupsIterative;
	}

	private Set<String> getRolesFromGroups(Session sess, Set<RoleRol> roles,
			Set<GrupoGrp> grupos) {
		Set<String> getRolesFromGroups = null;
		for (GrupoGrp g : grupos) {
			roles.addAll(g.getRoles());
			if (roles.contains(ROL_ADMIN)) {
				getRolesFromGroups = getAllRoles(sess);
				break;
			}
			getRolesFromGroups = transform(roles);
		}
		return getRolesFromGroups;
	}

	private Set<String> transform(Collection<RoleRol> roles) {
		Set<String> r = new LinkedHashSet<String>();
		for (RoleRol a : roles) {
			r.add(a.getName());
		}
		return r;
	}

	private Locale createLocale(UserUsr dao) {
		return new Locale("es", "ES");
	}
}
