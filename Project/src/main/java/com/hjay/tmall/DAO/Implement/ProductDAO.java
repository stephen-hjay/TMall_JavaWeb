package com.hjay.tmall.DAO.Implement;

import com.hjay.tmall.DAO.DAO;
import com.hjay.tmall.GlobalParameters;
import com.hjay.tmall.Utils.DBConnPoolUtils;
import com.hjay.tmall.Utils.DateFormatUtils;
import com.hjay.tmall.Entity.Bean;
import com.hjay.tmall.Entity.Implement.Category;
import com.hjay.tmall.Entity.Implement.Product;
import com.hjay.tmall.Entity.Implement.ProductImage;

import java.util.ArrayList;
import java.util.Date;
import java.sql.*;
import java.util.List;

public class ProductDAO implements DAO {

    public int getTotal(int cid) {
        int total = 0;
        try (Connection c = DBConnPoolUtils.getConnection(); Statement s = c.createStatement();) {

            String sql = "select count(*) from Product where cid = " + cid;

            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return total;
    }

    public boolean add(Bean bean_father) {
        Product bean = (Product) bean_father;
        String sql = "insert into Product values(null,?,?,?,?,?,?,?)";
        boolean result = false;
        try (Connection c = DBConnPoolUtils.getConnection(); PreparedStatement ps = c.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);) {
            ps.setString(1, bean.getName());
            ps.setString(2, bean.getSubTitle());
            ps.setFloat(3, bean.getOrignalPrice());
            ps.setFloat(4, bean.getPromotePrice());
            ps.setInt(5, bean.getStock());
            ps.setInt(6, bean.getCategory().getId());
            ps.setTimestamp(7, DateFormatUtils.date2timestamp(bean.getCreateDate()));
            result = ps.execute();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                bean.setId(id);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return result;
    }

    public boolean update(Bean bean_father) {
        Product bean = (Product) bean_father;
        String sql = "update Product set name= ?, subTitle=?, orignalPrice=?,promotePrice=?,stock=?, cid = ?, createDate=? where id = ?";
        boolean result = false;
        try (Connection c = DBConnPoolUtils.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

            ps.setString(1, bean.getName());
            ps.setString(2, bean.getSubTitle());
            ps.setFloat(3, bean.getOrignalPrice());
            ps.setFloat(4, bean.getPromotePrice());
            ps.setInt(5, bean.getStock());
            ps.setInt(6, bean.getCategory().getId());
            ps.setTimestamp(7, DateFormatUtils.date2timestamp(bean.getCreateDate()));
            ps.setInt(8, bean.getId());
            result = ps.execute();

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return result;

    }

    @Override
    public int getTotal() {
        return 0;
    }

    public boolean delete(int id) {
        boolean result = false;
        try (Connection c = DBConnPoolUtils.getConnection(); Statement s = c.createStatement();) {

            String sql = "delete from Product where id = " + id;

            result = s.execute(sql);

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return result;
    }

    public Product getById(int id) {
        Product bean = new Product();

        try (Connection c = DBConnPoolUtils.getConnection(); Statement s = c.createStatement();) {

            String sql = "select * from Product where id = " + id;

            ResultSet rs = s.executeQuery(sql);

            if (rs.next()) {

                String name = rs.getString("name");
                String subTitle = rs.getString("subTitle");
                float orignalPrice = rs.getFloat("orignalPrice");
                float promotePrice = rs.getFloat("promotePrice");
                int stock = rs.getInt("stock");
                int cid = rs.getInt("cid");
                Date createDate = DateFormatUtils.timestamp2date( rs.getTimestamp("createDate"));

                bean.setName(name);
                bean.setSubTitle(subTitle);
                bean.setOrignalPrice(orignalPrice);
                bean.setPromotePrice(promotePrice);
                bean.setStock(stock);
                Category category = new CategoryDAO().getById(cid);
                bean.setCategory(category);
                bean.setCreateDate(createDate);
                bean.setId(id);
                setFirstProductImage(bean);
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return bean;
    }

    public List<Product> list(int cid) {
        return list(cid,0, Short.MAX_VALUE);
    }

    public List<Product> list(int cid, int start, int count) {
        List<Product> beans = new ArrayList<Product>();
        Category category = new CategoryDAO().getById(cid);
        String sql = "select * from Product where cid = ? order by id desc limit ?,? ";

        try (Connection c = DBConnPoolUtils.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
            ps.setInt(1, cid);
            ps.setInt(2, start);
            ps.setInt(3, count);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Product bean = new Product();
                int id = rs.getInt(1);
                String name = rs.getString("name");
                String subTitle = rs.getString("subTitle");
                float orignalPrice = rs.getFloat("orignalPrice");
                float promotePrice = rs.getFloat("promotePrice");
                int stock = rs.getInt("stock");
                Date createDate = DateFormatUtils.timestamp2date( rs.getTimestamp("createDate"));

                bean.setName(name);
                bean.setSubTitle(subTitle);
                bean.setOrignalPrice(orignalPrice);
                bean.setPromotePrice(promotePrice);
                bean.setStock(stock);
                bean.setCreateDate(createDate);
                bean.setId(id);
                bean.setCategory(category);
                setFirstProductImage(bean);
                beans.add(bean);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return beans;
    }
    public List<Product> list() {
        return list(0,Short.MAX_VALUE);
    }

    public List<Product> list(int start, int count) {
        List<Product> beans = new ArrayList<Product>();

        String sql = "select * from Product limit ?,? ";

        try (Connection c = DBConnPoolUtils.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

            ps.setInt(1, start);
            ps.setInt(2, count);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Product bean = new Product();
                int id = rs.getInt(1);
                int cid = rs.getInt("cid");
                String name = rs.getString("name");
                String subTitle = rs.getString("subTitle");
                float orignalPrice = rs.getFloat("orignalPrice");
                float promotePrice = rs.getFloat("promotePrice");
                int stock = rs.getInt("stock");
                Date createDate = DateFormatUtils.timestamp2date( rs.getTimestamp("createDate"));

                bean.setName(name);
                bean.setSubTitle(subTitle);
                bean.setOrignalPrice(orignalPrice);
                bean.setPromotePrice(promotePrice);
                bean.setStock(stock);
                bean.setCreateDate(createDate);
                bean.setId(id);

                Category category = new CategoryDAO().getById(cid);
                bean.setCategory(category);
                beans.add(bean);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return beans;
    }

    /**
     * 用Product fill the list of the categories
     * @param cs
     */

    public void fill(List<Category> cs) {
        for (Category c : cs)
            fill(c);
    }

    /**
     * 用Product fill one categories
     * @param c
     */
    public void fill(Category c) {
        // "select * from Product where cid = ? order by id desc limit ?,? ";
        List<Product> ps = this.list(c.getId());
        c.setProducts(ps);
    }

    /**
     * split the List<Product> into several for the forestage display
     * @param cs
     */
    public void fillByRow(List<Category> cs) {
        int productNumberEachRow = GlobalParameters.ForeStageDisplay.PRODUCT_EACH_ROW;

        for (Category c : cs) {
            List<Product> products =  c.getProducts();
            List<List<Product>> productsByRow =  new ArrayList<>();

            for (int i = 0; i < products.size(); i+=productNumberEachRow) {
                int size = i+productNumberEachRow;
                size= size>products.size()?products.size():size;
                List<Product> productsOfEachRow = products.subList(i, size);
                productsByRow.add(productsOfEachRow);
            }
            c.setProductsByRow(productsByRow);
        }
    }

    public void setFirstProductImage(Product p) {
        List<ProductImage> pis= new ProductImageDAO().list(p, ProductImageDAO.type_single);
        if(!pis.isEmpty())
            p.setFirstProductImage(pis.get(0));
    }

    public void setSaleAndReviewNumber(Product p) {
        int saleCount = new OrderItemDAO().getSaleCount(p.getId());
        p.setSaleCount(saleCount);

        int reviewCount = new ReviewDAO().getCount(p.getId());
        p.setReviewCount(reviewCount);

    }

    public void setSaleAndReviewNumber(List<Product> products) {
        for (Product p : products) {
            setSaleAndReviewNumber(p);
        }
    }

    public List<Product> search(String keyword, int start, int count) {
        List<Product> beans = new ArrayList<Product>();

        if(null==keyword||0==keyword.trim().length())
            return beans;
        // 使用的模糊查询
        String sql = "select * from Product where name like ? limit ?,? ";

        try (Connection c = DBConnPoolUtils.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
            ps.setString(1, "%"+keyword.trim()+"%");
            ps.setInt(2, start);
            ps.setInt(3, count);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Product bean = new Product();
                int id = rs.getInt(1);
                int cid = rs.getInt("cid");
                String name = rs.getString("name");
                String subTitle = rs.getString("subTitle");
                float orignalPrice = rs.getFloat("orignalPrice");
                float promotePrice = rs.getFloat("promotePrice");
                int stock = rs.getInt("stock");
                Date createDate = DateFormatUtils.timestamp2date( rs.getTimestamp("createDate"));

                bean.setName(name);
                bean.setSubTitle(subTitle);
                bean.setOrignalPrice(orignalPrice);
                bean.setPromotePrice(promotePrice);
                bean.setStock(stock);
                bean.setCreateDate(createDate);
                bean.setId(id);

                Category category = new CategoryDAO().getById(cid);
                bean.setCategory(category);
                setFirstProductImage(bean);
                beans.add(bean);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return beans;
    }
}
