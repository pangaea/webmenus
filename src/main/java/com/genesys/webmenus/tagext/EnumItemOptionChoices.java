package com.genesys.webmenus.tagext;

import com.genesys.SystemServlet;
import com.genesys.repository.AuthenticationException;
import com.genesys.repository.ObjectManager;
import com.genesys.repository.ObjectQuery;
import com.genesys.repository.QueryResponse;
import com.genesys.repository.RepositoryObject;
import com.genesys.repository.RepositoryObjects;

public class EnumItemOptionChoices extends GuestAccess {
	private String m_optionId = null;
	
	public RepositoryObjects queryObjects(ObjectManager objectBean, CredentialsContext credContext)
	{
		try
		{
            ObjectQuery queryChoices = new ObjectQuery("CCMenuItemOptionChoice");
            queryChoices.setSortBy("choice_index");		// TODO: Fix this - it should reference the property, not the column
            queryChoices.setSortOrder("ASC");
            queryChoices.addProperty("menuitemoption", m_optionId);
            QueryResponse qrChoices = objectBean.Query( credContext.getCredentials(), queryChoices );
            return qrChoices.getObjects( queryChoices.getClassName() );
		}
		catch(AuthenticationException e)
		{
			SystemServlet.g_logger.error( "Expection caught in EnumItemOptionChoices::queryObjects" );
		}
		return null;
	}

	public void updateVariables( RepositoryObject obj )
	{
		try
		{
			setPageAttribute( "choiceId", obj.getId() );
			setPageAttribute( "choiceName", obj.getPropertyValue("name") );
			setPageAttribute( "choicePrice", obj.getPropertyValue("price") );
			setPageAttribute( "choiceIdx", Integer.toString(getIndex()-1) );
		}
		catch( Exception e )
		{
			SystemServlet.g_logger.error( "Expection caught in EnumItemOptions::updateVariables" );
		}
	}

	public void setOptionId( String optionId )
	{
		m_optionId = optionId;
	}
}
