package com.spring.security.auth;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.OperationNotSupportedException;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.ldap.InitialLdapContext;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.support.DefaultDirObjectFactory;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.ldap.SpringSecurityLdapTemplate;
import org.springframework.security.ldap.authentication.AbstractLdapAuthenticationProvider;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.spring.security.business.UserService;
import com.spring.security.entity.JwtUtil;
import com.spring.security.entity.Status;
import com.spring.security.entity.User;
import com.spring.security.exception.ApplicationException;


public class HRMSAuthentication extends AbstractLdapAuthenticationProvider{

	@Autowired
	UserService userService;
	
	@Autowired
	JwtUtil jwtUtil;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	private static final Pattern SUB_ERROR_CODE = Pattern.compile(".*data\\s([0-9a-f]{3,4}).*");

    // Error codes
    private static final int USERNAME_NOT_FOUND = 0x525;
    private static final int INVALID_PASSWORD = 0x52e;
    private static final int NOT_PERMITTED = 0x530;
    private static final int PASSWORD_EXPIRED = 0x532;
    private static final int ACCOUNT_DISABLED = 0x533;
    private static final int ACCOUNT_EXPIRED = 0x701;
    private static final int PASSWORD_NEEDS_RESET = 0x773;
    private static final int ACCOUNT_LOCKED = 0x775;

    private final String domain;
    private final String rootDn;
    private final String url;
    private boolean convertSubErrorCodesToExceptions;

    // Only used to allow tests to substitute a mock LdapContext
    ContextFactory contextFactory = new ContextFactory();

    /**
     * @param domain the domain for which authentication should take place
     */
//    public ActiveDirectoryLdapAuthenticationProvider(String domain) {
//        this (domain, null);
//    }

    /**
     * @param domain the domain name (may be null or empty)
     * @param url an LDAP url (or multiple URLs)
     */
    public HRMSAuthentication(String domain, String url) {		
        Assert.isTrue(StringUtils.hasText(url), "Url cannot be empty");
        this.domain = StringUtils.hasText(domain) ? domain.toLowerCase() : null;
        //this.url = StringUtils.hasText(url) ? url : null;
        this.url = url;
        rootDn = this.domain == null ? null : rootDnFromDomain(this.domain);
    }

    
    @Override
    protected DirContextOperations doAuthentication(UsernamePasswordAuthenticationToken auth) {
    	String methodName="doAuthentication";
		logger.debug(methodName+" starts ");
        String user = auth.getName();
        String pass = (String)auth.getCredentials();
        
        logger.debug(methodName+"user name ::"+user);
        logger.debug(methodName+"password ::"+pass);
        
        //after decryption 
        
        byte[] decodedUser = DatatypeConverter.parseBase64Binary(user);
        byte[] decodedPass = DatatypeConverter.parseBase64Binary(pass);
        String username=null;
        String password=null;
		try {
			username = new String(decodedUser, "UTF-8");
	        password=new String(decodedPass, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
		logger.debug(methodName+"user name after ::"+username);
        logger.debug(methodName+"password after::"+password);
      
        
        DirContext ctx = bindAsUser(username, password);

        try {
            return searchForUser(ctx, username);

        } catch (NamingException e) {
            logger.error("Failed to locate directory entry for authenticated user: " + username, e);
            throw badCredentials(e);
        } finally {
            LdapUtils.closeContext(ctx);
        }
    }

    public DirContextOperations autheticateUser(String olmId, String passwd){
    	String methodName="autheticateUser";
		logger.debug(methodName+" starts ");
		
		logger.debug(methodName+"user name  ::"+olmId);
        logger.debug(methodName+"password ::::::"+passwd);
		
		
    	DirContext ctx = bindAsUser(olmId, passwd);
    	
        try {
            return searchForUser(ctx, olmId);

        } catch (NamingException e) {
            logger.error("Failed to locate directory entry for authenticated user: " + passwd, e);
            throw badCredentials(e);
        } finally {
            LdapUtils.closeContext(ctx);
        }
    }
    
    /**
     * Creates the user authority list from the values of the {@code memberOf} attribute obtained from the user's
     * Active Directory entry.
     */
    @Override
    protected Collection<? extends GrantedAuthority> loadUserAuthorities(DirContextOperations userData, String username, String password) {
    	String methodName="loadUserAuthorities";
		logger.debug(methodName+" starts ");
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		logger.debug(methodName+"username"+username);
		   byte[] decodedUser = DatatypeConverter.parseBase64Binary(username);
	        byte[] decodedPass = DatatypeConverter.parseBase64Binary(password);
	        String userName=null;
	        String pass=null;
		try {
			userName = new String(decodedUser, "UTF-8");
	        pass=new String(decodedPass, "UTF-8");
			User user = userService.getUserByOlmId(userName.toString().toLowerCase());
			logger.debug(methodName+" user :: "+user.toString());
			if(user == null){
				throw new UsernameNotFoundException("Invalid Username/password");
			}
			String role = user.getRole();
			logger.debug(methodName+"role "+role);
			if(role != null){
				String[] roles = role.split(",");
				for(String r: roles){
					logger.debug("role :::::::::::::::::::::::::::::::::::: "+r);
					authorities.add(new SimpleGrantedAuthority("ROLE_"+r));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage(),e);
			// TODO: handle exception
			throw new UsernameNotFoundException(e.getMessage());
		} catch (Throwable e) {
			e.printStackTrace();
			logger.info(e.getMessage(),e);
			// TODO: handle exception
			throw new UsernameNotFoundException(e.getMessage());
		}
    	
        if (authorities.size() == 0) {
            logger.debug("No values for 'role' attribute.");

            return AuthorityUtils.NO_AUTHORITIES;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("'role' attribute values: " + authorities);
        }
        logger.debug(methodName+" ends ");
        return authorities;
    }

    private DirContext bindAsUser(String username, String password) {
    	String methodName="bindAsUser";
		logger.debug(methodName+" starts ");
        // TODO. add DNS lookup based on domain
        final String bindUrl = url;

        Hashtable<String,String> env = new Hashtable<String,String>();
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        String bindPrincipal = createBindPrincipal(username);
        logger.debug(methodName+"::"+bindPrincipal);
        env.put(Context.SECURITY_PRINCIPAL, bindPrincipal);
        env.put(Context.PROVIDER_URL, bindUrl);
        env.put(Context.SECURITY_CREDENTIALS, password);
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.OBJECT_FACTORIES, DefaultDirObjectFactory.class.getName());

        try {
            return contextFactory.createContext(env);
        } catch (NamingException e) {
            if ((e instanceof AuthenticationException) || (e instanceof OperationNotSupportedException)) {
                handleBindException(bindPrincipal, e);
                throw badCredentials(e);
            } else {
                throw LdapUtils.convertLdapException(e);
            }
        }
    }

    void handleBindException(String bindPrincipal, NamingException exception) {
    	String methodName="handleBindException";
		logger.debug(methodName+" starts ");
        if (logger.isDebugEnabled()) {
            logger.debug("Authentication for " + bindPrincipal + " failed:" + exception);
        }

        int subErrorCode = parseSubErrorCode(exception.getMessage());

        if (subErrorCode > 0) {
            logger.info("Active Directory authentication failed: " + subCodeToLogMessage(subErrorCode));

            if (convertSubErrorCodesToExceptions) {
                raiseExceptionForErrorCode(subErrorCode, exception);
            }
        } else {
            logger.debug("Failed to locate AD-specific sub-error code in message");
        }
        logger.debug(methodName+" ends ");
    }

    int parseSubErrorCode(String message) {
    	String methodName="parseSubErrorCode";
		logger.debug(methodName+" starts ");
        Matcher m = SUB_ERROR_CODE.matcher(message);

        if (m.matches()) {
            return Integer.parseInt(m.group(1), 16);
        }

        return -1;
    }

    void raiseExceptionForErrorCode(int code, NamingException exception) {
    	String methodName="raiseExceptionForErrorCode";
		logger.debug(methodName+" starts ");
        String hexString = Integer.toHexString(code);
        Throwable cause = new org.springframework.security.core.AuthenticationException(exception.getMessage(), exception) {
		};
        switch (code) {
            case PASSWORD_EXPIRED:
                throw new CredentialsExpiredException(messages.getMessage("LdapAuthenticationProvider.credentialsExpired",
                        "User credentials have expired"), cause);
            case ACCOUNT_DISABLED:
                throw new DisabledException(messages.getMessage("LdapAuthenticationProvider.disabled",
                        "User is disabled"), cause);
            case ACCOUNT_EXPIRED:
                throw new AccountExpiredException(messages.getMessage("LdapAuthenticationProvider.expired",
                        "User account has expired"), cause);
            case ACCOUNT_LOCKED:
                throw new LockedException(messages.getMessage("LdapAuthenticationProvider.locked",
                        "User account is locked"), cause);
            default:
                throw badCredentials(cause);
        }
        
    }

    String subCodeToLogMessage(int code) {
    	String methodName="subCodeToLogMessage";
		logger.debug(methodName+" starts ");
        switch (code) {
            case USERNAME_NOT_FOUND:
                return "User was not found in directory";
            case INVALID_PASSWORD:
                return "Supplied password was invalid";
            case NOT_PERMITTED:
                return "User not permitted to logon at this time";
            case PASSWORD_EXPIRED:
                return "Password has expired";
            case ACCOUNT_DISABLED:
                return "Account is disabled";
            case ACCOUNT_EXPIRED:
                return "Account expired";
            case PASSWORD_NEEDS_RESET:
                return "User must reset password";
            case ACCOUNT_LOCKED:
                return "Account locked";
        }

        return "Unknown (error code " + Integer.toHexString(code) +")";
    }

    private BadCredentialsException badCredentials() {
        return new BadCredentialsException(messages.getMessage(
                        "LdapAuthenticationProvider.badCredentials", "Bad credentials"));
    }

    private BadCredentialsException badCredentials(Throwable cause) {
        return (BadCredentialsException) badCredentials().initCause(cause);
    }

    //@SuppressWarnings("deprecation")
    private DirContextOperations searchForUser(DirContext ctx, String username) throws NamingException {
    	String methodName="searchForUser";
		logger.debug(methodName+" starts ");
		String[] attrIDs = { "sAMAccountName", "cn", "title", "mailnickname", "mail", "manager", "department", "telephoneNumber" };
        SearchControls searchCtls = new SearchControls();
        searchCtls.setReturningAttributes(attrIDs);
        searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        String searchFilter = "(&(objectClass=user)(sAMAccountName={0}))";

        final String bindPrincipal = createBindPrincipal(username);

        String searchRoot = rootDn != null ? rootDn : searchRootFromPrincipal(bindPrincipal);
        logger.info("User Name '" + username + ":: " + bindPrincipal);

        try {
        	//*********************find emailid from active directory
        	DirContextOperations d=SpringSecurityLdapTemplate.searchForSingleEntryInternal(ctx, searchCtls, searchRoot, searchFilter,
                    new Object[]{username});
        	logger.debug(d.toString());
        	try{
			//update emailid in db
			String updatePassword="update vm_user set EMAIL =? where olmid=?";
	        int i=jdbcTemplate.update(updatePassword,new Object[]{d.getAttributes().get("mail").get(0),username});
	        logger.debug(d.getAttributes().get("mail").get(0)+"updated value i::");
        	}catch(Exception e){
        		throw badCredentials(new ApplicationException(new Status(500,"Something went wrong"),"Something went wrong"));
        	}
            return d;
        } catch (IncorrectResultSizeDataAccessException incorrectResults) {
            if (incorrectResults.getActualSize() == 0) {
                UsernameNotFoundException userNameNotFoundException = new UsernameNotFoundException("User " + username + " not found in directory.");
                userNameNotFoundException.initCause(incorrectResults);
                throw badCredentials(userNameNotFoundException);
            }
            // Search should never return multiple results if properly configured, so just rethrow
            throw incorrectResults;
        }
    }

    private String searchRootFromPrincipal(String bindPrincipal) {
    	String methodName="searchRootFromPrincipal";
		logger.debug(methodName+" starts ");
        int atChar = bindPrincipal.lastIndexOf('@');

        if (atChar < 0) {
            logger.debug("User principal '" + bindPrincipal + "' does not contain the domain, and no domain has been configured");
            throw badCredentials();
        }

        return rootDnFromDomain(bindPrincipal.substring(atChar+ 1, bindPrincipal.length()));
    }

    private String rootDnFromDomain(String domain) {
        String[] tokens = StringUtils.tokenizeToStringArray(domain, ".");
        StringBuilder root = new StringBuilder();

        for (String token : tokens) {
            if (root.length() > 0) {
                root.append(',');
            }
            root.append("dc=").append(token);
        }

        return root.toString();
    }

    String createBindPrincipal(String username) {
    	String methodName="createBindPrincipal";
		logger.debug(methodName+" starts ");
        if (domain == null || username.toLowerCase().endsWith(domain)) {
            return username;
        }

        return username + "@" + domain;
    }

    /**
     * By default, a failed authentication (LDAP error 49) will result in a {@code BadCredentialsException}.
     * <p>
     * If this property is set to {@code true}, the exception message from a failed bind attempt will be parsed
     * for the AD-specific error code and a {@link CredentialsExpiredException}, {@link DisabledException},
     * {@link AccountExpiredException} or {@link LockedException} will be thrown for the corresponding codes. All
     * other codes will result in the default {@code BadCredentialsException}.
     *
     * @param convertSubErrorCodesToExceptions {@code true} to raise an exception based on the AD error code.
     */
    public void setConvertSubErrorCodesToExceptions(boolean convertSubErrorCodesToExceptions) {
        this.convertSubErrorCodesToExceptions = convertSubErrorCodesToExceptions;
    }

    static class ContextFactory {
        DirContext createContext(Hashtable<?,?> env) throws NamingException {
            return new InitialLdapContext(env, null);
        }
    }
    
    
   // @Override
    public Authentication authenticate(Authentication authentication)
    		throws org.springframework.security.core.AuthenticationException {
    	String methodName="authenticate";
		logger.debug(methodName+" starts ");
    	if(authentication instanceof JwtAuthenticationToken){
    		JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken)authentication;
    		try {
				User user = jwtUtil.parseToken(jwtAuthenticationToken.getToken());
			} catch (Exception e) {
				e.printStackTrace();
				throw new org.springframework.ldap.AuthenticationException(new AuthenticationException("Invalid Session!"));
			}
//    		if(user.getValidUpto() <= new Date().getTime()){
//    			throw new org.springframework.ldap.AuthenticationException(new AuthenticationException("Session timed out!"));
//    		}
//    		jwtAuthenticationToken.setAuthenticated(true);
    		UsernamePasswordAuthenticationToken successFullAuthentication = new UsernamePasswordAuthenticationToken(jwtAuthenticationToken.getPrincipal(), jwtAuthenticationToken.getCredentials(),jwtAuthenticationToken.getAuthorities());
    		return successFullAuthentication;
    	}else {
    		
    		
    		return super.authenticate(authentication);
    	}
    	
    }
    
    @Override
    public boolean supports(Class<?> authentication) {
    		return super.supports(authentication) || Integer.class.isAssignableFrom(authentication);
    }
    
  /*  public DirContextOperations onlyfor(String username, String password){
    	 DirContext ctx = bindAsUser(username, password);

         try {
        	 System.out.println("***");
             return searchForUserLocal(ctx, username);
             

         } catch (NamingException e) {
             logger.error("Failed to locate directory entry for authenticated user: " + username, e);
             throw badCredentials(e);
         } finally {
             LdapUtils.closeContext(ctx);
         }
    }
    
    @SuppressWarnings("deprecation")
    private DirContextOperations searchForUserLocal(DirContext ctx, String username) throws NamingException {
    	String methodName="searchForUser";
		logger.debug(methodName+" starts ");
        SearchControls searchCtls = new SearchControls();
        searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        String searchFilter = "(&(objectClass=user)(sAMAccountName={0}))";

        final String bindPrincipal = createBindPrincipal(username);

        String searchRoot = rootDn != null ? rootDn : searchRootFromPrincipal(bindPrincipal);
        logger.info("User Name '" + username + ":: " + bindPrincipal);

        try {
        	
            return SpringSecurityLdapTemplate.searchForSingleEntryInternal(ctx, searchCtls, searchRoot, searchFilter,
                new Object[]{username});
        } catch (IncorrectResultSizeDataAccessException incorrectResults) {
            if (incorrectResults.getActualSize() == 0) {
                UsernameNotFoundException userNameNotFoundException = new UsernameNotFoundException("User " + username + " not found in directory.");
                userNameNotFoundException.initCause(incorrectResults);
                throw badCredentials(userNameNotFoundException);
            }
            // Search should never return multiple results if properly configured, so just rethrow
            throw incorrectResults;
        }
    }*/

}
