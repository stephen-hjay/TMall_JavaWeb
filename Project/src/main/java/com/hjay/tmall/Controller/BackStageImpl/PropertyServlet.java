package com.hjay.tmall.Controller.BackStageImpl;

import com.hjay.tmall.Controller.BackStageBaseServlet;
import com.hjay.tmall.Entity.Implement.Category;
import com.hjay.tmall.Entity.Implement.Property;
import com.hjay.tmall.Utils.Error;
import com.hjay.tmall.Utils.Page;

import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/propertyServlet")
public class PropertyServlet extends BackStageBaseServlet {

	
	public String add(HttpServletRequest request, HttpServletResponse response, Page page) {
		// 这个 property 是绑定了category的
		int cid = Integer.parseInt(request.getParameter("cid"));
		Category c = categoryDAO.getById(cid);
		if (c == null){
			request.setAttribute("error",new Error("Not Category Id!"));
			// goes to the item not found page
			return "admin/404.jsp";
		}
		String name= request.getParameter("name");
		Property p = new Property();
		// one to multiple, thus property should set the category it belongs to
		p.setCategory(c);
		p.setName(name);
		propertyDAO.add(p);
		return "@admin_property_list?cid="+cid;
	}

	
	public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
		int id = Integer.parseInt(request.getParameter("id"));
		Property p = propertyDAO.getById(id);
		if (p == null){
			request.setAttribute("error",new Error("Not Product Id!"));
			// goes to the item not found page
			return "admin/404.jsp";
		}
		propertyDAO.delete(id);
		return "@admin_property_list?cid="+p.getCategory().getId();
	}


	public String edit(HttpServletRequest request, HttpServletResponse response, Page page) {
		int id = Integer.parseInt(request.getParameter("id"));
		Property p = propertyDAO.getById(id);
		if (p == null){
			request.setAttribute("error",new Error("Not Product Id!"));
			// goes to the item not found page
			return "admin/404.jsp";
		}
		request.setAttribute("p", p);
		return "admin/editProperty.jsp";		
	}

	
	public String update(HttpServletRequest request, HttpServletResponse response, Page page) {
		int cid = Integer.parseInt(request.getParameter("cid"));
		Category c = categoryDAO.getById(cid);
		if (c == null){
			request.setAttribute("error",new Error("Not Category Id!"));
			// goes to the item not found page
			return "admin/404.jsp";
		}
		
		int id = Integer.parseInt(request.getParameter("id"));
		String name= request.getParameter("name");
		Property p = new Property();
		p.setCategory(c);
		p.setId(id);
		p.setName(name);
		propertyDAO.update(p);
		return "@admin_property_list?cid="+p.getCategory().getId();
	}

	
	public String list(HttpServletRequest request, HttpServletResponse response, Page page) {
		// get request pass parameter
		int cid = Integer.parseInt(request.getParameter("cid"));
		Category c = categoryDAO.getById(cid);
		// if category not found then redirect to 404
		if (c == null){
			request.setAttribute("error",new Error("Not Category Id!"));
			// goes to the item not found page
			return "admin/404.jsp";
		}
		// select based on the value of category
		List<Property> ps = propertyDAO.list(cid, page.getStart(),page.getCount());
		int total = propertyDAO.getTotal(cid);
		page.setTotal(total);
		// set the cid passed to jsp
		// this is used for concatenating the get request parameters
		// <a  href="?page.start=${page.start-page.count}${page.param}" aria-label="Previous" >
		page.setParam("&cid="+c.getId());
		request.setAttribute("ps", ps);
		request.setAttribute("c", c);
		request.setAttribute("page", page);
		return "admin/listProperty.jsp";
	}
}
