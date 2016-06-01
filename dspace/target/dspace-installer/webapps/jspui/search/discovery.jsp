<%--
  - Display the form to refine the simple-search and dispaly the results of the search
  -
  - Attributes to pass in:
  -
  -   scope            - pass in if the scope of the search was a community
  -                      or a collection
  -   scopes 		   - the list of available scopes where limit the search
  -   sortOptions	   - the list of available sort options
  -   availableFilters - the list of filters available to the user
  -
  -   query            - The original query
  -   queryArgs		   - The query configuration parameters (rpp, sort, etc.)
  -   appliedFilters   - The list of applied filters (user input or facet)
  -
  -   search.error     - a flag to say that an error has occurred
  -   spellcheck	   - the suggested spell check query (if any)
  -   qResults		   - the discovery results
  -   items            - the results.  An array of Items, most relevant first
  -   communities      - results, Community[]
  -   collections      - results, Collection[]
  -
  -   admin_button     - If the user is an admin
  --%>
<%@page import="java.io.IOException"%>
<%@page import="java.io.FileNotFoundException"%>
<%@page import="java.util.Properties"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="org.dspace.content.DmsMetadaField"%>
<%@page import="org.dspace.core.Utils"%>
<%@page import="com.coverity.security.Escape"%>
<%@page import="org.dspace.discovery.configuration.DiscoverySearchFilterFacet"%>
<%@page import="org.dspace.app.webui.util.UIUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.dspace.discovery.DiscoverFacetField"%>
<%@page import="org.dspace.discovery.configuration.DiscoverySearchFilter"%>
<%@page import="org.dspace.discovery.DiscoverFilterQuery"%>
<%@page import="org.dspace.discovery.DiscoverQuery"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="java.util.Map"%>
<%@page import="org.dspace.discovery.DiscoverResult.FacetResult"%>
<%@page import="org.dspace.discovery.DiscoverResult"%>
<%@page import="org.dspace.content.DSpaceObject"%>
<%@page import="java.util.List"%>
<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"
    prefix="c" %>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>
<%@ page import="java.net.URLEncoder"            %>
<%@ page import="org.dspace.content.Community"   %>
<%@ page import="org.dspace.content.Collection"  %>
<%@ page import="org.dspace.content.Item"        %>
<%@ page import="org.dspace.search.QueryResults" %>
<%@ page import="org.dspace.sort.SortOption" %>
<%@ page import="java.util.Enumeration" %>
<%@ page import="java.util.Set" %>

<%!
public  String getPropValues(String xmlFilePath,String element ) throws IOException {
	String result = "";
	
	 FileInputStream inputStream=null;
	try {
		Properties prop = new Properties();
		//String propFileName = "config.properties";

		//inputStream = getClass().getClassLoader().getResourceAsStream(xmlFilePath);
	inputStream=new FileInputStream(xmlFilePath);
		if (inputStream != null) {
			prop.load(inputStream);
		} else {
			throw new FileNotFoundException("property file '" + xmlFilePath + "' not found in the classpath");
		}

		String webui_itemdisplay_default = prop.getProperty("jsp.search.filter."+element);

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
    // Get the attributes
	String xmlFilePath="D:\\CBSL\\DMS_5.4\\config\\Messages.properties";
    DmsMetadaField searchfield[]=(DmsMetadaField[])request.getAttribute("searchfield");
    DSpaceObject scope = (DSpaceObject) request.getAttribute("scope" );
	String folderscope=(String)request.getAttribute("folderscope");
	String subfoldescope=(String)request.getAttribute("subfoldescope");
	
    String searchScope = scope!=null?scope.getHandle():"";
    List<DSpaceObject> scopes = (List<DSpaceObject>) request.getAttribute("scopes");
	 List<DSpaceObject> folderscopes = (List<DSpaceObject>) request.getAttribute("folderscopes");
	  List<DSpaceObject> subfolderscopes = (List<DSpaceObject>) request.getAttribute("subfolderscopes");
	
    List<String> sortOptions = (List<String>) request.getAttribute("sortOptions");
    String searchType=(String)request.getParameter("type");
    String query = (String) request.getAttribute("query");
	if (query == null)
	{
	    query = "";
	}
    Boolean error_b = (Boolean)request.getAttribute("search.error");
    boolean error = (error_b == null ? false : error_b.booleanValue());
    
    DiscoverQuery qArgs = (DiscoverQuery) request.getAttribute("queryArgs");
    String sortedBy = qArgs.getSortField();
    String order = qArgs.getSortOrder().toString();
    String ascSelected = (SortOption.ASCENDING.equalsIgnoreCase(order)   ? "selected=\"selected\"" : "");
    String descSelected = (SortOption.DESCENDING.equalsIgnoreCase(order) ? "selected=\"selected\"" : "");
    String httpFilters ="";
	String spellCheckQuery = (String) request.getAttribute("spellcheck");
    List<DiscoverySearchFilter> availableFilters = (List<DiscoverySearchFilter>) request.getAttribute("availableFilters");
	List<String[]> appliedFilters = (List<String[]>) request.getAttribute("appliedFilters");
	List<String> appliedFilterQueries = (List<String>) request.getAttribute("appliedFilterQueries");
	if (appliedFilters != null && appliedFilters.size() >0 ) 
	{
	    int idx = 1;
	    for (String[] filter : appliedFilters)
	    {
	        httpFilters += "&amp;filter_field_"+idx+"="+URLEncoder.encode(filter[0],"UTF-8");
	        httpFilters += "&amp;filter_type_"+idx+"="+URLEncoder.encode(filter[1],"UTF-8");
	        httpFilters += "&amp;filter_value_"+idx+"="+URLEncoder.encode(filter[2],"UTF-8");
	        idx++;
	    }
	}
    int rpp          = qArgs.getMaxResults();
    int etAl         = ((Integer) request.getAttribute("etal")).intValue();

    String[] options = new String[]{"equals","contains","notequals","notcontains"};
    String[] booleanoptions = new String[]{"and","or","not"};
    // Admin user or not
    Boolean admin_b = (Boolean)request.getAttribute("admin_button");
    boolean admin_button = (admin_b == null ? false : admin_b.booleanValue());
%>
<%!
private String removewhitespaces(String str)
	{
		str= str.replaceAll("\\s+","").trim();
		//str=str.replace("_","");
		return str;
	}
%>
<c:set var="dspace.layout.head.last" scope="request">
<script type="text/javascript">
	var jQ = jQuery.noConflict();
	jQ(document).ready(function() {
		jQ( "#spellCheckQuery").click(function(){
			jQ("#query").val(jQ(this).attr('data-spell'));
			jQ("#main-query-submit").click();
		});
		jQ( "#filterquery" )
			.autocomplete({
				source: function( request, response ) {
					jQ.ajax({
						url: "<%= request.getContextPath() %>/json/discovery/autocomplete?query=<%= URLEncoder.encode(query,"UTF-8")%><%= httpFilters.replaceAll("&amp;","&") %>",
						dataType: "json",
						cache: false,
						data: {
							auto_idx: jQ("#filtername").val(),
							auto_query: request.term,
							auto_sort: 'count',
							auto_type: jQ("#filtertype").val(),
							location: '<%= searchScope %>'	
						},
						success: function( data ) {
							response( jQ.map( data.autocomplete, function( item ) {
								var tmp_val = item.authorityKey;
								if (tmp_val == null || tmp_val == '')
								{
									tmp_val = item.displayedValue;
								}
								return {
									label: item.displayedValue + " (" + item.count + ")",
									value: tmp_val
								};
							}))			
						}
					})
				}
			});
	});
	
	function validateFilters()
	{
	  return document.getElementById("filterquery").value.length > 0;
	}	
	</script>		
</c:set>
<style type="text/css">
    .bs-example{
    	margin: 20px;
    }
    .icon-input-btn{
        display: inline-block;
        position: relative;
    }
    .icon-input-btn input[type="submit"]{
        padding-left: 2em;
    }
    .icon-input-btn .glyphicon{
        display: inline-block;
        position: absolute;
        left: 0.65em;
        top: 30%;
    }
</style>
<script type="text/javascript">
$(document).ready(function(){
	$(".icon-input-btn").each(function(){
        var btnFont = $(this).find(".btn").css("font-size");
        var btnColor = $(this).find(".btn").css("color");
		$(this).find(".glyphicon").css("font-size", btnFont);
        $(this).find(".glyphicon").css("color", btnColor);
        if($(this).find(".btn-xs").length){
            $(this).find(".glyphicon").css("top", "24%");
        }
	}); 
});
</script>
<dspace:layout titlekey="jsp.search.title">

 
          <div class="row">
            <!-- left column -->
            <div class="col-md-12">
              <!-- general form elements -->
              <div class="box box-primary">
                <div class="box-header with-border">
                  <h2 class="box-title"><fmt:message key="jsp.search.title"/></h2>
                </div><!-- /.box-header -->                           
                  <div class="box-body">
				  <!-- form start -->  
				  <form action="simple-search" method="get" class="form-horizontal">
                    <div class="form-group">
					
					<label class="col-sm-2">Cabinet <fmt:message key="jsp.search.results.searchin"/></label>
					
					<div class="col-sm-5">
						<select name="location" id="tlocation" class="form-control">
						<%
							if (scope == null)
							{
								// Scope of the search was all of DSpace.  The scope control will list
								// "all of DSpace" and the communities.
						%>										
						<option selected="selected" value="/">All Cabinets</option>
						
						<%  }
							else
							{
						%>			
					 <option selected="selected" value="/">All Cabinets</option>					
						<%  }      
							for (DSpaceObject dso : scopes)
							{
						%>
						<option value="<%= dso.getHandle() %>" <%=dso.getHandle().equals(searchScope)?"selected=\"selected\"":"" %>>
							<%= dso.getName() %></option>
						<%
							}
							
						%>
						
						<%
						if(!folderscope.equals("") && folderscope.equals("folder"))
							{
						%>
						 <option selected="selected" value="/">--Select Folders--</option>
						<%
						}
						for (DSpaceObject dso : folderscopes)
							{
						%>
						<option value="<%= dso.getHandle() %>" <%=dso.getHandle().equals(searchScope)?"selected=\"selected\"":"" %>>
							<%= dso.getName() %></option>
						<%
							}
							
						%>
						
						<%
						if(!subfoldescope.equals("") && subfoldescope.equals("subfolder"))
							{
						%>
						 <option selected="selected" value="/">--Select Sub Folders--</option>
						<%
						}
						for (DSpaceObject dso : subfolderscopes)
							{
						%>
						<option value="<%= dso.getHandle() %>" <%=dso.getHandle().equals(searchScope)?"selected=\"selected\"":"" %>>
							<%= dso.getName() %></option>
						<%
							}
							
						%>
						
						
						</select>
					</div>	
                  </div>
				  
				<div class="form-group">
                  <label for="query" class="col-sm-2"><fmt:message key="jsp.search.results.searchfor"/>:</label>
                  
				  <div class="col-sm-5">	
					<input type="text" size="50" id="query" name="query" class="form-control"/>
                             
				<% if (StringUtils.isNotBlank(spellCheckQuery)) {%>
					<p class="lead"><fmt:message key="jsp.search.didyoumean"><fmt:param><a id="spellCheckQuery" data-spell="<%= Utils.addEntities(spellCheckQuery) %>" href="#"><%= spellCheckQuery %></a></fmt:param></fmt:message></p>
				<% } %>                  
                             <input type="hidden" value="<%= rpp %>" name="rpp" />
                                <input type="hidden" value="<%= Utils.addEntities(sortedBy) %>" name="sort_by" />
                                <input type="hidden" value="<%= Utils.addEntities(order) %>" name="order" />
				</div>				
								
								
						<% if (appliedFilters.size() > 0 ) { %> 
							<label class="col-sm-12"><fmt:message key="jsp.search.filter.applied" /></label>
								<%
									int idx = 1;
									for (String[] filter : appliedFilters)
									{
										boolean found = false;
										%>
										<div class="col-sm-3">
										<select id="filter_field_<%=idx %>" name="filter_field_<%=idx %>" class="form-control">
										<%
											for (DiscoverySearchFilter searchFilter : availableFilters)
											{
												//String fkey = "jsp.search.filter." + Escape.uriParam(searchFilter.getIndexFieldName());
												String fkey = getPropValues(xmlFilePath, searchFilter.getIndexFieldName());
												%>
												<option value="<%= Utils.addEntities(searchFilter.getIndexFieldName()) %>"
												<% 
														if (filter[0].equals(searchFilter.getIndexFieldName()))
														{
															%> selected="selected"<%
															found = true;
														}
														%>><%= fkey %></option><%
											}
										
											if (!found)
											{
												//String fkey = "jsp.search.filter." + Escape.uriParam(filter[0]);
												String fkey = getPropValues(xmlFilePath,Escape.uriParam(filter[0]));
												%>
												<option value="<%= Utils.addEntities(filter[0]) %>" selected="selected"><%= fkey %></option>
										<%	}
										%>
										</select>
										</div>
										
				<div class="col-sm-3">						
				<select id="filter_type_<%=idx %>" name="filter_type_<%=idx %>" class="form-control">
				
				<%
				  String val[];
				     if(searchType!=null && searchType.equals("boolean")) 
						{
						val= booleanoptions;
					 }else{
						 val= options;
					 }
					for (String opt : val)
					{
					    String fkey = "jsp.search.filter.op." + Escape.uriParam(opt);
					    %><option value="<%= Utils.addEntities(opt) %>"<%= opt.equals(filter[1])?" selected=\"selected\"":"" %>><fmt:message key="<%= fkey %>"/></option><%
					}
					
				%>
				
				</select>
				</div>
				
				<div class="col-sm-3">
				<input type="text" id="filter_value_<%=idx %>" name="filter_value_<%=idx %>" value="<%= Utils.addEntities(filter[2]) %>" size="45"/ class="form-control">
				</div>
				
				<div class="col-sm-3">
				<input class="btn btn-default" type="submit" id="submit_filter_remove_<%=idx %>" name="submit_filter_remove_<%=idx %>" value="Remove Filter" />			
				</div>
				<%
				idx++;
			}
		%>
		
<% } %>

		<input type="submit" id="main-query-submit" class="btn btn-primary" value="<fmt:message key="jsp.general.go"/>" />
		<a class="btn btn-default" href="<%= request.getContextPath()+"/simple-search" %>"><fmt:message key="jsp.search.general.new-search" /></a>	
			</div>
			
		</form>
		
<% if (availableFilters.size() > 0) { %>
	
	<form action="<%= request.getContextPath()+"/simple-search" %>" method="get" class="form-horizontal">
		<div class="form-group">		
			<label class="col-sm-12"><fmt:message key="jsp.search.filter.heading" /></label>
		</div>
		
		<div class="form-group">		
			<div class="col-sm-12">
				<p class="discovery-search-filters-hint"><fmt:message key="jsp.search.filter.hint" /></p>
			</div>
		</div>
		
		<div class="form-group">		
			<div class="col-sm-3">
				<input type="hidden" value="<%= Utils.addEntities(searchScope) %>" name="location" />
				<input type="hidden" value="<%= Utils.addEntities(query) %>" name="query" />
			<% if (appliedFilterQueries.size() > 0 ) { 
					int idx = 1;
					for (String[] filter : appliedFilters)
					{
						boolean found = false;
						%>
						<input type="hidden" id="filter_field_<%=idx %>" name="filter_field_<%=idx %>" value="<%= Utils.addEntities(filter[0]) %>" />
						<input type="hidden" id="filter_type_<%=idx %>" name="filter_type_<%=idx %>" value="<%= Utils.addEntities(filter[1]) %>" />
						<input type="hidden" id="filter_value_<%=idx %>" name="filter_value_<%=idx %>" value="<%= Utils.addEntities(filter[2]) %>" />
						<%
						idx++;
					}
			} %>
			<select id="filtername" name="filtername" class="form-control">
			
			<option value="title">Created By</option>
						<option value="author">Modified By</option>
						<option value="allotmentdate">Date</option>
			<% for(int i=0;i<searchfield.length;i++){ %>
						<option value="<%=removewhitespaces(searchfield[i].getFieldName())%>"><%= searchfield[i].getFieldLevel() %></option>
			<%} %>
			</select>
			</div>
			
			<div class="col-sm-3">
				<%  
				if(searchType!=null && searchType.equals("boolean")) 
				{
				%>
			</div>
		
		<div class="col-sm-3">
		
			<input type="hidden" value="<%=searchType%>" name="type"/>
			<select id="filtertype" name="filtertype" class="form-control">
				<%
					for (String opt : booleanoptions)
					{
						String fkey = "jsp.search.filter.op." + Escape.uriParam(opt);
						%><option value="<%= Utils.addEntities(opt) %>"><fmt:message key="<%= fkey %>"/></option><%
					}
				%>
			</select>
			<%}%>
			<% if(searchType==null){%>
				
			<select id="filtertype" name="filtertype" class="form-control">
				<%
					for (String opt :options)
					{
						String fkey = "jsp.search.filter.op." + Escape.uriParam(opt);
						%><option value="<%= Utils.addEntities(opt) %>"><fmt:message key="<%= fkey %>"/></option><%
					}
				%>
			</select>
		</div>	
		<%}%>
		
		<div class="col-sm-4">
			<input type="text" id="filterquery" name="filterquery" size="45" required="required" class="form-control" />
			<input type="hidden" value="<%= rpp %>" name="rpp" />
			<input type="hidden" value="<%= Utils.addEntities(sortedBy) %>" name="sort_by" />
			<input type="hidden" value="<%= Utils.addEntities(order) %>" name="order" />
		</div>

		<div class="col-sm-2">	
			<input class="btn btn-default" type="submit" value="<fmt:message key="jsp.search.filter.add"/>" onclick="return    validateFilters()" />
		</div>
		
		</div> 
	</form>		
<% } %>

        <%-- Include a component for modifying sort by, order, results per page, and et-al limit --%>
  
   <form action="<%= request.getContextPath()+"/simple-search" %>" method="get" class="form-horizontal">
   <div class="form-group">
   <div class="col-sm-3">
	   <input type="hidden" value="<%= Utils.addEntities(searchScope) %>" name="location" />
	   <input type="hidden" value="<%= Utils.addEntities(query) %>" name="query" />
		<% if (appliedFilterQueries.size() > 0 ) { 
					int idx = 1;
					for (String[] filter : appliedFilters)
					{
						boolean found = false;
						%>
						<input type="hidden" id="filter_field_<%=idx %>" name="filter_field_<%=idx %>" value="<%= Utils.addEntities(filter[0]) %>" />
						<input type="hidden" id="filter_type_<%=idx %>" name="filter_type_<%=idx %>" value="<%= Utils.addEntities(filter[1]) %>" />
						<input type="hidden" id="filter_value_<%=idx %>" name="filter_value_<%=idx %>" value="<%= Utils.addEntities(filter[2]) %>" />
						<%
						idx++;
					}
		} %>	
			   <label for="rpp"><fmt:message key="search.results.perpage"/></label>
			   <select name="rpp" class="form-control">
	<%
				   for (int i = 5; i <= 100 ; i += 5)
				   {
					   String selected = (i == rpp ? "selected=\"selected\"" : "");
	%>
					   <option value="<%= i %>" <%= selected %>><%= i %></option>
	<%
				   }
	%>
			   </select>
			   
	</div >
	
	<div class="col-sm-3">
<%
           if (sortOptions.size() > 0)
           {
%>
               <label for="sort_by"><fmt:message key="search.results.sort-by"/></label>
               <select name="sort_by" class="form-control">
                   <option value="score"><fmt:message key="search.sort-by.relevance"/></option>
<%
               for (String sortBy : sortOptions)
               {
                   String selected = (sortBy.equals(sortedBy) ? "selected=\"selected\"" : "");
                   String mKey = "search.sort-by." + Utils.addEntities(sortBy);
                   %> <option value="<%= Utils.addEntities(sortBy) %>" <%= selected %>><fmt:message key="<%= mKey %>"/></option><%
               }
%>
               </select>
		</div>
			
		<div class="col-sm-3">	
		<%
		  }
		%>
           <label for="order"><fmt:message key="search.results.order"/></label>
           <select name="order" class="form-control">
               <option value="ASC" <%= ascSelected %>><fmt:message key="search.order.asc" /></option>
               <option value="DESC" <%= descSelected %>><fmt:message key="search.order.desc" /></option>
           </select>
		 </div>		
		
		<div class="col-sm-12 text-right" style="margin-top:10px;">	
           <input class="btn btn-default" type="submit" name="submit_search" value="<fmt:message key="search.update" />" />
		</div>
	</div>
</form>
     
	
	</div>
	</div>
	
	
	
<% 

DiscoverResult qResults = (DiscoverResult)request.getAttribute("queryresults");
Item      [] items       = (Item[]      )request.getAttribute("items");
Community [] communities = (Community[] )request.getAttribute("communities");
Collection[] collections = (Collection[])request.getAttribute("collections");

if( error )
{
 %>
	<p align="center" class="submitFormWarn">
		<fmt:message key="jsp.search.error.discovery" />
	</p>
	<%
}
else if( qResults != null && qResults.getTotalSearchResults() == 0 )
{
 %>
    <%-- <p align="center">Search produced no results.</p> --%>
	     
		  <div class="row"> 
		   <div class="col-md-12">
				<div class="box box-primary">
					<div class="box-header with-border">
					</div>
					<div class="box-footer">
						<h4>
							<p align="center"><fmt:message key="jsp.search.general.noresults"/></p>
						</h4>
					</div>
				</div>
			</div>
		  </div>
	
<%
}
else if( qResults != null)
{
    long pageTotal   = ((Long)request.getAttribute("pagetotal"  )).longValue();
    long pageCurrent = ((Long)request.getAttribute("pagecurrent")).longValue();
    long pageLast    = ((Long)request.getAttribute("pagelast"   )).longValue();
    long pageFirst   = ((Long)request.getAttribute("pagefirst"  )).longValue();
    
    // create the URLs accessing the previous and next search result pages
    String baseURL =  request.getContextPath()
                    + (!searchScope.equals("") ? "/handle/" + searchScope : "")
                    + "/simple-search?query="
                    + URLEncoder.encode(query,"UTF-8")
                    + httpFilters
                    + "&amp;sort_by=" + sortedBy
                    + "&amp;order=" + order
                    + "&amp;rpp=" + rpp
                    + "&amp;etal=" + etAl
                    + "&amp;start=";

    String nextURL = baseURL;
    String firstURL = baseURL;
    String lastURL = baseURL;

    String prevURL = baseURL
            + (pageCurrent-2) * qResults.getMaxResults();

    nextURL = nextURL
            + (pageCurrent) * qResults.getMaxResults();
    
    firstURL = firstURL +"0";
    lastURL = lastURL + (pageTotal-1) * qResults.getMaxResults();


%>

	<%
		long lastHint = qResults.getStart()+qResults.getMaxResults() <= qResults.getTotalSearchResults()?
				qResults.getStart()+qResults.getMaxResults():qResults.getTotalSearchResults();
	%>
   
	
<% if (items.length > 0) { %>

          <div class="row">
            <!-- left column -->
            <div class="col-md-12">
              <!-- general form elements -->
              <div class="box box-primary">
                
				<div class="box-header with-border">
                  <h2 class="box-title"><fmt:message key="jsp.search.results.itemhits"/></h2>		  
				  <%
					if (admin_button)
					{
					 %>		
			<!--<input type="submit" class="btn btn-info  pull-right" name="submit_export_metadata" value="<fmt:message key="jsp.general.metadataexport.button"/>" />		-->
						<%
					}
				%>
				  
                </div><!-- /.box-header -->
                <!-- form start -->             
                  
				  <div class="box-body">
					  <div class="form-group">
					 <dspace:itemlist items="<%= items %>" authorLimit="<%= etAl %>" />
					 </div>
				 </div>
			  </div>
			</div>
		 </div>
	
<% } %>



<!-- give a content to the div -->
</div>

<% } %>
</section>  
</div>

</dspace:layout>
