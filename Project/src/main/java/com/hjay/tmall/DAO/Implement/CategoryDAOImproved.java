package com.hjay.tmall.DAO.Implement;

//CREATE TABLE category (
//        id int(11) UNIQUE NOT NULL AUTO_INCREMENT,
//        name varchar(255) DEFAULT NULL,
//        PRIMARY KEY (id)
//        ) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

import com.hjay.tmall.DAO.DAO;
import com.hjay.tmall.Utils.DBConnPoolUtils;
import com.hjay.tmall.Entity.Bean;
import com.hjay.tmall.Entity.Implement.Category;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.List;


public class CategoryDAOImproved implements DAO {
    private JdbcTemplate template = new JdbcTemplate(DBConnPoolUtils.getDataSource());

    public boolean add(Bean bean_father) {
        Category bean = (Category) bean_father;
        String sql = "insert into category(id,name) values(null,?)";
        Object args[] = {bean.getId()};
        int temp = template.update(sql, args);
        if (temp > 0) {
            System.out.println("Add Success");
        }else{
            System.out.println("Add Fail");
        }
        return temp>0;
    }


    public boolean delete(int id){
        String sql = "delete from category where id = ?";
        Object args[] = new Object[]{id};
        int temp = template.update(sql,args);
        if (temp > 0) {
            System.out.println("Delete Success");
        }else {
            System.out.println("Delete Fail");
        }
        return temp>0;
    }

    public boolean update(Bean bean_father) {
        Category bean = (Category) bean_father;
        String sql = "update category set name = ? where id = ?";
        Object args[] = new Object[]{bean.getName(),bean.getId()};
        int temp = template.update(sql,args);
        if (temp > 0) {
            System.out.println("Update Success");
        }else {
            System.out.println("Update Fail");
        }
        return temp>0;
    }

    public Category getById(int id){
        String sql = "select * from category where id = ?";
        Object args[] = new Object[]{id};
        Object category = template.queryForObject(sql,args,new BeanPropertyRowMapper(Category.class));
        return (Category) category;
    }


    public List<Category> list(int start, int count) throws SQLException {
        Connection connection = null;
        connection = DBConnPoolUtils.getConnection();
        String sql = "select * from category order by id desc limit ?,? ";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1,start);
        preparedStatement.setInt(2,count);
        List<Category> resultBeans  = template.query(preparedStatement.toString(), new RowMapper() {
            @Override
            public Bean mapRow(ResultSet resultSet, int i) throws SQLException {
                //  get data
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                //  encapsulate into object
                Category category = new Category();
                category.setId(id);
                category.setName(name);
                return category;
            }
        });
        DBConnPoolUtils.free(null,preparedStatement,connection);
        return resultBeans;
    }

    public List<Category> list() throws SQLException {
        return list(0,Short.MAX_VALUE);
    }

    public int getTotal(){
        String sql = "select count(*) from category";
        Integer result = (Integer) template.queryForObject(sql,Integer.class);
        return result;
    }

}
