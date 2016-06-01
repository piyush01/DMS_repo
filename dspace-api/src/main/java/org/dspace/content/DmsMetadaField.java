package org.dspace.content;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dspace.administer.DataTypesBean;
import org.dspace.administer.DmsNewField;
import org.dspace.administer.DmsfieldBean;
import org.dspace.administer.UserBean;
import org.dspace.authorize.AuthorizeException;
import org.dspace.authorize.AuthorizeManager;
import org.dspace.core.Context;
import org.dspace.core.LogManager;
import org.dspace.storage.rdbms.DatabaseManager;
import org.dspace.storage.rdbms.TableRow;
import org.dspace.storage.rdbms.TableRowIterator;

public class DmsMetadaField implements Serializable{


 
    /**
	 * 
	 */
	private static final long serialVersionUID = 1345234234234L;
	/*=====================================================================================*/
	private int ID=0;
	private int dmsfields_id=0;
	//private int collection_id=0;
	private String field_name;
    private String datatype;
    private String mandatory;
    private int datatype_id;
	private String displayvalue;
	private String storedvalue;
	private int searchstatus;
	private String field_lavel;
    /*=====================================================================================*/
    /** log4j logger */
    private static Logger log = Logger.getLogger(DmsMetadaField.class);

    /** The row in the table representing this type */
    private TableRow row;

    // cache of field by ID (Integer)
    private static Map<Integer, DmsMetadaField> id2field = null;

    /** metadatafield cache */
    private static Map<String, DmsMetadaField> metadatafieldcache = null;

   
    
    /**
     * Default constructor.
     */
    public DmsMetadaField()
    {
    }

    /**
     * Full constructor for new metadata field elements.
     *
     * @param schema schema to which the field belongs
     * @param element element of the field
     * @param qualifier qualifier of the field
     * @param scopeNote scope note of the field
     */
    public DmsMetadaField( String field_name, String datatype,String mandatory,int searchstatus,String field_lavel)
    {
        //this.collection_id = collection_id;
        this.field_name = field_name;
        this.datatype= datatype;
        this.mandatory=mandatory;
        this.searchstatus=searchstatus;
        this.field_lavel=field_lavel;
    }

    /**
     * Full constructor for existing metadata field elements.
     *
     * @param schemaID schema to which the field belongs
     * @param fieldID database ID of field.
     * @param element element of the field
     * @param qualifier qualifier of the field
     * @param scopeNote scope note of the field
     */
    public DmsMetadaField(int id, String field_name, String datatype,String mandatory,int searchstatus,String field_lavel)
    {
        this.ID = id;
        //this.collection_id = collection_id;
        this.field_name = field_name;
        this.datatype= datatype;
        this.mandatory=mandatory;
        this.searchstatus=searchstatus;
        this.field_lavel=field_lavel;
    }

    /**
     * Constructor to load the object from the database.
     *
     * @param row database row from which to populate object.
     */
    public DmsMetadaField(TableRow row)
    {
        if (row != null)
        {
            this.ID = row.getIntColumn("dmsfields_id");
           // this.collection_id= row.getIntColumn("collection_id ");
            this.field_name = row.getStringColumn("field_name");
            this.datatype = row.getStringColumn("datatype");
            this.mandatory = row.getStringColumn("mandatory");
            this.searchstatus=row.getIntColumn("searchstatus");
            this.field_lavel=row.getStringColumn("field_lavel");
            this.row = row;
        }
    }
    public int getID()
    {
        return ID;
    }
    public void setID(int id)
    {
        this.ID=id;
    }
  
    public int getSearchStatus()
    {
    	return searchstatus;
    }
    public void setSearchStatus(int searchstatus)
    {
    	this.searchstatus=searchstatus;
    }
    
    public String getFieldLevel()
    {
    	return field_lavel;
    }
    
    public void setFieldLevel(String fieldlavel)
    {
    	this.field_lavel=fieldlavel;
    }
    /**
     * Get the Collection ID.
     *
     * @return Collection ID
     */
    /*public int getCollectionID()
    {
        return collection_id;
    }*/

    /**
     * Set the Collection ID.
     *
     * @param Collection ID new value for Collection ID
     */
    /*public void setCollectionID(int collection_id)
    {
        this.collection_id = collection_id;
    }*/
    
    public void setFieldName(String fieldname)
    {
    	this.field_name=fieldname;
    }
    public void setDataType(String datatype)
    {
    	this.datatype=datatype;
    }
    public void setMandatory(String mandatory)
    {
    	this.mandatory=mandatory;
    }
    
    public String getFieldName()
    {
    	return field_name;
    }
    public String getDataType()
    {
    	return datatype;
    }
    public String getMandatory()
    {
    	return mandatory;
    }
    /*=========================================================================================*/
 
    /**
     * Creates a new metadata field.
     *
     * @param context
     *            DSpace context object
     * @throws IOException
     * @throws AuthorizeException
     * @throws SQLException
     * @throws NonUniqueMetadataException
     */
    public void create(Context context) throws IOException, AuthorizeException,
            SQLException, NonUniqueMetadataException
    {
        // Create a table row and update it with the values
        row = DatabaseManager.row("dmsfields");
        //row.setColumn("collection_id", collection_id);
        row.setColumn("field_name", field_name);
        row.setColumn("datatype",datatype);
        row.setColumn("mandatory",mandatory);
        row.setColumn("searchstatus", searchstatus);
        row.setColumn("field_lavel",field_lavel);
        DatabaseManager.insert(context, row);
        decache();

        // Remember the new row number
        this.ID = row.getIntColumn("dmsfields_id");

        log.info(LogManager.getHeader(context, "create_dmsfields",
                "metadata_field_id=" + row.getIntColumn("dmsfields_id")));
    }


 
    /**
     * Update the metadata field in the database.
     *
     * @param context dspace context
     * @throws SQLException
     * @throws AuthorizeException
     * @throws NonUniqueMetadataException
     * @throws IOException
     */
    public void update(Context context) throws SQLException,
            AuthorizeException, NonUniqueMetadataException, IOException
    {
        //row.setColumn("collection_id", collection_id);
        row.setColumn("field_name", field_name);
        row.setColumn("datatype", datatype);
        row.setColumn("mandatory", mandatory);
        row.setColumn("searchstatus",searchstatus);
        row.setColumn("field_lavel",field_lavel);
        DatabaseManager.update(context, row);
        decache();

        log.info(LogManager.getHeader(context, "update_dmsfields",
                "dmsfields_id=" + getID() + "Fiel_name=" + getFieldName()
                        + "data_type=" + getDataType()));
    }

    // invalidate the cache e.g. after something modifies DB state.
    private static void decache()
    {
        id2field = null;
    }

    private static boolean isCacheInitialized()
    {
        return id2field != null;
    }
   
    public static void setfieldscollection(Context ourContext,int collectionid,int fieldid) throws SQLException
    {
    	TableRow mappingRow = DatabaseManager.row("dmsfields2collection");
        mappingRow.setColumn("collectionid",collectionid);
        mappingRow.setColumn("fieldid", fieldid);
        DatabaseManager.insert(ourContext, mappingRow);
        
    }

	public static DmsMetadaField[] getAllFields(Context context) throws SQLException
	{
		List<DmsMetadaField> list=new ArrayList<DmsMetadaField>();
		TableRowIterator tri =DatabaseManager.queryTable(context, "dmsfields","SELECT * FROM dmsfields");
		try
        {
            // Make into DC Type objects
            while (tri.hasNext())
            {
                list.add(new DmsMetadaField(tri.next()));
            }
        }
		 finally
	        {
	            // close the TableRowIterator to free up resources
	            if (tri != null)
	            {
	                tri.close();
	            }
	        }

		DmsMetadaField[] typeArray = new DmsMetadaField[list.size()];
	        return (DmsMetadaField[]) list.toArray(typeArray);
	}
	
	public static DmsMetadaField[] getFieldNameByCollection(Context context,int id) throws SQLException
	{
		List<DmsMetadaField> list=new ArrayList<DmsMetadaField>();
		
		TableRowIterator tri=DatabaseManager.queryTable(context, "dmsfields","SELECT dmsfields.* FROM dmsfields, dmsfields2collection WHERE dmsfields2collection.fieldid=dmsfields.dmsfields_id AND dmsfields2collection.collectionid=?",id);
		try {
			while (tri.hasNext())
            {
                list.add(new DmsMetadaField(tri.next()));
            }
		} finally{
			// close the TableRowIterator to free up resources
            if (tri != null)
            {
                tri.close();
            }
		}
		DmsMetadaField[] typeArray = new DmsMetadaField[list.size()];
	        return (DmsMetadaField[]) list.toArray(typeArray);
	}
	
	public static DmsMetadaField[] getdatabyFieldName(Context context,String field_name) throws SQLException
	{
		List<DmsMetadaField> list=new ArrayList<DmsMetadaField>();
		
		TableRowIterator tri=DatabaseManager.queryTable(context,"dmsfields","SELECT * FROM dmsfields WHERE field_name ilike ?", field_name+"%");
		try {
			while (tri.hasNext())
            {
                list.add(new DmsMetadaField(tri.next()));
            }
		} finally{
			// close the TableRowIterator to free up resources
            if (tri != null)
            {
                tri.close();
            }
		}
		DmsMetadaField[] typeArray = new DmsMetadaField[list.size()];
	        return (DmsMetadaField[]) list.toArray(typeArray);
	
		
	}
	
	
	public static DmsMetadaField[] getAllSearchFields(Context context,int id) throws SQLException
	{
		List<DmsMetadaField> list=new ArrayList<DmsMetadaField>();
		
		TableRowIterator tri=DatabaseManager.queryTable(context, "dmsfields","SELECT dmsfields.* FROM dmsfields WHERE searchstatus=?",id);
		try {
			while (tri.hasNext())
            {
                list.add(new DmsMetadaField(tri.next()));
            }
		} finally{
			// close the TableRowIterator to free up resources
            if (tri != null)
            {
                tri.close();
            }
		}
		DmsMetadaField[] typeArray = new DmsMetadaField[list.size()];
	        return (DmsMetadaField[]) list.toArray(typeArray);
	}
	 public static void updateSearch(Context ourContext,String fieldname) throws SQLException
	    {
		 int a=0;
		 	PreparedStatement ps1=null;
		 
		 	try {
		 		ps1=ourContext.getDBConnection().prepareStatement("update dmsfields set searchstatus =1 where field_name ilike ?");
		 		ps1.setString(1,fieldname+"%");
		 		 a =ps1.executeUpdate();
		 	} catch (Exception e) {
		 		// TODO: handle exception
		 		log.info("Error in Update Search"+e);
		 	}
		 	 finally {
		 			if (ps1 != null) {
		 				try {
		 					ps1.close();
		 				} catch (SQLException s) {
		 					log.error("SQL QueryTable close Error - ", s);
		 					throw s;
		 				}
		 			}
		 			
		 	 }
	        
	    }
	 
	 public static boolean checkelement(Context context,String fieldname) throws SQLException
		{
			PreparedStatement ps=null;
			ResultSet rs=null;
			boolean ispresent=false;
			try {
				ps=context.getDBConnection().prepareStatement("SELECT element FROM metadatafieldregistry WHERE element ilike ?");
				ps.setString(1,fieldname+"%");
				rs=ps.executeQuery();
					if(rs.next())
					{
						ispresent=true;
					}

			} catch (Exception e) {
				log.info("Error in check Fiel Name data"+e);
			}
			
			 finally {
					if (ps != null) {
						try {
							ps.close();
						} catch (SQLException s) {
							log.error("SQL QueryTable close Error - ", s);
							throw s;
						}
					}
					if (rs != null) {
						try {
							rs.close();
						} catch (SQLException s) {
							log.error("SQL QueryTable close Error - ", s);
							throw s;
						}
					}
				}
			log.info("is present==========>"+ispresent);
		return ispresent;
			
		}

	
	/**
	  * Select all Data from Datatypes table
	  * 
	  * */
	 /*public static DmsMetadaField[] getDropDownValue(Context context,String fieldname) throws SQLException{
			List<DmsMetadaField> list=new ArrayList<DmsMetadaField>();
			
			TableRowIterator tri=DatabaseManager.queryTable(context,"datatypes","select * from datatypes where field_name ilike ?",fieldname);
			try {
				while (tri.hasNext())
	            {
	                list.add(new DmsMetadaField(tri.next()));
	            }
			} finally{
				// close the TableRowIterator to free up resources
	            if (tri != null)
	            {
	                tri.close();
	            }
			}
			DmsMetadaField[] typeArray = new DmsMetadaField[list.size()];
		        return (DmsMetadaField[]) list.toArray(typeArray);
		}*/
}
