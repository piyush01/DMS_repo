
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


 <script type="text/javascript">
    $(function() {
    $('.checkedggg').click(function(e) {
        e.preventDefault();
        var dialog = $('<p>Are you sure read document?</p>').dialog({
            buttons: {
                "Yes": function() 
				{
				 var url = $('.checked').attr("href");				 
				 var link = this.href;	          
                 alert("link--"+link );							
			   sendRequest('<%=request.getContextPath()%>/mytask?action=ajaxread&process_id=147', 'step_id');			   
					dialog.dialog('close');
					return true;
				},
                "No":  function() {
					dialog.dialog('close');
					return false;
					},
                "Cancel":  function() {
                    dialog.dialog('close');
					return false;
                }
            }
        });
		
      });
});


function read(e){

	if(e==0){
		alert("First of all you document vew.");
		return false;
	}else if(e==1){
		
		return true;
	}
	
	return false;
}

function AutoRefresh(t) {
               setTimeout("location.reload(true);", t);
            }
			
function sendRequest(url, target) {		
//alert("url:---"+url);	
	$('#' + target).empty();
	$.ajax({
		url : url,
		success : function(result) {
			
			var name = result.getElementsByTagName("name");
			var id = result.getElementsByTagName("id");
			var length = name.length;
			
			$('#' + target).append('<option value="0">--Select--</option>');
			for ( var i = 0; i < length; i++) {
				$('#' + target).append(
						'<option value="' + id[i].childNodes[0].nodeValue
								+ '">' + name[i].childNodes[0].nodeValue
								+ '</option>');
			}
		}
	});
	return true;
}

$( document ).ready(function() {
$('a.checked').on('click',function(){
	
    //var link = this.href;
	 var idd=this.id;
	var isupdate=sendRequest('<%=request.getContextPath()%>/mytask?action=ajaxread&process_id='+idd, 'step_id');
	//alert(isupdate);
	if(isupdate){
		//location.reload();
		//location.reload(true);
		AutoRefresh(1000);
		return true;
	}else{
  return false;
	}
  })    
});
  </script>
 
	<div class="panel panel-primary">
        <div class="panel-heading">My Task                
        </div>     
		<div class="panel-body">
		    <form action="<%= request.getContextPath() %>/mydspace" method="post"> </form>		
		</div>
		 <table class="table">
		<tbody>		 		
		 <tr>
            <th class="oddRowOddCol">Action</th>
			<!--<th class="oddRowOddCol">Workflow Name</th>
			<th class="oddRowOddCol">Step Name</th>!-->
			<th class="oddRowOddCol">Task No.</th>
			<th class="oddRowOddCol">Task Name</th>			
            <th  class="oddRowEvenCol">Document Name</th>
			<th  class="oddRowOddCol">Status</th>
            <th  class="oddRowEvenCol">Assign Date</th>
			<th  class="oddRowEvenCol">Due Date & Time</th>
            <th  class="oddRowOddCol">Assign By </th>
        </tr>	 
		<dspace:mytask userid="${user_id}" status="user" />
      	</tbody>
        </table>
</div>	
</dspace:layout>
