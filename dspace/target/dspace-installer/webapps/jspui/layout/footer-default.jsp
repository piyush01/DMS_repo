
<%--
  - Footer for home page
  --%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ page import="java.net.URLEncoder" %>
<%@ page import="org.dspace.app.webui.util.UIUtil" %>
 <%@ page import="org.dspace.content.Community" %>
 
 <%
   Community[] communityArray = (Community[]) request.getAttribute("communities");
%>
<!--<%
    String sidebar = (String) request.getAttribute("dspace.layout.sidebar");
%>



 <!-- Control Sidebar -->
      <aside class="control-sidebar control-sidebar-dark">
        <!-- Create the tabs -->
        <ul class="nav nav-tabs nav-justified control-sidebar-tabs">
          <li class="active"><a href="#control-sidebar-home-tab" data-toggle="tab"><span>Keyword</span></a></li>
          <li><a href="#control-sidebar-settings-tab" data-toggle="tab"><span>Filter</a></li>
        </ul>
        <!-- Tab panes -->
        <div class="tab-content">
          
		  <!-- Home tab content -->
          <div class="tab-pane active" id="control-sidebar-home-tab">
            <ul class="control-sidebar-menu">
              <li>
                
                  <h4 class="control-sidebar-subheading" style="margin-left:10px;">
                    To search with a keyword enter the keyword and hit enter.
                  </h4>
                
              </li>
            </ul><!-- /.control-sidebar-menu -->
			
			
			<!-- search form (Optional) -->        
		  <form method="get" action="<%= request.getContextPath() %>/simple-search" class="sidebar-form">
            <div class="input-group">
              <input type="text" name="query" id="tequery" size="25" class="form-control" placeholder="Search documents">
              <span class="input-group-btn">
                <button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-search"></span></button>          
              </span>
            </div>
          </form>
          <!-- /.search form -->
			
          </div><!-- /.tab-pane -->
          <!-- Stats tab content -->
          <div class="tab-pane" id="control-sidebar-stats-tab">Stats Tab Content</div><!-- /.tab-pane -->
          <!-- Settings tab content -->
          <div class="tab-pane" id="control-sidebar-settings-tab">
            <form action="simple-search" method="get">
					<!-- select -->
                    <div class="form-group">
                      <label>Search</label>
					  <select name="location" id="tlocation" class="form-control" >
							<option selected="selected" value="/">All Cabinets</option>
								<%
								for (int i = 0; i < communityArray.length; i++)
									{											
								%>
						    <option value="<%= communityArray[i].getHandle() %>"><%= communityArray[i].getMetadata("name") %>
							</option>
								<%
										}
								%>
						</select>					  
                    </div>
					
					<div class="form-group">
					<div class="input-group">
					  <input type="text" name="query" class="form-control" placeholder="Search For...">
					  <span class="input-group-btn">
					  <input value="Go" type="submit" class="btn btn-flat btn-info">						
					  </span>
					</div>
					</div>
            </form>
          </div><!-- /.tab-pane -->
        </div>
      </aside>
	  
      <div class="control-sidebar-bg"></div>

		<!-- Main Footer -->
      <footer class="main-footer">
        <!-- To the right -->
        <div class="pull-right hidden-xs">
          
        </div>
        <!-- Default to the left -->
        <strong>Copyright &copy; 2016 <a target="_blank" href="http://cbslgroup.in">CBSL Group</a>.</strong> All rights reserved.

		</div>

      </footer>
            
          
	 <!-- jQuery 2.1.4 -->
    <script src="<%= request.getContextPath() %>/static1/plugins/jQuery/jQuery-2.1.4.min.js"></script>
    <!-- Bootstrap 3.3.5 -->
    <script src="<%= request.getContextPath() %>/static1/bootstrap/js/bootstrap.min.js"></script>
    <!-- iCheck -->
    <script src="<%= request.getContextPath() %>/static1/plugins/iCheck/icheck.min.js"></script>
	<!-- AdminLTE App -->
    <script src="<%= request.getContextPath() %>/static1/dist/js/app.min.js"></script>
	
	<!-- DataTables -->
    <script src="<%= request.getContextPath() %>/static1/plugins/datatables/jquery.dataTables.min.js"></script>
    <script src="<%= request.getContextPath() %>/static1/plugins/datatables/dataTables.bootstrap.min.js"></script>
    <!-- SlimScroll -->
    <script src="<%= request.getContextPath() %>/static1/plugins/slimScroll/jquery.slimscroll.min.js"></script>
    <!-- FastClick -->
    <script src="<%= request.getContextPath() %>/static1/plugins/fastclick/fastclick.min.js"></script>
    <!-- AdminLTE App -->
	
      <script>
      $(function () {		
        $("#example1").DataTable();
        $('#example2').DataTable({
          "paging": true,
          "lengthChange": false,
          "searching": false,
          "ordering": true,
          "info": true,
          "autoWidth": false
        });
      });
    </script>
	
    <script>
      $(function () {
        $('input').iCheck({
          checkboxClass: 'icheckbox_square-blue',
          radioClass: 'iradio_square-blue',
          increaseArea: '20%' // optional
        });
      });
    </script>
	
    <script>
      $(function () {
        $('input').iCheck({
          checkboxClass: 'icheckbox_square-blue',
          radioClass: 'iradio_square-blue',
          increaseArea: '20%' // optional
        });
      });
    </script>	
	
	
	<script>
	function test(){
		 //var value = $(".testClick").attr("href");
		  
		   //window.location.value = value.href;
		var myLink = document.getElementById('myLink');		
		window.location.href = myLink.href;
			
		
	}
	</script>		
    </body>
</html>
