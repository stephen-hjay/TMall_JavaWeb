package com.hjay.tmall.Controller.ForeStageImpl;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hjay.tmall.Entity.Implement.*;
import com.hjay.tmall.GlobalParameters;
import com.hjay.tmall.Utils.Error;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.web.util.HtmlUtils;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hjay.tmall.Controller.ForeStageBaseServlet;
import com.hjay.tmall.Utils.Page;
import com.hjay.tmall.DAO.Implement.*;

@WebServlet("/foreServlet")
public class ForeServlet extends ForeStageBaseServlet {
	public String home(HttpServletRequest request, HttpServletResponse response, Page page) {
		List<Category> cs= new CategoryDAO().list();
		new ProductDAO().fill(cs);
		new ProductDAO().fillByRow(cs);
		request.setAttribute("cs", cs);
		return "home.jsp";
	}

	/**
	 * 注册功能
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 */

	public String register(HttpServletRequest request, HttpServletResponse response, Page page) {
		String name = request.getParameter("name");
		String password = request.getParameter("password");
		// 将特殊符号 转义

		/*注为什么要用 HtmlUtils.htmlEscape？
		因为有些同学在恶意注册的时候，会使用诸如 <script>alert('papapa')</script> 这样的名称，会导致网页打开就弹出一个对话框。
		那么在转义之后，就没有这个问题了。*/


		name = HtmlUtils.htmlEscape(name);


		//	System.out.println(name);
		boolean exist = userDAO.isExist(name);

		if (exist) {
			request.setAttribute("msg", "用户名已经被使用,不能使用");
			return "register.jsp";
		}

		User user = new User();
		user.setName(name);
		user.setPassword(password);
		System.out.println(user.getName());
		System.out.println(user.getPassword());
		userDAO.add(user);
		// redirect
		return "@registerSuccess.jsp";
	}


	public String login(HttpServletRequest request, HttpServletResponse response, Page page) {
		String name = request.getParameter("name");
		// 防止 恶意输入 密码
		name = HtmlUtils.htmlEscape(name);
		String password = request.getParameter("password");
		// request中 存的是对象
		User user = userDAO.getByUsrNameAndPwd(name,password);

		if(null==user){
			request.setAttribute("msg", "账号密码错误");
			return "login.jsp";
		}
		request.getSession().setAttribute("user", user);
		// redirect
		return "@forehome";
	}


	/**
	 * logout
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 */
	public String logout(HttpServletRequest request, HttpServletResponse response, Page page) {
		request.getSession().removeAttribute("user");
		// redirect
		return "@forehome";
	}


	public String product(HttpServletRequest request, HttpServletResponse response, Page page) {
		int pid = Integer.parseInt(request.getParameter("pid"));
		Product p = productDAO.getById(pid);
		if (p==null){
			request.setAttribute("error",new Error(GlobalParameters.ErrorMessage.INVALID_PRODUCT_ID));
			// goes to the item not found page
			return "admin/404.jsp";
		}


		List<ProductImage> productSingleImages = productImageDAO.list(p, ProductImageDAO.type_single);
		List<ProductImage> productDetailImages = productImageDAO.list(p, ProductImageDAO.type_detail);
		p.setProductSingleImages(productSingleImages);
		p.setProductDetailImages(productDetailImages);

		List<PropertyValue> pvs = propertyValueDAO.list(p.getId());

		List<Review> reviews = reviewDAO.list(p.getId());

		productDAO.setSaleAndReviewNumber(p);

		request.setAttribute("reviews", reviews);

		request.setAttribute("p", p);
		request.setAttribute("pvs", pvs);
		return "product.jsp";
	}







	public String checkLogin(HttpServletRequest request, HttpServletResponse response, Page page) {
		User user =(User) request.getSession().getAttribute("user");
		if(null!=user)
			return "%success";
		return "%fail";
	}

	public String loginAjax(HttpServletRequest request, HttpServletResponse response, Page page) {
		String name = request.getParameter("name");
		String password = request.getParameter("password");
		PrintWriter p;
		User user = userDAO.getByUsrNameAndPwd(name,password);

		if(null==user){
			request.setAttribute("msg", "Invalid Password or UserName");
			return "%fail";
		}
		request.getSession().setAttribute("user", user);
		return "%success";
	}



	public String search(HttpServletRequest request, HttpServletResponse response, Page page){
		String keyword = request.getParameter("keyword");
		List<Product> ps= new ProductDAO().search(keyword,0,20);
		productDAO.setSaleAndReviewNumber(ps); // 调用 review DAO  和 Sales DAO
		request.setAttribute("ps",ps);
		return "searchResult.jsp";
	}


	public String buyone(HttpServletRequest request, HttpServletResponse response, Page page) {
		int pid = Integer.parseInt(request.getParameter("pid"));
		int num = Integer.parseInt(request.getParameter("num"));
		Product p = productDAO.getById(pid);
		if (p==null){
			request.setAttribute("error",new Error(GlobalParameters.ErrorMessage.INVALID_PRODUCT_ID));
			// goes to the item not found page
			return "admin/404.jsp";
		}

		int oiid = 0;

		User user =(User) request.getSession().getAttribute("user");
		if(null==user)
			return "@login.jsp";
		boolean found = false;
		// a. 如果已经存在这个产品对应的OrderItem，并且还没有生成订单，即还在购物车中。
		// 那么就应该在对应的OrderItem基础上，调整数量
		List<OrderItem> ois = orderItemDAO.listByUser(user.getId());
		for (OrderItem oi : ois) {
			if(oi.getProduct().getId()==p.getId()){
				// 合并订单 orderItem
				oi.setNumber(oi.getNumber()+num);
				orderItemDAO.update(oi);
				found = true;
				oiid = oi.getId();
				break;
			}
		}
		// b. 如果不存在对应的OrderItem,那么就新增一个订单项OrderItem
		if(!found){
			OrderItem oi = new OrderItem();
			oi.setUser(user);
			oi.setNumber(num);
			oi.setProduct(p);
			orderItemDAO.add(oi);
			oiid = oi.getId();
		}

		// redirect


		return "@forebuy?oiid="+oiid;
	}


	public String buy(HttpServletRequest request, HttpServletResponse response, Page page){
		String[] oiids=request.getParameterValues("oiid");
		List<OrderItem> ois = new ArrayList<>();
		float total = 0;

		System.out.println(request.getAttribute("referer"));

		for (String strid : oiids) {
			int oiid = Integer.parseInt(strid);
			OrderItem oi= orderItemDAO.getById(oiid);
			// 计算 总价钱
			total +=oi.getProduct().getPromotePrice()*oi.getNumber();
			ois.add(oi);
		}

		request.getSession().setAttribute("ois", ois);
		request.setAttribute("total", total);
		return "buy.jsp";
	}

	public String addCart(HttpServletRequest request, HttpServletResponse response, Page page) {
		int pid = Integer.parseInt(request.getParameter("pid"));
		int num = Integer.parseInt(request.getParameter("num"));
		Product p = productDAO.getById(pid);
		if (p==null){
			request.setAttribute("error",new Error(GlobalParameters.ErrorMessage.INVALID_PRODUCT_ID));
			// goes to the item not found page
			return "%fail";
		}

		int oiid = 0;
		// request中 存的是对象
		User user =(User) request.getSession().getAttribute("user");
		if(null==user)
			return "@login.jsp";
		boolean found = false;


		// a. 如果已经存在这个产品对应的OrderItem，并且还没有生成订单，即还在购物车中。
		// 那么就应该在对应的OrderItem基础上，调整数量
		List<OrderItem> ois = orderItemDAO.listByUser(user.getId());
		for (OrderItem oi : ois) {
			if(oi.getProduct().getId()==p.getId()){
				// 合并订单 orderItem
				oi.setNumber(oi.getNumber()+num);
				orderItemDAO.update(oi);
				found = true;
				oiid = oi.getId();
				break;
			}
		}
		// b. 如果不存在对应的OrderItem,那么就新增一个订单项OrderItem
		if(!found){
			OrderItem oi = new OrderItem();
			oi.setUser(user);
			oi.setNumber(num);
			oi.setProduct(p);
			orderItemDAO.add(oi);
			oiid = oi.getId();
		}

		// redirect


		return "%success";
	}

	/**
	 * 登录状态 需要被拦截的
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 */
	public String cart(HttpServletRequest request, HttpServletResponse response, Page page) {
		User user =(User) request.getSession().getAttribute("user");
		if(null==user)
			return "@login.jsp";

		List<OrderItem> ois = orderItemDAO.listByUser(user.getId());
		request.setAttribute("ois", ois);
		return "cart.jsp";
	}


	public String changeOrderItem(HttpServletRequest request, HttpServletResponse response, Page page) {
		User user =(User) request.getSession().getAttribute("user");
		if(null==user)
			return "%fail";

		int pid = Integer.parseInt(request.getParameter("pid"));
		int number = Integer.parseInt(request.getParameter("number"));
		List<OrderItem> ois = orderItemDAO.listByUser(user.getId());
		for (OrderItem oi : ois) {
			if(oi.getProduct().getId()==pid){
				oi.setNumber(number);
				orderItemDAO.update(oi);
				break;
			}

		}
		return "%success";
	}

	public String deleteOrderItem(HttpServletRequest request, HttpServletResponse response, Page page){
		User user =(User) request.getSession().getAttribute("user");
		if(null==user)
			return "%fail";
		int oiid= 0;
		try{
			oiid = Integer.parseInt(request.getParameter("oiid"));
		}catch (NumberFormatException exception){
			request.setAttribute("error",new Error(GlobalParameters.ErrorMessage.INVALID_ORDERITEM_ID));
			// goes to the item not found page
			return "admin/404.jsp";
		}


		orderItemDAO.delete(oiid);
		return "%success";
	}


	public String createOrder(HttpServletRequest request, HttpServletResponse response, Page page){
		User user =(User) request.getSession().getAttribute("user");
		if(null==user)
			return "@login.jsp";

		List<OrderItem> ois= (List<OrderItem>) request.getSession().getAttribute("ois");
		if(ois.isEmpty())
			return "@login.jsp";

		String address = request.getParameter("address");
		String post = request.getParameter("post");
		String receiver = request.getParameter("receiver");
		String mobile = request.getParameter("mobile");
		String userMessage = request.getParameter("userMessage");

		Order order = new Order();
		String orderCode = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + RandomUtils.nextInt(1000,10000);

		order.setOrderCode(orderCode);
		order.setAddress(address);
		order.setPost(post);
		order.setReceiver(receiver);
		order.setMobile(mobile);
		order.setUserMessage(userMessage);
		order.setCreateDate(new Date());
		order.setUser(user);
		order.setStatus(GlobalParameters.OrderStatus.WAITDELIVERY);

		orderDAO.add(order);
		float total =0;
		for (OrderItem oi: ois) {
			oi.setOrder(order);
			orderItemDAO.update(oi);
			total+=oi.getProduct().getPromotePrice()*oi.getNumber();
		}

		return "@forealipay?oid="+order.getId() +"&total="+total;
	}

	public String alipay(HttpServletRequest request, HttpServletResponse response, Page page){
		// forward
		return "alipay.jsp";
	}

	public String payed(HttpServletRequest request, HttpServletResponse response, Page page) {
		int oid = Integer.parseInt(request.getParameter("oid"));
		Order order = orderDAO.getById(oid);
		order.setStatus(GlobalParameters.OrderStatus.WAITDELIVERY);
		order.setPayDate(new Date());
		new OrderDAO().update(order);
		request.setAttribute("o", order);
		return "payed.jsp";
	}

	public String bought(HttpServletRequest request, HttpServletResponse response, Page page) {
		User user =(User) request.getSession().getAttribute("user");
		List<Order> os= orderDAO.list(user.getId(),GlobalParameters.OrderStatus.DELETED);

		orderItemDAO.fill(os);

		request.setAttribute("os", os);

		return "bought.jsp";
	}

	/*
	 * 付完款 查看状态
	 */

	public String confirmPay(HttpServletRequest request, HttpServletResponse response, Page page) {
		int oid = Integer.parseInt(request.getParameter("oid"));
		Order o = orderDAO.getById(oid);
		orderItemDAO.fill(o);
		request.setAttribute("o", o);
		return "confirmPay.jsp";
	}



	public String orderConfirmed(HttpServletRequest request, HttpServletResponse response, Page page) {
		int oid = Integer.parseInt(request.getParameter("oid"));
		Order o = orderDAO.getById(oid);
		o.setStatus(GlobalParameters.OrderStatus.WAITREVIEW);
		o.setConfirmDate(new Date());
		orderDAO.update(o);
		return "orderConfirmed.jsp";
	}


	public String deleteOrder(HttpServletRequest request, HttpServletResponse response, Page page){
		int oid = Integer.parseInt(request.getParameter("oid"));
		Order o = orderDAO.getById(oid);
		o.setStatus(GlobalParameters.OrderStatus.DELETED);
		orderDAO.update(o);
		return "%success";
	}

	public String review(HttpServletRequest request, HttpServletResponse response, Page page) {
		int oid = Integer.parseInt(request.getParameter("oid"));
		Order o = orderDAO.getById(oid);
		orderItemDAO.fill(o);
		Product p = o.getOrderItems().get(0).getProduct();
		List<Review> reviews = reviewDAO.list(p.getId());
		productDAO.setSaleAndReviewNumber(p);
		request.setAttribute("p", p);
		request.setAttribute("o", o);
		request.setAttribute("reviews", reviews);
		return "review.jsp";
	}


	public String doreview(HttpServletRequest request, HttpServletResponse response, Page page) {
		int oid = Integer.parseInt(request.getParameter("oid"));
		Order o = orderDAO.getById(oid);
		o.setStatus(GlobalParameters.OrderStatus.FINISH);
		orderDAO.update(o);
		int pid = Integer.parseInt(request.getParameter("pid"));
		Product p = productDAO.getById(pid);

		String content = request.getParameter("content");

		content = HtmlUtils.htmlEscape(content);

		User user =(User) request.getSession().getAttribute("user");
		Review review = new Review();
		review.setContent(content);
		review.setProduct(p);
		review.setCreateDate(new Date());
		review.setUser(user);
		reviewDAO.add(review);

		return "@forereview?oid="+oid+"&showonly=true";
	}


	/**
	 *  计算月销量
	 * for(OrderItem oi:ois) {
	 * 			Product p=oi.getProduct();
	 * 			List<Review> reviews=reviewDAO.list(p.getId());
	 * 			for(OrderItem oi2:orderItemDAO.listByProduct(p.getId())) {
	 * 				int oid2=oi2.getOrder().getId();
	 * 				Order o2=orderDAO.get(oid2);
	 * 				Date date=o2.getCreateDate();
	 * 				Date date2=new Date();
	 * 				SimpleDateFormat sdf=new SimpleDateFormat("MM");
	 * 				int time=Integer.parseInt(sdf.format(date));
	 * 				int time2=Integer.parseInt(sdf.format(date2));
	 * 				//System.out.println(time+" "+time2);
	 * 				if(time==time2) {
	 * 					monthNumber+=oi2.getNumber();
	 * 					//System.out.println(monthNumber);
	 *                                }* 			}
	 * 			productDAO.setSaleAndReviewNumber(p);
	 * 			p.setmonuthNumber(monthNumber);
	 * 			monthNumber=0;
	 * 			p.setProductReviews(reviews);
	 * 			ps.add(p);
	 * 		}
	 */

}
