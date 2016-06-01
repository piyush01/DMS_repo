
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ page import="java.util.List"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="org.dspace.app.webui.util.JSPManager" %>
<%@ page import="org.dspace.core.ConfigurationManager" %>
<%@ page import="org.dspace.app.util.Util" %>
<%@ page import="javax.servlet.jsp.jstl.core.*" %>
<%@ page import="org.dspace.app.webui.util.UIUtil" %>
<%@ page import="javax.servlet.jsp.jstl.fmt.*" %>
<%@ page import="org.dspace.app.webui.servlet.admin.EditCommunitiesServlet" %>
<%@ page import="org.dspace.eperson.EPerson" %>

<%
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
	

	// Is anyone logged in?
	EPerson user = (EPerson) request.getAttribute("dspace.current.user");

    // Get the current page, minus query string
    String currentPage = UIUtil.getOriginalURL(request);    
    int c = currentPage.indexOf( '?' );
    if( c > -1 )
    {
        currentPage = currentPage.substring(0, c);
    }
    
    // E-mail may have to be truncated
    String navbarEmail = null;
    if (user != null)
    {
        navbarEmail = user.getEmail();
    }

%>

<!DOCTYPE html>
<html>
    <head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
        <title><%= title %> | <%= siteName %></title>
     
		<!--NEW THEME-->
	<!-- Tell the browser to be responsive to screen width -->
	 <link rel="shortcut icon" href="<%= request.getContextPath() %>/favicon.ico" type="image/x-icon"/>
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
	<script type="text/javascript" src="<%= request.getContextPath() %>/utils.js"></script>
<!--END NEW THEME-->

        

<script type='text/javascript' src="<%= request.getContextPath() %>/static/js/jquery/jquery-1.10.2.min.js"></script>
	<script type='text/javascript' src='<%= request.getContextPath() %>/static/js/jquery/jquery-ui-1.10.3.custom.min.js'></script>
	

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
             $(this).parents("ul.treeview-menu").css('display', 'none');
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

    <%-- HACK: leftmargin, topmargin: for non-CSS compliant Microsoft IE browser --%>
    <%-- HACK: marginwidth, marginheight: for non-CSS compliant Netscape browser --%>
   
<body class="skin-blue sidebar-mini">
	
<!--<a class="sr-only" href="#content">Skip navigation</a>-->
	<div class="wrapper">
	
		<header onclick="" class="main-header"> 
			
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
         
                
				  <nav class="navbar navbar-static-top" role="navigation">
	   
						<a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button">
							<span class="sr-only">Toggle navigation</span>
						  </a>
									 
					   
					   <div class="navbar-custom-menu">
							<ul class="nav navbar-nav">
							 <!-- User Account Menu -->
							  <li class="dropdown user user-menu">
						 
						<!-- Menu Toggle Button -->
								<a href="#" class="dropdown-toggle" data-toggle="dropdown">
								  <!-- The user image in the navbar-->
								  <i class="fa fa-user"></i>
								  <!-- hidden-xs hides the username on small devices so only the image appears. -->
								 

									<%
										if (user != null)
										{
									%>
									   <span class="hidden-xs"><fmt:message key="jsp.layout.navbar-default.loggedin">
										  <fmt:param><%= navbarEmail %></fmt:param>
									  </fmt:message>
										</span>
									<%
										}
									%>
								
								</a>
								
<!-- Menu Body -->								
						
<!---------------------------------->							
							  <ul class="dropdown-menu"><!-- Menu Body -->
								  <li class="user-body">
									<div class="col-xs-12 ">
									  <a href="<%= request.getContextPath() %>/mydspace">My DMS</a></div>
								 </li>  
							<!--	<li class="user-body">								  
									<div class="col-xs-12 ">
									  <a href="<%= request.getContextPath() %>/subscribe">Receive email Updates</a></div>								   
								</li>  -->
								<li class="user-body">
								   
									<div class="col-xs-12 ">
									    <a href="<%= request.getContextPath() %>/profile">Edit Profile</a></div>
									</li>  
								
								<li class="user-body">
									<div class="col-xs-12 ">
									  <a class="" href="<%= request.getContextPath() %>/dspace-admin">Administrator</a>
									</div>
						
								</li>
							  <li class="user-body">
								 <div class="col-xs-12 ">
									  <a class="" href="<%= request.getContextPath() %>/logout">Logout</a>
									</div>
							  </li>
							  </ul>
			  <!--------------------------->
							  
							</li>
							</ul>
						  </div>
				</nav>	
				<%
					}
					else
					{
						%>
						<div class="container">
							<dspace:include page="/layout/navbar-minimal.jsp" />
						</div>
				<%    	
					}
				%>
				</header>
				
				<!-- Left side column. contains the logo and sidebar -->
      <aside class="main-sidebar">

        <!-- sidebar: style can be found in sidebar.less -->
        <section class="sidebar">
          
          <!-- Sidebar Menu -->
		  <div id="topnav">
          <ul class="sidebar-menu">            
            <!-- Optionally, you can add icons to the links -->
            <li class=""><a href="<%= request.getContextPath() %>/"><i class="fa fa-home"></i> <span><fmt:message key="jsp.layout.navbar-default.home"/></a></li>
			
			<!-- <li class="treeview">
               <a href="#">
                <i class="fa fa-laptop"></i> <span><fmt:message key="jsp.layout.navbar-admin.contents"/></span>
                <i class="fa fa-angle-left pull-right"></i>
              </a>
			    <ul class="treeview-menu">
				 <li class="treeview">	
					<a href="<%= request.getContextPath() %>/tools/community-list">
					<i class="fa fa-circle-o"></i><span><fmt:message key="jsp.layout.navbar-admin.communities-collections"/></span>		
				  </a>			  
				  </li>
				  <li class="treeview">	
					<a href="<%= request.getContextPath() %>/tools/edit-item">
					<i class="fa fa-circle-o"></i><span><fmt:message key="jsp.layout.navbar-admin.items"/></span> 				
				  </a>			  
				  </li>
				   <li class="treeview">	
					<a href="<%= request.getContextPath() %>/dspace-admin/supervise">
					<i class="fa fa-circle-o"></i><span><fmt:message key="jsp.layout.navbar-admin.supervisors"/></span> 				
				  </a>			  
				  </li>
				  <li class="treeview">	
					<a href="<%= request.getContextPath() %>/dspace-admin/curate">
					<i class="fa fa-circle-o"></i><span><fmt:message key="jsp.layout.navbar-admin.curate"/></span>   				
				  </a>			  
				  </li>
				
				  <li class="treeview">	
					<a href="<%= request.getContextPath() %>/dspace-admin/workflow">
					<i class="fa fa-circle-o"></i><span><fmt:message key="jsp.layout.navbar-admin.workflow"/></span>   				
				  </a>			  
				  </li>
				 
				  
				  <li class="treeview">	
					<a href="<%= request.getContextPath() %>/dspace-admin/withdrawn">
					<i class="fa fa-circle-o"></i><span><fmt:message key="jsp.layout.navbar-admin.withdrawn"/></span> 				
				  </a>			  
				  </li>
				  <li class="treeview">	
					<a href="<%= request.getContextPath() %>/dspace-admin/privateitems">
					<i class="fa fa-circle-o"></i><span><fmt:message key="jsp.layout.navbar-admin.privateitems"/></span> 				
				  </a>			  
				  </li>
				  <li class="treeview">	
					<a href="<%= request.getContextPath() %>/dspace-admin/metadataimport">
					<i class="fa fa-circle-o"></i><span><fmt:message key="jsp.layout.navbar-admin.metadataimport"/></span> 				
				  </a>			  
				  </li>
				  <li class="treeview">	
					<a href="<%= request.getContextPath() %>/dspace-admin/batchimport">
					<i class="fa fa-circle-o"></i><span><fmt:message key="jsp.layout.navbar-admin.batchimport"/></span> 				
				  </a>			  
				  </li>
				 
				</ul>
			 </li>
			   -->
			 <li class="treeview">
				   <a href="#">
					<i class="fa fa-check"></i> <span><fmt:message key="jsp.layout.navbar-admin.accesscontrol"/></span>
					<i class="fa fa-angle-left pull-right"></i>
				  </a>
					<ul class="treeview-menu">
						 <li class="treeview">
						   <a href="#">
							<i class="fa fa-plus-circle"></i><fmt:message key="jsp.layout.navbar-admin.epeople"/>
							<i class="fa fa-angle-left pull-right"></i>               
						  </a>
						  <ul class="treeview-menu">
								<li><a href="<%= request.getContextPath() %>/dspace-admin/edit-epeople?action=add"><i class="fa fa-minus-circle"></i> <fmt:message key="jsp.layout.navbar-admin.CreateUser"/></a></li>
								<li><a href="<%= request.getContextPath() %>/dspace-admin/edit-epeople"><i class="fa fa-minus-circle"></i> <fmt:message key="jsp.layout.navbar-admin.userlist"/></a></li>
							<!--	<li><a href="<%= request.getContextPath() %>/dspace-admin/new-user"><i class="fa fa-circle-o"></i>
								New User</a></li>-->
							</ul>
						 </li>
						<li class="treeview">
						   <a href="#">
						   <i class="fa fa-plus-circle"></i><span><fmt:message key="jsp.layout.navbar-admin.groups"/></span><i class="fa fa-angle-left pull-right"></i>               
						  </a>
							<ul class="treeview-menu">
								<li>
								<a href="<%= request.getContextPath() %>/tools/group-edit">
								<i class="fa fa-minus-circle"></i> <fmt:message key="jsp.layout.navbar-admin.creategroups"/>
								</a>
								</li>
								<!--<li><a href="<%= request.getContextPath() %>/tools/group-edit"><i class="fa fa-circle-o"></i> <fmt:message key="jsp.layout.navbar-admin.listgroups"/></a></li>-->
								
							  </ul>
						 </li>			 
						<li class="treeview">
						   <a href="">
						   <i class="fa fa-plus-circle"></i><span><fmt:message key="jsp.layout.navbar-admin.authorization"/></span>
							<i class="fa fa-angle-left pull-right"></i>
						  </a>
						  <ul class="treeview-menu">
								<li><a href="<%= request.getContextPath() %>/tools/authorize?action=submit_community"><i class="fa fa-minus-circle"></i> <fmt:message key="jsp.dspace-admin.authorize-main.manage1"/></a></li> 
								 <li><a href="<%= request.getContextPath() %>/tools/authorize?action=submit_folder"><i class="fa fa-minus-circle"></i>Folder's Policies</a></li>
								<li><a href="<%= request.getContextPath() %>/tools/authorize?action=submit_collection"><i class="fa fa-minus-circle"></i> <fmt:message key="jsp.dspace-admin.authorize-main.manage2"/></a></li>
								<li><a href="<%= request.getContextPath() %>/tools/authorize?action=submit_item"><i class="fa fa-minus-circle"></i> <fmt:message key="jsp.dspace-admin.authorize-main.manage3"/></a></li>
								<li><a href="<%= request.getContextPath() %>/tools/authorize?action=submit_advanced"><i class="fa fa-minus-circle"></i> <fmt:message key="jsp.dspace-admin.authorize-main.advanced"/></a></li>
							   
							  </ul>
						 </li>
						<!-- <li class="treeview">
						   <a href="<%= request.getContextPath() %>/dspace-admin/add-workflow">
						   <i class="fa fa-circle-o"></i><span><fmt:message key="jsp.layout.navbar-admin.workflow"/></span>              
						  </a>
						 </li>-->
					</ul>
				</li>
				
				<!-- <li class=""><a href="<%= request.getContextPath() %>/statistics"><i class="fa fa-bar-chart"></i> <span><fmt:message key="jsp.layout.navbar-admin.statistics"/></a></li>-->
				 
				 <li class="treeview">
				   <a href="#">
					<i class="fa fa-wrench"></i> <span><fmt:message key="jsp.layout.navbar-admin.settings"/></span>
					<i class="fa fa-angle-left pull-right"></i>
				  </a>
					<ul class="treeview-menu">
						<!-- <li class="treeview">
						   <a href="<%= request.getContextPath() %>/dspace-admin/metadata-schema-registry">
							<i class="fa fa-minus-circle"></i><span><fmt:message key="jsp.layout.navbar-admin.metadataregistry"/></span>
						  </a>
						 </li>-->
						 
						 <li class="treeview"><a href="<%= request.getContextPath() %>/dspace-admin/format-registry"><i class="fa fa-minus-circle"></i><span>File Format  Registry</span></a>
						 </li>
						 
						  <li class="treeview">
						   <a href="<%= request.getContextPath() %>/dspace-admin/news-edit">
						   <i class="fa fa-minus-circle"></i><span><fmt:message key="jsp.layout.navbar-admin.editnews"/></span>               
						  </a>
						 </li>
						 
						<!-- <li class="treeview">
						   <a href="<%= request.getContextPath() %>/dspace-admin/license-edit">
						   <i class="fa fa-minus-circle"></i><span><fmt:message key="jsp.layout.navbar-admin.editlicense"/></span>               
						  </a>
						 </li>	-->	
					 
						<li class="treeview">
						   <a href="">
						   <i class="fa fa-plus-circle"></i><span>Metadata Registry</span>
							<i class="fa fa-angle-left pull-right"></i>
						  </a>
						  <ul class="treeview-menu">
								<li><a href="<%= request.getContextPath() %>/dspace-admin/Dms-metadata-field-registry?dc_schema_id=1"><i class="fa fa-minus-circle"></i> Add Fields</a></li> 
								<li><a href="<%= request.getContextPath() %>/dspace-admin/Dms-metadata-schema-registry?action=update_file"><i class="fa fa-minus-circle"></i> Assign Fields</a></li>
								<li><a href="<%= request.getContextPath() %>/dspace-admin/Dms-metadata-schema-registry?action=update_search"><i class="fa fa-minus-circle"></i> Assign Fields in Search</a></li>
							  </ul>
						 </li>
						
					</ul>
				</li>
			
			<li class="treeview">
               <a href="#">
                <i class="fa fa-cogs"></i> <span>Storage Hierarchy Settings</span>
                <i class="fa fa-angle-left pull-right"></i>
              </a>
			    <ul class="treeview-menu">
				 <li class="treeview">	
			    <a href="">
               <!--<a href="<%= request.getContextPath() %>/dspace-admin/edit-communities?action=<%=EditCommunitiesServlet.START_CREATE_COMMUNITY%>">-->
			    <a href="<%= request.getContextPath() %>/dspace-admin/create-folder?action=cabinet">
							<i class="fa fa-minus-circle"></i><span>Create New Cabinet</span>   				
						  </a>	   				
              </a>			  
			  </li>
			  
			  <li class="treeview">	
			    <a href="">
               <a href="<%= request.getContextPath() %>/dspace-admin/create-folder?action=folder">
							<i class="fa fa-minus-circle"></i><span>Create Folder</span>   				
						  </a>	   				
              </a>			  
			  </li>
			   <li class="treeview">	
			    <a href="">
               <a href="<%= request.getContextPath() %>/dspace-admin/create-folder?action=subfolder">
							<i class="fa fa-minus-circle"></i><span>Create Sub Folder</span>  				
						  </a>	   				
              </a>			  
			  </li>
			  
			  <!--<li class="treeview">	
					<a href="<%= request.getContextPath() %>/community-list">
					<i class="fa fa-circle-o"></i><span><fmt:message key="jsp.layout.navbar-admin.communities-collections"/></span>		
				  </a>			  
				  </li>-->
			</ul>
		</li> 
	        </ul><!-- /.sidebar-menu -->
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
      <!--  <section class="content-header">
          <h1>
            My Cabinets            
          </h1>
		   <%-- Location bar --%>
			<%
				if (locbar)
				{
			%>
				<dspace:include page="/layout/location-bar.jsp" />
			<%
				}
			%>
          
        </section>-->
	  
		 <!-- Main content -->
        <section class="content">			