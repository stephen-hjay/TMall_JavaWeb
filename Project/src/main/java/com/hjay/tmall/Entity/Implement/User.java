package com.hjay.tmall.Entity.Implement;

/*
CREATE TABLE user (
  id int(11) UNIQUE NOT NULL AUTO_INCREMENT,
  name varchar(255) DEFAULT NULL,
  password varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 */

import com.hjay.tmall.Entity.Bean;

public class User extends Bean {
    private String password;
    private String name;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
     * for review anonymous name
     * @return
     */
    public String getAnonymousName(){
        if(null==name)
            return null;

        if(name.length()<=1)
            return "*";

        if(name.length()==2)
            return name.substring(0,1) +"*";

        char[] cs =name.toCharArray();
        for (int i = 1; i < cs.length-1; i++) {
            cs[i]='*';
        }
        return new String(cs);

    }
    // for test only
    @Override
    public String toString() {
        return "User{" +
                "password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
