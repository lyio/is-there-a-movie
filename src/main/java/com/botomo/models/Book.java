package com.botomo.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.botomo.StringUtils;
import io.vertx.core.json.JsonObject;

public class Book {

    private String _id;
    
    @NotNull(message="Title must not be null") 
    @Size(min=1, message="Title must not be empty")
    private String title;
    
    private String subtitle;
    
    @NotNull(message="Author must not be null") 
    @Size(min=1, message="Author must not be empty")
    private String author;
    
    @Pattern(regexp="[1-9]{4}", message="The year must be formatted as four numbers string")
    private String year;
    
    private int downs;
    
    private int ups;

    public Book() {	}

    public Book(JsonObject json){
    	this._id = json.getString("_id");
    	this.title = json.getString("title");
    	this.subtitle = json.getString("subtitle");
    	this.author = json.getString("author");
    	this.year = json.getString("year");
    	this.downs = json.getInteger("downs");
    	this.ups = json.getInteger("ups");
    }
    
    /**
     * Converts this object to a JsonObject.
     * The id is only set if it's not set yet. Else the id field
     * should be populated by the database.
     * @return A JsonObject object representing this object.
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.put("title", this.title);
        json.put("subtitle", this.subtitle);
        json.put("author", this.author);
        json.put("year", this.year);
        json.put("ups", this.ups);
        json.put("downs", this.downs);
        if (!StringUtils.isNullOrEmpty(this._id)) {
            json.put("_id", _id);
        }

        return json;
    }

    @Override
	public String toString() {
		return "Book [id=" 
				+ _id 
				+ ", title=" 
				+ title 
				+ ", subtitle=" 
				+ subtitle 
				+ ", author=" 
				+ author 
				+ ", year="
				+ year 
				+ ", downs=" 
				+ downs 
				+ ", ups=" 
				+ ups 
				+ "]";
	}

    public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
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
