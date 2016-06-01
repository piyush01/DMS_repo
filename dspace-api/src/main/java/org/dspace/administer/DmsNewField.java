package org.dspace.administer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.dspace.content.MetadataField;
import org.dspace.core.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DmsNewField {
	 private static final Logger log = LoggerFactory.getLogger(DmsNewField.class);
	 
	 private int dmsfields_id; 
	  //private int collection_id ;
	  private String field_name;
	  private String datatype; 
	  private String mandatory; 
	  
	  private int datatype_id;
		private String displayvalue;
		private String storedvalue;
		
		
	  public int getDatatype_id() {
			return datatype_id;
		}

		public void setDatatype_id(int datatype_id) {
			this.datatype_id = datatype_id;
		}

		public String getDisplayvalue() {
			return displayvalue;
		}

		public void setDisplayvalue(String displayvalue) {
			this.displayvalue = displayvalue;
		}

		public String getStoredvalue() {
			return storedvalue;
		}

		public void setStoredvalue(String storedvalue) {
			this.storedvalue = storedvalue;
		}

	public int getDmsfields_id() {
			return dmsfields_id;
		}

		public void setDmsfields_id(int dmsfields_id) {
			this.dmsfields_id = dmsfields_id;
		}

		public String getField_name() {
			return field_name;
		}

		public void setField_name(String field_name) {
			this.field_name = field_name;
		}

		public String getDatatype() {
			return datatype;
		}

		public void setDatatype(String datatype) {
			this.datatype = datatype;
		}

		public String getMandatory() {
			return mandatory;
		}

		public void setMandatory(String mandatory) {
			this.mandatory = mandatory;
		}
	
/*	public static DmsNewField[] getAllFields(Context context) throws SQLException
	{
		List<DmsfieldBean> list=new ArrayList<DmsfieldBean>();
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps=context.getDBConnection().prepareStatement("SELECT * FROM dmsfields");
			rs=ps.executeQuery();
				while(rs.next()){
					DmsfieldBean dmsfieldbean=new DmsfieldBean();
					dmsfieldbean.setDmsfields_id(rs.getInt(1));
					//dmsfieldbean.setCollection_id(rs.getInt(2));
					dmsfieldbean.setField_name(rs.getString(2));
					dmsfieldbean.setDatatype(rs.getString(3));
					dmsfieldbean.setMandatory(rs.getString(4));
					list.add(dmsfieldbean);
					log.info("inside DmsNewField==============================>"+list.size());
					}
		} catch (Exception e) {
			log.info("Error in getting all data"+e);
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

		 DmsNewField[] typeArray = new DmsNewField[list.size()];
	        return (DmsNewField[]) list.toArray(typeArray);
	}
	
	
	public static DmsNewField[] getFieldNameByCollection(Context context,int id) throws SQLException
	{
		List<DmsfieldBean> list=new ArrayList<DmsfieldBean>();
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps=context.getDBConnection().prepareStatement("SELECT dmsfields.* FROM dmsfields, dmsfields2collection WHERE dmsfields2collection.fieldid=dmsfields.dmsfields_id AND dmsfields2collection.collectionid=?");
			ps.setInt(1, id);
			rs=ps.executeQuery();
				while(rs.next()){
					DmsfieldBean dmsfieldbean=new DmsfieldBean();
					dmsfieldbean.setDmsfields_id(rs.getInt(1));
					//dmsfieldbean.setCollection_id(rs.getInt(2));
					dmsfieldbean.setField_name(rs.getString(2));
					dmsfieldbean.setDatatype(rs.getString(3));
					dmsfieldbean.setMandatory(rs.getString(4));
					list.add(dmsfieldbean);
					log.info("inside DmsNewField from dmsfields2collection==============================>"+list.size());
					}
		} catch (Exception e) {
			log.info("Error in getting all data"+e);
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
		 DmsNewField[] typeArray = new DmsNewField[list.size()];
	        return (DmsNewField[]) list.toArray(typeArray);
	}
	
	public static DmsNewField[] getdatabyFieldName(Context context,String field_name) throws SQLException
	{
		List<DmsfieldBean> list=new ArrayList<DmsfieldBean>();
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps=context.getDBConnection().prepareStatement("SELECT * FROM dmsfields WHERE field_name ilike ?");
			ps.setString(1, field_name+"%");
			rs=ps.executeQuery();
				while(rs.next()){
					DmsfieldBean dmsfieldbean=new DmsfieldBean();
					dmsfieldbean.setDmsfields_id(rs.getInt(1));
					//dmsfieldbean.setCollection_id(rs.getInt(2));
					dmsfieldbean.setField_name(rs.getString(2));
					dmsfieldbean.setDatatype(rs.getString(3));
					dmsfieldbean.setMandatory(rs.getString(4));
					list.add(dmsfieldbean);
					log.info("inside DmsNewField Method getdatabyFieldName ==============================>"+list.size());
					}
					
					
				
		} catch (Exception e) {
			log.info("Error in getting all data"+e);
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
		 DmsNewField[] typeArray = new DmsNewField[list.size()];
	        return (DmsNewField[]) list.toArray(typeArray);
	
		
	}
	*/

	public boolean checkFieldName(Context context,String fieldname) throws SQLException
	{
		PreparedStatement ps=null;
		ResultSet rs=null;
		boolean ispresent=false;
		try {
			ps=context.getDBConnection().prepareStatement("SELECT field_name FROM dmsfields WHERE field_name ilike ?");
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
	

	/*	*//**
	  * Select all Data from Datatypes table
	  * 
	  * */
	 public List<DataTypesBean> getDropDownValue(Context context,String fieldname) throws SQLException{
			List<DataTypesBean> list=new ArrayList<DataTypesBean>();
			PreparedStatement ps=null;
			ResultSet rs=null;
			try{
				ps=context.getDBConnection().prepareStatement("select * from datatypes where field_name ilike ?");
				ps.setString(1,fieldname+"%");
				rs=ps.executeQuery();
					while(rs.next()){
						DataTypesBean data=new DataTypesBean();
						data.setDatatype_id(rs.getInt(1));
						data.setField_name(rs.getString(2));
						data.setDatatype(rs.getString(3));
						data.setDisplayvalue(rs.getString(4));
						data.setStoredvalue(rs.getString(5));
						list.add(data);
						log.info("inside DataTypes==============================>"+list.size());
						}
				}catch(SQLException e){
					log.info("Error in getting all DataTypes data"+e);
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
			return list;
			
			/* DmsNewField[] typeArray = new DmsNewField[list.size()];
		        return (DmsNewField[]) list.toArray(typeArray);*/
		}
	 //@update status.........
	/* public int updateStatus(Context context,int collectionid,String fieldname)throws SQLException
	 {
		 int a=0;
	 	PreparedStatement ps1=null;
	 
	 	try {
	 		ps1=context.getDBConnection().prepareStatement("update dmsfields set collection_id=? where field_name=?");
	 		ps1.setInt(1, collectionid);
	 		ps1.setString(2,fieldname);
	 		 a =ps1.executeUpdate();
	 		 log.info("a value======>"+a);
	 	} catch (Exception e) {
	 		// TODO: handle exception
	 		log.info("Error in Update data"+e);
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
		return a;

	 }
	 public List<DmsfieldBean> getBrowsedatabycollectionId(Context context,int id) throws SQLException
		{
			List<DmsfieldBean> list=new ArrayList<DmsfieldBean>();
			PreparedStatement ps=null;
			ResultSet rs=null;
			try {
				ps=context.getDBConnection().prepareStatement("SELECT * FROM MetadataFieldRegistry WHERE browsestatus='n' and collection_id=?");
				ps.setInt(1, id);
				rs=ps.executeQuery();
					while(rs.next()){
						DmsfieldBean dmsfieldbean=new DmsfieldBean();
						dmsfieldbean.setDmsfields_id(rs.getInt(1));
						dmsfieldbean.setCollection_id(rs.getInt(2));
						dmsfieldbean.setField_name(rs.getString(3));
						dmsfieldbean.setDatatype(rs.getString(4));
						dmsfieldbean.setMandatory(rs.getString(5));
						list.add(dmsfieldbean);
						log.info("inside DmsNewField==============================>"+list.size());
						}
			} catch (Exception e) {
				log.info("Error in getting all data"+e);
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
			return list;
		}
		public List<DmsfieldBean> getSearchdatabycollectionId(Context context,int id) throws SQLException
		{
			List<DmsfieldBean> list=new ArrayList<DmsfieldBean>();
			PreparedStatement ps=null;
			ResultSet rs=null;
			try {
				ps=context.getDBConnection().prepareStatement("SELECT * FROM MetadataFieldRegistry WHERE searchstatus='n' and collection_id=?");
				ps.setInt(1, id);
				rs=ps.executeQuery();
					while(rs.next()){
						DmsfieldBean dmsfieldbean=new DmsfieldBean();
						dmsfieldbean.setDmsfields_id(rs.getInt(1));
						dmsfieldbean.setCollection_id(rs.getInt(2));
						dmsfieldbean.setField_name(rs.getString(3));
						dmsfieldbean.setDatatype(rs.getString(4));
						dmsfieldbean.setMandatory(rs.getString(5));
						list.add(dmsfieldbean);
						log.info("inside DmsNewField==============================>"+list.size());
						}
			} catch (Exception e) {
				log.info("Error in getting all data"+e);
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
			return list;
		}*/
}
