package auth;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.SimpleTimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import main.Daemon;

import org.apache.log4j.Level;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jboss.util.Base64;

import utils.HibernateUtil2;
import utils.Usuarioclave;

public class AuthConnector {

	private LdapContext ctx = null;
	private int pwdMaxAge = 0;
	private int pwdExpireWarningPpo = 0;
	private NamingEnumeration<SearchResult> results = null;
	private SearchControls controls = new SearchControls();
	private Date pwdChangedTime;
	private int pwdExpireWarning = 0;
	private String oldPasswd = "";
	private Usuarioclave dbUsuarioClave = null;

	public enum changePasswdReturn {
		Ok, Fail, InHistory, Invalid
	}

	public enum passwordStatus {
		Ok, Wrong, Expired, Locked, Expiring
	}

	public enum userStatus {
		New, Migrated, NoMigrated
	}

	public AuthConnector() {
		getContextImpl();
	}

	private void getContextImpl() {

		Hashtable<String, String> env = new Hashtable<String, String>(7);

		env.put(Context.INITIAL_CONTEXT_FACTORY,
				"com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, Daemon.getConfig("LDAP_PROVIDER_URL"));
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL,
				Daemon.getConfig("LDAP_SECURITY_PRINCIPAL"));
		env.put(Context.SECURITY_CREDENTIALS,
				Daemon.getConfig("LDAP_SECURITY_CREDENTIALS"));
		env.put(Context.SECURITY_PROTOCOL, "ssl");
		env.put("com.sun.jndi.ldap.connect.pool", "false");
		try {
			ctx = new InitialLdapContext(env, null);

			controls = new SearchControls();
			controls.setReturningAttributes(new String[] { "*", "+" });
			controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			results = ctx.search(Daemon.getConfig("LDAP_PPOLICY_DN"),
					"(objectclass=pwdPolicy)", controls);
			while (results.hasMore()) {
				SearchResult searchResult = results.next();
				Attributes attributes = searchResult.getAttributes();
				pwdMaxAge = Integer.parseInt(attributes.get("pwdMaxAge")
						.toString().split(":")[1].trim());
				pwdExpireWarningPpo = Integer.parseInt(attributes
						.get("pwdExpireWarning").toString().split(":")[1]
						.trim());
			}
		} catch (NamingException e) {
			Daemon.getMiLog().log(Level.ERROR,
					"Conectando al LDAP|" + e.getMessage());
			this.closeConn();
		}
	}

	private String getMD5(String input) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");

			md.reset();
			md.update(input.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			Daemon.getMiLog().log(Level.ERROR, "Get MD5|" + e.getMessage());
		} catch (UnsupportedEncodingException e) {
			Daemon.getMiLog().log(Level.ERROR, "Get MD5|" + e.getMessage());
		}
		return Base64.encodeBytes(md.digest());
	}

	public changePasswdReturn changePassword(String user, String passwd,
			String fdn) {
		changePasswdReturn retval = changePasswdReturn.Ok;
		try {
			if (this.validarClave(passwd, fdn)) {
				ctx.addToEnvironment(Context.SECURITY_PRINCIPAL,
						Daemon.getConfig("LDAP_SECURITY_PRINCIPAL"));
				ctx.addToEnvironment(Context.SECURITY_CREDENTIALS,
						Daemon.getConfig("LDAP_SECURITY_CREDENTIALS"));
				ctx.reconnect(null);
				results = ctx.search(getUserDN(user),
						"(objectclass=simpleSecurityObject)", controls);
				List<String> lHistory = new ArrayList<String>();
				while (results.hasMore()) {
					SearchResult searchResult = results.next();
					Attributes attributes = searchResult.getAttributes();
					if (attributes.get("pwdHistory") != null) {
						String[] history = attributes.get("pwdHistory")
								.toString().split(",");
						for (int i = 0; i < history.length; i++) {
							if (history[i].contains("}"))
								lHistory.add(history[i].split("}")[1]);
							else
								lHistory.add(history[i].split("}")[0]);
						}
					}
				}
				String md5pwd = getMD5(passwd);
				if (lHistory.contains(md5pwd)) {
					retval = changePasswdReturn.InHistory;
				}

				if (retval != changePasswdReturn.InHistory) {
					ModificationItem[] mods = new ModificationItem[1];
					mods[0] = new ModificationItem(
							LdapContext.REPLACE_ATTRIBUTE, new BasicAttribute(
									"userPassword", "{MD5}" + md5pwd));
					ctx.modifyAttributes(getUserDN(user), mods);
				}
			} else {
				retval = changePasswdReturn.Invalid;
			}
		} catch (NamingException e) {
			Daemon.getMiLog().log(Level.ERROR,
					"Cambiando Password en LDAP|" + e.getMessage());
			retval = changePasswdReturn.Fail;
		}
		this.closeConn();
		return retval;
	}

	private String getUserDN(String user) {
		return "uid=" + user + "," + Daemon.getConfig("LDAP_USERS_OU");

	}

	private passwordStatus getUserInfo(String user) throws NamingException,
			ParseException {
		passwordStatus retval = passwordStatus.Ok;

		results = ctx.search(getUserDN(user),
				"(objectclass=simpleSecurityObject)", controls);
		while (results.hasMore()) {
			SearchResult searchResult = results.next();
			Attributes attributes = searchResult.getAttributes();

			DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			df.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));

			String startDateString = attributes.get("pwdChangedTime")
					.toString().split(":")[1].trim();

			pwdChangedTime = df.parse(startDateString);

			long expiringTime = (System.currentTimeMillis() - pwdChangedTime
					.getTime()) / 1000;

			if (pwdExpireWarningPpo <= expiringTime
					&& pwdMaxAge >= expiringTime) {
				pwdExpireWarning = (int) (pwdMaxAge - expiringTime) / 86400;
				retval = passwordStatus.Expiring;
			}

			if (pwdMaxAge <= expiringTime)
				retval = passwordStatus.Expired;

			if (attributes.get("pwdAccountLockedTime") != null) {
				retval = passwordStatus.Locked;
			}

		}
		return retval;
	}

	public passwordStatus authUser(String user, String passwd) {
		passwordStatus retval = passwordStatus.Ok;
		try {
			retval = getUserInfo(user);
			if (retval == passwordStatus.Ok) {
				ctx.addToEnvironment(Context.SECURITY_PRINCIPAL,
						getUserDN(user));
				ctx.addToEnvironment(Context.SECURITY_CREDENTIALS, passwd);
				ctx.reconnect(null);
			}
		} catch (NamingException e) {
			if (e.getMessage().contains("49")) {
				retval = passwordStatus.Wrong;
			}
			Daemon.getMiLog().log(Level.ERROR, "Auth Error|" + e.getMessage());
		} catch (ParseException e) {
			Daemon.getMiLog().log(Level.ERROR, "Auth Error|" + e.getMessage());
		}
		this.closeConn();
		return retval;
	}

	public int getPwdExpireWarning() {
		return pwdExpireWarning;
	}

	public userStatus initUser(String user) {
		userStatus retval = userStatus.New;
		try {
			results = ctx.search(getUserDN(user),
					"(objectclass=simpleSecurityObject)", controls);
			if (results.hasMore()) {
				retval = userStatus.Migrated;
			}
		} catch (NamingException e) {
			if (e.getMessage().contains("32")) {
				Attributes attributes = new BasicAttributes();

				Attribute objectClass = new BasicAttribute("objectClass");
				objectClass.add("account");
				objectClass.add("simpleSecurityObject");
				objectClass.add("top");
				attributes.put(objectClass);
				Attribute userPass = new BasicAttribute("userPassword");
				userPass.add("AAAAA99");
				attributes.put(userPass);

				try {
					ctx.createSubcontext(getUserDN(user), attributes);
				} catch (NamingException e1) {
					Daemon.getMiLog().log(Level.ERROR,
							"InitUser Error|" + e.getMessage());
				}
			}
		}
		this.closeConn();
		return retval;

	}

	public String getOldPasswd() {
		return oldPasswd;
	}

	private boolean validarClave(String clave, String fdn) {

		boolean res = true;
		if (clave == null || clave.isEmpty()) {
			res = false;
		} else {

			Pattern r = Pattern.compile("^[0-9]{4}$");
			Matcher m = r.matcher(clave.trim());

			if (m.find()) {
				r = Pattern.compile("^([0-9])\\1*$");
				m = r.matcher(clave.trim());
				if (m.find())
					res = false;

				if ("0123456789".contains(clave.trim()))
					res = false;

				if ("9876543210".contains(clave.trim()))
					res = false;

				if (fdn.length() > 5) {
					if (clave.trim().equals(fdn.substring(0, 4)))
						res = false;

					if (clave.trim().equals(
							fdn.substring(2, 4) + fdn.substring(0, 2)))
						res = false;

					if (clave.trim().equals(
							fdn.substring(0, 2) + fdn.substring(4, 6)))
						res = false;

					if (clave.trim().equals(
							fdn.substring(4, 6) + fdn.substring(0, 2)))
						res = false;
				} else {
					res = false;
				}
			} else {
				res = false;
			}
		}
		return res;
	}

	private void closeConn() {
		if (ctx != null) {
			try {
				ctx.close();
			} catch (NamingException e1) {
				Daemon.getMiLog().log(Level.ERROR,
						"Cerrando Conexion LDAP|" + e1.getMessage());
			}
		}
		if (results != null) {
			try {
				results.close();
			} catch (NamingException e1) {
				Daemon.getMiLog().log(Level.ERROR,
						"Cerrando Conexion LDAP|" + e1.getMessage());
			}
		}
	}

}
