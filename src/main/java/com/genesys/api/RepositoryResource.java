package com.genesys.api;

import java.util.*;

import org.json.*;

import com.genesys.SystemServlet;
import com.genesys.repository.*;

public class RepositoryResource {
	static public JSONArray getItems(String class_name, String[] params, HashMap<String, String> query){
		JSONArray items = new JSONArray();
		try {
			ObjectManager objectBean = SystemServlet.getObjectManager();
	    	Credentials info = new Credentials();
	    	if( objectBean.SystemLogin( "admin", info ) == true )
	    	{
	        	ObjectQuery queryObj = new ObjectQuery(class_name);
	        	if(query != null){
		        	for(Map.Entry<String, String> entry : query.entrySet()){
		        		queryObj.addProperty(entry.getKey(), entry.getValue());
		        	}
	        	}
	        	queryObj.setSortBy("created");
	        	queryObj.setSortByPrefix("T1.");
	        	queryObj.setSortOrder("DESC");
	    		RepositoryObjectIterator objIter = new RepositoryObjectIterator(objectBean.Query(info, queryObj));
	    		while(objIter.each()){
	    			RepositoryObject obj = objIter.getObj();
	    			JSONObject item = new JSONObject();
	    			item.put("id", obj.getId());
	    			for (String s: params){
	    				item.put(s, obj.getPropertyValue(s));
	    			}
	    			items.put(item);
	    		}
	    		objectBean.Logout(info);
	    	}
        } catch (Exception e) {
            e.printStackTrace();
        }
    	return items;
	}
	static public long getItemsCount(String class_name, HashMap<String, String> query){
		long count = 0;
		try {
			ObjectManager objectBean = SystemServlet.getObjectManager();
	    	Credentials info = new Credentials();
	    	if( objectBean.SystemLogin( "admin", info ) == true )
	    	{
	        	ObjectQuery queryObj = new ObjectQuery(class_name);
	        	queryObj.setCountOnly(true);
	        	if(query != null){
		        	for(Map.Entry<String, String> entry : query.entrySet()){
		        		queryObj.addProperty(entry.getKey(), entry.getValue());
		        	}
	        	}
	        	QueryResponse res = objectBean.Query(info, queryObj);
	        	count = res.getCount();
	    		objectBean.Logout(info);
	    	}
        } catch (Exception e) {
            e.printStackTrace();
        }
    	return count;
	}
	static public JSONObject getItem(String class_name, String[] params, String id){
		JSONObject value = null;
		try {
			value = new JSONObject();
			ObjectManager objectBean = SystemServlet.getObjectManager();
	    	Credentials info = new Credentials();
	    	if( objectBean.SystemLogin( "admin", info ) == true )
	    	{
	        	ObjectQuery queryObj = new ObjectQuery(class_name);
	        	queryObj.addProperty("id", id);
	    		RepositoryObjectIterator objIter = new RepositoryObjectIterator(objectBean.Query(info, queryObj));
	    		if(objIter.each()){
	    			RepositoryObject obj = objIter.getObj();
	    			value.put("id", obj.getId());
	    			for (String s: params){
	    				value.put(s, obj.getPropertyValue(s));
	    			}
	    		}
	    		objectBean.Logout(info);
	    	}
        } catch (Exception e) {
            e.printStackTrace();
        }
    	return value;
	}
}