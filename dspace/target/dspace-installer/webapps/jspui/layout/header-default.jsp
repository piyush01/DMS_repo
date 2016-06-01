
<%--
  - HTML header for main home page
  --%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ page import="java.util.List"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="org.dspace.app.webui.util.JSPManager" %>
<%@ page import="org.dspace.core.ConfigurationManager" %>
<%@ page import="org.dspace.app.util.Util" %>
<%@ page import="javax.servlet.jsp.jstl.core.*" %>
<%@ page import="javax.servlet.jsp.jstl.fmt.*" %>
<%@ page import="org.dspace.app.webui.servlet.admin.EditCommunitiesServlet" %>
<%@ page import="org.dspace.eperson.EPerson" %>

<%
   EPerson user = (EPerson) request.getAttribute("dspace.current.user"); 
    String title = (String) request.getAttribute("dspace.layout.title");
    String navbar = (String) request.getAttribute("dspace.layout.navbar");
    boolean locbar = ((Boolean) request.getAttribute("dspace.layout.locbar")).booleanValue();
    String siteName = ConfigurationManager.getProperty("dspace.name");
    String feedRef = (String)request.getAttribute("dspace.layout.feedref");
    boolean osLink = ConfigurationManager.getBooleanProperty("websvc.opensearch.autolink");
    String osCtx = ConfigurationManager.getProperty("websvc.opensearch.svccontext");
    String osName = ConfigurationManager.getProperty("websvc.opensearch.shortname");
    List parts = (List)request.getAttribute("dspace.layout.linkparts");
    String extraHeadData = (String)request.getAttribute("dspace.layout.head");
    String extraHeadDataLast = (String)request.getAttribute("dspace.layout.head.last");
    String dsVersion = Util.getSourceVersion();
    String generator = dsVersion == null ? "DSpace" : "DSpace "+dsVersion;
    String analyticsKey = ConfigurationManager.getProperty("jspui.google.analytics.key");
%>

<!DOCTYPE html>

<html>
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title><%= siteName %>: <%= title %></title>
        <!--OLD THEME-->
		<!--<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="Generator" content="<%= generator %>" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="shortcut icon" href="<%= request.getContextPath() %>/favicon.ico" type="image/x-icon"/>
	    <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/jquery-ui-1.10.3.custom/redmond/jquery-ui-1.10.3.custom.css" type="text/css" />
	    <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/bootstrap/bootstrap.min.css" type="text/css" />
	    <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/bootstrap/bootstrap-theme.min.css" type="text/css" />
	    <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/bootstrap/dspace-theme.css" type="text/css" />-->
		
		<!--END OLD THEME-->

        
	<!--old <script type='text/javascript' src="<%= request.getContextPath() %>/static/js/jquery/jquery-1.10.2.min.js"></script>
	<script type='text/javascript' src='<%= request.getContextPath() %>/static/js/jquery/jquery-ui-1.10.3.custom.min.js'></script>
	<script type='text/javascript' src='<%= request.getContextPath() %>/static/js/bootstrap/bootstrap.min.js'></script>
	<script type='text/javascript' src='<%= request.getContextPath() %>/static/js/holder.js'></script>
	<script type="text/javascript" src="<%= request.getContextPath() %>/utils.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath() %>/static/js/choice-support.js"> </script>
    <script type='text/javascript' src='<%= request.getContextPath() %>/static/js/bootstrap/multiselect.js'></script>
	<script type='text/javascript' src='<%= request.getContextPath() %>/static/js/bootstrap/multiselect.min.js'></script>
 <script type="text/javascript" src="<%= request.getContextPath() %>/static/js/jqcontextmenu.js"> </script>
 <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/static/css/jqcontextmenu.css" />-->
  
  <link rel="shortcut icon" href="<%= request.getContextPath() %>/favicon.ico" type="image/x-icon"/>
  <script type='text/javascript' src="<%= request.getContextPath() %>/static/js/jquery/jquery-1.10.2.min.js"></script>
<script type='text/javascript' src='<%= request.getContextPath() %>/static/js/jquery/jquery-ui-1.10.3.custom.min.js'></script>
	 <script type='text/javascript' src='<%= request.getContextPath() %>/static/js/bootstrap/multiselect.js'></script>
	<script type='text/javascript' src='<%= request.getContextPath() %>/static/js/bootstrap/multiselect.min.js'></script>
	<script type="text/javascript" src="<%= request.getContextPath() %>/utils.js"></script>
	<link rel="shortcut icon" href="<%= request.getContextPath() %>/favicon.ico" type="image/x-icon"/>
	<!--NEW THEME-->
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
	<!-- skin-blue.min -->
	<link rel="stylesheet" href="<%= request.getContextPath() %>/static1/dist/css/skins/skin-blue.min.css">
	 <!-- DataTables -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/static1/plugins/datatables/dataTables.bootstrap.css">
	 <link rel="stylesheet" href="<%= request.getContextPath() %>/static1/dms.css">
	
<!--END NEW THEME-->    

<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
  <script src="<%= request.getContextPath() %>/static/js/html5shiv.js"></script>
  <script src="<%= request.getContextPath() %>/static/js/respond.min.js"></script>
<![endif]-->
<script type='text/javascript'>

$(document).ready(function()
{
$(".account").click(function()
{
var X=$(this).attr('id');
if(X==1)
{
$(".submenu").hide();
$(this).attr('id', '0');
}
else
{
$(".submenu").show();
$(this).attr('id', '1');
}

});

//Mouse click on sub menu
$(".submenu").mouseup(function()
{
return false
});

//Mouse click on my account link
$(".account").mouseup(function()
{
return false
});


//Document Click
$(document).mouseup(function()
{
$(".submenu").hide();
$(".account").attr('id', '');
});
});

 </script>
  <script type='text/javascript'>
$(document).ready(function(){
   $('#topnav ul li ul.treeview-menu li a').click(function(e){
     if ($(this).attr('class') != 'active'){		 	  
       $('#topnav ul li a').removeClass('active');
       $(this).addClass('active');		  
	  // event.preventDefault();
	   var url=$(this).attr('href');   
	    window.location = url;	
     }
   });
       $('a').filter(function(){
            return this.href === document.location.href;
       }).addClass('active')
	   
       $("ul.treeview-menu > li > a").each(function () {
         var currentURL = document.location.href;
         var thisURL = $(this).attr("href");		 
         if (currentURL.indexOf(thisURL) != -1) {			
             $(this).parents("ul.treeview-menu").css('display', 'block');
         }		  
       });
       $('#topnav > ul > li > a').each(function(){		
      var currURL = document.location.href;
      var myHref= $(this).attr('href');
      if (currURL.match(myHref)) {
            $(this).addClass('active');
            $(this).parent().find("ul.treeview-menu").css('display', 'block');
      }
    });			  
});


</script>

<!----------- for button icon -->
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
<!----------- end for button icon -->
    </head>

<body class="hold-transition skin-blue sidebar-mini">
<%
 if(user==null)
 {
	%>
	<jsp:forward page ="/login/password.jsp" />
<%
 }
%>

<div class="wrapper">
<!-- old <a class="sr-only" href="#content">Skip navigation</a>-->
	<header class="main-header">
		 <!-- Logo -->
        <a href="<%= request.getContextPath() %>" class="logo">
          <!-- mini logo for sidebar mini 50x50 pixels -->
          <span class="logo-mini"><b>DMS</b></span>
          <!-- logo for regular state and mobile devices -->
          <span class="logo-lg"><b>eZeeFile</b></span>
        </a>
		
		
		
    <%
    if (!navbar.equals("off"))
    {
%>
<dspace:include page="<%= navbar %>" />
           
<%
    }
    else
    {
    	%>       
      <dspace:include page="/layout/navbar-minimal.jsp" />
       
<%    	
    }
%>
	</header>

  <!-- Left side column. contains the logo and sidebar -->
      <aside class="main-sidebar">

        <!-- sidebar: style can be found in sidebar.less -->
        <section class="sidebar">

          <!-- Sidebar user panel (optional) -->
          <!--<div class="user-panel" style="background:#fff;">
            
              <img src="images/dspace-logo-only.png" class="img-circle" alt="User Image">
            
            
          </div>-->

          
          <!-- Sidebar Menu -->
		<div id="topnav">
          <ul class="sidebar-menu">            
            <!-- Optionally, you can add icons to the links -->
            <li class=""><a href="<%= request.getContextPath() %>/"><i class="fa fa-home"></i> <span><fmt:message key="jsp.layout.navbar-default.home"/>  </span></a></li>		  
            <li class="treeview">
              <a href="#">
                <i class="fa fa-database"></i> <span> My Cabinets </span>
                <i class="fa fa-angle-left pull-right"></i>
              </a>				           
			  <ul class="treeview-menu">
 		      <dspace:include page="/folder.jsp" />  
              </ul>						  
            </li>							  
			  <li class="treeview">
				   <a href="#">
					<i class="fa fa-search"></i> <span>Search</span>
					<i class="fa fa-angle-left pull-right"></i>
				  </a>
			    <ul class="treeview-menu">				
				<li class="treeview">
               <a href="<%= request.getContextPath() %>/advanced-search">
               <i class="fa fa-circle-o"></i><span>Advance Search</span>               
              </a>
			  </li>	 
			   <li class="treeview">
               <a href="<%= request.getContextPath() %>/simple-search">
               <i class="fa fa-circle-o"></i><span>Simple Search</span>               
              </a>
			  </li>
			</ul>
			</li>			  
          </ul><!-- /.sidebar-menu Specific Case Search -->
		  </div>
        </section>
  <!-- /.sidebar -->
  
      </aside>			
        <!-- /.sidebar -->
		<!-- Content Wrapper. Contains page content -->
      <div class="content-wrapper">
		<!-- Content Header (Page header) -->
        <section class="content-header">
          <h6 style="margin:0;">
              <%-- Location bar --%>
			<%
				if (locbar)
				{
			%>
				<dspace:include page="/layout/location-bar.jsp" />
			<%
				}
			%>          
          </h6>
		    
        </section>	  
		 <!-- Main content -->
        <section class="content">
	  



		