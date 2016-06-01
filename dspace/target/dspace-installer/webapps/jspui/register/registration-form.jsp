<%--
  - Registration information form
  -
  - Form where new users enter their personal information and select a
  - password.
  -
  - Attributes to pass in:
  -
  -   eperson          - the EPerson who's registering
  -   token            - the token key they've been given for registering
  -   set.password     - if Boolean true, the user can set a password
  -   missing.fields   - if a Boolean true, the user hasn't entered enough
  -                      information on the form during a previous attempt
  -   password.problem - if a Boolean true, there's a problem with password
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>
	<!--<link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/bootstrap/containerscroll.css" type="text/css" />-->
<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>
<%@ page import="java.util.Locale"%>
<%@ page import="org.dspace.core.Utils" %>
<%@ page import="org.dspace.core.I18nUtil" %>
<%@ page import="org.dspace.app.webui.util.UIUtil" %>
<%@ page import="org.dspace.app.webui.servlet.RegisterServlet" %>
<%@ page import="org.dspace.eperson.EPerson" %>
<%@ page import="org.dspace.eperson.Group" %>


<%
  EPerson eperson = (EPerson) request.getAttribute( "eperson" ); 
  boolean emailExists = (request.getAttribute("email_exists") != null);
	Group[] groups =(Group[]) request.getAttribute("groups");
%>
<!------------------------>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>CBSL CMS | Log in</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <!-- Bootstrap 3.3.5 -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/static1/bootstrap/css/bootstrap.min.css">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css">
    <!-- Ionicons -->
    <link rel="stylesheet" href="https://code.ionicframework.com/ionicons/2.0.1/css/ionicons.min.css">
    <!-- Theme style -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/static1/dist/css/AdminLTE.min.css">
    <!-- iCheck -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/static1/plugins/iCheck/square/blue.css">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
        <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>
 <body class="hold-transition login-page" style="background-image:url(../jspui/image/himalayas_background_1920x1080.jpg); background-size:cover;">	
          
	<div class="login-box">
      <div class="login-logo">
        <a href="index.html"><i class="fa fa-home"></i> <b>CBSL</b> DMS</a>
      </div><!-- /.login-logo -->
      <div class="login-box-body">	  
        <p class="login-box-msg"><b><fmt:message key="jsp.register.registration-form.title"/></b> </p>
		 
		<!---------------------------------->
	  <p><!--<fmt:message key="jsp.components.login-form.enter"/>-->Please enter the following information. <br>All Fields are Required <!--fields are marked with '<b>*</b>'--></p>
	  
					<%
					if(emailExists)
					{
					%>
					<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a><fmt:message key="jsp.dspace-admin.eperson-edit.emailexists"/> </div>
					<%
					}
					%>
	<form  action="<%= request.getContextPath() %>/register" id="registration" method="post">
	
		<div class="form-group has-feedback">
            
			<input class="form-control" type="text" name="email" id="temail" size="40" placeholder="Your Email (eg:abc@xyz.com)" />
            
			
        </div>
		<div class="form-group has-feedback">
           
            <input class="form-control" type="password" name="password" id="tpassword" size="40" placeholder="Password"/>
           
        </div>
		<div class="form-group has-feedback">
           
            <input class="form-control" type="password" name="password_confirm" id="tpassword_confirm" size="40" placeholder="Confirm Password"/>
            
        </div>
        <div class="form-group has-feedback">
           
            <input class="form-control" type="text" name="first_name" id="tfirst_name" size="40" placeholder="Your First Name" />
           
			
			
        </div>
		<div class="form-group has-feedback">
           
            <input class="form-control" type="text" name="last_name" id="tlast_name" size="40" placeholder="Your Last Name"/>
           
			
			
        </div>
		<div class="form-group has-feedback">
           
            <input class="form-control" type="text" name="phone" id="tphone" size="40" maxlength="32" placeholder="10 Digit Mobile Number"/>
           
			
			
        </div>
		
		<div class="form-group has-feedback">
           
            <input class="form-control"   name="designation" id="tdesignation" size="40" placeholder="User Designation"/>
            
			
			
        </div>
		<div class="form-group has-feedback">
           
            <select class="form-control"  name="group_id" id="tgroup_id" >
                           <option value="0">Select Department</option>
                    <%for(int i = 0; i < groups.length; i++ ) {%>
                            <option value="<%=groups[i].getID() %>"><%=groups[i].getName()%></option>
                        <%  } %>
                </select>        
			
			
        </div>
		<div class="form-group has-feedback">
           
            <input class="form-control" type="text" name="superiorename" id="tsuperiorname" size="40" placeholder="Superior Name"/>
            
        </div>
		<div class="form-group has-feedback">
           
            <input class="form-control" type="text" name="superioremailid" id="tsuperioremail" size="40" placeholder="Superior Email (eg:abc@xyz.com)"/>
            
        </div>
		
       <input class="btn btn-primary" id="registration" type="submit" name="submit_register" value="<fmt:message key="jsp.register.registration-form.complete.button"/>" />
		<input class="btn btn-danger pull-right" type="submit" name="submit_cancel" value="<fmt:message key="jsp.dspace-admin.general.cancel"/>" />
	   
		</form>
      
	  <!------------------------------------->
	 
      </div><!-- /.login-box-body -->
    </div><!-- /.login-box -->	  
		  
	
  <!-- jQuery 2.1.4 -->
    <script src="<%= request.getContextPath() %>/static1/plugins/jQuery/jQuery-2.1.4.min.js"></script>
    <!-- Bootstrap 3.3.5 -->
    <script src="<%= request.getContextPath() %>/static1/bootstrap/js/bootstrap.min.js"></script>
    <!-- iCheck -->
    <script src="<%= request.getContextPath() %>/static1/plugins/iCheck/icheck.min.js"></script>
    <script>
      $(function () {
        $('input').iCheck({
          checkboxClass: 'icheckbox_square-blue',
          radioClass: 'iradio_square-blue',
          increaseArea: '20%' // optional
        });
      });
    </script>
	<!--
	<script type="text/javascript">
	
$(document).ready(function() {
	$('#registration').click(function(e)
	{
		var userid = $('#temail').val();
		var pass=$('#tpassword').val();
		var cpass=$('#tpassword_confirm').val();
	var fname=$('#tfirst_name').val();
	var lname=$('#tlast_name').val();
	var mobile=$('#tphone').val();
	var designation=$('#tdesignation').val();
		var dept=$('#tgroup').val();
		var sname=$('#tsuperiorname').val();
		var sid=$('#tsuperioremail').val();
var userid_regex = /^[\w\-\.\+]+\@[a-zA-Z0-9\.\-]+\.[a-zA-z0-9]{2,4}$/;	
var letters = /^[0-9]+$/;  
if(userid.length==0)
	{
		$('#error-alert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Please enter your email id.</div>');			
		$("#temail").focus();
		return false;
	}
	else if(!userid.match(userid_regex))
	{
		$('#error-alert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Email address should be as eg:someone@example.com.</div>');			
		$("#temail").focus();
		return false;
	}
	else if(fname.length==0)
	{
		$('#error-alert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Please Enter the First Name.</div>');			
		$("#tfirst_name").focus();
		return false;
		
	}
	else if(fname.match(letters))
	{
		$('#error-alert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>First name should not be numeric character only.</div>');			
		$("#tfirst_name").focus();
		return false;
	}
	else if(lname.match(letters))
	{
		$('#error-alert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Last name should not be numeric character only.</div>');			
		$("#tlast_name").focus();
		return false;
	}
	else if(mobile.length==0)
		{
			$('#error-alert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Please Enter mobile number.</div>');			
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
			$('#error-alert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Mobile number should  contain 10 digits.</div>');			
			$('#tphone').focus();
			return false;
		}
		else if(pass.length==0)
		{
			$('#error-alert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Please Enter Password.</div>');			
			$('#tpassword').focus();
			return false;
			
		}
		else if(pass.length<6)
		{
			$('#error-alert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Password must be of at least 6 character.</div>');			
			$('#tpassword').focus();
			return false;
		}
		else if(cpass.length==0)
		{
			$('#error-alert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Please Enter Confirm Password.</div>');			
			$('#tpassword_confirm').focus();
			return false;
		}
		else if(pass!=cpass)
		{
			$('#error-alert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Password does not match.</div>');			
			$('#tpassword').focus();
			return false;
		}
		else if(designation.length==0)
		{
			$('#error-alert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Please Enter Designation.</div>');			
			$('#tdesignation').focus();
			return false;
		}
		else if(designation.match(letters))
		{
			$('#error-alert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>User Designation should not be numeric only.</div>');			
			$('#tdesignation').focus();
			return false;
		}
		else if(sname.length==0)
		{
			
			$('#error-alert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Please Enter Superior Name.</div>');			
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
			$('#error-alert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Please Enter Superior mail address.</div>');			
			$('#tsuperioremail').focus();
			return false;
		}
		else if(!sid.match(userid_regex))
		{
			$('#error-alert').html('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>Superior mail address should be as eg:someone@example.com</div>');			
			$('#tsuperioremail').focus();
			return false;
		}
		})
});

</script>
	-->
	
  </body>
</html>


