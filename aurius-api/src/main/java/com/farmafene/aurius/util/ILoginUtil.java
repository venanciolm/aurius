package com.farmafene.aurius.util;

import java.util.Properties;

import com.farmafene.aurius.AuriusAuthException;
import com.farmafene.aurius.AuthInfo;

public interface ILoginUtil {

	AuthInfo createInfo(Properties props) throws AuriusAuthException;;

	AuthInfo processLogin(AuthInfo info) throws AuriusAuthException;

	void processLogout(AuthInfo info);

	AuthInfo getServerAuthInfo(AuthInfo info);
}
