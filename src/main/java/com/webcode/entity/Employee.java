package com.webcode.entity;

import java.io.Serializable;

/**
 * @ClassName Employee.java
 * @Description TODO
 * @Author wushaopei
 * @Date 2020年4月14日
 * @Version 1.0
 */
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Integer id;
    
    private String name;
     
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}
