package org.dspace.administer;

import org.apache.log4j.Logger;
import org.dspace.content.DmsMetadaField;

public class DmsfieldBean {

	private int dmsfields_id; 
	  private int collection_id ;
	  private String field_name;
	  private String datatype; 
	  private String mandatory; 
	    
	    private static Logger log = Logger.getLogger(DmsfieldBean.class);

		public int getDmsfields_id() {
			return dmsfields_id;
		}

		public void setDmsfields_id(int dmsfields_id) {
			this.dmsfields_id = dmsfields_id;
		}

		public int getCollection_id() {
			return collection_id;
		}

		public void setCollection_id(int collection_id) {
			this.collection_id = collection_id;
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
	    
	    
	
}
