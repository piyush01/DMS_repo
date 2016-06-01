
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<%@ page import="javax.servlet.http.*,javax.servlet.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
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

function read(){
		 $('#mylink').unbind('click', false);
	return true;
}
function start(){
		 alert("Can not start task yet.");
	return false;
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
		<dspace:docsumbit status="document"/>	
		</tbody>
        </table>
</div>	
</dspace:layout>
