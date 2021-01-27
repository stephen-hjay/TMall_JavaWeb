<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*"%>
 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="../include/admin/adminHeader.jsp"%>
<%@include file="../include/admin/adminNavigator.jsp"%>

<script>
$(function(){
	$("button.orderPageCheckOrderItems").click(function(){
		var oid = $(this).attr("oid");
		$("tr.orderPageOrderItemTR[oid="+oid+"]").toggle();
	});
});

</script>

<title>Order Management</title>


<div class="workingArea">
	<h1 class="label label-info" >Order Management</h1>
	<br>
	<br>
	
	<div class="listDataTableDiv">
		<table class="table table-striped table-bordered table-hover1  table-condensed">
			<thead>
				<tr class="success">
					<th>ID</th>
					<th>Status</th>
					<th>Amount</th>
					<th width="100px">Quantity</th>
					<th width="100px">User Name</th>
					<th>Created Time</th>
					<th>Pay Time</th>
					<th>Delivery Time</th>
					<th>Confirm Time</th>
					<th width="120px">Manipulation</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${os}" var="o">
					<tr>
						<td>${o.id}</td>
						<td>${o.statusDesc}</td>
						<td>￥<fmt:formatNumber type="number" value="${o.total}" minFractionDigits="2"/></td>
						<td align="center">${o.totalNumber}</td>
						<td align="center">${o.user.name}</td>
						
						<td><fmt:formatDate value="${o.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						<td><fmt:formatDate value="${o.payDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						<td><fmt:formatDate value="${o.deliveryDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						<td><fmt:formatDate value="${o.confirmDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>

						<td>
							<button oid=${o.id} class="orderPageCheckOrderItems btn btn-primary btn-xs">Details</button>
							
							<c:if test="${o.status=='waitDelivery'}">
								<a href="admin_order_delivery?id=${o.id}">
									<button class="btn btn-primary btn-xs">Deliver</button>
								</a>							
							</c:if>
						</td>
					</tr>
					<tr class="orderPageOrderItemTR"  oid=${o.id}>
						<td colspan="10" align="center">
							
							<div  class="orderPageOrderItem">
								<table width="800px" align="center" class="orderPageOrderItemTable">
									<c:forEach items="${o.orderItems}" var="oi">
										<tr>
											<td align="left">
												<img width="40px" height="40px" src="img/productSingle/${oi.product.firstProductImage.id}.jpg">
											</td>	
											
											<td>
												<a href="foreproduct?pid=${oi.product.id}">
													<span>${oi.product.name}</span>
												</a>											
											</td>
											<td align="right">
											
												<span class="text-muted">${oi.number}个</span>												
											</td>
											<td align="right">
											
												<span class="text-muted">Unit Price：￥${oi.product.promotePrice}</span>
											</td>

										</tr>
									</c:forEach>
								
								</table>
							</div>
						
						</td>
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
