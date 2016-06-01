<%@page import="org.dspace.content.Community"%>
<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>
<%@ page import="org.dspace.core.ConfigurationManager" %>
<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>
<%@ page import="java.lang.String" %>
<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>
<%
boolean createfolder=(request.getAttribute("folder.create")!=null);
boolean createcabinet=(request.getAttribute("cabinet.create")!=null);
boolean createsubfolder=(request.getAttribute("subfolder.create")!=null);
%>
<dspace:layout style="submission" titlekey="jsp.tools.edit-community.title"
		       navbar="admin"
		       locbar="link"
		       parentlink="/dspace-admin"
		       parenttitlekey="jsp.administer" nocache="true">
			 <section class="content">
          <div class="row">
           		 <!-- left column -->
            		<div class="col-md-12">
              		<!-- general form elements -->
              				<div class="box box-primary">
                					<div class="box-header with-border">
             						<%if(createcabinet){ %>
					<div class="alert alert-success"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
						Cabinet Create Successfully.
					</div>
					<%} %>	
					<%if(createfolder){ %>
					<div class="alert alert-success"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
						Folder Create Successfully.
					</div>
					<%} %>	 
					<%if(createsubfolder){ %>
					<div class="alert alert-success"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
						Sub Folder Create Successfully.
					</div>
					<%} %>	     
                					</div>
    
                </div>
                </div>
                </div>
                </section>
</dspace:layout>
