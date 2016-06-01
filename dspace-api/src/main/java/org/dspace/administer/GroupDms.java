package org.dspace.administer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.dspace.content.DmsMetadaField;
import org.dspace.core.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GroupDms {

	 private static final Logger log = LoggerFactory.getLogger(GroupDms.class);
	 
	 GroupBean groupbean=new GroupBean();

	public GroupBean getGroupbean() {
		return groupbean;
	}

	public void setGroupbean(GroupBean groupbean) {
		this.groupbean = groupbean;
	}
	//Get Group name
	 public String getGroupName(Context context,String name) throws SQLException{
		 String groupname="";
		 int group_id;
			PreparedStatement ps=null;
			ResultSet rs=null;
			
			try{
				ps=context.getDBConnection().prepareStatement("select e.eperson_group_id,m.text_value as name from epersongroup e left join  metadatavalue m on m.resource_id = e.eperson_group_id  and m.metadata_field_id =64 where m.text_value ilike ?");
				ps.setString(1, name+"%");
				rs=ps.executeQuery();
					if(rs.next()){
						group_id=rs.getInt(1);
						groupname=rs.getString(2);
						log.info("inside getGroupName method@==============================>"+groupname);
						}
					else
					{
						group_id=0;
						groupname=null;
					}
				
				}catch(SQLException e){
					log.info("Error in getting all Group name"+e);
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
			
			return groupname;
		}
	 
	 
}
