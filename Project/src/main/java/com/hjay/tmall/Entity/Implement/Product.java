package com.hjay.tmall.Entity.Implement;


import com.hjay.tmall.Entity.Bean;

import java.util.Date;
import java.util.List;

/*
CREATE TABLE product (
  id int(11) UNIQUE NOT NULL AUTO_INCREMENT,
  name varchar(255) DEFAULT NULL,
  subTitle varchar(255) DEFAULT NULL,
  orignalPrice float DEFAULT NULL,
  promotePrice float DEFAULT NULL,
  stock int(11) DEFAULT NULL,
  cid int(11) DEFAULT NULL,
  createDate datetime DEFAULT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_product_category FOREIGN KEY (cid) REFERENCES category (id)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;
 */
public class Product extends Bean {
    private String name;
    private String subTitle;
    private float orignalPrice;
    private float promotePrice;
    private int stock;
    private Date createDate;
    private Category category;
    private int id;
    // default display image
    private ProductImage firstProductImage;
    private List<ProductImage> productImages;
    // display on item brief page
    private List<ProductImage> productSingleImages;
    // display on item detail pages
    private List<ProductImage> productDetailImages;
    // for review
    private  List<Review> reviews;

    private int reviewCount;
    private int saleCount;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSubTitle() {
        return subTitle;
    }
    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }
    public float getOrignalPrice() {
        return orignalPrice;
    }
    public void setOrignalPrice(float orignalPrice) {
        this.orignalPrice = orignalPrice;
    }
    public float getPromotePrice() {
        return promotePrice;
    }
    public void setPromotePrice(float promotePrice) {
        this.promotePrice = promotePrice;
    }
    public int getStock() {
        return stock;
    }
    public void setStock(int stock) {
        this.stock = stock;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String toString(){
        return name;
    }
    public ProductImage getFirstProductImage() {
        return firstProductImage;
    }
    public void setFirstProductImage(ProductImage firstProductImage) {
        this.firstProductImage = firstProductImage;
    }
    public List<ProductImage> getProductImages() {
        return productImages;
    }
    public void setProductImages(List<ProductImage> productImages) {
        this.productImages = productImages;
    }
    public List<ProductImage> getProductSingleImages() {
        return productSingleImages;
    }
    public void setProductSingleImages(List<ProductImage> productSingleImages) {
        this.productSingleImages = productSingleImages;
    }
    public List<ProductImage> getProductDetailImages() {
        return productDetailImages;
    }
    public void setProductDetailImages(List<ProductImage> productDetailImages) {
        this.productDetailImages = productDetailImages;
    }
    public int getReviewCount() {
        return reviewCount;
    }
    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }
    public int getSaleCount() {
        return saleCount;
    }
    public void setSaleCount(int saleCount) {
        this.saleCount = saleCount;
    }

    public void setProdcutReviews(List<Review> reviews){
        this.reviews = reviews;
    }


}