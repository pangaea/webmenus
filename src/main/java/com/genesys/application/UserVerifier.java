package com.genesys.application;

import org.restlet.security.LocalVerifier;
import com.twmacinta.util.*;

import com.genesys.SystemServlet;
import com.genesys.repository.*;

public class UserVerifier extends LocalVerifier {

    @Override
    public int verify(String identifier, char[] secret) {
    	char[] hash = null;
		try
		{
			MD5 md5 = new MD5();
			md5.Update(new String(secret), null);
			hash = md5.asHex().toCharArray();
		}
		catch(Exception e){}
		char[] pass = getLocalSecret(identifier);
        return compare(hash, getLocalSecret(identifier)) ? RESULT_VALID
                : RESULT_INVALID;
    }
    
    @Override
    public char[] getLocalSecret(String identifier) {
        // Could have a look into a database, LDAP directory, etc.
        char[] ret = null;
        try {
			ObjectManager objectBean = SystemServlet.getObjectManager();
	    	Credentials info = new Credentials();
	    	if( objectBean.SystemLogin( "admin", info ) == true )
	    	{
	    		ObjectQuery queryObj = new ObjectQuery("CClient");
	    		queryObj.addProperty("username", identifier);
	    		RepositoryObjectIterator objIter = new RepositoryObjectIterator(objectBean.Query(info, queryObj));
	    		if(objIter.each()){
	    			RepositoryObject obj = objIter.getObj();
	    			if(obj.getPropertyValue("role.admin").equalsIgnoreCase( "Y" ) == true ){
	    				ret = obj.getPropertyValue("password").toCharArray();
	    			}
	    		}
	    		objectBean.Logout(info);
	    	}
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }

}