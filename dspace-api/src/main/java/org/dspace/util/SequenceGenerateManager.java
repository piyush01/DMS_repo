package org.dspace.util;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.dspace.core.Context;
import org.dspace.workflowprocess.AdhocWorkflowManager;

public class SequenceGenerateManager implements Serializable
{
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 12132132L;
	private static Logger log = Logger.getLogger(SequenceGenerateManager.class);
	
	public static int getGeneratedId(Context context,String table) throws SQLException {
		int id = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = context.getDBConnection().prepareStatement(" SELECT nextval('"+table+"') ");
			rs = ps.executeQuery();
			if (rs.next()) // found
			{
				id = Integer.valueOf(rs.getString(1));
			}
		} catch (Exception ex) {
			System.out.println("Error in get Generate Id======>" + ex.getMessage());
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
		
		return id;
	}
}
