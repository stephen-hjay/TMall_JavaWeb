<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>

<nav class="top ">
		<a href="${contextPath}">
			<span style="color:#C40000;margin:0px" class=" glyphicon glyphicon-home redColor"></span>
			TMall HomePage
		</a>	
		
		<span>Welcome To TMall</span>

<%--	这里会根据用户是否登录，决定是否显示退出按钮，或者登录注册按钮，以及购物车中的商品数量--%>

		<c:if test="${!empty user}">
			<a href="login.jsp">${user.name}</a>
			<a href="forelogout">Exit</a>
		</c:if>
		
		<c:if test="${empty user}">
			<a href="login.jsp">Sign In</a>
			<a href="register.jsp">Sign Up</a>
		</c:if>


		<span class="pull-right">
			<a href="forebought">Orders</a>
			<a href="forecart">
			<span style="color:#C40000;margin:0px" class=" glyphicon glyphicon-shopping-cart redColor" id="shoppingCart"></span>
			Shopping Cart:<strong>${cartTotalItemNumber}</strong></a>
		</span>
		
		
</nav>



