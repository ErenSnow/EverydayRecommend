package com.eren.everydayrecommend.home.model;

/**
 * 作者：Leon
 * 描述:
 */
public class DrawModel {

    private int resourceID;
    private String title;

    public DrawModel(int resourceID, String title) {
        this.resourceID = resourceID;
        this.title = title;
    }

    public int getResourceID() {
        return resourceID;
    }

    public void setResourceID(int resourceID) {
        this.resourceID = resourceID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
