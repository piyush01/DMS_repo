
<%--
  - Footer for home page
  --%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ page import="java.net.URLEncoder" %>
<%@ page import="org.dspace.app.webui.util.UIUtil" %>

</div>

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
          <form action="#" method="get" class="sidebar-form">
            <div class="input-group">
              <input type="text" name="q" class="form-control" placeholder="Search...">
              <span class="input-group-btn">
                <button type="submit" name="search" id="search-btn" class="btn btn-flat"><i class="fa fa-search"></i></button>
              </span>
            </div>
          </form>
          <!-- /.search form -->
			
          </div><!-- /.tab-pane -->
          <!-- Stats tab content -->
          <div class="tab-pane" id="control-sidebar-stats-tab">Stats Tab Content</div><!-- /.tab-pane -->
          <!-- Settings tab content -->
          <div class="tab-pane" id="control-sidebar-settings-tab">
            <form method="post">
					<!-- select -->
                    <div class="form-group">
                      <label>Search</label>
                      <select class="form-control">
                        <option>My Community</option>
                        <option>My Vault</option>
                        <option>All DMS</option>
                        <option>Test</option>
                        <option>Test one</option>
                      </select>
                    </div>
					
					<div class="form-group">
					<div class="input-group">
					  <input type="text" name="q" class="form-control" placeholder="Search For...">
					  <span class="input-group-btn">
						<button type="submit" name="search" id="search-btn" class="btn btn-flat btn-info"><i class="fa fa-search"></i></button>
					  </span>
					</div>
					</div>
					
					<!-- select -->
                    <div class="form-group">
                      <label>Add Filter</label>
                      <select class="form-control">
                        <option>Status</option>
                        <option>My Document</option>
                        <option>Option 1</option>
                        <option>Option 2</option>
                        <option>Option 3</option>
                      </select>
                    </div>
					
					<!-- select -->
                    <div class="form-group">                      
                      <select class="form-control">
                        <option>Equals</option>
                        <option>Contains</option>
                        <option>ID</option>
                        <option>Not Equals</option>
                        <option>Option 3</option>
                      </select>
                    </div>
					
					<div class="form-group">  
						<div class="input-group">
						  <input type="text" name="q" class="form-control" placeholder="Search...">
						  <span class="input-group-btn">
							<button type="submit" name="search" id="search-btn" class="btn btn-flat btn-info">Add</button>
						  </span>
						</div>
					</div>	
					
					<div class="form-group">
						<button type="submit" class="btn btn-info btn-flat">Start New Search</button>
					</div>
					
					<!-- select -->
                    <div class="form-group">
                      <label>Result per Page</label>
                      <select class="form-control">
                        <option>10</option>
                        <option>25</option>
                        <option>50</option>
                        <option>75</option>
                        <option>100</option>
                      </select>
                    </div>
					
					<!-- select -->
                    <div class="form-group">
                      <label>Sort Document By</label>
                      <select class="form-control">
                        <option>10</option>
                        <option>25</option>
                        <option>50</option>
                        <option>75</option>
                        <option>100</option>
                      </select>
                    </div>
					
					<!-- select -->
                    <div class="form-group">
                      <label>In Order</label>
                      <select class="form-control">
                        <option>Descending</option>
                        <option>Ascending</option>
                      </select>
                    </div>
					
					<!-- select -->
                    <div class="form-group">
                      <label>Authors/Record</label>
                      <select class="form-control">
                        <option>All</option>
                        <option>10</option>
						<option>50</option>
                        <option>100</option>
                      </select>
                    </div>
					
					<div class="form-group">
						<button type="submit" class="btn btn-info btn-flat">Update</button>
						<button type="submit" class="btn btn-warning btn-flat">Export Metadata</button>
					</div>
			<!-- /.form-group -->
            </form>
          </div><!-- /.tab-pane -->
        </div>
      </aside><!-- /.control-sidebar -->
      <!-- Add the sidebar's background. This div must be placed
           immediately after the control sidebar -->
      <div class="control-sidebar-bg"></div>



		<!-- Main Footer -->
      <footer class="main-footer">
        <!-- To the right -->
        <div class="pull-right hidden-xs">
          
        </div>
        <!-- Default to the left -->
        <strong>Copyright &copy; 2016 <a target="_blank" href="http://cbslgroup.in">CBSL Group</a>.</strong> All rights reserved.

		

      </footer>
            
            <!-- <footer class="navbar navbar-inverse navbar-bottom">
             <div id="designedby" class="container text-muted">
             <fmt:message key="jsp.layout.footer-default.theme-by"/> <a href="#"><img
                                    src="<%= request.getContextPath() %>/image/logo-cineca-small.png"
                                    alt="Logo CINECA" /></a>
			<div id="footer_feedback" class="pull-right">                                    
                                <p class="text-muted"><fmt:message key="jsp.layout.footer-default.text"/>&nbsp;-
                                <a target="_blank" href="<%= request.getContextPath() %>/feedback"><fmt:message key="jsp.layout.footer-default.feedback"/></a>
                                <a href="<%= request.getContextPath() %>/htmlmap"></a></p>
                                </div>
			</div>
    </footer>-->
	
	
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
	function test(){
		 //var value = $(".testClick").attr("href");
		  
		   //window.location.value = value.href;
		var myLink = document.getElementById('myLink');		
		window.location.href = myLink.href;
			
		
	}
	</script>
    </body>
</html>