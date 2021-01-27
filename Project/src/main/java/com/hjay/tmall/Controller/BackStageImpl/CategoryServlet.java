package com.hjay.tmall.Controller.BackStageImpl;

import com.hjay.tmall.Controller.BackStageBaseServlet;
import com.hjay.tmall.Entity.Implement.Category;
import com.hjay.tmall.Utils.ImageUtils;
import com.hjay.tmall.Utils.Page;


import javax.imageio.ImageIO;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/categoryServlet")
public class CategoryServlet extends BackStageBaseServlet {
    /**
     * handle the item adding :
     * @param request
     * @param response
     * @param page
     * @return
     */
        public String add(HttpServletRequest request, HttpServletResponse response, Page page) {
            // 由于 是以二进制多媒体数据上传的，所以需要特别处理才可以获得 name domain
            Map<String,String> params = new HashMap<>();
            // 1. parseUpload 获取上传文件的输入流
            InputStream inputStream = super.parseUpload(request, params);
            //  2. parseUpload 方法会修改params 参数，并且把浏览器提交的name信息放在其中

            // get the params and inputstream
            String name= params.get("name");

            // 3. 从params 中取出name信息，并根据这个name信息，借助categoryDAO，向数据库中插入数据
            Category c = new Category();
            c.setName(name);
            // c get the primary key value
            categoryDAO.add(c);

            // 4. 根据request.getServletContext().getRealPath( "img/category")，定位到存放分类图片的目录
            File imageFolder= new File(request.getSession().getServletContext().getRealPath("img/category"));
            File file = new File(imageFolder,c.getId()+".jpg");

            try {
                if(null!=inputStream && 0!=inputStream.available()){
                    try(FileOutputStream fileOutputStream = new FileOutputStream(file)){
                        byte b[] = new byte[1024 * 1024];
                        int length = 0;
                        while (-1 != (length = inputStream.read(b))) {
                            fileOutputStream.write(b, 0, length);
                        }
                        fileOutputStream.flush();
                        //通过如下代码，把文件保存为jpg格式
                        BufferedImage img = ImageUtils.change2jpg(file);
                        ImageIO.write(img, "jpg", file);
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // redirect
            return "@admin_category_list";
        }

        public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
            int id = Integer.parseInt(request.getParameter("id"));
            categoryDAO.delete(id);
            return "@admin_category_list";
        }

        public String edit(HttpServletRequest request, HttpServletResponse response, Page page) {
            int id = Integer.parseInt(request.getParameter("id"));
            Category c = categoryDAO.getById(id);
            request.setAttribute("c", c);
            return "admin/editCategory.jsp";
        }

        public String update(HttpServletRequest request, HttpServletResponse response, Page page) {
            Map<String,String> params = new HashMap<>();

            // let the parseUpload handle the non-text request data and get an inputstream

            // 1. parseUpload 获取上传文件的输入流
            InputStream is = super.parseUpload(request, params);

            //  2. parseUpload 方法会修改params 参数，并且把浏览器提交的name信息放在其中
            String name= params.get("name");
            int id = Integer.parseInt(params.get("id"));
            // 3. 从params 中取出name信息，并根据这个name信息，借助categoryDAO，向数据库中插入数据
            Category c = new Category();
            c.setId(id);
            c.setName(name);
            categoryDAO.update(c);
            // 4. 根据request.getServletContext().getRealPath( "img/category")，定位到存放分类图片的目录
            File  imageFolder= new File(request.getServletContext().getRealPath("img/category"));
            // 5. 文件命名以保存到数据库的分类对象的id+".jpg"的格式命名
            File file = new File(imageFolder,c.getId()+".jpg");
            file.getParentFile().mkdirs();

            try {
                if(null!=is && 0!=is.available()){
                    try(FileOutputStream fos = new FileOutputStream(file)){
                        byte b[] = new byte[1024 * 1024];
                        int length = 0;
                        while (-1 != (length = is.read(b))) {
                            fos.write(b, 0, length);
                        }
                        fos.flush();
                        //通过如下代码，把文件保存为jpg格式
                        BufferedImage img = ImageUtils.change2jpg(file);
                        ImageIO.write(img, "jpg", file);
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return "@admin_category_list";

        }

        public String list(HttpServletRequest request, HttpServletResponse response, Page page) {


            List<Category> categories = categoryDAO.list(page.getStart(),page.getCount());
            // set total pages
            int total = categoryDAO.getTotal();
            page.setTotal(total);

            request.setAttribute("thecs", categories);
            request.setAttribute("page", page);

            return "admin/listCategory.jsp";
        }
    }
