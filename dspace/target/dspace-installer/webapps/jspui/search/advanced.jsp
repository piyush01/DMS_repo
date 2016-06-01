
<%--
  - Advanced Search JSP
  -
  -
  -
  --%>
  <%@page import="java.io.IOException"%>
<%@page import="java.io.FileNotFoundException"%>
<%@page import="java.util.Properties"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="org.dspace.content.DmsMetadaField"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page import="org.dspace.content.Community" %>
<%@ page import="org.dspace.search.QueryResults" %>
<%@ page import="java.util.*" %>
<%@ page import="org.dspace.core.ConfigurationManager" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>
<%!
private String removewhitespaces(String str)
	{
		str= str.replaceAll("\\s+","").trim();
		//str=str.replace("_","");
		return str;
	}
	
	public  String getPropValues(String xmlFilePath,String element ) throws IOException {
	String result = "";
	
	 FileInputStream inputStream=null;
	try {
		Properties prop = new Properties();
	inputStream=new FileInputStream(xmlFilePath);
		if (inputStream != null) {
			prop.load(inputStream);
		} else {
			throw new FileNotFoundException("property file '" + xmlFilePath + "' not found in the classpath");
		}

		String webui_itemdisplay_default = prop.getProperty("jsp.search.advanced.type."+element);

		result = webui_itemdisplay_default;
		//System.out.println(result + "\nProgram Ran on " + time + " by user=" + user);
	} catch (Exception e) {
		System.out.println("Exception: " + e);
	} finally {
		inputStream.close();
	}
	return result;
}
%>

<%
	String xmlFilePath="D:\\CBSL\\DMS_5.4\\config\\Messages.properties";
	DmsMetadaField searchfield[]=(DmsMetadaField[])request.getAttribute("searchfield");
    Community [] communityArray = (Community[] )request.getAttribute("communities");
	String query1 			= request.getParameter("query1") == null ? "" : request.getParameter("query1");
	String query2 			= request.getParameter("query2") == null ? "" : request.getParameter("query2");
	String query3 			= request.getParameter("query3") == null ? "" : request.getParameter("query3");

	String field1 			= request.getParameter("field1") == null ? "ANY" : request.getParameter("field1");
	String field2 			= request.getParameter("field2") == null ? "ANY" : request.getParameter("field2");
	String field3 			= request.getParameter("field3") == null ? "ANY" : request.getParameter("field3");

	String conjunction1 	= request.getParameter("conjunction1") == null ? "AND" : request.getParameter("conjunction1");
	String conjunction2 	= request.getParameter("conjunction2") == null ? "AND" : request.getParameter("conjunction2");

        QueryResults qResults = (QueryResults)request.getAttribute("queryresults");

	//Read the configuration to find out the search indices dynamically
	int idx = 1;
	String definition;
	ArrayList<String> searchIndices = new ArrayList<String>();
	//Map<String,String> searchIndex = new HashMap<String,String>();
	int dateIndex = -1;
	String dateIndexConfig = ConfigurationManager.getProperty("search.index.date");
	while ( ((definition = ConfigurationManager.getProperty("jspui.search.index.display." + idx))) != null){
		String index = definition;
		searchIndices.add(index);
		if (index.equals(dateIndexConfig))
			dateIndex = idx+1;
	    idx++;
	 } 
	for(int i=0;i<searchfield.length;i++)
	{
		//definition =searchfield[i].getFieldName();
		definition =removewhitespaces(searchfield[i].getFieldName());
		String index = definition;
		searchIndices.add(index);
		if (index.equals(dateIndexConfig))
			dateIndex = idx+1;
	}
	
	// backward compatibility
	if (searchIndices.size() == 0)
	{
	    searchIndices.add("ANY");
	    searchIndices.add("author");
        searchIndices.add("title");
        searchIndices.add("keyword");
        searchIndices.add("abstract");
        searchIndices.add("series");
        searchIndices.add("sponsor");
        searchIndices.add("identifier");
        searchIndices.add("language");
	}
	/* if(searchIndex.size()==0){
		searchIndex.put("title","Status");
		searchIndex.put("author","Applicant Name");
		searchIndex.put("subject","Building Type");
	} */
%>
		
<script type="text/javascript">
$(document).ready(function() {
	$('#searchbtn').click(function(e) {		
		var query1 = $('#tquery1').val();	
		var query2=$('#tquery2').val();
		
		if (query1.length==0) {
		  
		  alert("Please enter value in first value!");		  
					
			$('#tquery1').focus();
			return false;
		}		
		else if(query2.length==0)
		{
			alert("Please enter value in second value!");
			$('#tquery2').focus();
			return false;
		}		
		
     });
});

</script>
		
<dspace:layout locbar="nolink" titlekey="jsp.search.advanced.title"> 				

          <div class="row">
            <!-- left column -->
            <div class="col-md-12">
              <!-- general form elements -->
              <div class="box box-primary">
                <div class="box-header with-border">
                  <h3 class="box-title text-danger">
				<center> Note:-Please enter must be two field value!.</center></h3>
                </div><!-- /.box-header -->
                
				<!-- form start -->   
				<form class="form-horizontal" action="<%= request.getContextPath() %>/eZeeAdvance-search" method="get">			<input type="hidden" name="advanced" value="true"/>	
                  <div class="box-body">
				  
                    <div class="form-group">
						<div class="col-sm-2">
						</div>
					
						<label class="col-md-2">Select Cabinet</label> 
						  <div class="col-md-3">                
						  <select name="location" class="form-control" >
							<option selected="selected" value="/">All Cabinets</option>
								<%
										for (int i = 0; i < communityArray.length; i++)
										{
								%>
											<option value="<%= communityArray[i].getHandle() %>"><%= communityArray[i].getMetadata("name") %>
											</option>
								<%
										}
								%>
						</select>
						</div>
                    </div>
					
                    <div class="form-group">
					
					  <div class="col-sm-2">
						</div>
					  
                      <label class="col-md-3"><fmt:message key="jsp.search.advanced.type"/></label>
					  
					  <label for="exampleInputFile" class="col-sm-5"><fmt:message key="jsp.search.advanced.searchfor"/></label>
					  
					</div>
					
					<div class="form-group">
						<div class="col-sm-2">
						</div>
						
						<div class="col-md-3">
						<select name="field1" id="tfield1" class="form-control">	
						<option value="title">Created By</option>
						<option value="author">Modified By</option>
						<option value="allotmentdate">Date</option>
						
						<%for(int i=0;i<searchfield.length;i++){ %>
						<option value="<%=removewhitespaces(searchfield[i].getFieldName())%>"><%= searchfield[i].getFieldLevel()%></option>
						 <%} %>
						  </select>
						</div>
						
						<div class="col-sm-3">
							<input class="form-control" style="border-color: red;  type="text" name="query1" id="tquery1" value="<%=StringEscapeUtils.escapeHtml(query1)%>"  />
						</div>
					
					</div>

					<div class="form-group">
						
						<div class="col-sm-2">
							<select name="conjunction1" class="form-control" >
								<option value="AND" <%= conjunction1.equals("AND") ? "selected=\"selected\"" : "" %>> <fmt:message key="jsp.search.advanced.logical.and" /> 
								</option>
								<option value="OR" <%= conjunction1.equals("OR") ? "selected=\"selected\"" : "" %>> <fmt:message key="jsp.search.advanced.logical.or" />
								</option>
								<option value="NOT" <%= conjunction1.equals("NOT") ? "selected=\"selected\"" : "" %>> <fmt:message key="jsp.search.advanced.logical.not" />
								</option>
							 </select>
						</div>
						
						<div class="col-md-3">
							 <select name="field1" id="tfield1" class="form-control">		
							<option value="title">Created By</option>
							<option value="author">Modified By</option>
							<option value="allotmentdate">Date</option>							 
						<%for(int i=0;i<searchfield.length;i++){ %>
						<option value="<%=removewhitespaces(searchfield[i].getFieldName())%>"><%= searchfield[i].getFieldLevel() %></option>
						 <%} %>
						 
						  </select>
						</div>
						
						<div class="col-md-3">
							<input id="tquery2" class="form-control" style="border-color: red;  type="text" name="query2" value="<%=StringEscapeUtils.escapeHtml(query2)%>" />
						</div>
						
					</div>	
					
					<div class="form-group">
					  
					  <div class="col-sm-2">
						 <select name="conjunction2" class="form-control">
							<option value="AND" <%= conjunction2.equals("AND") ? "selected=\"selected\"" : "" %>> <fmt:message key="jsp.search.advanced.logical.and" /> </option>
							<option value="OR" <%= conjunction2.equals("OR") ? "selected=\"selected\"" : "" %>> <fmt:message key="jsp.search.advanced.logical.or" /> </option>
							<option value="NOT" <%= conjunction2.equals("NOT") ? "selected=\"selected\"" : "" %>> <fmt:message key="jsp.search.advanced.logical.not" /> </option>
						  </select>
					  </div>
					  
					  <div class="col-md-3">
							 <select name="field1" id="tfield1" class="form-control">							 
						<option value="title">Created By</option>
						<option value="author">Modified By</option>
						<option value="allotmentdate">Date</option>						
						<%for(int i=0;i<searchfield.length;i++){ %>
						<option value="<%=removewhitespaces(searchfield[i].getFieldName())%>"><%= searchfield[i].getFieldLevel() %></option>
						 <%} %>
						  </select>
						</div>
						<div class="col-md-3">
							<input  class="form-control" type="text" name="query3" value="<%=StringEscapeUtils.escapeHtml(query2)%>" />
						</div>
						
					</div>
					
					<div class="form-group">
						<div class="col-sm-12 text-right">
							<!--<a class="btn btn-success"  name="submit" ><i class="fa fa-plus"></i> Add Filter<a/>-->
							<input id="searchbtn" class="btn btn-primary" type="submit" name="submit" value="<fmt:message key="jsp.search.advanced.search2"/>" />
							<input class="btn btn-danger" type="reset" name="reset" value=" <fmt:message key="jsp.search.advanced.clear"/>" />	
						</div>
							
											
					</div>
						
					
                  </div><!-- /.box-body -->
				  </form>
               
              </div><!-- /.box -->
			  <% if( request.getParameter("query") != null)
{
			if( qResults.getErrorMsg()!=null )
			{
				String qError = "jsp.search.error." + qResults.getErrorMsg();
			%>
				<p align="center" class="text-danger" class="submitFormWarn"><fmt:message key="<%= qError %>"/></p>
			 <%
			}else
			{ %>
				<p class="text-danger" align="center"><strong><fmt:message key="jsp.search.general.noresults"/></strong></p>
			 <%
			 }
		}
		%>
		   </div>
		   
		  </div> 	
 </div> 	


</dspace:layout>
