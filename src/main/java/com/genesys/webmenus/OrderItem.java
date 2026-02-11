///////////////////////////////////////
// Copyright (c) 2004-2011 Kevin Jacovelli
// All Rights Reserved
///////////////////////////////////////

package com.genesys.webmenus;

import java.text.NumberFormat;
import java.util.*;
import java.math.BigDecimal;
import java.text.*;

public class OrderItem
{
	private String id;
	private String name;
	private String description;
	private String size;
	private BigDecimal price;
	private String options;
	private int quantity;

	public OrderItem( String id, String name, String description, String size, BigDecimal price, int quantity, String options )
	{
		this.id = id;
		setName( name );
		setDesc( description );
		setSize( size );
		setPrice( price );
		setQuantity( quantity );
		setOptions( options );
	}
	public String getId()
	{
		return this.id;
	}
	public void setName( String name )
	{
		this.name = name;
	}
	public String getName()
	{
		return this.name;
	}
	public void setDesc( String description )
	{
		this.description = description;
	}
	public String getDesc()
	{
		return this.description;
	}
	public void setSize( String size )
	{
		this.size = size;
	}
	public String getSize()
	{
		return this.size;
	}
	public void setPrice( BigDecimal price )
	{
		this.price = price;
	}
	public double getPrice()
	{
		return this.price.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	public String getPriceStr()
	{
		NumberFormat n = NumberFormat.getCurrencyInstance(Locale.US); 
		return n.format(getPrice());
	}
	public void setQuantity( int quantity )
	{
		this.quantity = quantity;
	}
	public int getQuantity()
	{
		return this.quantity;
	}
	public String getTotalStr()
	{
		NumberFormat n = NumberFormat.getCurrencyInstance(Locale.US); 
		return n.format(getQuantity() * getPrice());
	}
	public void setOptions( String options )
	{
		this.options = options;
	}
	public String getOptions()
	{
		return this.options;
	}
}
