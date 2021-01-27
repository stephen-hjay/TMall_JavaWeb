<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="java.util.*"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="../include/admin/adminHeader.jsp"%>
<%@include file="../include/admin/adminNavigator.jsp"%>


<title>Edit Property</title>

<div class="workingArea">
	<ol class="breadcrumb">
	  <li><a href="admin_category_list">All Category</a></li>
	  <li><a href="admin_property_list?cid=${p.category.id}">${p.category.name}</a></li>
	  <li class="active">Edit Property</li>
	</ol>

	<div class="panel panel-warning editDiv">
		<div class="panel-heading">Edit Property</div>
		<div class="panel-body">
			<form method="post" id="editForm" action="admin_property_update">
				<table class="editTable">
					<tr>
						<td>Property Name</td>
						<td><input id="name" name="name" value="${p.name}"
							type="text" class="form-control"></td>
					</tr>
					<tr class="submitTR">
						<td colspan="2" align="center">
						<input type="hidden" name="id" value="${p.id}">
						<input type="hidden" name="cid" value="${p.category.id}">
						<button type="submit" class="btn btn-success">Submit</button></td>
					</tr>
				</table>
			</form>
		</div>
	</div>
</div>