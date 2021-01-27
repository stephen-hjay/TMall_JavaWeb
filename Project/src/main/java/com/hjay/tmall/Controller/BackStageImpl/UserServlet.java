package com.hjay.tmall.Controller.BackStageImpl;

import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hjay.tmall.Controller.BackStageBaseServlet;
import com.hjay.tmall.Entity.Implement.User;
import com.hjay.tmall.Utils.Page;

/**
 * 增加交由前台用户注册功能
 * 删除不提供（用户信息是最重要的资料）
 * 修改不提供，应该由前台用户自己完成
 */

@WebServlet("/userServlet")
public class UserServlet extends BackStageBaseServlet {



	
	public String add(HttpServletRequest request, HttpServletResponse response, Page page) {

		return null;
	}

	
	public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
		return null;
	}

	
	public String edit(HttpServletRequest request, HttpServletResponse response, Page page) {
		return null;		
	}

	
	public String update(HttpServletRequest request, HttpServletResponse response, Page page) {
		return null;
	}

	
	public String list(HttpServletRequest request, HttpServletResponse response, Page page) {
		List<User> us = userDAO.list(page.getStart(),page.getCount());
		int total = userDAO.getTotal();
		page.setTotal(total);
		
		request.setAttribute("us", us);
		request.setAttribute("page", page);
		
		return "admin/listUser.jsp";
	}
}
