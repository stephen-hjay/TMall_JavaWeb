
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>

	
<script>
$(function(){
	
	<c:if test="${!empty msg}">
	$("span.errorMessage").html("${msg}");
	$("div.registerErrorMessageDiv").css("visibility","visible");		
	</c:if>


	$("input#password").keyup(function(){

		var txt=$(this).val(); //获取密码框内容
		var len=txt.length; //获取内容长度

		if(txt=='' || len<6){
			$("label").show();
			$("label").addClass("tips");
		}else {
			$("label").hide();
		}

		checkpwd($(this));
	});

	//全部都是灰色的
	function primary(){
		$("p.pwdColor span").removeClass("co1,co2,co3");
	}

	//密码强度为弱的时候
	function weak(){
		$("span.c1").addClass("co1");
		$("span.c2").removeClass("co2");
		$("span.c3").removeClass("co3");
	}
	//密码强度为中等的时候
	function middle(){
		$("span.c1").addClass("co1");
		$("span.c2").addClass("co2");
		$("span.c3").removeClass("co3");
	}

	//密码强度为强的时候
	function strong(){
		$("span.c1").addClass("co1");
		$("span.c2").addClass("co2");
		$("span.c3").addClass("co3");
	}

	/**判断密码的强弱规则
	 1、如果是单一的字符（全是数字 或 字母 ）长度小于 6  弱
	 2、如果是两两混合 (数字+字母（大小） 或 数字+特殊字符  或 特殊字符+字母  长度大于 8  中)
	 3、如果是三者组合 (数字 +大写字母+小写字母 或 数字+字母+特殊字符 长度>8  强）)
	 **/

	//密码强弱判断函数

	function checkpwd(obj){
		var txt = $.trim(obj.val());//输入框内容 trim处理两端空格
		var len = txt.length;
		var num = /\d/.test(txt);//匹配数字
		var small = /[a-z]/.test(txt);//匹配小写字母
		var big = /[A-Z]/.test(txt);//匹配大写字母
		var corps = /\W/.test(txt);//特殊符号
		var val = num + small+big+corps; //四个组合


		if(len<1){
			primary();
		}else if(len<6){
			weak();
		}else if(len>6 && len<=8){
			if(val==1){
				weak();
			}else if(val==2){
				middle();
			}
		}else if(len>8){
			if(val==1){
				weak();
			}else if(val==2){
				middle();
			}else if(val==3){
				strong();
			}
			return false;
		}

	}


	
	$(".registerForm").submit(function(){
		if(0==$("#name").val().length){
			$("span.errorMessage").html("请输入用户名");
			$("div.registerErrorMessageDiv").css("visibility","visible");			
			return false;
		}


		if(0==$("#password").val().length){
			$("span.errorMessage").html("请输入密码");
			$("div.registerErrorMessageDiv").css("visibility","visible");			
			return false;
		}

		var pattern = /^.*(?=.{8,20})(?=.*\d)(?=.*[A-Z]{2,})(?=.*[a-z]{2,})(?=.*[!@#$%^&*?\(\)]).*$/;
		if(!pattern.test($("#password").val())){
			$("span.errorMessage").html("最短8位，最长20位\n" +
					"必须包含1个数字\n" +
					"必须包含2个小写字母\n" +
					"必须包含2个大写字母\n" +
					"必须包含1个特殊字符");
			$("div.registerErrorMessageDiv").css("visibility","visible");
			return false;
		}


		if(0==$("#repeatpassword").val().length){
			$("span.errorMessage").html("请输入重复密码");
			$("div.registerErrorMessageDiv").css("visibility","visible");			
			return false;
		}

		if($("#password").val() !=$("#repeatpassword").val()){
			$("span.errorMessage").html("重复密码不一致");
			$("div.registerErrorMessageDiv").css("visibility","visible");			
			return false;
		}		

		return true;
	});
})
</script>


<%--form提交数据到路径 foreregister,导致ForeServlet.register()方法被调用--%>
<form method="post" action="foreregister" class="registerForm">


<div class="registerDiv">
	<div class="registerErrorMessageDiv">
		<div class="alert alert-danger" role="alert">
		  <button type="button" class="close" data-dismiss="alert" aria-label="Close"></button>
		  	<span class="errorMessage"></span>
		</div>		
	</div>

	
	<table class="registerTable" align="center">
		<tr>
			<td  class="registerTip registerTableLeftTD">Set User Name</td>
			<td></td>
		</tr>
		<tr>
			<td class="registerTableLeftTD">User Name</td>
			<td  class="registerTableRightTD"><input id="name" name="name" placeholder="Once registered, user name could not be changed." > </td>
		</tr>
		<tr>
			<td  class="registerTip registerTableLeftTD">Set Password</td>
			<td  class="registerTableRightTD">Set password to protect your account</td>
		</tr>		
		<tr>
			<td class="registerTableLeftTD">Password</td>
			<td class="registerTableRightTD">
				<input id="password" name="password" type="password"  placeholder="Set your password" >
				<p class="pwdColor">
					<span class="c1"></span>
					<span class="c2"></span>
					<span class="c3"></span>
				</p>
				<p class="pwdText">
					<span>Weak</span>
					<span>Medium</span>
					<span>Strong</span>
				</p>

			</td>

		</tr>
		<tr>
			<td class="registerTableLeftTD">Password Repeat</td>
			<td class="registerTableRightTD"><input id="repeatpassword" type="password"   placeholder="Please repeat your password" > </td>
		</tr>
				
		<tr>
			<td colspan="2" class="registerButtonTD">
				<a href="registerSuccess.jsp"><button>Submit</button></a>
			</td>
		</tr>				
	</table>
</div>


</form>