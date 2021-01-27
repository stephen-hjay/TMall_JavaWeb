package com.hjay.tmall.DAO;

import com.hjay.tmall.Entity.Bean;

import java.sql.SQLException;
import java.util.List;

public  interface  DAO {
    // return a Bean
    public Bean getById(int id);

    // add a record
    public boolean add( Bean bean_father);

    // delete a record
    public boolean delete(int id);

    // return a list contains all beans -> for generic
    public List<? extends Bean> list() throws SQLException;

    // return a list contains beans for pagin
    public List<? extends Bean> list(int start, int count) throws SQLException;

    // update a record
    public boolean update(Bean bean_father);

    // return the number of all records
    public int getTotal();
}
