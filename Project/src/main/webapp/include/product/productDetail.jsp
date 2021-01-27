

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>

	
<div class="productDetailDiv" >
	<div class="productDetailTopPart">
		<a href="#nowhere" class="productDetailTopPartSelectedLink selected">Details</a>
		<a href="#nowhere" class="productDetailTopReviewLink"> Review<span class="productDetailTopReviewLinkNumber">${p.reviewCount}</span> </a>
	</div>
	
	
	<div class="productParamterPart">
		<div class="productParamter">Product Property Values：</div>

		<%--	1、fn:substring方法是按首尾索引值截取子字符串，且包含子串的首字符索引值，不含尾字符索引值；	--%>
		<div class="productParamterList">
			<c:forEach items="${pvs}" var="pv">
				<span>${pv.property.name}:  ${fn:substring(pv.value, 0, 10)} </span>
			</c:forEach>
		</div>
		<div style="clear:both"></div>
	</div>
	
	<div class="productDetailImagesPart">
		<c:forEach items="${p.productDetailImages}" var="pi">
			<img src="img/productDetail/${pi.id}.jpg">
		</c:forEach>
	</div>
</div>

