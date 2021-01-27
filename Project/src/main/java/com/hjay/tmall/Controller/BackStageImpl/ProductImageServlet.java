package com.hjay.tmall.Controller.BackStageImpl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hjay.tmall.Controller.BackStageBaseServlet;
import com.hjay.tmall.DAO.Implement.ProductImageDAO;
import com.hjay.tmall.Entity.Implement.Product;
import com.hjay.tmall.Entity.Implement.ProductImage;
import com.hjay.tmall.GlobalParameters;
import com.hjay.tmall.Utils.Error;
import com.hjay.tmall.Utils.ImageUtils;
import com.hjay.tmall.Utils.Page;

@WebServlet("/productImageServlet")
public class ProductImageServlet extends BackStageBaseServlet {

	public String add(HttpServletRequest request, HttpServletResponse response, Page page) {
		//上传文件的输入流
		InputStream is = null;

		//提交上传文件时的其他参数
		Map<String,String> params = new HashMap<>();

		//解析上传 获取 图片上传流
        is = parseUpload(request, params);		
		
        //根据上传的参数生成productImage对象
		String type= params.get("type");// 图片类型
		int pid = Integer.parseInt(params.get("pid"));
		Product p = productDAO.getById(pid);
		if (p == null){
			request.setAttribute("error",new Error(GlobalParameters.ErrorMessage.INVALID_PRODUCT_ID));
			// goes to the item not found page
			return "admin/404.jsp";
		}
		
		ProductImage pi = new ProductImage();
		pi.setType(type);
		pi.setProduct(p);
		productImageDAO.add(pi);
		
		
		//生成文件
    	String fileName = pi.getId()+ ".jpg";
        String imageFolder;
        String imageFolder_small=null;
        String imageFolder_middle=null;
        if(ProductImageDAO.type_single.equals(pi.getType())){
        	imageFolder= request.getSession().getServletContext().getRealPath("img/productSingle");
        	imageFolder_small= request.getSession().getServletContext().getRealPath("img/productSingle_small");
        	imageFolder_middle= request.getSession().getServletContext().getRealPath("img/productSingle_middle");
        } else{
			imageFolder= request.getSession().getServletContext().getRealPath("img/productDetail");
		}

        File f = new File(imageFolder, fileName);
        f.getParentFile().mkdirs();
        
        // 复制文件
		try {
			if(null!=is && 0!=is.available()){
			    try(FileOutputStream fos = new FileOutputStream(f)){
			        byte b[] = new byte[1024 * 1024];
			        int length = 0;
			        while (-1 != (length = is.read(b))) {
			            fos.write(b, 0, length);
			        }
			        fos.flush();
			        //通过如下代码，把文件保存为jpg格式
			        BufferedImage img = ImageUtils.change2jpg(f);
			        ImageIO.write(img, "jpg", f);

			        // single type needs to generate two smaller pics
			        if(ProductImageDAO.type_single.equals(pi.getType())){
			        	File f_small = new File(imageFolder_small, fileName);
			        	File f_middle = new File(imageFolder_middle, fileName);
			        	ImageUtils.resizeImage(f, 56, 56, f_small);
			        	ImageUtils.resizeImage(f, 217, 190, f_middle);
			        }
			    } catch(Exception e){
			    	e.printStackTrace();
			    }
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "@admin_productImage_list?pid="+p.getId();
	}

	public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
		int id = Integer.parseInt(request.getParameter("id"));
		ProductImage pi = productImageDAO.getById(id);
		if (pi == null){
			request.setAttribute("error",new Error(GlobalParameters.ErrorMessage.INVALID_PRODUCT_IMAGE_ID));
			// goes to the item not found page
			return "admin/404.jsp";
		}
		productImageDAO.delete(id);
		
        
        if(ProductImageDAO.type_single.equals(pi.getType())){
        	String imageFolder_single= request.getSession().getServletContext().getRealPath("img/productSingle");
        	String imageFolder_small= request.getSession().getServletContext().getRealPath("img/productSingle_small");
        	String imageFolder_middle= request.getSession().getServletContext().getRealPath("img/productSingle_middle");

        	// file delete
        	File f_single =new File(imageFolder_single,pi.getId()+".jpg");
    		f_single.delete();
    		File f_small =new File(imageFolder_small,pi.getId()+".jpg");
    		f_small.delete();
    		File f_middle =new File(imageFolder_middle,pi.getId()+".jpg");
    		f_middle.delete();
        } else{
        	String imageFolder_detail= request.getSession().getServletContext().getRealPath("img/productDetail");
    		File f_detail =new File(imageFolder_detail,pi.getId()+".jpg");
    		f_detail.delete();        	
        }
		return "@admin_productImage_list?pid="+pi.getProduct().getId();
	}

	
	public String edit(HttpServletRequest request, HttpServletResponse response, Page page) {
		return null;		
	}

	
	public String update(HttpServletRequest request, HttpServletResponse response, Page page) {
		return null;	
	}

	
	public String list(HttpServletRequest request, HttpServletResponse response, Page page) {
		int pid = Integer.parseInt(request.getParameter("pid"));
		Product p =productDAO.getById(pid);
		if (p == null){
			request.setAttribute("error",new Error(GlobalParameters.ErrorMessage.INVALID_PRODUCT_ID));
			// goes to the item not found page
			return "admin/404.jsp";
		}
		List<ProductImage> pisSingle = productImageDAO.list(p, ProductImageDAO.type_single);
		List<ProductImage> pisDetail = productImageDAO.list(p, ProductImageDAO.type_detail);
		
		request.setAttribute("p", p);
		request.setAttribute("pisSingle", pisSingle);
		request.setAttribute("pisDetail", pisDetail);
		
		return "admin/listProductImage.jsp";
	}
}
