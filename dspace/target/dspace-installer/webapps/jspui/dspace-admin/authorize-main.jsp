
<%--
  - main page for authorization editing
  -
  - Attributes:
  -   none
  -
  - Returns:
  -   submit_community
  -   submit_collection
  -   submit_item
  -       item_handle
  -       item_id
  -   submit_advanced
  -
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>

<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>

<%@ page import="org.dspace.content.Collection" %>

<% request.setAttribute("LanguageSwitch", "hide"); %>

<%
// this space intentionally left blank
%>

<dspace:layout style="submission" titlekey="jsp.dspace-admin.authorize-main.title"
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
			 <h1 class="box-title"><fmt:message key="jsp.dspace-admin.authorize-main.adm"/>
					  <dspace:popup page="<%= LocaleSupport.getLocalizedMessage(pageContext, \"help.site-admin\") + \"#authorize\"%>"><fmt:message key="jsp.help"/></dspace:popup>
				</h1>	<br/>		  			
				 <h3 class="box-title"><fmt:message key="jsp.dspace-admin.authorize-main.choose"/></h3>		
				<div class="box-tools pull-right">
                <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                <button class="btn btn-box-tool" data-widget="remove"><i class="fa fa-remove"></i></button>
               </div>				 
                </div><!-- /.box-header -->                
				<!-- form start -->				
           <form class="form-horizontal" method="post" action="">    
		   <div class="box-body">
				 <div class="form-group">
					<div class="col-sm-2"> 
						</div>
						<div class="col-sm-5">
						
							<input class="btn btn-info col-md-12" type="submit" name="submit_community" value="<fmt:message key="jsp.dspace-admin.authorize-main.manage1"/>" />
							<input class="btn btn-info col-md-12" type="submit" name="submit_collection" value="<fmt:message key="jsp.dspace-admin.authorize-main.manage2"/>" />
							<input class="btn btn-info col-md-12" type="submit" name="submit_item" value="<fmt:message key="jsp.dspace-admin.authorize-main.manage3"/>" />
							<input class="btn btn-info col-md-12" type="submit" name="submit_advanced" value="<fmt:message key="jsp.dspace-admin.authorize-main.advanced"/>" />
							
						</div>
					<div class="col-sm-5"></div>
				</div>				
          </div> 
    </form>
	</div>
     </div>
 </div>
 </section> 
		  
</dspace:layout>
