 package org.dspace.app.webui.servlet.admin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.Logger;
import org.dspace.administer.DataTypesBean;
import org.dspace.administer.DmsNewField;
import org.dspace.administer.DmsfieldBean;
import org.dspace.administer.UserBean;
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
import org.xml.sax.SAXException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.java.swing.plaf.windows.resources.windows;
import com.sun.jndi.url.dns.dnsURLContext;


public class DmsMetadataSchemaServlet extends DSpaceServlet {


    /** Logger */
    private static Logger log = Logger.getLogger(DmsMetadataSchemaServlet.class);
    private String clazz = "org.dspace.app.webui.servlet.admin.DmsMetadataSchemaServlet";
    
   
    
   /* DmsMetadaField dmsmetadata=new DmsMetadaField();*/
	DmsNewField dmsnewfield=new DmsNewField();
	List<DataTypesBean> datatype=new ArrayList<DataTypesBean>();
	public DmsNewField getDmsnewfield() {
		return dmsnewfield;
	}

	public void setDmsnewfield(DmsNewField dmsnewfield) {
		this.dmsnewfield = dmsnewfield;
	}

	protected void doDSGet(Context context, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException
    {	Collection[] collections = Collection.findAll(context);
    	String action=request.getParameter("action");
    	  if(action!=null && action.equals("update_file"))
          {
    		  //Collection[] collections = Collection.findAll(context);
    		  request.setAttribute("collections", collections);
    		  JSPManager.showJSP(request, response,"/dspace-admin/update-config-files.jsp");
    		  context.complete();
          }
    	  else if(action!=null && action.equals("field_list"))
    	  {
    		 DmsMetadaField[] allnewfield=DmsMetadaField.getAllFields(context);
    		 
    		  request.setAttribute("fields",allnewfield);
    		  JSPManager.showJSP(request, response,"/dspace-admin/fields.jsp");
    	  }
    	  else if(action!=null && action.equals("update_search"))
    	  {
    		  DmsMetadaField[] allnewfield=DmsMetadaField.getAllFields(context);
    		  DmsMetadaField[] allsearchfield=DmsMetadaField.getAllSearchFields(context, 1);
    		  request.setAttribute("all_field",allnewfield);
    		  request.setAttribute("allsearchfield", allsearchfield);
    		  JSPManager.showJSP(request, response,"/dspace-admin/add-dms-search-field.jsp");
    	  }
    	  else
    	  {
        // GET just displays the list of type
        showSchemas(context, request, response);
    	  }
    	 /* else if(action!=null && action.equals("fieldlist"))
    	  {
    		  List<DmsfieldBean> list1=new ArrayList<DmsfieldBean>();
    		  list1=dmsnewfield.getData(context);
    		  request.setAttribute("fielddata", list1);
    		  JSPManager.showJSP(request, response,"/dspace-admin/dms-fields-list.jsp");
    		  context.complete();
    	  }*/
    	 
        
    }

    protected void doDSPost(Context context, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException
    {
    	//final String xmlFilePath = "D:\\workspace5.4\\dspace-5.4-src-release\\dspace\\config\\dspace.cfg";
    	//final String filename = "D:\\workspace5.4\\dspace-5.4-src-release\\dspace-api\\src\\main\\resources\\Messages.properties";
        String button = UIUtil.getSubmitButton(request, "submit");
    	Collection[] collections = Collection.findAll(context);
        String collection_id=request.getParameter("collection_id");
        String browse=request.getParameter("browsefield");
        String search=request.getParameter("searchfield");
        Collection collection = null;
       // String fields[]=request.getParameterValues("fields");
        String fieldname=request.getParameter("fieldname");
        if (button.equals("submit_add"))
        {
            // We are either going to create a new dc schema or update and
            // existing one depending on if a schema_id was passed in
            String id = request.getParameter("dc_schema_id");

            // The sanity check will update the request error string if needed
            if (!sanityCheck(request))
            {
                showSchemas(context, request, response);
                context.abort();
                return;
            }

            try
            {
                if (id.equals(""))
                {
                    // Create a new metadata schema
                    MetadataSchema schema = new MetadataSchema();
                    schema.setNamespace(request.getParameter("namespace"));
                    schema.setName(request.getParameter("short_name"));
                    schema.create(context);
                    showSchemas(context, request, response);
                    context.complete();
                }
                else
                {
                    // Update an existing schema
                    MetadataSchema schema = MetadataSchema.find(context,
                            UIUtil.getIntParameter(request, "dc_schema_id"));
                    schema.setNamespace(request.getParameter("namespace"));
                    schema.setName(request.getParameter("short_name"));
                    schema.update(context);
                    showSchemas(context, request, response);
                    context.complete();
                }
            }
            catch (NonUniqueMetadataException e)
            {
                request.setAttribute("error",
                        "Please make the namespace and short name unique.");
                showSchemas(context, request, response);
                context.abort();
                return;
            }
        }
        else if (button.equals("submit_delete"))
        {
            // Start delete process - go through verification step
            MetadataSchema schema = MetadataSchema.find(context, UIUtil
                    .getIntParameter(request, "dc_schema_id"));
            request.setAttribute("schema", schema);
            JSPManager.showJSP(request, response,
                    "/dspace-admin/confirm-delete-mdschema.jsp");
        }
        else if (button.equals("submit_confirm_delete"))
        {
            // User confirms deletion of type
            MetadataSchema dc = MetadataSchema.find(context, UIUtil.getIntParameter(request, "dc_schema_id"));
            dc.delete(context);
            showSchemas(context, request, response);
            context.complete();
        }
        /*=========================================================*/
        else if(button.equals("submit_update_input_form"))
        {
        	DmsMetadaField[] allnewfield=DmsMetadaField.getAllFields(context);
        	//list=dmsnewfield.getAllFields(context);
        	 collection = Collection.find(context,Integer.parseInt(collection_id));
        	/*log.info("list size=========>"+list.size());
        	if(list.size()==0)
        	{
        		request.setAttribute("errormessage1",Boolean.TRUE);
        		request.setAttribute("collections", collections);
        		request.setAttribute("collection", collection);
       		  	JSPManager.showJSP(request, response,"/dspace-admin/update-config-files.jsp");
       		  	context.complete();
        	}else{*/
        	
        	 DmsMetadaField[] fieldnamecollection=DmsMetadaField.getFieldNameByCollection(context,Integer.parseInt(collection_id));
        	// getfields=dmsnewfield.getFieldNameByCollection(context, Integer.parseInt(collection_id));
     		request.setAttribute("fields",fieldnamecollection);
        	request.setAttribute("collection", collection);
        	request.setAttribute("dmsfields", allnewfield);
        	JSPManager.showJSP(request, response,"/dspace-admin/add-dms-inputform.jsp");
        	context.complete();
        	/*}*/
        }
        /*else if(button.equals("submit_update_browse"))
        {
        	log.info("submit_update_browse browse value==========>"+browse);
        	list=dmsnewfield.getBrowsedatabycollectionId(context, Integer.parseInt(collection_id));
        	 collection = Collection.find(context,Integer.parseInt(collection_id));
        	if(list.size()==0){
        		request.setAttribute("errormessage1",Boolean.TRUE);
        		request.setAttribute("collections", collections);
        		request.setAttribute("collection", collection);
       		  	JSPManager.showJSP(request, response,"/dspace-admin/update-config-files.jsp");
       		  	context.complete();
        	}else{
        	request.setAttribute("collection", collection);
        	request.setAttribute("dmsfields", list);
        	JSPManager.showJSP(request, response,"/dspace-admin/add-dms-browse-files.jsp");
        	context.complete();
        	}
        }
        else if(button.equals("submit_update_searchFields"))
        {
        	log.info("submit_update_searchFields search value==========>"+search);
        	list=dmsnewfield.getSearchdatabycollectionId(context, Integer.parseInt(collection_id));
        	 collection = Collection.find(context,Integer.parseInt(collection_id));
        	if(list.size()==0)
        	{
        		request.setAttribute("errormessage1",Boolean.TRUE);
        		request.setAttribute("collections", collections);
        		request.setAttribute("collection", collection);
       		  	JSPManager.showJSP(request, response,"/dspace-admin/update-config-files.jsp");
       		  	context.complete();
        	}else
        	{
        	request.setAttribute("collection", collection);
        	request.setAttribute("dmsfields", list);
        	JSPManager.showJSP(request, response,"/dspace-admin/add-dms-search-field.jsp");
        	context.complete();	
        	}
        }*/
        else if(button.equals("submit_add_InputForm"))
        {
        	
        	String collectionid=request.getParameter("collectionid");
        	DmsMetadaField[] databyfieldname=DmsMetadaField.getdatabyFieldName(context,removewhitespaces(fieldname).toLowerCase());
        	//list1=dmsnewfield.getdatabyFieldName(context,fieldname);
        	//DmsMetadaField[] datatypevalue=DmsMetadaField.getDropDownValue(context, fieldname);
        	datatype=dmsnewfield.getDropDownValue(context, fieldname);
        	collection=Collection.find(context, Integer.parseInt(collectionid));
        	log.info("lavel value========>"+fieldname);
        	log.info("collection id====>"+collection);
        	int fieldid = 0;
    		for(int j=0;j<databyfieldname.length;j++)
			{
				String collection_handle=collection.getHandle();
				String form_name=collection.getName()+"-"+collection.getID();
				form_name=removewhitespaces(form_name);
				String quall=null;
				String element=databyfieldname[j].getFieldName();
				String itemdisplaydata="dc"+"."+element;
				String metadatavalue=element;
				fieldid=databyfieldname[j].getID();

				log.info("Collection handle===========>"+collection_handle);
				log.info("form_name=============>"+form_name);
				log.info("itemdisplaydata=========>"+itemdisplaydata);
			
				try {
					log.info("call Add_FormMapElement method=============>");
					UpdateInputFormData.Add_FormMapElement(collection_handle,form_name);
					UpdateInputFormData.Add_FormFieldElement(form_name,"dc",element,quall,databyfieldname[j].getFieldLevel().trim(),databyfieldname[j].getDataType(),databyfieldname[j].getMandatory());
					UpdateInputFormData.UpdateItemDisplayValue(itemdisplaydata);
					UpdateInputFormData.updateMessage(metadatavalue,databyfieldname[j].getFieldLevel());
				} catch (Exception e) {
					e.printStackTrace();
				}
				log.info("FieldNAme============>"+databyfieldname[j].getFieldName());
				log.info("DataType============>"+databyfieldname[j].getDataType());
			}
    		if(datatype==null||datatype.isEmpty())
    		{
    			
    		}
    		else
    		{
    			for(int i=0;i<datatype.size();i++)
        		{
        			try {
        				UpdateInputFormData.Add_valuePair(datatype.get(i).getField_name().trim(),datatype.get(i).getDisplayvalue(), datatype.get(i).getStoredvalue());
    				} catch (XPathExpressionException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
        			log.info("DisplayValue===================>"+datatype.get(i).getDisplayvalue());
        			log.info("StoredValue===================>"+datatype.get(i).getStoredvalue());
        		}
    		}
    	
    		DmsMetadaField.setfieldscollection(context, Integer.parseInt(collectionid), fieldid);
    		log.info("field id=================>"+fieldid);
    		DmsMetadaField[] allfield=DmsMetadaField.getAllFields(context);
    		//list=dmsnewfield.getAllFields(context);
    		 //getfields=dmsnewfield.getFieldNameByCollection(context, Integer.parseInt(collectionid));
    		 DmsMetadaField[] fields=DmsMetadaField.getFieldNameByCollection(context, Integer.parseInt(collectionid));
    		 request.setAttribute("fields",fields);
    		request.setAttribute("collection", collection);
    		request.setAttribute("message", Boolean.TRUE);
    		request.setAttribute("dmsfields",allfield);
    		  JSPManager.showJSP(request, response,"/dspace-admin/add-dms-inputform.jsp");
    		  context.complete();
        }
        else if(button.equals("submit_SearchField"))
        {
        	
        	//int fieldid=Integer.parseInt(request.getParameter("fieldid"));
        	DmsMetadaField[] databyfieldname=DmsMetadaField.getdatabyFieldName(context, fieldname);
        	MetadataField dc1 = new MetadataField();
        	log.info("lavel value =====>"+fieldname);
        	for(int i=0;i<databyfieldname.length;i++)
    		{
        		String element=databyfieldname[i].getFieldName();
        		
    			String value=element+":dc."+element+".*";
    			try {
    				UpdateInputFormData.createBeanInDiscovery(element,"dc"+"."+element);
    				UpdateInputFormData.createsearchFilterdefaultConfiguration(element);
    				UpdateInputFormData.AddAdvanceSearch(value);
    				UpdateInputFormData.AddAdvance(element);
					//UpdateInputForm_data.createsidebarFacetsdefaultConfiguration(element);
					//UpdateInputForm_data.UpdateMessageforsearchsidefacet(element,element);
    				UpdateInputFormData.UpdateMessageforsearch(element,databyfieldname[i].getFieldLevel());
    				UpdateInputFormData.UpdateMessageforAdvancesearch(element,databyfieldname[i].getFieldLevel());
				} catch (XPathExpressionException | ParserConfigurationException | SAXException
						| TransformerException | ConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
        	if(!DmsMetadaField.checkelement(context, removewhitespaces(fieldname).toLowerCase()))
        	{
        		 dc1.setSchemaID(1);
                 dc1.setElement(removewhitespaces(fieldname).toLowerCase());
                 String quall=null;
                 dc1.setQualifier(quall);
                 dc1.setScopeNote("");
                 try {
					dc1.create(context);
				} catch (NonUniqueMetadataException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        	  DmsMetadaField.updateSearch(context,removewhitespaces(fieldname).toLowerCase());
        DmsMetadaField[] allnewfield=DmsMetadaField.getAllFields(context);
      DmsMetadaField[] allsearchfield=DmsMetadaField.getAllSearchFields(context, 1);
      request.setAttribute("allsearchfield", allsearchfield);
   		  request.setAttribute("all_field",allnewfield);
        	request.setAttribute("message", Boolean.TRUE);
    		  JSPManager.showJSP(request, response,"/dspace-admin/add-dms-search-field.jsp");
    		  context.complete();
        }
       /* else if(button.equals("submit_add_BrowseField"))
        {
        	
        	String collectionid=request.getParameter("collectionid");
        	log.info("collection id=======>"+collection);
        	list1=dmsnewfield.getdatabyFieldName(context, field_name);
        	collection=Collection.find(context,Integer.parseInt(collectionid));
        	log.info("lavel value =====>"+fieldname);
        	log.info("collection id====>"+collection);
        	for(int i=0;i<list1.size();i++){
        		try {
					UpdateInputForm_data.AddBrowseField(list1.get(i).getField_name()+":metadata:"+"dc"+"."+list1.get(i).getField_name()+"."+":text");
					UpdateInputForm_data.AddBrowsemenu(list1.get(i).getField_name(), list1.get(i).getField_name());
					UpdateInputForm_data.AddBrowsetype(list1.get(i).getField_name(), list1.get(i).getField_name());
        		} catch (ConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		}
        	
        	
        	list=dmsnewfield.getBrowsedatabycollectionId(context, Integer.parseInt(collectionid));
        	request.setAttribute("collection", collection);
        	request.setAttribute("message", Boolean.TRUE);
        	request.setAttribute("dmsfields", list);
    		  JSPManager.showJSP(request, response,"/dspace-admin/add-dms-browse-files.jsp");
    		  context.complete();
        }
        else if(button.equals("submit_add_SearchField"))
        {
        	
        	String collectionid=request.getParameter("collectionid");
        	collection =Collection.find(context, Integer.parseInt(collectionid));
        	list1=dmsnewfield.getdatabyFieldName(context, field_name);
        	log.info("lavel value =====>"+fieldname);
        	log.info("collection id====>"+collection);
        	for(int i=0;i<list1.size();i++)
    		{
    			
    			try {
    				UpdateInputForm_data.createBeanInDiscovery(list1.get(i).getField_name(),"dc"+"."+list1.get(i).getField_name());
					UpdateInputForm_data.createsearchFilterdefaultConfiguration(list1.get(i).getField_name());
					UpdateInputForm_data.createsidebarFacetsdefaultConfiguration(list1.get(i).getField_name());
					UpdateInputForm_data.UpdateMessageforsearchsidefacet(list1.get(i).getField_name(),list1.get(i).getField_name());
					UpdateInputForm_data.UpdateMessageforsearch(list1.get(i).getField_name(),list1.get(i).getField_name());
				} catch (XPathExpressionException | ParserConfigurationException | SAXException
						| TransformerException | ConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
        	
        	
        	list=dmsnewfield.getSearchdatabycollectionId(context,Integer.parseInt(collectionid));
        	request.setAttribute("collection", collection);
        	request.setAttribute("message", Boolean.TRUE);
        	request.setAttribute("dmsfields", list);
    		  JSPManager.showJSP(request, response,"/dspace-admin/add-dms-search-field.jsp");
    		  context.complete();
        }*/
        else if(button.equals("submit_cancel"))
        {
        request.setAttribute("collections", collections);
  		  JSPManager.showJSP(request, response,"/dspace-admin/update-config-files.jsp");
  		  context.complete();
        }
        /*==========================================================*/
        else
        {
            // Cancel etc. pressed - show list again
            showSchemas(context, request, response);
        }
    }

    /**
     * Return false if the schema arguments fail to pass the constraints. If
     * there is an error the request error String will be updated with an error
     * description.
     *
     * @param request
     * @return true of false
     */
    private boolean sanityCheck(HttpServletRequest request)
    {
        Locale locale = request.getLocale();
        ResourceBundle labels =
            ResourceBundle.getBundle("Messages", locale);
        
        // TODO: add more namespace checks
        String namespace = request.getParameter("namespace");
        if (namespace.length() == 0)
        {
            return error(request, labels.getString(clazz  + ".emptynamespace"));
        }

        String name = request.getParameter("short_name");
        if (name.length() == 0)
        {
            return error(request, labels.getString(clazz  + ".emptyname"));
        }
        if (name.length() > 32)
        {
            return error(request, labels.getString(clazz  + ".nametolong"));
        }
        for (int ii = 0; ii < name.length(); ii++)
        {
            if (name.charAt(ii) == ' ' || name.charAt(ii) == '_'
                    || name.charAt(ii) == '.')
            {
                return error(request,
                        labels.getString(clazz  + ".illegalchar"));
            }
        }

        return true;
    }

    /**
     * Bind the error text to the request object.
     *
     * @param request
     * @param text
     * @return false
     */
    private boolean error(HttpServletRequest request, String text)
    {
        request.setAttribute("error", text);
        return false;
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
     * @throws ServletException
     * @throws IOException
     * @throws SQLException
     * @throws IOException
     */
    private void showSchemas(Context context, HttpServletRequest request,
            HttpServletResponse response) throws ServletException,
            SQLException, IOException
    {
        MetadataSchema[] schemas = MetadataSchema.findAll(context);
        request.setAttribute("schemas", schemas);
        log.info("Showing Schemas");
		JSPManager.showJSP(request, response, "/dspace-admin/dms-list-metadata-schemas.jsp");
    }
    private int getSchemaID(HttpServletRequest request)
    {
        int schemaID = MetadataSchema.DC_SCHEMA_ID;
        if (request.getParameter("dc_schema_id") != null)
        {
            schemaID = Integer.parseInt(request.getParameter("dc_schema_id"));
        }
        return schemaID;
    }
    
    private String removewhitespaces(String str)
   	{
   		str= str.replaceAll("\\s+","").trim();
   		//str=str.replace("_","");
   		return str;
   	}
}
