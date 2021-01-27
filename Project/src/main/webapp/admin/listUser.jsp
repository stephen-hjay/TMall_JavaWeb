<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*"%>
 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="../include/admin/adminHeader.jsp"%>
<%@include file="../include/admin/adminNavigator.jsp"%>

<script>
</script>

<title>User Management</title>


<div class="workingArea">
	<h1 class="label label-info" >User Management</h1>

	<br>
	<br>
	
	<div class="listDataTableDiv">
		<table class="table table-striped table-bordered table-hover  table-condensed">
			<thead>
				<tr class="success">
					<th>ID</th>
					<th>User Name</th>
					<th>User Password</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${us}" var="u">
				<tr>
					<td>${u.id}</td>
					<td>${u.name}</td>
					<td>${u.password}</td>
				</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	
	<div class="pageDiv">
		<%@include file="../include/admin/adminPage.jsp" %>
	</div>
	
	
</div>

<%@include file="../include/admin/adminFooter.jsp"%>
