package com.genesys.util.email;

import java.io.*;
import java.util.Date;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import com.genesys.SystemServlet;
import com.genesys.repository.AuthenticationException;
import com.genesys.repository.Credentials;
import com.genesys.repository.ObjectManager;
import com.genesys.repository.ObjectQuery;
import com.genesys.repository.QueryResponse;
import com.genesys.repository.RepositoryObject;
import com.genesys.repository.RepositoryObjects;

public class PdfGenerator {

	private static Font catFont 	= new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
	private static Font redFont 	= new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.RED);
	private static Font subFont 	= new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
	private static Font smallBold 	= new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);

	private static String getCurrencyString(double price){
		NumberFormat n = NumberFormat.getCurrencyInstance(Locale.US); 
		return n.format(price);
	}

	private static String getCurrencyString(String price){
		BigDecimal bdTaxRate = new BigDecimal(price);
		return getCurrencyString(bdTaxRate.doubleValue());
	}
	
	public static ByteArrayOutputStream generatePdf(RepositoryObject oOrder, Credentials info) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
	    	Document document = new Document();
	    	PdfWriter.getInstance(document, os);
	    	document.open();
	    	addMetaData(document, oOrder.getPropertyValue("location.name"));
	
			ObjectManager objectBean = SystemServlet.getObjectManager();
			String orderId = oOrder.getPropertyValue("id");		
			String customerPhone = "";
			ObjectQuery queryPatron = new ObjectQuery( "CEPatron" );
			queryPatron.addProperty("email", oOrder.getPropertyValue("email"));
			QueryResponse qrPatron = objectBean.Query( info, queryPatron );
			RepositoryObjects oPatrons = qrPatron.getObjects( queryPatron.getClassName() );
			if( oPatrons.count() > 0 )
			{
				RepositoryObject oPatron = oPatrons.get(0);
				customerPhone = oPatron.getPropertyValue("phone_num");
			}
	
		    Paragraph preface = new Paragraph();
		    addEmptyLine(preface, 1);
		    preface.add(new Paragraph("Order Notification", catFont));
		    addEmptyLine(preface, 1);
		    preface.add(new Paragraph("Time: " + oOrder.getPropertyValue("order_time"), smallBold));
		    addEmptyLine(preface, 1);
		    preface.add(new Paragraph("Customer Email: " + oOrder.getPropertyValue("email"), smallBold));
		    addEmptyLine(preface, 1);
		    preface.add(new Paragraph("Customer Phone: " + customerPhone, smallBold));
		    addEmptyLine(preface, 1);
		    preface.add(new Paragraph("Location: " + oOrder.getPropertyValue("location.name"), smallBold));
		    addEmptyLine(preface, 1);
		    preface.add(new Paragraph("Order ID: " + orderId, smallBold));
		    addEmptyLine(preface, 1);
		    preface.add(new Paragraph("Delivery: " + oOrder.getPropertyValue("delivery"), smallBold));
		    addEmptyLine(preface, 1);
		    preface.add(new Paragraph("Delivery Info: " + oOrder.getPropertyValue("delivery_info"), smallBold));
		    addEmptyLine(preface, 3);
	      
	      
		    PdfPTable table = new PdfPTable(4);
		    PdfPCell c1 = new PdfPCell(new Phrase("Item"));
		    c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		    c1.setBackgroundColor(BaseColor.GRAY);
		    table.addCell(c1);
	
			c1 = new PdfPCell(new Phrase("Each"));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBackgroundColor(BaseColor.GRAY);
			table.addCell(c1);
	
			c1 = new PdfPCell(new Phrase("Quantity"));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBackgroundColor(BaseColor.GRAY);
			table.addCell(c1);
	  
			c1 = new PdfPCell(new Phrase("Price"));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBackgroundColor(BaseColor.GRAY);
			table.addCell(c1);
	  
			table.setHeaderRows(1);
			
			// Query order items
			ObjectQuery queryOrderItem = new ObjectQuery( "CCMenuOrderItem" );
			queryOrderItem.addProperty("menuorder", orderId);
			QueryResponse qrOrderItem = objectBean.Query( info, queryOrderItem );
			RepositoryObjects oOrderItems = qrOrderItem.getObjects( queryOrderItem.getClassName() );
			for( int i = 0; i < oOrderItems.count(); i++ )
			{
				RepositoryObject oOrderItem = oOrderItems.get(i);
				table.addCell(oOrderItem.getPropertyValue("name"));
				String sFormattedPrice = getCurrencyString(oOrderItem.getPropertyValue_Real("price"));
				table.addCell(sFormattedPrice);
				table.addCell(oOrderItem.getPropertyValue("quantity"));
				double item_total = oOrderItem.getPropertyValue_Real("price") * oOrderItem.getPropertyValue_Int("quantity");
				table.addCell(getCurrencyString(item_total));
			}
			
			PdfPCell seperatorCell = new PdfPCell(new Phrase(""));
			seperatorCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
			seperatorCell.setFixedHeight(4);
			table.addCell(seperatorCell);
			table.addCell(seperatorCell);
			table.addCell(seperatorCell);
			table.addCell(seperatorCell);

			table.addCell("Subtotal");
			table.addCell("");
			table.addCell("");
			table.addCell(getCurrencyString(oOrder.getPropertyValue("subtotal")));
			
			table.addCell("Tax(" + oOrder.getPropertyValue("tax_rate") + "%)");
			table.addCell("");
			table.addCell("");
			table.addCell(getCurrencyString(oOrder.getPropertyValue("tax")));
			
			table.addCell("Total");
			table.addCell("");
			table.addCell("");
			table.addCell(getCurrencyString(oOrder.getPropertyValue("total")));
			
			preface.add(table);
	      
			document.add(preface);
			document.close();
		
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
		return os;
	}

	// iText allows to add metadata to the PDF which can be viewed in your Adobe
	// Reader
	// under File -> Properties
	private static void addMetaData(Document document, String locName) {
		document.addTitle(locName);
		document.addSubject("Order Notification");
		document.addAuthor(locName);
		document.addCreator(locName);
	}

	private static void addEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}
} 