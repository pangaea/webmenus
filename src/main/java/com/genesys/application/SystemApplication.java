package com.genesys.application;

//import com.dg.demo.restlet.resource.CityXMLResource;
//import com.dg.demo.restlet.resource.MapResource;
//import com.dg.demo.restlet.resource.SystemResource;
import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.routing.Router;
import org.restlet.data.MediaType;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.representation.StringRepresentation;
import org.restlet.security.*;
import org.restlet.data.ChallengeScheme;
//import org.restlet.ext.crypto.DigestAuthenticator;

import com.genesys.api.AccountResource;
import com.genesys.api.AccountDetailsResource;

/**
 * The Application Root Class.  Maps all of the resources.
 */
public class SystemApplication extends Application {

    /**
     * Creates a new SystemApplication object.
     */
    public SystemApplication() {
        //empty
    }

    /**
     * Public Constructor to create an instance of SystemApplication.
     *
     * @param parentContext - the org.restlet.Context instance
     */
    public SystemApplication(Context parentContext) {
        super(parentContext);
    }
    
    /**
     * Creates a root Restlet that will receive all incoming calls.
     */
    @Override
    public Restlet createInboundRoot() {
    	
        //Create the authenticator, the authorizer and the router that will be protected
        ChallengeAuthenticator authenticator = createAuthenticator();
        Router router = createRouter();

        //Protect the resource by enforcing authentication then authorization
        authenticator.setNext(router);
        return authenticator;
    }
    
    private ChallengeAuthenticator createAuthenticator() {
    	ChallengeAuthenticator guard = new ChallengeAuthenticator(
                getContext(), ChallengeScheme.HTTP_BASIC, "realm");

        //Attach verifier to check authentication and enroler to determine roles
    	UserVerifier uv = new UserVerifier();
        guard.setVerifier(uv);
        return guard;
    }
    
    private Router createRouter() {
        //Attach Server Resources to given URL
        Router router = new Router(getContext());
        router.attach("/accounts", AccountResource.class);
        router.attach("/accounts/{id}", AccountDetailsResource.class);
        return router;
    }
}