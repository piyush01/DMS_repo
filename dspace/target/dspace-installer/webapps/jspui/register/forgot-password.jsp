 
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>	
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>
<%@ page import="org.dspace.app.webui.servlet.RegisterServlet" %>
<link rel="shortcut icon" href="<%= request.getContextPath() %>/favicon.ico" type="image/x-icon"/>
<%
    boolean retry = (request.getAttribute("retry") != null);
%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>eZeeFile |Forgot Password</title>
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
    <body class="hold-transition login-page" style=" background-size:cover; background:#fff;">	<!-- background-image:url(../jspui/image/himalayas_background_1920x1080.jpg);-->
	
	<div class="login-box" style="width:1060px !important;">
      <div class="login-logo" style="text-align:left !important;">
        <a href="<%= request.getContextPath() %>"> eZeeFile</a>
		<a href="" style="float:right"><img src="http://cbslgroup.in/img/logo/logo_cbsl.png" ></a>		
      </div><!-- /.login-logo -->
	  
	  <div class="login-box-body" style="width:69% !important; float:left !important; border:1px solid #ccc; min-height:280px;">		
		<h2>Forgot Password! </h2>
	  
	  <p>eZeeFile Enterprise Document Management System.</p> 
	 <p>Track, manage and store your documents and reduce paper.</p>
	  
	  <!--<p>An electronic document management system is a system of software which is used for organizing and storing different kinds of documents. This technology is used to capture, manage, preserveand deliver content and documents.</p>
	  
	  <p>A Document Management System is the traffic cop that brings order to this chaos: organizing it by client/matter, categorizing the types of documents and directing them to the proper place. The primary goal is to eliminate paper and the cost associated with it.</p>
-->
   

	<div style="position:relative; top:100px; left:0; font-weight:bold;"> Help Desk - Tel no. : 011-47547700 | Email Id : ezeefileadmin@cbsl-india.com</div>
	  
	  </div>
	  
	  
	  
      <div class="login-box-body" style="width:29% !important; float:right !important; border:1px solid #ccc;">	  
        <!--<p class="login-box-msg"><b>LogIn To DMS</b> </p>-->
		 
		<!---------------------------------->
     <form class="form-horizontal" action="<%= request.getContextPath() %>/forgot" method="post"> 
     <input type="hidden" name="step" value="<%= RegisterServlet.ENTER_EMAIL_PAGE %>"/>
      <!--<p><strong><a href="<%= request.getContextPath() %>/register"><fmt:message key="jsp.components.login-form.newuser"/></a></strong></p>-->
	  <p><fmt:message key="jsp.register.forgot-password.info2"/></p>
		<div class="form-group has-feedback">
            <!--<label class="col-md-offset-3 col-md-2 control-label" for="tlogin_email"><fmt:message key="jsp.components.login-form.email"/></label>-->
			
			<input class="form-control" type="text" placeholder="Email" name="email" id="temail" />
           <span class="glyphicon glyphicon-envelope form-control-feedback"></span>
			
            <!--<div class="col-md-3">
            	<input class="form-control" type="text" name="login_email" id="tlogin_email" tabindex="1" />
            </div>-->
			</div>
		<div class="row">
            <div class="col-xs-8">
              <div class="checkbox icheck">
                <!--<label>
                  <input type="checkbox"> Remember Me
                </label>-->
              </div>
            </div><!-- /.col -->
            <div class="col-xs-4">
              <input class="btn btn-primary pull-right" type="submit" name="submit_forgotpassword" value="<fmt:message key="jsp.register.forgot-password.forgot.button"/>" />
            </div><!-- /.col -->
          </div>
		
      </form>
	  <!------------------------------------->
	 <a href="<%= request.getContextPath() %>/password-login">Back to login  page</a><br>    
        
		<!--<a class="text-center" href="<%= request.getContextPath() %>/register"><fmt:message key="jsp.components.login-form.newuser"/></a>-->
		
      </div><!-- /.login-box-body -->
	  
	  <div style="position:fixed; bottom:50px; width:75%; margin:0; padding:0; text-align:center;">Copyright 2016 CBSL GROUP. All rights reserved</div>
	  
    </div><!-- /.login-box -->
	  
      <script type="text/javascript">
		document.loginform.login_email.focus();
	  </script>
	
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
  </body>
</html>