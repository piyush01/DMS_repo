<%--
  - Form requesting a Handle or internal item ID for item editing
  -
  - Attributes:
  -     invalid.id  - if this attribute is present, display error msg
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>
<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>

<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>

<%@ page import="org.dspace.core.ConfigurationManager" %>

<dspace:layout style="submission" titlekey="jsp.tools.get-item-id.title"
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
					<div class="box-tools pull-right">
					<button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
					<button class="btn btn-box-tool" data-widget="remove"><i class="fa fa-remove"></i></button>
					</div>
					<h5>
					<fmt:message key="jsp.tools.get-item-id.heading"/>
						<dspace:popup page="<%= LocaleSupport.getLocalizedMessage(pageContext, \"help.site-admin\") + \"#items\"%>"><fmt:message key="jsp.morehelp"/></dspace:popup>						
					</h5>
					<p class="help-block"><fmt:message key="jsp.dspace-admin.supervise-list.subheading"/></p>
					<%
						if (request.getAttribute("invalid.id") != null) { %>					
						<p class="alert alert-warning"><fmt:message key="jsp.tools.get-item-id.info1">
							<fmt:param><%= request.getContextPath() %>/dspace-admin/edit-communities</fmt:param>
						</fmt:message></p>
					<%  } %>

						<div><fmt:message key="jsp.tools.get-item-id.info2"/></div>
					</div>
					    
					<form class="form-horizontal" method="get" action="">
					<div class="box-body"> 
						<div class="form-group">
							<div class="col-sm-2">
								<label class="col-md-2" for="thandle"><fmt:message key="jsp.tools.get-item-id.handle"/></label> 
							</div>
								
							<div class="col-sm-5">	
								<input class="form-control" type="text" name="handle" id="thandle" value="<%= ConfigurationManager.getProperty("handle.prefix") %>/" size="12"/>
								</div>
							<div class="col-sm-5"></div>
				    	</div>	
						
						<div class="form-group">
							<div class="col-sm-2">
								<label class="col-md-2" for="thandle"><fmt:message key="jsp.tools.get-item-id.internal"/>
								</label>
							</div>
							<div class="col-sm-5">
							<input class="form-control" type="text" name="item_id" id="titem_id" size="12"/>
							</div>
							<div class="col-sm-5">
							</div>
						</div>
						<div class="form-group">
							<div class="col-sm-2">
							</div>
								<div class="col-sm-5">
								<input class="btn btn-success" type="submit" name="submit" value="<fmt:message key="jsp.tools.get-item-id.find.button"/>" />
							</div>
							<div class="col-sm-5">
							</div>
					 </div>
				</div>					 
        </form>
		</div>
		</div>
	</div>		
	</section>
</dspace:layout>
