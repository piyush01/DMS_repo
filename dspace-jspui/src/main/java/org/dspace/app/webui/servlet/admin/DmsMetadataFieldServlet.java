package org.dspace.app.webui.servlet.admin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dspace.administer.DataTypes;
import org.dspace.administer.DmsNewField;
import org.dspace.administer.DmsfieldBean;
import org.dspace.app.webui.servlet.DSpaceServlet;
import org.dspace.app.webui.util.JSPManager;
import org.dspace.app.webui.util.UIUtil;
import org.dspace.authorize.AuthorizeException;
import org.dspace.content.Collection;
import org.dspace.content.DmsMetadaField;
import org.dspace.content.MetadataField;
import org.dspace.content.MetadataSchema;
import org.dspace.content.NonUniqueMetadataException;
import org.dspace.content.UpdateInputFormData;
import org.dspace.core.Context;

public class DmsMetadataFieldServlet extends DSpaceServlet {

	 /** Logger */
    //private static Logger log = Logger.getLogger(DmsMetadaFieldervlet.class);
	private static final Logger log=Logger.getLogger(DmsMetadataFieldServlet.class);
    private String clazz = "org.dspace.app.webui.servlet.admin.DmsMetadaFieldervlet";
DmsNewField dmsnewfield=new DmsNewField();
    
	public DmsNewField getDmsnewdield() {
		return dmsnewfield;
	}

	public void setDmsnewdield(DmsNewField dmsnewdield) {
		this.dmsnewfield = dmsnewdield;
	}
/*	 List<DmsfieldBean> list=new ArrayList<DmsfieldBean>();*/
	
    /**
     * @see org.dspace.app.webui.servlet.DSpaceServlet#doDSGet(org.dspace.core.Context,
     *      javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    protected void doDSGet(Context context, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException
    {
    	 
         showTypes(context, request, response);
         //JSPManager.showJSP(request, response,"/dspace-admin/dms-metadata-field-registry.jsp");
    	
        
    }
    /**
     * @see org.dspace.app.webui.servlet.DSpaceServlet#doDSPost(org.dspace.core.Context,
     *      javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    protected void doDSPost(Context context, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException
    {
    	String button = UIUtil.getSubmitButton(request, "submit");
    	// Get access to the localized resource bundle
        Locale locale = context.getCurrentLocale();
        ResourceBundle labels = ResourceBundle.getBundle("Messages", locale);
    	String element=request.getParameter("element");
    	String fieldname=request.getParameter("fieldname");
    	if(button.equals("submit_add"))
    	{
    		if(dmsnewfield.checkFieldName(context, request.getParameter("fieldname")))
    		{
    			 request.setAttribute("checkmessage",Boolean.TRUE);
                 showTypes(context, request, response);
                 context.complete();	
    		}
    		else
    		{
    			try{
                	DmsMetadaField dc = new DmsMetadaField();
                	 MetadataField dc1 = new MetadataField();
                	DataTypes data=new DataTypes();
                    //dc.setCollectionID();
                	dc.setFieldName(removewhitespaces(fieldname).toLowerCase());
                	dc.setDataType(request.getParameter("inputtype_id"));
                	dc.setMandatory(request.getParameter("required_field"));
                	dc.setSearchStatus(0);
                	dc.setFieldLevel(fieldname);
                    dc.create(context);
                    String myelement=removewhitespaces(fieldname).toLowerCase();
                   if(!DmsMetadaField.checkelement(context, myelement))
                   {
                       dc1.setSchemaID(1);
                       dc1.setElement(myelement);
                       String quall=null;
                       dc1.setQualifier(quall);
                       dc1.setScopeNote("");
                       dc1.create(context);
                   }

                    /*
                     * Code Add By Sanjeev Kumar 9-04-2016
                     * */
                   String displayvalue[]=request.getParameterValues("dropdown_value");
                   if(request.getParameter("inputtype_id").equals("dropdown"))
                   {
                	   if(displayvalue!=null && displayvalue.length>0)
                	   {
                	   for(int i=0;i<displayvalue.length;i++)
                       {
                	  if(displayvalue[i].equals("--Please Select--"))
                	  {
                		  data.setFieldName(request.getParameter("fieldname"));
                   	   		data.setDataType(request.getParameter("inputtype_id"));
                          	data.setDispalyValue(displayvalue[i].toLowerCase());
                	  }else
                	  {
                		  	data.setFieldName(request.getParameter("fieldname"));
                   	   		data.setDataType(request.getParameter("inputtype_id"));
                          	data.setDispalyValue(displayvalue[i]);
                          	data.setStoredValue(displayvalue[i]);  
                	  }
                	   
                        data.create(context);
                        log.info("DropDownValue=================>"+displayvalue[i]);
                       }
                	   }
                      
                   }
                   /*
                    * Code End By Sanjeev Kumar 9-04-2016
                    * */
                    request.setAttribute("field_add",Boolean.TRUE);
                    showTypes(context, request, response);
                    //JSPManager.showJSP(request, response,"/dspace-admin/dms-list-metadata-field-registry.jsp");
                    context.complete();	
        			}
        			catch(NonUniqueMetadataException e)
        			{
        				e.printStackTrace();
        			}}
    		}
    	else if(button.equals("submit_cancel"))
    	{
    		//DmsNewField[] newfield=DmsNewField.getAllFields(context);
    		DmsMetadaField[] newfield=DmsMetadaField.getAllFields(context);
    		log.info("field size========>"+newfield.length);
    		//list=dmsnewfield.getAllFields(context);
    		request.setAttribute("fields",newfield);
    		JSPManager.showJSP(request, response,"/dspace-admin/fields.jsp");
    	}
    	
    }
    
    private String removewhitespaces(String str)
	{
		str= str.replaceAll("\\s+","").trim();
		//str=str.replace("_","");
		return str;
	}
    /**
     * Show list of DC type
     * 
     * @param context
     *            Current DSpace context
     * @param request
     *            Current HTTP request
     * @param response
     *            Current HTTP response
     * @param schemaID
     * @throws ServletException
     * @throws IOException
     * @throws SQLException
     * @throws AuthorizeException
     */
    private void showTypes(Context context, HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException, SQLException,
            AuthorizeException
    {

        JSPManager.showJSP(request, response,"/dspace-admin/dms-metadata-field-registry.jsp");
    }

   }
