package com.hjay.tmall.Controller.BackStageImpl;

import java.util.Date;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hjay.tmall.Controller.BackStageBaseServlet;
import com.hjay.tmall.Entity.Implement.Order;
import com.hjay.tmall.GlobalParameters;
import com.hjay.tmall.Utils.Error;
import com.hjay.tmall.Utils.Page;

@WebServlet("/orderServlet")
public class OrderServlet extends BackStageBaseServlet {

	
	public String add(HttpServletRequest request, HttpServletResponse response, Page page) {
		return null;
	}

	
	public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
		return null;
	}

	public String delivery(HttpServletRequest request, HttpServletResponse response, Page page) {
		int id = Integer.parseInt(request.getParameter("id"));
		Order o = orderDAO.getById(id);
		if (o==null){
			request.setAttribute("error",new Error(GlobalParameters.ErrorMessage.INVALID_ORDER_ID));
			// goes to the item not found page
			return "admin/404.jsp";
		}
		o.setDeliveryDate(new Date());
		o.setStatus(GlobalParameters.OrderStatus.WAITCONFIRM);
		orderDAO.update(o);
		return "@admin_order_list";
	}

	
	public String edit(HttpServletRequest request, HttpServletResponse response, Page page) {
		return null;	
	}

	
	public String update(HttpServletRequest request, HttpServletResponse response, Page page) {
		return null;
	}

	
	public String list(HttpServletRequest request, HttpServletResponse response, Page page) {
		List<Order> os = orderDAO.list(page.getStart(),page.getCount());
		// fill the order with amount and money
		orderItemDAO.fill(os);
		int total = orderDAO.getTotal();
		page.setTotal(total);
		
		request.setAttribute("os", os);
		request.setAttribute("page", page);
		
		return "admin/listOrder.jsp";
	}
}
