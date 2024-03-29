/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.app.webui.jsptag;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dspace.app.itemmarking.ItemMarkingExtractor;
import org.dspace.app.itemmarking.ItemMarkingInfo;
import org.dspace.app.webui.util.UIUtil;
import org.dspace.authorize.AuthorizeException;
import org.dspace.authorize.AuthorizeManager;
import org.dspace.browse.*;
import org.dspace.content.Bitstream;
import org.dspace.content.Bundle;
import org.dspace.content.DCDate;
import org.dspace.content.DSpaceObject;
import org.dspace.content.DmsMetadaField;
import org.dspace.content.Metadatum;
import org.dspace.content.Item;
import org.dspace.content.Thumbnail;
import org.dspace.content.service.ItemService;
import org.dspace.core.ConfigurationManager;
import org.dspace.core.Constants;
import org.dspace.core.Context;
import org.dspace.core.LogManager;
import org.dspace.core.Utils;
import org.dspace.storage.bitstore.BitstreamStorageManager;
import org.dspace.sort.SortOption;
import org.dspace.utils.DSpace;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.jstl.fmt.LocaleSupport;
import javax.servlet.jsp.tagext.TagSupport;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.StringTokenizer;

import org.dspace.content.authority.MetadataAuthorityManager;
import org.dspace.handle.HandleManager;

/**
 * Tag for display a list of items
 *
 * @author Robert Tansley
 * @version $Revision$
 */
public class BrowseListTag extends TagSupport
{
	 /** log4j category */
    private static Logger log = Logger.getLogger(BrowseListTag.class);

    /** Items to display */
    private transient BrowseItem[] items;
    private Integer collection_id=0;

    /** Row to highlight, -1 for no row */
    private int highlightRow = -1;

    /** Column to emphasise, identified by metadata field */
    private String emphColumn;

    /** Config value of thumbnail view toggle */
    private static boolean showThumbs;

    /** Config browse/search width and height */
    private static int thumbItemListMaxWidth;

    private static int thumbItemListMaxHeight;

    /** Config browse/search thumbnail link behaviour */
    private static boolean linkToBitstream = false;

    /** Config to include an edit link */
    private boolean linkToEdit = true;
    private boolean linkToView=false;
    /** Config to disable cross links */
    private boolean disableCrossLinks = false;

    /** The default fields to be displayed when listing items */
    private static final String DEFAULT_LIST_FIELDS;

    /** The default widths for the columns */
    private static final String DEFAULT_LIST_WIDTHS;

    /** The default field which is bound to the browse by date */
    private static String dateField = "dc.date.issued";

    /** The default field which is bound to the browse by title */
    private static String titleField = "dc.title";
    private static String viewField="dc.subject";
    private static String authorField = "dc.contributor.*";

    private int authorLimit = -1;

    private transient BrowseInfo browseInfo;

    private static final long serialVersionUID = 8091584920304256107L;
    
    
    
    public Integer getCollection_id() {
		return collection_id;
	}

	public void setCollection_id(Integer collection_id) {
		this.collection_id = collection_id;
	}

	String filename=""; 
    static
    {
        getThumbSettings();

        if (showThumbs)
        {
            DEFAULT_LIST_FIELDS = "thumbnail, dc.date.issued(date), dc.title, dc.contributor.*";
            DEFAULT_LIST_WIDTHS = "*, 130, 60%, 40%";
        }
        else
        {
            DEFAULT_LIST_FIELDS = "dc.date.issued(date), dc.title, dc.contributor.*";
            DEFAULT_LIST_WIDTHS = "130, 60%, 40%";
        }

        // get the date and title fields
        String dateLine = ConfigurationManager.getProperty("webui.browse.index.date");
        if (dateLine != null)
        {
            dateField = dateLine;
        }

        String titleLine = ConfigurationManager.getProperty("webui.browse.index.title");
        if (titleLine != null)
        {
            titleField = titleLine;
        }

        // get the author truncation config
        String authorLine = ConfigurationManager.getProperty("webui.browse.author-field");
        if (authorLine != null)
        {
        	authorField = authorLine;
        }
    }

    public BrowseListTag()
    {
        super();
    }

    public int doStartTag() throws JspException
    {
        JspWriter out = pageContext.getOut();
        HttpServletRequest hrq = (HttpServletRequest) pageContext.getRequest();
        String handle = null;
        DSpaceObject dso = null;   
        DmsMetadaField[] fieldnamecollection=new DmsMetadaField[0] ;
        Context context;
		try {
			 context = UIUtil.obtainContext(hrq);
		     fieldnamecollection= DmsMetadaField.getFieldNameByCollection(context, collection_id);
		 
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
       
        
        /* just leave this out now
        boolean emphasiseDate = false;
        boolean emphasiseTitle = false;

        if (emphColumn != null)
        {
            emphasiseDate = emphColumn.equalsIgnoreCase("date");
            emphasiseTitle = emphColumn.equalsIgnoreCase("title");
        }
        */

        // get the elements to display
        String browseListLine  = null;
        String browseWidthLine = null;

        // As different indexes / sort options may require different columns to be displayed
        // try to obtain a custom configuration based for the browse that has been performed
        if (browseInfo != null)
        {
            SortOption so = browseInfo.getSortOption();
            BrowseIndex bix = browseInfo.getBrowseIndex();

            // We have obtained the index that was used for this browse
            if (bix != null)
            {
                // First, try to get a configuration for this browse and sort option combined
                if (so != null && browseListLine == null)
                {
                    browseListLine  = ConfigurationManager.getProperty("webui.itemlist.browse." + bix.getName() + ".sort." + so.getName() + ".columns");
                    browseWidthLine = ConfigurationManager.getProperty("webui.itemlist.browse." + bix.getName() + ".sort." + so.getName() + ".widths");
                }

                // We haven't got a sort option defined, so get one for the index
                // - it may be required later
                if (so == null)
                {
                    so = bix.getSortOption();
                }
            }

            // If no config found, attempt to get one for this sort option
            if (so != null && browseListLine == null)
            {
                browseListLine  = ConfigurationManager.getProperty("webui.itemlist.sort." + so.getName() + ".columns");
                browseWidthLine = ConfigurationManager.getProperty("webui.itemlist.sort." + so.getName() + ".widths");
            }

            // If no config found, attempt to get one for this browse index
            if (bix != null && browseListLine == null)
            {
                browseListLine  = ConfigurationManager.getProperty("webui.itemlist.browse." + bix.getName() + ".columns");
                browseWidthLine = ConfigurationManager.getProperty("webui.itemlist.browse." + bix.getName() + ".widths");
            }

            // If no config found, attempt to get a general one, using the sort name
            if (so != null && browseListLine == null)
            {
                browseListLine  = ConfigurationManager.getProperty("webui.itemlist." + so.getName() + ".columns");
                browseWidthLine = ConfigurationManager.getProperty("webui.itemlist." + so.getName() + ".widths");
            }

            // If no config found, attempt to get a general one, using the index name
            if (bix != null && browseListLine == null)
            {
                browseListLine  = ConfigurationManager.getProperty("webui.itemlist." + bix.getName() + ".columns");
                browseWidthLine = ConfigurationManager.getProperty("webui.itemlist." + bix.getName() + ".widths");
            }
        }

        if (browseListLine == null)
        {
            browseListLine  = ConfigurationManager.getProperty("webui.itemlist.columns");
            browseWidthLine = ConfigurationManager.getProperty("webui.itemlist.widths");
        }

        // Have we read a field configration from dspace.cfg?
        if (browseListLine != null)
        {
            // If thumbnails are disabled, strip out any thumbnail column from the configuration
            if (!showThumbs && browseListLine.contains("thumbnail"))
            {
                // Ensure we haven't got any nulls
                browseListLine  = browseListLine  == null ? "" : browseListLine;
                browseWidthLine = browseWidthLine == null ? "" : browseWidthLine;

                // Tokenize the field and width lines
                StringTokenizer bllt = new StringTokenizer(browseListLine,  ",");
                StringTokenizer bwlt = new StringTokenizer(browseWidthLine, ",");

                StringBuilder newBLLine = new StringBuilder();
                StringBuilder newBWLine = new StringBuilder();
                while (bllt.hasMoreTokens() || bwlt.hasMoreTokens())
                {
                    String browseListTok  = bllt.hasMoreTokens() ? bllt.nextToken() : null;
                    String browseWidthTok = bwlt.hasMoreTokens() ? bwlt.nextToken() : null;

                    // Only use the Field and Width tokens, if the field isn't 'thumbnail'
                    if (browseListTok == null || !browseListTok.trim().equals("thumbnail"))
                    {
                        if (browseListTok != null)
                        {
                            if (newBLLine.length() > 0)
                            {
                                newBLLine.append(",");
                            }

                            newBLLine.append(browseListTok);
                        }

                        if (browseWidthTok != null)
                        {
                            if (newBWLine.length() > 0)
                            {
                                newBWLine.append(",");
                            }

                            newBWLine.append(browseWidthTok);
                        }
                    }
                }

                // Use the newly built configuration file
                browseListLine  = newBLLine.toString();
                browseWidthLine = newBWLine.toString();
            }
        }
        else
        {
            browseListLine  = DEFAULT_LIST_FIELDS;
            browseWidthLine = DEFAULT_LIST_WIDTHS;
        }

        // Arrays used to hold the information we will require when outputting each row
        String[] fieldArr  = browseListLine == null  ? new String[0] : browseListLine.split("\\s*,\\s*");
        String[] widthArr  = browseWidthLine == null ? new String[0] : browseWidthLine.split("\\s*,\\s*");
        boolean isDate[]   = new boolean[fieldArr.length];
        boolean emph[]     = new boolean[fieldArr.length];
        boolean isAuthor[] = new boolean[fieldArr.length];
        boolean viewFull[] = new boolean[fieldArr.length];
        String[] browseType = new String[fieldArr.length];
        String[] cOddOrEven = new String[fieldArr.length];

        try
        {
        	// Get the interlinking configuration too
            CrossLinks cl = new CrossLinks();
            // Get a width for the table
            String tablewidth = ConfigurationManager.getProperty("webui.itemlist.tablewidth");

            // If we have column widths, try to use a fixed layout table - faster for browsers to render
            // but not if we have to add an 'edit item' button - we can't know how big it will be
            if (widthArr.length > 0 && widthArr.length == fieldArr.length && linkToEdit)
            {
                // If the table width has been specified, we can make this a fixed layout
                if (!StringUtils.isEmpty(tablewidth))
                {
                    out.println("<table id=\"example1\" class=\"table table-bordered table-striped\" >");
                }
                else
                {
                    // Otherwise, don't constrain the width
                    out.println("<table id=\"example1\"  class=\"table table-bordered table-striped\" >");
                }
                
                // Output the known column widths
                out.print("<colgroup>");

                for (int w = 0; w < widthArr.length; w++)
                {
                    out.print("<col width=\"");

                    // For a thumbnail column of width '*', use the configured max width for thumbnails
                    if (fieldArr[w].equals("thumbnail") && widthArr[w].equals("*"))
                    {
                        out.print(thumbItemListMaxWidth);
                    }
                    else
                    {
                        out.print(StringUtils.isEmpty(widthArr[w]) ? "*" : widthArr[w]);
                    }
                    out.print("\" />");
                }
               
                out.println("</colgroup>");
            }
            else if (!StringUtils.isEmpty(tablewidth))
            {
                out.println("<table id=\"example1\"  class=\"table table-bordered table-striped\" >");
            }
            else
            {
                out.println("<table id=\"example1\"  class=\"table table-bordered table-striped\" >");
            }
            
            // Output the table headers
            out.println("<thead><tr>");
             if(fieldnamecollection.length>0)
             {
            	 emph     = new boolean[fieldnamecollection.length];
            	  out.print("<th><strong>Document Name</strong></th>");
            	 for (int i = 0; i <fieldnamecollection.length; i++)
            	 {
            	// output the header
                 out.print("<th><strong>"+fieldnamecollection[i].getFieldName()+"</strong></th>");
            	 }
             }
             else
             {            
            out.print("<th><strong>Document Name</strong></th>");
           
            for (int colIdx = 0; colIdx < fieldArr.length; colIdx++)
            {
            	String  field = fieldArr[colIdx].toLowerCase().trim();
                cOddOrEven[colIdx] = (((colIdx + 1) % 2) == 0 ? "Odd" : "Even");

                // find out if the field is a date
                if (field.indexOf("(date)") > 0)
                {
                    field = field.replaceAll("\\(date\\)", "");
                    isDate[colIdx] = true;
                }

                // Cache any modifications to field
                fieldArr[colIdx] = field;

                // find out if this is the author column
                if (field.equals(authorField))
                {
                	isAuthor[colIdx] = true;
                }

                // find out if this field needs to link out to other browse views
                if (cl.hasLink(field))
                {
                	browseType[colIdx] = cl.getLinkType(field);
                	viewFull[colIdx] = BrowseIndex.getBrowseIndex(browseType[colIdx]).isItemIndex();
                }
                
                if (field.equals(emphColumn))
                {
                	emph[colIdx] = true;
                }

                // prepare the strings for the header
                String id = "t" + Integer.toString(colIdx + 1);
                String css = "oddRow" + cOddOrEven[colIdx] + "Col";
                String message = "itemlist." + field;

                String markClass = "";
                if (field.startsWith("mark_"))
                {
                	markClass = " "+field+"_th";
                }
                
                // output the header
                out.print("<th><strong>"+ LocaleSupport.getLocalizedMessage(pageContext, message)+ "</strong></th>");
                
                       
            }   
           
             }
                     
            if (linkToEdit)
            {
                         out.print("<th>&nbsp</th>");
                        
            }

            out.print("</tr></thead><tbody>");
            
            // now output each item row
            if(fieldnamecollection.length>0)
            {
                for (int i = 0; i < items.length; i++)
                {
                  	out.print("<tr>"); 
                    // now prepare the XHTML frag for this division       
                  	int count=0;
                    handle=items[i].getHandle();
                     String document=getFileName(handle);
                     if (document.indexOf(".") > 0)
                    	 document = document.substring(0, document.lastIndexOf("."));
                     
                     out.print("<td><a  href=\"" + hrq.getContextPath() + "/handle/"+ items[i].getHandle() + "\">" + document+ "</a></td>");
                       
                    for (int colIdx = 0; colIdx < fieldnamecollection.length; colIdx++)
                    {
                    	
                    	  String field = fieldnamecollection[colIdx].getFieldName();                    	  
                        StringTokenizer eq = new StringTokenizer(field, ".");                        
                        String[] tokens = { "", "", "" };
                        int k = 0;
                        
                        while(eq.hasMoreTokens())
                        {
                            tokens[k] = eq.nextToken().toLowerCase().trim();
                            k++;
                        }
                        
                        String schema = tokens[0];
                        String element = tokens[1];
                        String qualifier = tokens[2];
                        // first get hold of the relevant metadata for this column
                        Metadatum[] metadataArray;
                        metadataArray = items[i].getMetadata("dc", schema, Item.ANY, Item.ANY);                        
                        if (metadataArray == null)
                        {
                        	metadataArray = new Metadatum[0];
                        }
                        
                        // now prepare the content of the table division
                        String metadata = "-";                    
                        
                         if (metadataArray.length > 0)
                         {
                        	metadata = Utils.addEntities(metadataArray[0].value);                        	
                            // format the date field correctly
                           
                            // format the title field correctly (as long as the item isn't withdrawn, link to it)
                             if (field.equals(titleField))
                            {
                                             	
                                 if(linkToView)
                                 { 
                                  Integer item_id= getItemId(handle);                                                            	 
                            
                            	metadata = "<a  href=\"" + hrq.getContextPath() + "/handle/"
                                        + items[i].getHandle() + "\">"
                                        + Utils.addEntities(metadataArray[0].value)
                                        + "</a>"; 
                               }
                               else
                               {
                                metadata = Utils.addEntities(metadataArray[0].value);                               
                               }
                            }
                             
                            else if(field.equals("viewField")){
                           	 metadata = "<a class=\"btn btn-primary\" href=\"" + hrq.getContextPath() + "/tools/edit-item?item_id" + items[i].getID()+ "\">View</a>";
                           }
                             // format all other fields
                            else
                            {
                            	 metadata = Utils.addEntities(metadataArray[0].value);  
                            }
                        }
                        //In case title has no value, replace it with "undefined" so as the user has something to
                    	//click in order to access the item page
                        
                        else if (field.equals(titleField)){
                        	String undefined = LocaleSupport.getLocalizedMessage(pageContext, "itemlist.title.undefined");
                        	if (items[i].isWithdrawn())
                            {
                                metadata = "<span style=\"font-style:italic\">("+undefined+")</span>";
                            }
                            // format the title field correctly (as long as the item isn't withdrawn, link to it)
                            else
                            {
                              metadata = "<a href=\"" + hrq.getContextPath() + "/handle/"
                                + items[i].getHandle() + "\">"
                                + "<span style=\"font-style:italic\">("+undefined+")</span>"
                                + "</a>";
                            }
                        }
                        
                        /* if(count==0 && metadataArray.length!=0)
                         {
                          metadata = "<a  href=\"" + hrq.getContextPath() + "/handle/"
                                     + items[i].getHandle() + "\">"
                                     + Utils.addEntities(metadataArray[0].value)
                                     + "</a>";  
                           count=count+1;;
                         }*/
                        
                        out.print("<td></strong>"+ metadata+"</strong></td>");                        	
                      
                    }
                      count=0;
                    try
                    {
    					autorization(handle);
    				} catch (AuthorizeException e) {
    					// TODO Auto-generated catch block
    				log.info("error:authorization---"+e.getMessage());
    				}             
                   
    		         Integer item_id= getItemId(handle);      		         
    		       
                    if (linkToEdit)
                    { 
                      String id = "t" + Integer.toString(cOddOrEven.length + 1);
                        out.print("<td>"
                            + "<form method=\"get\" action=\"" + hrq.getContextPath() + "/tools/edit-item\">"
                            + "<input type=\"hidden\" name=\"handle\" value=\"" + items[i].getHandle() + "\" />"
                            + "<input type=\"hidden\" name=\"action\" value=\"1\" />"
                            + "<input type=\"hidden\" name=\"item_id\" value=\""+item_id+"\" />"
                            + "<span class=\"icon-input-btn\"><span class=\"glyphicon glyphicon-edit\"></span><input class=\"btn btn-success btn-xs\" type=\"submit\" value=\"Edit\" />&nbsp;"
                            + "<span class=\"icon-input-btn\"><span class=\"glyphicon glyphicon-trash\"></span><input class=\"btn btn-danger btn-xs\" name=\"submit_delete\" type=\"submit\" value=\"Delete\" /></form>"
                            + "</td>");
                    }
                    else
                    {
                    	out.print("<td></td>");
                    }
                   out.println("</tr>");
                }
                
            }
            
            else
            {
            for (int i = 0; i < items.length; i++)
            {
              	out.print("<tr>"); 
                // now prepare the XHTML frag for this division
               
                handle=items[i].getHandle();
                String document=getFileName(handle);    
                if (document.indexOf(".") > 0)
               	 document = document.substring(0, document.lastIndexOf("."));
                out.print("<td><a  href=\"" + hrq.getContextPath() + "/handle/"+ items[i].getHandle() + "\">" + document+ "</a></td>");                
                for (int colIdx = 0; colIdx < fieldArr.length; colIdx++)
                {
                                   	 
                	  String field = fieldArr[colIdx];              	  
                	 
                    StringTokenizer eq = new StringTokenizer(field, ".");

                    String[] tokens = { "", "", "" };
                    int k = 0;
                    while(eq.hasMoreTokens())
                    {
                        tokens[k] = eq.nextToken().toLowerCase().trim();
                        k++;
                    }
                    String schema = tokens[0];
                    String element = tokens[1];
                    String qualifier = tokens[2];

                    // first get hold of the relevant metadata for this column
                    Metadatum[] metadataArray;
                    
                    if (qualifier.equals("*"))
                    {
                        metadataArray = items[i].getMetadata(schema, element, Item.ANY, Item.ANY);
                    }
                    else if (qualifier.equals(""))
                    {
                        metadataArray = items[i].getMetadata(schema, element, null, Item.ANY);
                    }
                    else
                    {
                        metadataArray = items[i].getMetadata(schema, element, qualifier, Item.ANY);
                    }

                    // save on a null check which would make the code untidy
                    if (metadataArray == null)
                    {
                    	metadataArray = new Metadatum[0];
                    }

                    // now prepare the content of the table division
                    String metadata = "-";
                    if (field.equals("thumbnail"))
                    {
                        metadata = getThumbMarkup(hrq, items[i]);
                    }
                    else  if (field.startsWith("mark_"))
                    {
                        metadata = UIUtil.getMarkingMarkup(hrq, items[i], field);
                    }
                    else if (metadataArray.length > 0)
                    {
                        // format the date field correctly
                        if (isDate[colIdx])
                        {
                            DCDate dd = new DCDate(metadataArray[0].value);
                            metadata = UIUtil.displayDate(dd, false, false, hrq);
                        }
                        // format the title field correctly for withdrawn and private items (ie. don't link)
                        else if (field.equals(titleField) && items[i].isWithdrawn())
                        {
                            metadata = Utils.addEntities(metadataArray[0].value);
                        }
                        // format the title field correctly (as long as the item isn't withdrawn, link to it)
                        else if (field.equals(titleField))
                        {
                        	metadata=Utils.addEntities(metadataArray[0].value);
                        	/*
                         	metadata = "<a  href=\"" + hrq.getContextPath() + "/handle/"
                                    + items[i].getHandle() + "\">"
                                    + Utils.addEntities(metadataArray[0].value)
                                    + "</a>";                           
                        */}
                        else if(field.equals("viewField")){
                       	 metadata = "<a class=\"btn btn-primary\" href=\"" + hrq.getContextPath() + "/tools/edit-item?item_id" + items[i].getID()+ "\">View</a>";
                       }
                        else
                        {
                        	boolean truncated = false;
                        	int loopLimit = metadataArray.length;
                        	if (isAuthor[colIdx])
                        	{
                        		int fieldMax = (authorLimit == -1 ? metadataArray.length : authorLimit);
                        		loopLimit = (fieldMax > metadataArray.length ? metadataArray.length : fieldMax);
                        		truncated = (fieldMax < metadataArray.length);
                        		log.debug("Limiting output of field " + field + " to " + Integer.toString(loopLimit) + " from an original " + Integer.toString(metadataArray.length));
                        	}

                            StringBuffer sb = new StringBuffer();
                            for (int j = 0; j < loopLimit; j++)
                            {
                            	String startLink = "";
                            	String endLink = "";
                            	if (!StringUtils.isEmpty(browseType[colIdx]) && !disableCrossLinks)
                            	{
                                    String argument;
                                    String value;
                                    if (metadataArray[j].authority != null &&
                                            metadataArray[j].confidence >= MetadataAuthorityManager.getManager()
                                                .getMinConfidence(metadataArray[j].schema, metadataArray[j].element, metadataArray[j].qualifier))
                                    {
                                        argument = "authority";
                                        value = metadataArray[j].authority;
                                    }
                                    else
                                    {
                                        argument = "value";
                                        value = metadataArray[j].value;
                                    }
                            		if (viewFull[colIdx])
                            		{
                            			argument = "vfocus";
                            		}
                            		startLink = "<a href=\"" + hrq.getContextPath() + "/browse?type=" + browseType[colIdx] + "&amp;" +
                                        argument + "=" + URLEncoder.encode(value,"UTF-8");

                                    if (metadataArray[j].language != null)
                                    {
                                        startLink = startLink + "&amp;" +
                                            argument + "_lang=" + URLEncoder.encode(metadataArray[j].language, "UTF-8");
									}

                                    if ("authority".equals(argument))
                                    {
                                        startLink += "\" class=\"authority " +browseType[colIdx] + "\">";
                                    }
                                    else
                                    {
                                        startLink = startLink + "\">";
                                    }
                            		endLink = "</a>";
                            	}
                            	sb.append(startLink);
                                sb.append(Utils.addEntities(metadataArray[j].value));
                                sb.append(endLink);
                                if (j < (loopLimit - 1))
                                {
                                    sb.append("; ");
                                }
                            }
                            if (truncated)
                            {
                            	String etal = LocaleSupport.getLocalizedMessage(pageContext, "itemlist.et-al");
                                sb.append(", ").append(etal);
                            }
                            metadata = sb.toString();
                        }
                    }
                    //In case title has no value, replace it with "undefined" so as the user has something to
                	//click in order to access the item page
                    
                    else if (field.equals(titleField)){
                    	String undefined = LocaleSupport.getLocalizedMessage(pageContext, "itemlist.title.undefined");
                    	if (items[i].isWithdrawn())
                        {
                            metadata = "<span style=\"font-style:italic\">("+undefined+")</span>";
                        }
                        // format the title field correctly (as long as the item isn't withdrawn, link to it)
                        else
                        {
                         /* metadata = "<a href=\"" + hrq.getContextPath() + "/handle/"
                            + items[i].getHandle() + "\">"
                            + "<span style=\"font-style:italic\">("+undefined+")</span>"
                            + "</a>";*/
                        }
                    }
                    
                    // prepare extra special layout requirements for dates
                    String extras = "";
                    if (isDate[colIdx])
                    {
                        extras = "nowrap=\"nowrap\" align=\"right\"";
                    }
                    
                    String markClass = "";
                    if (field.startsWith("mark_"))
                    {
                    	markClass = " "+field+"_tr";
                    }
                    String id = "t" + Integer.toString(colIdx + 1);
                    out.print("<td><strong>" + metadata +  "</strong></td>");
                }
                
                try
                {
					autorization(handle);
				} catch (AuthorizeException e) {
					// TODO Auto-generated catch block
				log.info("error:authorization---"+e.getMessage());
				}             
                /*               	
		         HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();                	
		         String viewerUrl1=listBitstreams(handle);         
		         String url=getCheckOutUrl(handle);*/
		         Integer item_id= getItemId(handle);  
		         
                 // Add column for 'edit item' links
		         
                if (linkToEdit)
                { 
                   String id = "t" + Integer.toString(cOddOrEven.length + 1);
                    out.print("<td>"
                        + "<form method=\"get\" action=\"" + hrq.getContextPath() + "/tools/edit-item\">"
                        + "<input type=\"hidden\" name=\"handle\" value=\"" + items[i].getHandle() + "\" />"
                        + "<input type=\"hidden\" name=\"action\" value=\"1\" />"
                        + "<input type=\"hidden\" name=\"item_id\" value=\""+item_id+"\" />"
                        + "<span class=\"icon-input-btn\"><span class=\"glyphicon glyphicon-edit\"></span><input class=\"btn btn-success btn-xs\" type=\"submit\" value=\"Edit\" />&nbsp;"
                        + "<span class=\"icon-input-btn\"><span class=\"glyphicon glyphicon-trash\"></span><input class=\"btn btn-danger btn-xs\" name=\"submit_delete\" type=\"submit\" value=\"Delete\" /></form>"
                        + "</td>");
                }
                else
                {
                	out.print("<td></td>");
                }
               out.println("</tr>");
            }
            }

            // close the table
            out.println("</tbody></table>");
        }
        catch (IOException ie)
        {
            throw new JspException(ie);
        } catch (BrowseException e)
        {
        	throw new JspException(e);
        }

        return SKIP_BODY;
    } 
    
    
    private void autorization(String handle) throws IOException,AuthorizeException
    {
    	DSpaceObject dso = null;
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();         
        try
        {
            Context context = UIUtil.obtainContext(request);
        	if (handle != null)
            {
                dso = HandleManager.resolveToObject(context, handle);
            }         	
        	 Item item = (Item) dso;        	
        	// Ensure the user has authorisation
             AuthorizeManager.authorizeAction(context, item, Constants.READ);      
             log.info(LogManager.getHeader(context, "view_item", "handle="+ handle));
             // show edit link
             if (item.canEdit())
             { // set a variable to create an edit button
                 request.setAttribute("admin_button", Boolean.TRUE);
                 linkToEdit=true;
             }
             else
             {
            	 linkToEdit=false;
             }
                	
        }
        catch(SQLException sqle)
        {
        	throw new IOException(sqle.getMessage(), sqle);
        }
                
     
     
    }
    
    
    private Boolean isMoreFiles(String handle ) throws IOException
    {
    	DSpaceObject dso = null;
    	Boolean filesExist = false;     	  
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();   
        Integer item_id=0;
        try
        {
            Context context = UIUtil.obtainContext(request);
        	if (handle != null)
            {
                dso = HandleManager.resolveToObject(context, handle);                   	
        	Item item = (Item) dso;   
        	item_id=item.getID();
        	Bundle[] bundles = item.getBundles("ORIGINAL");       	   
           	
            		for (int i = 0; i < bundles.length; i++)
            		{
            			Bitstream[] bitstreams = bundles[i].getBitstreams();
            			log.info("file size:-------------->"+bitstreams.length);
            			if(bitstreams.length>1){
            				filesExist=true;
            			}

                	}
            } 
         
        }
        catch(SQLException sqle)
        {
        	throw new IOException(sqle.getMessage(), sqle);
        }                
              
      return filesExist;
    }
    
    private Integer getItemId(String handle) throws IOException
    {
    	DSpaceObject dso = null;
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();   
        Integer item_id=0;
        try
        {
            Context context = UIUtil.obtainContext(request);
        	if (handle != null)
            {
                dso = HandleManager.resolveToObject(context, handle);                   	
        	Item item = (Item) dso;   
        	item_id=item.getID();        	            		
            } 
         
        }
        catch(SQLException sqle)
        {
        	throw new IOException(sqle.getMessage(), sqle);
        }                
              
      return item_id;
    }
    
    
    private String listBitstreams(String handle ) throws IOException
    {
    	DSpaceObject dso = null;
    	String viewerUrl1="";
        JspWriter out = pageContext.getOut();      
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();         
        try
        {
             Context context = UIUtil.obtainContext(request);
        	if (handle != null)
            {
                dso = HandleManager.resolveToObject(context, handle);
            } 
        	
        	 Item item = (Item) dso;        	
        	Bundle[] bundles = item.getBundles("ORIGINAL");
        	boolean filesExist = false;            
                    	
            		for (int i = 0; i < bundles.length; i++)
            		{
            			Bitstream[] bitstreams = bundles[i].getBitstreams();

            			for (int k = 0; k < bitstreams.length; k++)
            			{
            				// Skip internal types
            				if (!bitstreams[k].getFormat().isInternal())
            				{  
            					String filename="";            					
            					filename=UIUtil.encodeBitstreamName(bitstreams[k].getName(), Constants.DEFAULT_ENCODING);
            					
            					String extension = "";
            					int j = filename.lastIndexOf('.');
            					if (j > 0) {
            					extension = filename.substring(j+1);
            					}
            					
            				String bsUrl = "bitstream/"+ item.getHandle()+ "/" + bitstreams[k].getSequenceID()
                                         + "/" + UIUtil.encodeBitstreamName(bitstreams[k].getName(), Constants.DEFAULT_ENCODING);							 
							if(extension.equals("pdf"))
							{
            				 viewerUrl1="/viewer.jsp?file="+bsUrl;
							}
							else
							{
								viewerUrl1="";
								viewerUrl1=viewerUrl1.trim()+"/"+bsUrl;
							}							  
							  
							}
            			}
            		}
           
        }
        catch(SQLException sqle)
        {
        	throw new IOException(sqle.getMessage(), sqle);
        }
                
       
      return viewerUrl1;
    }
    
    private String getCheckOutUrl(String handle ) throws IOException
    {
    	DSpaceObject dso = null;
    	String bsUrl="";
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();         
        try
        {
             Context context = UIUtil.obtainContext(request);
        	if (handle != null)
            {
                dso = HandleManager.resolveToObject(context, handle);
            } 
        	
        	 Item item = (Item) dso;        	
        	Bundle[] bundles = item.getBundles("ORIGINAL");
            		for (int i = 0; i < bundles.length; i++)
            		{
            			Bitstream[] bitstreams = bundles[i].getBitstreams();

            			for (int k = 0; k < bitstreams.length; k++)
            			{
            				// Skip internal types
            				if (!bitstreams[k].getFormat().isInternal())
            				{  
            					String filename="";            					
            					filename=UIUtil.encodeBitstreamName(bitstreams[k].getName(), Constants.DEFAULT_ENCODING);
            					
            				 bsUrl =request.getContextPath()+"/bitstream/"+ item.getHandle()+ "/" + bitstreams[k].getSequenceID()
                                         + "/" + UIUtil.encodeBitstreamName(bitstreams[k].getName(), Constants.DEFAULT_ENCODING);		 
											  
							}
            			}
            		}
                  }
        catch(SQLException sqle)
        {
        	throw new IOException(sqle.getMessage(), sqle);
        }
                
      return bsUrl;
    }
    
    
    
    private String getFileName(String handle) throws IOException
    {    	      
    	DSpaceObject dso = null;
    	String viewerUrl1="";
        JspWriter out = pageContext.getOut();      
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();         
        try
        {
             Context context = UIUtil.obtainContext(request);
        	if (handle != null)
            {
                dso = HandleManager.resolveToObject(context, handle);
            } 
        	
        	 Item item = (Item) dso;        	
        	Bundle[] bundles = item.getBundles("ORIGINAL");
        	boolean filesExist = false;            
            for (Bundle bnd : bundles)
            {
            	filesExist = bnd.getBitstreams().length > 0;
            	if (filesExist)
            	{
            		break;
            	}
            }
            
            // if user already has uploaded at least one file
        	if (!filesExist)
        	{
        	
        	}
        	else
        	{
            	// if primary bitstream is html, display a link for only that one to
            	// HTMLServlet            	
            		for (int i = 0; i < bundles.length; i++)
            		{
            			Bitstream[] bitstreams = bundles[i].getBitstreams();

            			for (int k = 0; k < bitstreams.length; k++)
            			{
            				// Skip internal types
            				if (!bitstreams[k].getFormat().isInternal())
            				{              							
            				 filename=bitstreams[k].getName();            					
							}
            			}
            		}
            	            	
        	}
        }
        catch(SQLException sqle)
        {
        	throw new IOException(sqle.getMessage(), sqle);
        }                    
       
      return filename;
    }
          
        
    public BrowseInfo getBrowseInfo()
    {
    	return browseInfo;
    }

    public void setBrowseInfo(BrowseInfo browseInfo)
    {
    	this.browseInfo = browseInfo;
    	setItems(browseInfo.getBrowseItemResults());
    	authorLimit = browseInfo.getEtAl();
    }

    public boolean getLinkToEdit()
    {
        return linkToEdit;
    }

    public void setLinkToEdit(boolean edit)
    {
        this.linkToEdit = edit;
    }

    public boolean getDisableCrossLinks()
    {
        return disableCrossLinks;
    }

    public void setDisableCrossLinks(boolean links)
    {
        this.disableCrossLinks = links;
    }

    /**
     * Get the items to list
     *
     * @return the items
     */
    public BrowseItem[] getItems()
    {
        return (BrowseItem[]) ArrayUtils.clone(items);
    }

    /**
     * Set the items to list
     *
     * @param itemsIn
     *            the items
     */
    public void setItems(BrowseItem[] itemsIn)
    {
        items = (BrowseItem[]) ArrayUtils.clone(itemsIn);
    }

    /**
     * Get the row to highlight - null or -1 for no row
     *
     * @return the row to highlight
     */
    public String getHighlightrow()
    {
        return String.valueOf(highlightRow);
    }

    /**
     * Set the row to highlight
     *
     * @param highlightRowIn
     *            the row to highlight or -1 for no highlight
     */
    public void setHighlightrow(String highlightRowIn)
    {
        if ((highlightRowIn == null) || highlightRowIn.equals(""))
        {
            highlightRow = -1;
        }
        else
        {
            try
            {
                highlightRow = Integer.parseInt(highlightRowIn);
            }
            catch (NumberFormatException nfe)
            {
                highlightRow = -1;
            }
        }
    }

    /**
     * Get the column to emphasise - "title", "date" or null
     *
     * @return the column to emphasise
     */
    public String getEmphcolumn()
    {
        return emphColumn;
    }

    /**
     * Set the column to emphasise - "title", "date" or null
     *
     * @param emphColumnIn
     *            column to emphasise
     */
    public void setEmphcolumn(String emphColumnIn)
    {
        emphColumn = emphColumnIn;
    }

    public void release()
    {
        highlightRow = -1;
        emphColumn = null;
        items = null;
    }

    /* get the required thumbnail config items */
    private static void getThumbSettings()
    {
        showThumbs = ConfigurationManager
                .getBooleanProperty("webui.browse.thumbnail.show");

        if (showThumbs)
        {
            thumbItemListMaxHeight = ConfigurationManager
                    .getIntProperty("webui.browse.thumbnail.maxheight");

            if (thumbItemListMaxHeight == 0)
            {
                thumbItemListMaxHeight = ConfigurationManager
                        .getIntProperty("thumbnail.maxheight");
            }

            thumbItemListMaxWidth = ConfigurationManager
                    .getIntProperty("webui.browse.thumbnail.maxwidth");

            if (thumbItemListMaxWidth == 0)
            {
                thumbItemListMaxWidth = ConfigurationManager
                        .getIntProperty("thumbnail.maxwidth");
            }
        }

        String linkBehaviour = ConfigurationManager
                .getProperty("webui.browse.thumbnail.linkbehaviour");

        if (linkBehaviour != null && linkBehaviour.equals("bitstream"))
        {
            linkToBitstream = true;
        }
    }

    /*
     * Get the (X)HTML width and height attributes. As the browser is being used
     * for scaling, we only scale down otherwise we'll get hideously chunky
     * images. This means the media filter should be run with the maxheight and
     * maxwidth set greater than or equal to the size of the images required in
     * the search/browse
     */
    private String getScalingAttr(HttpServletRequest hrq, Bitstream bitstream)
            throws JspException
    {
        BufferedImage buf;

        try
        {
            Context c = UIUtil.obtainContext(hrq);

            InputStream is = BitstreamStorageManager.retrieve(c, bitstream
                    .getID());

            //AuthorizeManager.authorizeAction(bContext, this, Constants.READ);
            // 	read in bitstream's image
            buf = ImageIO.read(is);
            is.close();
        }
        catch (SQLException sqle)
        {
            throw new JspException(sqle.getMessage(), sqle);
        }
        catch (IOException ioe)
        {
            throw new JspException(ioe.getMessage(), ioe);
        }

        // now get the image dimensions
        float xsize = (float) buf.getWidth(null);
        float ysize = (float) buf.getHeight(null);

        // scale by x first if needed
        if (xsize > (float) thumbItemListMaxWidth)
        {
            // calculate scaling factor so that xsize * scale = new size (max)
            float scaleFactor = (float) thumbItemListMaxWidth / xsize;

            // now reduce x size and y size
            xsize = xsize * scaleFactor;
            ysize = ysize * scaleFactor;
        }

        // scale by y if needed
        if (ysize > (float) thumbItemListMaxHeight)
        {
            float scaleFactor = (float) thumbItemListMaxHeight / ysize;

            // now reduce x size
            // and y size
            xsize = xsize * scaleFactor;
            ysize = ysize * scaleFactor;
        }

        StringBuffer sb = new StringBuffer("width=\"").append(xsize).append(
                "\" height=\"").append(ysize).append("\"");

        return sb.toString();
    }

    /* generate the (X)HTML required to show the thumbnail */
    private String getThumbMarkup(HttpServletRequest hrq, BrowseItem item)
            throws JspException
    {
    	try
    	{
            Context c = UIUtil.obtainContext(hrq);
            Thumbnail thumbnail = ItemService.getThumbnail(c, item.getID(), linkToBitstream);

            if (thumbnail == null)
    		{
    			return "";
    		}
        	StringBuffer thumbFrag = new StringBuffer();

        	if (linkToBitstream)
        	{
        		Bitstream original = thumbnail.getOriginal();
        		String link = hrq.getContextPath() + "/bitstream/" + item.getHandle() + "/" + original.getSequenceID() + "/" +
        						UIUtil.encodeBitstreamName(original.getName(), Constants.DEFAULT_ENCODING);
        		thumbFrag.append("<a target=\"_blank\" href=\"" + link + "\" />");
        	}
        	else
        	{
        		String link = hrq.getContextPath() + "/handle/" + item.getHandle();
        		thumbFrag.append("<a href=\"" + link + "\" />");
        	}

        	Bitstream thumb = thumbnail.getThumb();
        	String img = hrq.getContextPath() + "/retrieve/" + thumb.getID() + "/" +
        				UIUtil.encodeBitstreamName(thumb.getName(), Constants.DEFAULT_ENCODING);
        	String alt = thumb.getName();
            String scAttr = getScalingAttr(hrq, thumb);
            thumbFrag.append("<img src=\"")
                    .append(img)
                    .append("\" alt=\"").append(alt).append("\" ")
                     .append(scAttr)
                     .append("/ border=\"0\"></a>");

        	return thumbFrag.toString();
        }
        catch (SQLException sqle)
        {
        	throw new JspException(sqle.getMessage(), sqle);
        }
        catch (UnsupportedEncodingException e)
        {
            throw new JspException("Server does not support DSpace's default encoding. ", e);
        }
    }
}
