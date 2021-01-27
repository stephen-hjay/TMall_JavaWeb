package com.hjay.tmall.Controller.BackStageImpl;

import java.util.Date;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hjay.tmall.Controller.BackStageBaseServlet;
import com.hjay.tmall.Entity.Implement.Category;
import com.hjay.tmall.Entity.Implement.Product;
import com.hjay.tmall.Entity.Implement.Property;
import com.hjay.tmall.Entity.Implement.PropertyValue;
import com.hjay.tmall.Utils.Error;
import com.hjay.tmall.Utils.Page;

@WebServlet("/productServlet")
public class ProductServlet extends BackStageBaseServlet {
	
	public String add(HttpServletRequest request, HttpServletResponse response, Page page) {
		//
		int cid = Integer.parseInt(request.getParameter("cid"));
		Category c = categoryDAO.getById(cid);
		if (c == null){
			request.setAttribute("error",new Error("Not Category Id!"));
			// goes to the item not found page
			return "admin/404.jsp";
		}

		String name= request.getParameter("name");
		String subTitle= request.getParameter("subTitle");
		float orignalPrice = Float.parseFloat(request.getParameter("orignalPrice"));
		float promotePrice = Float.parseFloat(request.getParameter("promotePrice"));
		int stock = Integer.parseInt(request.getParameter("stock"));
		Product p = new Product();
		p.setCategory(c);
		p.setName(name);
		p.setSubTitle(subTitle);
		p.setOrignalPrice(orignalPrice);
		p.setPromotePrice(promotePrice);
		p.setStock(stock);
		p.setCreateDate(new Date());
		productDAO.add(p);
		return "@admin_product_list?cid="+cid;
	}

	
	public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
		int id = Integer.parseInt(request.getParameter("id"));
		Product p = productDAO.getById(id);
		if (p == null){
			request.setAttribute("error",new Error("Not Product Id!"));
			// goes to the item not found page
			return "admin/404.jsp";
		}
		productDAO.delete(id);
		return "@admin_product_list?cid="+p.getCategory().getId();
	}


	public String edit(HttpServletRequest request, HttpServletResponse response, Page page) {
		int id = Integer.parseInt(request.getParameter("id"));
		Product p = productDAO.getById(id);
		if (p == null){
			request.setAttribute("error",new Error("Not Product Id!"));
			// goes to the item not found page
			return "admin/404.jsp";
		}
		request.setAttribute("p", p);
		return "admin/editProduct.jsp";		
	}
	
	public String editPropertyValue(HttpServletRequest request, HttpServletResponse response, Page page) {
		int id = Integer.parseInt(request.getParameter("id"));
		Product p = productDAO.getById(id);
		if (p == null){
			request.setAttribute("error",new Error("Not Product Id!"));
			// goes to the item not found page
			return "admin/404.jsp";
		}
		request.setAttribute("p", p);

		List<Property> pts= propertyDAO.list(p.getCategory().getId());
		// fill all the property values with the records in database
		propertyValueDAO.init(p);

		List<PropertyValue> pvs = propertyValueDAO.list(p.getId());
		
		request.setAttribute("pvs", pvs);
		
		return "admin/editProductValue.jsp";		
	}

	public String updatePropertyValue(HttpServletRequest request, HttpServletResponse response, Page page) {
		int pvid = Integer.parseInt(request.getParameter("pvid"));
		String value = request.getParameter("value");
		
		PropertyValue pv =propertyValueDAO.getById(pvid);
		if (pv == null){
			request.setAttribute("error",new Error("Not PropertyValue Id!"));
			// goes to the item not found page
			return "admin/404.jsp";
		}
		pv.setValue(value);
		propertyValueDAO.update(pv);
		// send response to the front end with success

		// front-end use ajax to parse this response
		return "%success";
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
		int stock = Integer.parseInt(request.getParameter("stock"));
		float orignalPrice = Float.parseFloat(request.getParameter("orignalPrice"));
		float promotePrice = Float.parseFloat(request.getParameter("promotePrice"));
		String subTitle= request.getParameter("subTitle");
		String name= request.getParameter("name");
		
		Product p = new Product();

		p.setName(name);
		p.setSubTitle(subTitle);
		p.setOrignalPrice(orignalPrice);
		p.setPromotePrice(promotePrice);
		p.setStock(stock);
		p.setId(id);
		p.setCategory(c);		

		productDAO.update(p);
		return "@admin_product_list?cid="+p.getCategory().getId();
	}

	
	public String list(HttpServletRequest request, HttpServletResponse response, Page page) {
		int cid = Integer.parseInt(request.getParameter("cid"));
		Category c = categoryDAO.getById(cid);
		if (c == null){
			request.setAttribute("error",new Error("Not Category Id!"));
			// goes to the item not found page
			return "admin/404.jsp";
		}
		
		List<Product> ps = productDAO.list(cid, page.getStart(),page.getCount());
		
		int total = productDAO.getTotal(cid);
		page.setTotal(total);
		page.setParam("&cid="+c.getId());
		
		request.setAttribute("ps", ps);
		request.setAttribute("c", c);
		request.setAttribute("page", page);
		return "admin/listProduct.jsp";
	}
}
