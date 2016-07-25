package com.botomo;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import com.botomo.models.Book;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class BeanValidator {
	private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
	
	public <T>JsonObject validate(T object){
		
		Set<ConstraintViolation<T>> violations = this.validator.validate(object);
		
		System.out.println("FETCHED Vs: " + violations);
		
		JsonObject jo = new JsonObject();
		JsonArray ja = new JsonArray();
		
		for(ConstraintViolation<T> violation : violations){
			JsonObject v = new JsonObject()
					.put("class", violation.getLeafBean().getClass().getName())
					.put("message", violation.getMessage())
					.put("value", violation.getInvalidValue());
			ja.add(v);
		}
		
		jo.put("violations", ja);
		
		System.out.println("VALIDATION -> " + jo.encodePrettily());
		
		return jo;
	}
	
//	public static void main(String[] args) {
//		
//		Book b = new Book();
//		b.setTitle(null);
//		b.setAuthor("");
//		b.setYear("123");
//		
//		BeanValidator bv = new BeanValidator();
//		bv.<Book>validate(b);
//		
//	}
	
}
