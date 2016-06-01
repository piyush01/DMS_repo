
<%--
  - Form requesting a Handle or internal item ID for item editing
  -
  - Attributes:
  -     invalid.id  - if this attribute is present, display error msg
  --%>

<%@page import="org.dspace.content.ItemIterator"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>
<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>
<%@ page import="org.dspace.core.ConfigurationManager" %>
<%
ItemIterator item=(ItemIterator)request.getAttribute("item.iterator");
%>
<dspace:layout style="submission" titlekey="jsp.dspace-admin.item-select.title"
               navbar="admin"
               locbar="link"
               parenttitlekey="jsp.administer"
               parentlink="/dspace-admin">


  <section class="content">
          <div class="row">
            <!-- left column -->
            <div class="col-md-12">
              <!-- general form elements -->
              <div class="box box-primary">
                <div class="box-header with-border">
                  <h3 class="box-title">
				  Document Policy
				  <%--<fmt:message key="jsp.dspace-admin.item-select.heading"/>
					<dspace:popup page="<%= LocaleSupport.getLocalizedMessage(pageContext, \"help.site-admin\") + \"#itempolicies\"%>"><fmt:message key="jsp.morehelp"/></dspace:popup>--%>
					</h3>	
             
					<%
						if (request.getAttribute("invalid.id") != null) { %>
						<p class="alert alert-warning"><fmt:message key="jsp.dspace-admin.item-select.text">
							<fmt:param><%= request.getContextPath() %>/dspace-admin/edit-communities</fmt:param>
						</fmt:message></p>
					<%  } %>
					 <div><fmt:message key="jsp.dspace-admin.item-select.enter"/></div>
            </div><!-- /.box-header -->                
		 <!-- form start -->
		 
     
      
	   <form class="form-horizontal" method="post" action="">	
	   <div class="box-body">
	   <div class="form-group">
		<div class="col-sm-2"> 
		 <label>	
           <fmt:message key="jsp.dspace-admin.item-select.handle"/>
		   </label>            
		 </div>
		 <div class="col-sm-3"> 
           <input class="form-control" type="text" name="handle" id="thandle" value="<%= ConfigurationManager.getProperty("handle.prefix") %>/" size="12"/>		
		</div>
		 <div class="col-sm-3"> </div>
		 </div>
		 <div class="form-group">
		  <div class="col-sm-2">
			<label><fmt:message key="jsp.dspace-admin.item-select.id"/>
			</label>
		  </div>		  
             <div class="col-sm-3">
			 <input class="form-control" type="text" name="item_id" id="titem_id" size="12"/>				
 		    </div> 
       <div class="col-sm-3">	</div>	
      </div>	
	   <div class="form-group">
		  <div class="col-sm-2"></div>
		  <div class="col-sm-3">
		  <input class="btn btn-primary" type="submit" name="submit_item_select" value="<fmt:message key="jsp.dspace-admin.item-select.find"/>" />
		  <input class="btn btn-danger" type="reset" name="submit_collection_select_cancel" value="<fmt:message key="jsp.dspace-admin.general.cancel"/>" />
		  </div>
		   <div class="col-sm-3">	</div>	
		  </div>
		  </div>
    </form>
         </div>
		  </div>
		  </div>		
    </section>
</dspace:layout>
