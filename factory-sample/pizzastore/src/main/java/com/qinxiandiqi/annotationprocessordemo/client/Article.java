package com.qinxiandiqi.annotationprocessordemo.client;

import com.hannesdorfmann.annotationprocessing101.factory.annotation.BeanInfo;
import com.hannesdorfmann.annotationprocessing101.factory.annotation.Print;

/**
 * Created by Jianan on 2015/10/24.
 */
@BeanInfo
@Print
public class Article {
	
	@BeanInfo
    private String id;
    @BeanInfo
    private int department;
    @BeanInfo
    private String status;

    public Article() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDepartment() {
        return department;
    }

    public void setDepartment(int department) {
        this.department = department;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @BeanInfo
    public void activate() {
        setStatus("active");
    }

    @BeanInfo
    public void deactivate() {
        setStatus("inactive");
    }
}
