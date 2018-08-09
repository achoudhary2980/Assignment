package com.wiproassignment.arun.wiproassignment.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class AllAboutCanada {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("errorcode")
    @Expose
    private String errorcode;
    @SerializedName("rows")
    @Expose
    private List<Row> rows = null;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

    public String getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(String errorcode) {
        this.errorcode = errorcode;
    }
}