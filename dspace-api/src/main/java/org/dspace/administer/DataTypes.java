package org.dspace.administer;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import org.dspace.core.Context;
import org.dspace.core.LogManager;
import org.dspace.storage.rdbms.DatabaseManager;
import org.dspace.storage.rdbms.TableRow;

public class DataTypes{
	
	private int ID=0;
	private int datatype_id;
	
	private String field_name; 
	
	private String datatype; 
	
	private String displayvalue; 
	
	private String storedvalue; 
	
	private static final Logger log = Logger.getLogger(DataTypes.class);
	
	
    private TableRow row;

    private static Map<Integer, DataTypes> id2field = null;

    private static Map<String, DataTypes> metadatafieldcache = null;

    /**
     * Default constructor.
     */
    public DataTypes()
    {
    }
    
    public DataTypes(String fieldname,String datatype, String displayvalue, String storedvalue)
    {
        this.field_name = fieldname;
        this.datatype = datatype;
        this.displayvalue = displayvalue;
        this.storedvalue=storedvalue;
    }
    public DataTypes(int id,String fieldname,String datatype, String displayvalue, String storedvalue)
    {
    	this.ID=id;
        this.field_name = fieldname;
        this.datatype = datatype;
        this.displayvalue = displayvalue;
        this.storedvalue=storedvalue;
    }
    public DataTypes(TableRow row)
    {
        if (row != null)
        {
        	 this.ID= row.getIntColumn("datatype_id");
            this.field_name = row.getStringColumn("field_name");
            this.datatype = row.getStringColumn("datatype");
            this.displayvalue = row.getStringColumn("displayvalue");
            this.storedvalue = row.getStringColumn("storedvalue");
            this.row = row;
        }
    }
    
    public int getID()
    {
        return ID;
    }
    public String getFieldName()
    {
        return field_name;
    }

    public void setFieldName(String field)
    {
        this.field_name = field;
    }
    public String getDataType()
    {
        return datatype;
    }

    public void setDataType(String datatype)
    {
        this.datatype = datatype;
    }
    
    public String getDispalyValue()
    {
        return displayvalue;
    }

    public void setDispalyValue(String displayvalue)
    {
        this.displayvalue = displayvalue;
    }
    
    public String getStoredValue()
    {
        return storedvalue;
    }

    public void setStoredValue(String stdvalue)
    {
        this.storedvalue = stdvalue.toLowerCase();
    }
    
    public void create(Context context) throws IOException, SQLException
    	{
			// Create a table row and update it with the values
			row = DatabaseManager.row("datatypes");
			row.setColumn("field_name", field_name);
			row.setColumn("datatype", datatype);
			row.setColumn("displayvalue", displayvalue);
			row.setColumn("storedvalue",storedvalue);
			DatabaseManager.insert(context, row);
			decache();

			// Remember the new row number
			this.ID = row.getIntColumn("datatype_id");
			
			log.info(LogManager.getHeader(context, "create_datatype_id",
			        "datatype_id=" + row.getIntColumn("datatype_id")));
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
    
    public void update(Context context) throws SQLException, IOException
	{
					
					row.setColumn("field_name",field_name);
					row.setColumn("datatype",datatype);
					row.setColumn("displayvalue",displayvalue);
					row.setColumn("storedvalue", storedvalue);
					DatabaseManager.update(context, row);
					decache();
					
					log.info(LogManager.getHeader(context, "update_datatypes",
					        "datatypes_id=" + getID() + "field_name=" + getFieldName()
					                + "datatype=" + getDataType()));
		}

}
