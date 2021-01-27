package com.hjay.tmall.Entity.Implement;
/*
CREATE TABLE category (
  id int(11) UNIQUE NOT NULL AUTO_INCREMENT,
  name varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;
 */

import com.hjay.tmall.Entity.Bean;

import java.util.List;

public class Category extends Bean {

    private String name;
    private int id;
    List<Product> products;
    List<List<Product>> productsByRow;

    @Override
    public int getId() {
        return id;
    }
    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Category [name=" + name + "]";
    }


    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }


    /**
     * for page display, to show products according to category by rows
     *
     * @return
     */
    public List<List<Product>> getProductsByRow() {
        return productsByRow;
    }

    public void setProductsByRow(List<List<Product>> productsByRow) {
        this.productsByRow = productsByRow;
    }

}
