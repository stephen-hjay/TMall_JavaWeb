<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="java.util.*"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="../include/admin/adminHeader.jsp"%>
<%@include file="../include/admin/adminNavigator.jsp"%>

<script>
	$(function() {

		$("#addForm").submit(function() {
			if (checkEmpty("name", "属性名称"))
				return true;
			return false;
		});
	});
</script>

<title>属性管理</title>


<div class="workingArea">

	<ol class="breadcrumb">
	  <li><a href="admin_category_list">All Categories</a></li>
	  <li><a href="admin_property_list?cid=${c.id}">${c.name}</a></li>
	  <li class="active">Property Management</li>
	</ol>



	<div class="listDataTableDiv">
		<table
			class="table table-striped table-bordered table-hover  table-condensed">
			<thead>
				<tr class="success">
					<th>ID</th>
					<th>Property Name</th>
					<th>Edit</th>
					<th>Delete</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${ps}" var="p">

					<tr>
						<td>${p.id}</td>
						<td>${p.name}</td>
						<td><a href="admin_property_edit?id=${p.id}"><span
								class="glyphicon glyphicon-edit"></span></a></td>
						<td><a deleteLink="true"
							href="admin_property_delete?id=${p.id}"><span
								class=" 	glyphicon glyphicon-trash"></span></a></td>

					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>

	<div class="pageDiv">
		<%@include file="../include/admin/adminPage.jsp"%>
	</div>

	<div class="panel panel-warning addDiv">
		<div class="panel-heading">New Property</div>
		<div class="panel-body">
			<form method="post" id="addForm" action="admin_property_add">
				<table class="addTable">
					<tr>
						<td>Property Name</td>
						<td><input id="name" name="name" type="text"
							class="form-control"></td>
					</tr>
					<tr class="submitTR">
						<td colspan="2" align="center">
<%--							a hidden domain not to reveal the info to the client--%>
							<input type="hidden" name="cid" value="${c.id}">
							<button type="submit" class="btn btn-success">Submit</button>
						</td>
					</tr>
				</table>
			</form>
		</div>
	</div>

</div>

<%@include file="../include/admin/adminFooter.jsp"%>
