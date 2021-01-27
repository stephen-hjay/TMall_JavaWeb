<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>


<%--user login modal --%>
<div class="modal " id="loginModal" tabindex="-1" role="dialog" >
	<div class="modal-dialog loginDivInProductPageModalDiv">
	        <div class="modal-content">
					<div class="loginDivInProductPage">
						<div class="loginErrorMessageDiv">
							<div class="alert alert-danger" >
							  <button type="button" class="close" data-dismiss="alert" aria-label="Close"></button>
							  	<span class="errorMessage"></span>
							</div>
						</div>


							
						<div class="login_acount_text">User Log In</div>
						<div class="loginInput " >
							<span class="loginInputIcon ">
								<span class=" glyphicon glyphicon-user"></span>
							</span>
							<input id="name" name="name" placeholder="Phone/Email/User Name" type="text">
						</div>
						
						<div class="loginInput " >
							<span class="loginInputIcon ">
								<span class=" glyphicon glyphicon-lock"></span>
							</span>
							<input id="password" name="password"  type="password" placeholder="Password" type="text">
						</div>
									<span class="text-danger">Do Not Input Authentic Password</span><br><br>
						<div>
							<a href="#nowhere">Forget Password?</a>
							<a href="register.jsp" class="pull-right">Sign Up</a>
						</div>
						<div style="margin-top:20px">
							<button class="btn btn-block redButton loginSubmitButton" type="submit">Submit</button>
						</div>
					</div>	
	      </div>
	</div>
</div>

<%--删除模态--%>
<div class="modal" id="deleteConfirmModal" tabindex="-1" role="dialog" >
	<div class="modal-dialog deleteConfirmModalDiv">
       <div class="modal-content">
          <div class="modal-header">
            <button data-dismiss="modal" class="close" type="button"><span aria-hidden="true">×</span><span class="sr-only">Close</span></button>
            <h4 class="modal-title">确认删除？</h4>
          </div>
          <div class="modal-footer">
            <button data-dismiss="modal" class="btn btn-default" type="button">关闭</button>
            <button class="btn btn-primary deleteConfirmButton" id="submit" type="button">确认</button>
          </div>
        </div>
      </div>
	</div>
</div>