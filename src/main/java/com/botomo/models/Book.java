package com.botomo.models;

import java.util.Date;

import io.vertx.core.json.JsonObject;

/**
 * Created by Thomas on 11.07.2016.
 */
public class Book {

    private int id;
    private String mongo_id;
    private String title;
    private String subtitle;
    private String author;
    private String year;
    private int downs;
    private int ups;
    
    public Book() {	}

    public Book(JsonObject json){
    	this.mongo_id = json.getJsonObject("_id").getString("$oid");
    	this.title = json.getString("title");
    	this.subtitle = json.getString("subtitle");
    	this.author = json.getString("author");
    	this.year = json.getString("year");
    	this.downs = json.getInteger("downs");
    	this.ups = json.getInteger("ups");
    }
    
    public boolean search(String searchTerm) {
        return title.contains(searchTerm) || author.contains(searchTerm) || subtitle.contains(searchTerm);
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getDowns() {
        return downs;
    }

    public void setDowns(int downs) {
        this.downs = downs;
    }

    public int getUps() {
        return ups;
    }

    public void setUps(int ups) {
        this.ups = ups;
    }
}
