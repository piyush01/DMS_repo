
<%--
  - eperson editor - for new or existing epeople
  -
  - Attributes:
  -   eperson - eperson to be edited
  -   email_exists - if non-null, user has attempted to enter a duplicate email
  -                  address, so an error should be displayed
  -
  - Returns:
  -   submit_save   - admin wants to save edits
  -   submit_delete - admin wants to delete edits
  -   submit_cancel - admin wants to cancel
  -
  -   eperson_id
  -   email
  -   firstname
  -   lastname
  -   phone
  -   language
  -   can_log_in          - (boolean)
  -   require_certificate - (boolean)
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>


<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.Locale"%>

<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>

<%@ page import="org.dspace.core.I18nUtil" %>
<%@ page import="org.dspace.eperson.EPerson, org.dspace.core.ConfigurationManager" %>
<%@ page import="org.dspace.eperson.Group"   %>
<%@ page import="org.dspace.core.Utils" %>
<link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/bootstrap/containerscroll.css" type="text/css" />

<%@ page import="org.dspace.core.Constants"           %>
<%@ page import="org.dspace.eperson.Group"            %>

<script type="text/javascript">
$(document).ready(function() {
	$('#update').click(function(e) {
		var userid_regex = /^[\w\-\.\+]+\@[a-zA-Z0-9\.\-]+\.[a-zA-z0-9]{2,4}$/;
		var letters = /^[0-9]+$/; 
		var userid = $('#temail').val();	
		var pass=$('#tpassword').val();
		var fname=$('#tfirstname').val();
		var lname=$('#tlastname').val();
		var mobile=$('#tphone').val();
		var designation=$('#tdesignation').val();
		var dept=$('#tgroup').val();
		var sname=$('#tsuperiorname').val();
		var sid=$('#tsuperioremail').val();
		if (userid.length==0) {
		 
			$('#error-alert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Please enter your email address.</div>');			
			$('#temail').focus();
			return false;
		}
		else if (!userid.match(userid_regex)) {
		 
			$('#error-alert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Email address should be as eg:someone@example.com.</div>');			
			$('#temail').focus();
			return false;
		}
		else if(pass.length==0)
		{
			$('#error-alert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Please Enter the Password.</div>');			
			$('#tpassword').focus();
			return false;
		}
		else if(pass.length<6)
		{
			$('#error-alert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Password must be of al least 6 characters.</div>');			
			$('#tpassword').focus();
			return false;
		}
		else if(fname.length==0)
		{
			$('#error-alert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Please Enter the First Name.</div>');			
			$('#tfirstname').focus();
			return false;
		}
		else if(fname.match(letters))
		{
			$('#error-alert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>First name should not be numeric character only.</div>');			
			$('#tfirstname').focus();
			return false;
		}
		else if(lname.match(letters))
		{
			$('#error-alert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Last name should not be numeric character only.</div>');			
			$('#tlastname').focus();
			return false;
		}
		else if(mobile.length==0)
		{
			$('#error-alert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Please Enter the mobile number.</div>');			
			$('#tphone').focus();
			return false;
		}
		else if(isNaN(mobile))
		{
			$('#error-alert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Mobile number should not contain alphabets.</div>');			
			$('#tphone').focus();
			return false;
		}
		else if(!(mobile.length==10))
		{
			$('#error-alert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Mobile number should contain 10 digits.</div>');			
			$('#tphone').focus();
			return false;
		}
		else if(designation.length==0)
		{
			$('#error-alert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Please Enter the user Designation.</div>');			
			$('#tdesignation').focus();
			return false;
		}
		else if(designation.match(letters))
		{
			$('#error-alert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>User Designation should not be numeric character only.</div>');			
			$('#tdesignation').focus();
			return false;
		}
		else if(sname.length==0)
		{
			
			$('#error-alert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Please Enter the Superior Name.</div>');			
			$('#tsuperiorname').focus();
			return false;
		}
		else if(sname.match(letters))
		{
			
			$('#error-alert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Superior name should not be numeric only.</div>');			
			$('#tsuperiorname').focus();
			return false;
		}
		else if(sid.length==0)
		{
			$('#error-alert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Please Enter the Superior email address.</div>');			
			$('#tsuperioremail').focus();
			return false;
		}
		else if(!sid.match(userid_regex))
		{
			$('#error-alert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Superior email address should be as eg:someone@example.com.</div>');			
			$('#tsuperioremail').focus();
			return false;
		}
     });
});

</script>

<%
    EPerson eperson = (EPerson) request.getAttribute("eperson");

	Group [] groupMemberships = null;
	if(request.getAttribute("group.memberships") != null)
	{
		groupMemberships = (Group []) request.getAttribute("group.memberships");
	}
	
    String email     = eperson.getEmail();
    String firstName = eperson.getFirstName();
    String lastName  = eperson.getLastName();
    String phone     = eperson.getMetadata("phone");
    String netid = eperson.getNetid();
    String password= eperson.getPassword();
	 String superioremail = eperson.getSuperiorEmail();
    String superiorname = eperson.getSuperiorName();
    String userdesignation=eperson.getUserDesignation();
   String language     = eperson.getMetadata("language"); 
    boolean emailExists = (request.getAttribute("email_exists") != null);

    boolean ldap_enabled = ConfigurationManager.getBooleanProperty("authentication-ldap", "enable");
    
    
    Group[] groups =(Group[]) request.getAttribute("groups");
/*     int sortBy = ((Integer)request.getAttribute("sortby" )).intValue();
    int first = ((Integer)request.getAttribute("first")).intValue();  */
%>
  	
<dspace:layout style="submission" titlekey="jsp.dspace-admin.eperson-edit.title"
               navbar="admin"
               locbar="link"
               parenttitlekey="jsp.administer"
               parentlink="/dspace-admin"
               nocache="true">
 
          <div class="row">
            <!-- left column -->
            <div class="col-md-12">
              <!-- general form elements -->
              <div class="box box-primary">
                <div class="box-header with-border">
				 <% if (emailExists)
						{ %>
					<div class="box-footer">
					<div class="alert alert-danger">
					<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a><fmt:message key="jsp.dspace-admin.eperson-edit.emailexists"/>
					</div>
					</div>
					<%  } %>
                  <h3 class="box-title">Please enter the following information. Required fields are marked with a (<span class="star">*</span>)
				  </h3>
				 <div id="error-alert"></div>  
                </div><!-- /.box-header -->
                <!-- form start -->
    
                  <form class="form-horizontal" method="post" action="" name="UserEdit" id="UserEdit">
           <div class="box-body">				
                  <div class="form-group">	
				  <div class="col-sm-2">						
                 <label for="exampleInputEmail1">
                  <fmt:message key="jsp.dspace-admin.eperson-edit.email"/><span class="star">*</span>
				</label>
				 </div>
				 <div class="col-sm-3">
			   <input type="hidden" name="eperson_id" value="<%=eperson.getID()%>"/>
			   <input class="form-control" type="text" name="email" maxlength="113" size="40" id="temail"  value="<%=email == null ? "" : Utils.addEntities(email)%>"/>
			    </div>
			   <div class="col-sm-3">
			    </div>
				</div>           
		   <div class="form-group">	
		    <div class="col-sm-2">						
             <label for="exampleInputEmail1">
             <fmt:message key="jsp.dspace-admin.eperson-edit.pass"/><span class="star">*</span>
           </div>
		    <div class="col-sm-3">
           <input class="form-control" type="password" name="password" id="tpassword" maxlength="113"  size="40" value="<%=password == null ? "" : Utils.addEntities(password) %>"/>
         </div>
       <div class="col-sm-3">
	   </div>
	   </div>
	   
	    <div class="form-group">	
		 <div class="col-sm-2">						
         <label for="exampleInputEmail1">
      <fmt:message key="jsp.dspace-admin.eperson.general.firstname"/><span class="star">*</span></td>
       </div>
	  <div class="col-sm-3">
	   <input class="form-control" type="text" name="firstname" id="tfirstname" maxlength="113" size="40" value="<%=firstName == null ? "" : Utils.addEntities(firstName) %>"/>
	   </div>
	   <div class="col-sm-3">
		</div>
		</div>
		
		<div class="form-group">	
		 <div class="col-sm-2">						
         <label for="exampleInputEmail1">
	   <fmt:message key="jsp.dspace-admin.eperson.general.lastname"/>
	   </div>
	    <div class="col-sm-3">
	  <input class="form-control" type="text" name="lastname" id="tlastname" maxlength="113" size="40" value="<%=lastName == null ? "" : Utils.addEntities(lastName) %>"/>
	  </div>
	 <div class="col-sm-3">
	 </div>
	</div>
		<% if (ldap_enabled) { %>
	<div class="form-group">	
		 <div class="col-sm-2">						
         <label for="exampleInputEmail1">
	    LDAP NetID:	</label>
	   </div>
	   <div class="col-sm-3">
	<input class="form-control" name="netid" maxlength="113" size="40" value="<%=netid == null ? "" : Utils.addEntities(netid) %>" />
	</div>
	<div class="col-sm-3">
	 </div>
	</div>
	  <% } %>
	<div class="form-group">	
		 <div class="col-sm-2">						
         <label for="exampleInputEmail1">
	      <fmt:message key="jsp.dspace-admin.eperson-edit.phone"/><span class="star">*</span>
	    </label>
		 </div>
	  <div class="col-sm-3">
	<input class="form-control"   name="phone" id="tphone" size="40" value="<%=phone == null ? "" : Utils.addEntities(phone) %>"/>
	 </div>
	<div class="col-sm-3">
	 </div>
	</div>
	<div class="form-group">	
		 <div class="col-sm-2">						
         <label for="exampleInputEmail1">
	 User Designation<span class="star">*</span></label>
	 </div>
	   <div class="col-sm-3">
	<input class="form-control"   name="designation" id="tdesignation" size="40" value="<%=userdesignation == null ? "" : Utils.addEntities(userdesignation) %>"/>
	 </div>
	<div class="col-sm-3">
	 </div>
	</div>
	<div class="form-group">	
		 <div class="col-sm-2">						
         <label for="exampleInputEmail1">
	     Select Department
		 </label>
	   </div>
	   <div class="col-sm-3">
	<div class="containerscroll" >
	  <table>
	        
           <tr><td>&nbsp;&nbsp;<b>--Select Department--</b></td></tr>                 						   
<%  
for(int i = 0; i < groups.length; i++ ) 
{ 			int x=0;
		for(int j=0; j<groupMemberships.length; j++)
     	{
			if(groupMemberships[j].getID()==groups[i].getID())
			{
				x=groupMemberships[j].getID();
%>
						<tr>
						<td>
                          &nbsp;&nbsp;<input  type="checkbox" name="group_id" value="<%=groupMemberships[j].getID()%>" checked/> &nbsp;<%=groupMemberships[j].getName()%> 
						  <td>
						  </tr>
	<%
		}
		}
		if(groups[i].getID()!=x)
		{
			
	%>
					<tr>
					<td>
                     &nbsp;&nbsp;<input  type="checkbox" name="group_id" value="<%=groups[i].getID() %>"/> &nbsp;<%=groups[i].getName()%>  
						  <td>
						  </tr>	
<%
}
}
%>

						  
		
		</table>			
			</div>
			 </div>
	<div class="col-sm-3">
	 </div>
	</div>
	
		<div class="form-group">	
		 <div class="col-sm-2">						
         <label for="exampleInputEmail1">
	Superior Name<span class="star">*</span></label>
	</div>
	<div class="col-sm-3">
	<input class="form-control" type="text" name="superiorename" id="tsuperiorname" value="<%=superiorname == null ? "" :Utils.addEntities(superiorname) %>"/>
	 </div>
	<div class="col-sm-3">
	 </div>
	</div>
	
	<div class="form-group">	
		 <div class="col-sm-2">						
         <label for="exampleInputEmail1">
	Superior Email ID<span class="star">*</span></label>
	 </div>
	 <div class="col-sm-3">
	<input class="form-control" type="text" name="superioremailid" id="tsuperioremail" value="<%=superioremail == null ? "" :Utils.addEntities(superioremail) %>"/></td>
	 </div>
	<div class="col-sm-3">
	<select class=""  name="language" id="tlanguage" style="display:none;">
			<%
				Locale[] supportedLocales = I18nUtil.getSupportedLocales();

				for (int i = supportedLocales.length-1; i >= 0; i--)
				{
					String lang = supportedLocales[i].toString();
					String selected = "";
					
					if (language == null || language.equals(""))
					{ if(lang.equals(I18nUtil.getSupportedLocale(request.getLocale()).getLanguage()))
						{
							selected = "selected=\"selected\"";
						}
					}
					else if (lang.equals(language))
					{ selected = "selected=\"selected\"";}
		%>
					 <option <%= selected %>
						value="<%= lang %>"><%= supportedLocales[i].getDisplayName(I18nUtil.getSupportedLocale(request.getLocale())) %></option>
		<%
				}
		%>
		</select>
		<input type="hidden" name="can_log_in" id="tcan_log_in" value="true"/>
	 </div>
	</div>
	
	<div class="form-group">	
		 <div class="col-sm-2">
		 </div>
		 <div class="col-sm-3">
     <c:choose>
    <c:when test="${userupdate=='update'}">
	<input class="btn btn-primary" type="submit" name="submit_save"  value="Update" id="update" />
	 <input class="btn btn-danger" type="submit" name="submit_cancel" value="<fmt:message key="jsp.dspace-admin.general.cancel"/>" />
     </c:when>    
    <c:otherwise>
	
 		<input class="btn btn-primary" type="submit" name="submit_save" id="save"  value="<fmt:message key="jsp.dspace-admin.general.save"/>" />
		   <input class="btn btn-success" type="reset" name="clear_reset" value="<fmt:message key="jsp.dspace-admin.general.clear"/>" />
		   <input class="btn btn-danger" type="submit" name="submit_add_cancel" value="<fmt:message key="jsp.dspace-admin.general.cancel"/>" />	
		   
    </c:otherwise>
</c:choose>
     </div>
		 <div class="col-sm-3">
		 </div>
		 </div>
		 
		 <div class="box box-primary">
		 <div class="from-footer "> 
			
			<%
			  if((groupMemberships != null) && (groupMemberships.length>0))
			  {
			%>			
			<h5><fmt:message key="jsp.dspace-admin.eperson-edit.groups"/></h5>			
			<div class="col-sm-3">
			  <ul style="list-style-type: number;">
				<%  for(int i=0; i<groupMemberships.length; i++)
					{
					String myLink = groupMemberships[i].getName();
					String args   = "submit_edit&amp;group_id="+groupMemberships[i].getID();
					
					myLink = "<a href=\""
					+request.getContextPath()
					+"/tools/group-edit?"+args+"\">" + myLink + "</a>";
				%>
					<li><%=myLink%></li>
				<%  } %>
				</ul>
			  </div>
			  <% } %>  
			  <div class="col-sm-3">
			   </div>
			    </div>
			  </div>
	
	 </div>
      </form>
</div>
</div>
</div>

</dspace:layout>
