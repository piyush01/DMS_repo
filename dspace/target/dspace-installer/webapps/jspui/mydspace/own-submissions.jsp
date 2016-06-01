
<%--
  - Show user's previous (accepted) submissions
  -
  - Attributes to pass in:
  -    user     - the e-person who's submissions these are (EPerson)
  -    items    - the submissions themselves (Item[])
  -    handles  - Corresponding Handles (String[])
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>

<%@ page import="org.dspace.content.Item" %>
<%@ page import="org.dspace.eperson.EPerson" %>

<%
    EPerson eperson = (EPerson) request.getAttribute("user");
    Item[] items = (Item[]) request.getAttribute("items");
%>
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
<dspace:layout style="default" locbar="link"
               parentlink="/mydspace"
               parenttitlekey="jsp.mydspace"
               titlekey="jsp.mydspace">
  <div class="row">
	<div class="col-md-12">
	 <!-- general form elements -->
              <div class="box box-primary">
             <div class="box-header with-border">				
		     <h3 class="box-title"><fmt:message key="jsp.mydspace.own-submissions.title"/></h3>
<%
    if (items.length == 0)
    {
%>
    <%-- <p>There are no items in the main archive that have been submitted by you.</p> --%>
	<p><fmt:message key="jsp.mydspace.own-submissions.text1"/></p>
<%
    }
    else
    {
%>
    <%-- <p>Below are listed your previous submissions that have been accepted into
    the archive.</p> --%>
	<p><fmt:message key="jsp.mydspace.own-submissions.text2"/></p>
<%
        if (items.length == 1)
        {
%>
    <%-- <p>There is <strong>1</strong> item in the main archive that was submitted by you.</p> --%>
	<p><fmt:message key="jsp.mydspace.own-submissions.text3"/></p>
<%
        }
        else
        {
%>
    <p><fmt:message key="jsp.mydspace.own-submissions.text4">
        <fmt:param><%= items.length %></fmt:param>
    </fmt:message></p>
	
<%
        }
%>
</div>
<div class="box-body">	
<div class="col-sm-12">
    <dspace:itemlist items="<%= items %>" />
</div>
	
<%
    }
%>

	
	<p align="center"><a class="btn btn-primary pull-right" href="<%= request.getContextPath() %>/mydspace"><fmt:message key="jsp.mydspace.general.backto-mydspace"/></a></p>
	</div>
	</div>
	</div>
	</div>
	</div>
</dspace:layout>
