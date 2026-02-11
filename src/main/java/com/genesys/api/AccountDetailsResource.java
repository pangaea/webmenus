package com.genesys.api;

import java.util.*;
import org.json.*;
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONStringer;
import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.ext.json.*;
import org.restlet.resource.*;

public class AccountDetailsResource extends ServerResource {

    /** The underlying Item object. */
	String accountId = null;
	JSONObject item = null;
	
    @Override
    protected void doInit() throws ResourceException {
        // /accounts/{id}.
    	this.accountId = (String) getRequest().getAttributes().get("id");

        // Get the item directly from the "web service".
    	String[] params = {"name", "created"};
        this.item = RepositoryResource.getItem("CEAccount", params, this.accountId);
        if(this.item != null){
        	try{
        		JSONObject counts = new JSONObject();
                HashMap query = new HashMap<String, String>();
                query.put("role", this.accountId);
        		counts.put("locations", RepositoryResource.getItemsCount("CELocation", query));
        		counts.put("menus", RepositoryResource.getItemsCount("CCMenu", query));
        		counts.put("menuitems", RepositoryResource.getItemsCount("CCMenuItem", query));
        		counts.put("orders", RepositoryResource.getItemsCount("CCMenuOrder", query));
        		this.item.put("counts", counts);

        		String[] loc_params = {"name", "created"};
        		JSONArray locations = RepositoryResource.getItems("CELocation", loc_params, query);
        		if(locations != null){
        			this.item.put("locations", locations);
        		}
        	} catch (JSONException e) {
        		// Log this
        	}
        }
        setExisting(this.item != null);
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
    	JsonRepresentation representation = new JsonRepresentation(this.item);
		getResponse().setStatus(Status.SUCCESS_OK);
        return representation;
    }
}