
<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<%@ page import="javax.servlet.http.*,javax.servlet.*" %>
<dspace:layout style="submission" titlekey="jsp.mydspace" nocache="true">
<style>

table.scroll tbody,
table.scroll thead { display: block; }
table.scroll tbody {
    height: 430px;
    //overflow-y: auto;
    overflow: scroll;
}
tbody { border-top: 1px solid #3D86C5; }
</style>
<script>
$(document).ready(function(){		
		$('#mylink').bind('click', false);
	});

$(function() {
       $('#doc').click(function() {
		   $('docview').submit();
      $('#mylink').unbind('click', false);
      });   
});
function read(){
		 $('#mylink').unbind('click', false);
	return true;
}
</script>

               
 

	<div class="panel panel-primary">
        <div class="panel-heading">My Task                
        </div>     
		<div class="panel-body">
		    <form action="<%= request.getContextPath() %>/mydspace" method="post"> </form>		
		</div>
		 <table class="table">
		<tbody>
	    <c:if test="${fn:length(supervisortasklist) <= 0}">		
			<center><p class="text-danger">You have been not assigned any task's yet.</p>  </center>
		  </c:if>		  
		<tr>
		 <tr>
            <th class="oddRowOddCol">Action</th>
		    <th class="oddRowOddCol">Task No.</th>
				<th class="oddRowOddCol">Task Name</th>
            <th  class="oddRowEvenCol">Document Name</th>		
            <th  class="oddRowOddCol">Status</th>
            <th  class="oddRowEvenCol">Assign Date</th>
			<th  class="oddRowEvenCol">Due Date & Time</th>
            <th  class="oddRowOddCol">Assign By</th>
        </tr>
		<dspace:mytask userid="${user_id}" status="supervisor" />		
		</tbody>
        </table>
</div>	
</dspace:layout>
