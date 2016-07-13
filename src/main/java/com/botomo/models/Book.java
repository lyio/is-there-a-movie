package com.botomo.models;

import java.util.Date;

/**
 * Created by Thomas on 11.07.2016.
 */
public class Book {

    private int id;
    private String title;
    private String subtitle;
    private String author;
    private Date year;
    private int downs;
    private int ups;

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

    public Date getYear() {
        return year;
    }

    public void setYear(Date year) {
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
