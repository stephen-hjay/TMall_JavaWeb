<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*"%>
 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="../include/admin/adminHeader.jsp"%>
<%@include file="../include/admin/adminNavigator.jsp"%>



<title>Edit Category</title>


<script>
$(function(){
	
	$("#editForm").submit(function(){
		if(!checkEmpty("name","分类名称"))
			return false;

		return true;
	});
});

</script>

<div class="workingArea">

	<ol class="breadcrumb">
	  <li><a href="admin_category_list">All Categories</a></li>
	  <li class="active">Edit Category</li>
	</ol>

	<div class="panel panel-warning editDiv">
	  <div class="panel-heading">Edit Category</div>
	  <div class="panel-body">
	    	<form method="post" id="editForm" action="admin_category_update"  enctype="multipart/form-data">
	    		<table class="editTable">
	    			<tr>
	    				<td>Category Name</td>
	    				<td><input  id="name" name="name" value="${c.name}" type="text" class="form-control"></td>
	    			</tr>
	    			<tr>
	    				<td>Category Picture</td>
	    				<td>
	    					<input id="categoryPic" accept="image/*" type="file" name="filepath" />
	    				</td>
	    			</tr>	    			
	    			<tr class="submitTR">
	    				<td colspan="2" align="center">
	    					<input type="hidden" name="id" value="${c.id}">
	    					<button type="submit" class="btn btn-success">Submit</button>
	    				</td>
	    			</tr>
	    		</table>
	    	</form>
	  </div>
	</div>	
</div>