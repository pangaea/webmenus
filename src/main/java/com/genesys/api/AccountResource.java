package com.genesys.api;

import java.io.IOException;
import org.json.*;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.ext.json.*;
import org.restlet.resource.*;

public class AccountResource extends ServerResource {

	JSONArray items = null;

    @Override
    protected void doInit() throws ResourceException {
        // /accounts
    	String[] params = {"name", "created"};
		this.items = RepositoryResource.getItems("CEAccount", params, null);
		setExisting(this.items != null);
    }

    /**
     * Handle DELETE requests.
     */
    @Delete
    public void removeItem() {
//        if (item != null) {
//            // Remove the item from the list.
//            getItems().remove(item.getName());
//        }
//
//        // Tells the client that the request has been successfully fulfilled.
//        setStatus(Status.SUCCESS_NO_CONTENT);
    }

    /**
     * Handle PUT requests.
     * 
     * @throws IOException
     */
    @Put
    public void storeItem(Representation entity) throws IOException {
//        // The PUT request updates or creates the resource.
//        if (item == null) {
//            item = new Item(itemName);
//        }
//
//        // Update the description.
//        Form form = new Form(entity);
//        item.setDescription(form.getFirstValue("description"));
//
//        if (getItems().putIfAbsent(item.getName(), item) == null) {
//            setStatus(Status.SUCCESS_CREATED);
//        } else {
//            setStatus(Status.SUCCESS_OK);
//        }
    }

    @Get("json")
    public Representation toJson() {
		// Login to object manager
    	JsonRepresentation representation = null;
    	getResponse().setStatus(Status.SUCCESS_ACCEPTED);
    	try{
    	
    		JSONObject res = new JSONObject();
    		res.put("total", 1);
    		res.put("page", 1);
    		res.put("perPage", 20);
    		res.put("items", this.items);
    		representation = new JsonRepresentation(res);
    	}
    	catch(JSONException e){
    		
    	}
		getResponse().setStatus(Status.SUCCESS_OK);
        return representation;
    }
}